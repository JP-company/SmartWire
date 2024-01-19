import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:smartwire_mobile/dto/alarm_setting_dto.dart';
import 'package:smartwire_mobile/firebase/config/notification_config.dart';
import 'package:smartwire_mobile/firebase/firebase_options.dart';
import 'package:smartwire_mobile/login_page.dart';
import 'package:smartwire_mobile/home_page.dart';
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import 'dto/jwt_dto.dart';
import 'local_storage/local_storage.dart';
import 'package:http/http.dart' as http;

Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  print("백그라운드 메시지 수신: ${message.notification!.title}, ${message.notification!.body}");
}

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options:DefaultFirebaseOptions.currentPlatform
  );
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  NotificationConfig.initialize();

  var connectivityResult = await (Connectivity().checkConnectivity());
  if (connectivityResult == ConnectivityResult.none) {
    runApp(InternetError());
    return;
  }

  /// 앱 처음 실행 시 자동 로그인 시도
  JwtDto jwtDto = await autoLogin() ?? JwtDto();
  runApp(MyApp(jwtDto: jwtDto));
}

class InternetError extends StatelessWidget {
  const InternetError({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "SMARTWIRE MOBILE APP",
      theme: ThemeData(
        primarySwatch: Colors.green,
      ),
      home: Scaffold(
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const CircleAvatar(
                backgroundImage: AssetImage('assets/images/smartwire.png'),
                radius: 32.0,
              ),
              SizedBox(height: 20,),
              Text("인터넷 연결을 확인해 주세요.",
                style: TextStyle(
                  fontSize: 18,
                ),
                textAlign: TextAlign.center,
              ),
              TextButton(
                  onPressed: () { main(); },
                  child: Text("새로고침",)
              )
            ],
          ),
        ),
      ),
    );
  }
}


class MyApp extends StatelessWidget {
  const MyApp({Key? key, required this.jwtDto}) : super(key: key);
  final JwtDto jwtDto;

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => jwtDto),
        ChangeNotifierProvider(create: (context) => AlarmSettingDto()),
      ],
      child: MaterialApp(
        title: "SMARTWIRE MOBILE APP",
        theme: ThemeData(
          primarySwatch: Colors.green,
        ),
        home: jwtDto.jwtMemberDto == null ? LoginPage() : HomePage(),
      ),
    );
  }
}

Future<JwtDto?> autoLogin() async {
  /// Local Storage 에서 jwt 토큰 꺼내옴
  var jwt = await LocalStorage.load("jwt");

  if (jwt != null) {
    Map<String, String> headers = {
      'Authorization': jwt
    };

    final response = await http.get(
      Uri.parse('https://smartwire-backend-f39394ac6218.herokuapp.com/api/auto_login'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      JwtDto jwtDto = JwtDto.fromJson(json.decode(response.body));
      jwtDto.jwt = jwt;
      return jwtDto; // 자동 로그인 성공
    }

  }
  return null;
}