import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smartwire_mobile/local_storage/remember_member.dart';
import 'package:http/http.dart' as http;

import 'dto/jwt_dto.dart';
import 'dto/log_dto.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<HomePage> createState() {
    return _HomePageState();
  }
}

class _HomePageState extends State<HomePage> {
  dynamic logList;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.5,
        backgroundColor: Colors.white,
        iconTheme: IconThemeData(color: Colors.black),
        title: Text(
          'SMART WIRE',
          style: TextStyle(
            color: Colors.black,
            fontSize: 18.0,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      endDrawer: SafeArea(
        child: Drawer(
          child: ListView(
            padding: EdgeInsets.zero,
            children: <Widget>[
              Column(
                children: [
                  SizedBox( height: 40 ),
                  Text('${Provider.of<JwtDto>(context).jwtMemberDto?.companyName} 님 안녕하세요.',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16.0,
                    ),
                  ),
                  SizedBox( height: 40 ),
                  Container(
                    margin: EdgeInsets.fromLTRB(20, 0, 20, 0),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(8.0),
                      boxShadow: const [
                        BoxShadow(
                          color: Color(0xffD9D9D9),
                          blurRadius: 8,
                        )
                      ]
                    ),
                    child: Column(
                      children: [
                        ListTile(
                          title: Text('계정 정보'),
                          onTap: () {
                            print("계정 정보 버튼 클릭");
                            Navigator.pushNamed(context, '/member');
                          },
                        ),
                        Divider(
                          thickness: 1.0,
                          height: 0.0,
                          color: Color(0xffF4F4F4),
                          indent: 16.0,
                          endIndent: 16.0,
                        ),
                        ListTile(
                          title: Text('알림 설정'),
                          onTap: () {
                            print("알림 설정 버튼 클릭");
                            Navigator.pushNamed(context, '/alarm_setting');
                          },
                        ),
                        Divider(
                          thickness: 1.0,
                          height: 0.0,
                          color: Color(0xffF4F4F4),
                          indent: 16.0,
                          endIndent: 16.0,
                        ),
                        ListTile(
                          title: Text('로그아웃'),
                          onTap: () async {
                            print("로그아웃 버튼 클릭");
                            Provider.of<JwtDto>(context, listen: false).jwtMemberDto = null;
                            Provider.of<JwtDto>(context, listen: false).machineDtoList = null;
                            RememberMember.saveMemberJwt("");
                            Navigator.pushNamedAndRemoveUntil(context, '/login', (route) => false);
                          },
                        ),
                      ],
                    ),
                  )
                ],
              ),
            ],
          ),
        ),
      ),
      body: Container(
        padding: EdgeInsets.fromLTRB(24.0, 8.0, 24.0, 0.0),
        child: ListView.builder(
          itemCount: 2,
          itemBuilder: (context, index) {
            return Container(
              margin: EdgeInsets.only(top: 16.0),
              height: 150,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(16.0),
                  color: Colors.white,
                  boxShadow: const [
                    BoxShadow(
                      color: Color(0xffD9D9D9),
                      blurRadius: 8,
                    )
                  ]
              ),
              child: Column(
                children: [
                  Container(
                    padding: EdgeInsets.fromLTRB(24.0, 0, 24.0, 0),
                    height: 48,
                    decoration: const BoxDecoration(
                      borderRadius:
                      BorderRadius.vertical(top: Radius.circular(16.0)),
                      color: Color(0xff00C220),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          '1호기',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 16.0,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        CircleAvatar(
                          backgroundImage: AssetImage('assets/images/working.gif'),
                          radius: 12.0,
                          backgroundColor: Color(0xff00C220),
                        ),
                      ],
                    ),
                  ),
                  Container(
                      margin: EdgeInsets.fromLTRB(24.0, 18.0, 24.0, 18.0),
                      decoration: const BoxDecoration(),
                      child: Column(
                        children: [
                          Row(
                            children: [
                              Text('파일명'),
                              SizedBox(width: 16.0),
                              Text(
                                '123.NC',
                                style: TextStyle(
                                  fontSize: 14.0,
                                  fontWeight: FontWeight.bold,
                                ),
                              )
                            ],
                          ),
                          SizedBox(
                            height: 8.0,
                          ),
                          Row(
                            children: [
                              Text('로그명'),
                              SizedBox(width: 16.0),
                              Text('작업 재시작',
                                  style: TextStyle(
                                      fontSize: 14.0,
                                      fontWeight: FontWeight.bold,
                                      color: Color(0xff00C220)
                                  )),
                            ],
                          ),
                          Row(
                            children: [
                              SizedBox(width: 56.0),
                              Text('2023-10-01 18:20:13',
                                  style: TextStyle(
                                    fontSize: 14.0,
                                    fontWeight: FontWeight.bold,
                                  )),
                            ],
                          ),
                        ],
                      )),
                ],
              ),
            );
          }
        ),
      ),
    );
  }
}


