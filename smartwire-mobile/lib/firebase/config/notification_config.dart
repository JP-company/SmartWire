import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:provider/provider.dart';
import 'package:smartwire_mobile/dto/alarm_setting_dto.dart';
import 'package:smartwire_mobile/dto/jwt_dto.dart';
import 'package:smartwire_mobile/local_storage/local_storage.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:http/http.dart' as http;

class NotificationConfig {
  NotificationConfig._();

  static final FlutterLocalNotificationsPlugin _flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();
  static final FirebaseMessaging messaging = FirebaseMessaging.instance;
  static final AndroidNotificationChannel channel = const AndroidNotificationChannel(
    'high_importance_channel', // id
    'High Importance Notifications', // title
    description: 'This channel is used for important notifications.', // description
    importance: Importance.max,
  );

  static initialize() async {
    AndroidInitializationSettings initializationSettingsAndroid =
    const AndroidInitializationSettings("@mipmap/ic_launcher");

    DarwinInitializationSettings initializationSettingsIOS =
    const DarwinInitializationSettings(
      requestAlertPermission: false,
      requestBadgePermission: false,
      requestSoundPermission: false,
    );

    InitializationSettings initializationSettings = InitializationSettings(
      android: initializationSettingsAndroid,
      iOS: initializationSettingsIOS,
    );

    await _flutterLocalNotificationsPlugin
        .resolvePlatformSpecificImplementation<AndroidFlutterLocalNotificationsPlugin>()
        ?.createNotificationChannel(channel);

    await _flutterLocalNotificationsPlugin.initialize(initializationSettings);
  }

  static void initializeAfterLogin(BuildContext context) {
    _requestPermission();
    _saveFCMTokenAndAlarmSettingToDB(context);
    _setPushNotificationListener();
  }

  static void _requestPermission() async {
    await messaging.requestPermission(
      alert: true,
      announcement: false,
      badge: true,
      carPlay: false,
      criticalAlert: false,
      provisional: false,
      sound: true,
    );
  }
  static Future<void> _saveFCMTokenAndAlarmSettingToDB(BuildContext context) async {

    /// Provider jwt 없으면 return;
    String? jwt = Provider.of<JwtDto>(context, listen: false).jwt;
    if (jwt == null || jwt == "") { return; }

    /// fcmToken 생성
    FirebaseMessaging.instance.getToken().then((fcmToken) async {

      // LocalStorage "fcmToken" 꺼내옴
      var savedFCMToken = await LocalStorage.load("fcmToken");
      Provider.of<AlarmSettingDto>(context, listen: false).fcmToken = fcmToken;

      // LocalStorage "fcmToken" 가 없거나 현재 fcmToken 와 다르면
      if (savedFCMToken == null || savedFCMToken == "" || savedFCMToken != fcmToken) {

        // LocalStorage "fcmToken" 저장, Provider fcmToken 저장
        await LocalStorage.save("fcmToken", fcmToken);

        // LocalStorage "alarmSetting" 확인 후 없으면 LocalStorage, Provider 초기화 (최초 저장)
        var alarmSetting = await LocalStorage.load("alarmSetting");
        if (alarmSetting == null || alarmSetting == "") {
          alarmSetting = "y20:0,23:0";
          bool save = await LocalStorage.save("alarmSetting", alarmSetting);
          if (save) {
            Provider.of<AlarmSettingDto>(context, listen: false).isAlarmTrue = true;
            Provider.of<AlarmSettingDto>(context, listen: false).startTime = TimeOfDay(hour: 20, minute: 0);
            Provider.of<AlarmSettingDto>(context, listen: false).lastTime = TimeOfDay(hour: 23, minute: 0);
          }
        }

        // 서버에 fcmToken, alarmSetting POST 저장 요청
        Map<String, String> headers = {
          'Content-Type' : 'application/json',
          'Authorization': jwt
        };
        var data = jsonEncode({
          "fcmToken": fcmToken,
          "alarmSetting" : alarmSetting,
        });

        final response = await http.post(
          Uri.parse(
              'https://smartwire-backend-f39394ac6218.herokuapp.com/api/fcm_token'),
          headers: headers,
          body: data,
        );

        print('response= ${response.statusCode}, ${response.body}');
      }

    });

    /// fcmToken Refresh Listener 등록
    FirebaseMessaging.instance.onTokenRefresh.listen((fcmToken) async {

      // LocalStorage "fcmToken" 저장, Provider fcmToken 저장
      await LocalStorage.save("fcmToken", fcmToken);
      Provider.of<AlarmSettingDto>(context, listen: false).fcmToken = fcmToken;

      var alarmSetting = await LocalStorage.load("alarmSetting");

      // 서버에 fcmToken, alarmSetting POST 저장 요청
      Map<String, String> headers = {
        'Content-Type' : 'application/json',
        'Authorization': jwt
      };
      var data = jsonEncode({
        "fcmToken": fcmToken,
        "alarmSetting" : alarmSetting,
      });

      final response = await http.post(
        Uri.parse(
            'https://smartwire-backend-f39394ac6218.herokuapp.com/api/fcm_token'),
        headers: headers,
        body: data,
      );

      print('Token Refresh response= ${response.statusCode}, ${response.body}');
    });
  }


  static void _setPushNotificationListener() {
    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      RemoteNotification? notification = message.notification;

      // flutter local notification 으로 대신 알림 보여줌
      if (notification != null) {
        print("포그라운드 메시지 수신");
        _showNotification(notification);
      }
    });
  }

  static void _showNotification(RemoteNotification notification) async {
    AndroidNotificationDetails androidPlatformChannelSpecifics =
    AndroidNotificationDetails(
        channel.id,
        channel.name,
        channelDescription: channel.description,
        importance: Importance.max,
        priority: Priority.max,
        showWhen: true
    );

    NotificationDetails platformChannelSpecifics = NotificationDetails(
      android: androidPlatformChannelSpecifics,
      // iOS: DarwinNotificationDetails(
      //   // badgeNumber: 1,
      // ),
    );

    await _flutterLocalNotificationsPlugin.show(
        notification.hashCode,
        notification.title,
        notification.body,
        platformChannelSpecifics,
        // payload: "item x"
    );

  }
}