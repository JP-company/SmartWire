import 'dart:convert';

import 'package:get/get.dart';
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

  static void initializeAfterLogin() {
    _requestPermission();
    _saveFCMTokenAndAlarmSettingToDB();
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

  static Future<void> _saveFCMTokenAndAlarmSettingToDB() async {
    FirebaseMessaging.instance.getToken().then((fcmToken) async {
      Future<dynamic> futureFCMToken = LocalStorage.load("fcmToken");
      var savedFCMToken = await futureFCMToken;

      if (savedFCMToken == null || savedFCMToken == "" || savedFCMToken != fcmToken) {
        Future<dynamic> futureJwt = LocalStorage.load("jwt");
        var jwt = await futureJwt;

        if (jwt != null && jwt != "") {
          await LocalStorage.save("fcmToken", fcmToken);

          var alarmSetting = await LocalStorage.load("alarmSetting");
          if (alarmSetting == null || alarmSetting == "") {
            alarmSetting = "y20:00,23:59";
            await LocalStorage.save("alarmSetting", alarmSetting);
          }

          Map<String, String> headers = {
            'Content-Type' : 'application/json',
            'Authorization': jwt
          };

          print("초기 알람 설정 시간=$alarmSetting");

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
      }
    });

    FirebaseMessaging.instance.onTokenRefresh.listen((fcmToken) async {
      Future<dynamic> futureFCMToken = LocalStorage.load("fcmToken");
      var savedFCMToken = await futureFCMToken;

      if (savedFCMToken == null || savedFCMToken == "" || savedFCMToken != fcmToken) {
        Future<dynamic> futureJwt = LocalStorage.load("jwt");
        var jwt = await futureJwt;

        if (jwt != null && jwt != "") {
          await LocalStorage.save("fcmToken", fcmToken);

          var alarmSetting = await LocalStorage.load("alarmSetting");
          if (alarmSetting == null || alarmSetting == "") {
            alarmSetting = "y20:00,23:59";
            await LocalStorage.save("alarmSetting", alarmSetting);
          }

          Map<String, String> headers = {
            'Content-Type' : 'application/json',
            'Authorization': jwt
          };

          print("초기 알람 설정 시간=$alarmSetting");

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
      }
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
      iOS: DarwinNotificationDetails(
        badgeNumber: 1,
      ),
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