import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:smartwire_mobile/alarm_setting_page.dart';
import 'package:smartwire_mobile/login_page.dart';
import 'package:smartwire_mobile/home_page.dart';
import 'package:smartwire_mobile/member_page.dart';
import 'package:provider/provider.dart';

import 'dto/jwt_dto.dart';
import 'dto/log_dto.dart';
import 'local_storage/remember_member.dart';
import 'package:http/http.dart' as http;

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  JwtDto? jwtDto = await autoLogin();
  List<LogDto> logList = <LogDto>[];
  jwtDto = (jwtDto == null) ? JwtDto() : jwtDto;
  if (jwtDto != null) {
    logList = await getLogList();
  }
  runApp(MyApp(
      jwtDto: jwtDto,
      logList: logList,
  ));
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key, required this.jwtDto, required this.logList}) : super(key: key);
  final JwtDto jwtDto;
  final List<LogDto> logList;

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => jwtDto),
        ChangeNotifierProvider(create: (context) => logList)
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

Future<List<LogDto>> getLogList() async {
  Future<dynamic> future = RememberMember.loadMemberJwt();
  var jwt = await future;

  if (jwt != null) {
    Map<String, String> headers = {
      'Authorization': jwt
    };

    final response = await http.get(
      Uri.parse('https://smartwire-backend-f39394ac6218.herokuapp.com/api/home'),
      headers: headers,
    );

    if (response.statusCode == 200) {
      List<LogDto> LogList = json.decode(response.body).map((dynamic item) => LogDto.fromJson(item)).toList();
      return LogList;
    }
  }
  return <LogDto>[];
}