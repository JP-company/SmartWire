import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:smartwire_mobile/alarm_setting_page.dart';
import 'package:smartwire_mobile/firebase_options.dart';
import 'package:smartwire_mobile/login_page.dart';
import 'package:smartwire_mobile/home_page.dart';
import 'package:smartwire_mobile/member_page.dart';
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';

import 'dto/jwt_dto.dart';
import 'local_storage/remember_member.dart';
import 'package:http/http.dart' as http;

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options:DefaultFirebaseOptions.currentPlatform
  );

  JwtDto? jwtDto = await autoLogin();
  jwtDto = (jwtDto == null) ? JwtDto() : jwtDto;
  runApp(MyApp(
      jwtDto: jwtDto,
  ));
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key, required this.jwtDto}) : super(key: key);
  final JwtDto jwtDto;

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => jwtDto)
      ],
      child: MaterialApp(
        initialRoute: jwtDto.jwtMemberDto == null ? '/login': '/',
        routes: {
          '/login' : (context) => const LoginPage(),
          '/' : (context) => const HomePage(),
          '/alarm_setting' : (context) => const AlarmSettingPage(),
          '/member' : (context) => const MemberPage(),
        },
        title: "SMART WIRE MOBILE APP",
        theme: ThemeData(
          primarySwatch: Colors.green,
        ),
      ),
    );
  }
}

Future<JwtDto?> autoLogin() async {
  Future<dynamic> future = RememberMember.loadMemberJwt();
  var jwt = await future;

  if (jwt != null) {
    Map<String, String> headers = {
      'Authorization': jwt
    };

    final response = await http.get(
      Uri.parse('https://smartwire-backend-f39394ac6218.herokuapp.com/api/auto_login'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      return JwtDto.fromJson(json.decode(response.body));; // 자동 로그인 성공
    }
  }
  return null;
}