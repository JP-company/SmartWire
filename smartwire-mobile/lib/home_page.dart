import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:smartwire_mobile/alarm_setting_page.dart';
import 'package:smartwire_mobile/firebase/config/notification_config.dart';
import 'package:smartwire_mobile/local_storage/local_storage.dart';
import 'package:http/http.dart' as http;
import 'package:smartwire_mobile/login_page.dart';
import 'package:smartwire_mobile/member_page.dart';

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

  Future<bool> _deleteFCMTokenFromDB() async {
    var savedFCMToken = await LocalStorage.load("fcmToken");
    if (savedFCMToken != null) {
      var jwt = await LocalStorage.load("jwt");

      if (jwt != null) {
        Map<String, String> headers = {
          'Content-Type' : 'application/json',
          'Authorization': jwt
        };

        var data = jsonEncode({
          "fcmToken": savedFCMToken,
        });

        final response = await http.post(
          Uri.parse(
              'https://smartwire-backend-f39394ac6218.herokuapp.com/api/fcm_token/delete'),
          headers: headers,
          body: data,
        );

        print('reponse= ${response.statusCode}, ${response.body}');
        if (response.statusCode == 200) {
          return true;
        }
      }
    }
    return false;
  }
  Future<List<LogDto>> getLogList() async {
    Future<dynamic> future = LocalStorage.load("jwt");
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
        List<LogDto> logList = (json.decode(response.body) as List).map((item) => LogDto.fromJson(item)).toList();

        return logList;
      }
    }
    return <LogDto>[];
  }
  Future<void> refresh() async {
    setState(() {
      getLogList();
    });
  }

  @override
  void initState() {
    NotificationConfig.initializeAfterLogin(context);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    TimeOfDay timeOfDay = TimeOfDay.now();

    return Scaffold(
      appBar: AppBar(
        elevation: 0.5,
        backgroundColor: Colors.white,
        centerTitle: false,
        iconTheme: IconThemeData(color: Colors.black),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              'SMART WIRE',
              style: TextStyle(
                color: Colors.black,
                fontSize: 18.0,
                fontWeight: FontWeight.bold,
              ),
            ),

            Container(
              padding: EdgeInsets.all(4),
              decoration: BoxDecoration(
                // color: Colors.grey,
                borderRadius: BorderRadius.circular(4),
                border: Border.all(color: Colors.black, width: 2.0),
              ),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Icon(Icons.access_time_outlined,),
                  SizedBox(width: 2),
                  Text("${timeOfDay.hour.toString().padLeft(2, '0')}:${timeOfDay.minute.toString().padLeft(2, '0')}",
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 18.0,
                      fontWeight: FontWeight.w500
                    ),
                  ),
                ],
              ),
            )
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
            setState(() {
              getLogList();
            });
          },
        backgroundColor: Colors.black,
        child: Icon(Icons.refresh)
      ),

      endDrawer: SafeArea(
        child: Drawer(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
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
                            Navigator.push(context, MaterialPageRoute(builder: (context) => const MemberPage()));
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
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => const AlarmSettingPage()
                                )
                            );
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
                            _logout();
                          },
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              Container(
                height: 112,
                color: Color(0xffF4F4F4),
                child: Center(
                  child: Text("대표자 전정표\n문의 - 010-8714-4246\n전화, 문자 주시면 빠르게 답변 드리겠습니다.",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Color(0xff999999)
                    ),
                  ),
                ),
              )
            ],
          ),
        ),
      ),
      body: RefreshIndicator(
        color: Colors.black,
        onRefresh: () => refresh(),
        child: FutureBuilder<List<LogDto>>(
          future: getLogList(),
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              // 데이터 로딩 중인 경우의 화면
              return Center(child: CircularProgressIndicator(
                color: Colors.black,
              ));
            } else if (snapshot.hasError) {
              // 데이터 로딩 중에 오류가 발생한 경우
              print('Error: ${snapshot.error}');
              return Center(child: Text("네트워크 연결을 확인해주세요."));
            } else {
              // 데이터 로딩이 성공한 경우
              List<LogDto> logList = snapshot.data ?? [];
              // 로그 리스트를 사용하여 화면을 구성
              return Column(
                children: [
                  // SizedBox(height: 4.0),
                  // Text("새로 고침 시간: 2023-10-18 17:30:53",
                  //   style: TextStyle(
                  //     fontWeight: FontWeight.bold
                  //   ),
                  // ),
                  Flexible(
                    child: ListView.builder(
                        padding: EdgeInsets.only(top: 16.0,bottom: 24.0),
                        itemCount: logList.length,
                        itemBuilder: (context, index) {

                          var status = logList[index].log?.split("_")[0] ?? "none";
                          Color color = status == "start" ? Color(0xff00C220) :
                                        status == "stop" || status == "reset" ? Color(0xffFF6262) :
                                        status == "done" ? Color(0xff00A3FF) :
                                        Color(0xff666666);

                          String image = status == "start" ? "assets/images/working.gif" :
                                         status == "stop" || status == "reset" ? "assets/images/breaking.png" :
                                         status == "done" ? "assets/images/done.png" :
                                         "assets/images/error.png";
                          double fontSize = status == "none" ? 0.0 : 14.0;

                          var machineName = logList[index].machineName ?? "";
                          var file = logList[index].file ?? "-";
                          var log = logList[index].log?.split("_")[1] ?? "기계와의 연결을 확인해 주세요.";
                          var logDate = logList[index].date ?? "";
                          var logTime = logList[index].logTime ?? "";

                          // if (index == 0) {
                          //   return Center(
                          //     child: Column(
                          //       children: [
                          //         Text("새로 고침한 시간 = 17:30:14",
                          //           style: TextStyle(
                          //               fontWeight: FontWeight.bold
                          //           ),
                          //         ),
                          //         SizedBox(
                          //           height: 4.0,
                          //         )
                          //       ],
                          //     ),
                          //   );
                          // }

                          return Container(
                            margin: EdgeInsets.fromLTRB(24.0, 0.0, 24.0, 16.0),
                            decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(16.0),
                                color: Colors.white,
                                boxShadow: const [
                                  BoxShadow(
                                    color: Color(0xffD9D9D9),
                                    blurRadius: 8.0,
                                  )
                                ]
                            ),
                            child: Column(
                              children: [
                                Container(
                                  padding: EdgeInsets.fromLTRB(24.0, 0, 24.0, 0),
                                  height: 48,
                                  decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.vertical(top: Radius.circular(16.0)),
                                    color: color,
                                  ),
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Text(
                                        machineName,
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: 16.0,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                      CircleAvatar(
                                        backgroundImage: AssetImage(image),
                                        radius: 12.0,
                                        backgroundColor: color,
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
                                              file,
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
                                          crossAxisAlignment: CrossAxisAlignment.start,
                                          children: [
                                            Text('로그명'),
                                            SizedBox(width: 16.0),
                                            Column(
                                              crossAxisAlignment: CrossAxisAlignment.start,
                                              children: [
                                                Text(log,
                                                    style: TextStyle(
                                                        fontSize: 14.0,
                                                        fontWeight: FontWeight.bold,
                                                        color: color
                                                    )),
                                                Text(logDate + " " + logTime,
                                                    style: TextStyle(
                                                      fontSize: fontSize,
                                                      fontWeight: FontWeight.bold,
                                                    )),
                                              ],
                                            ),
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
                ],
              );
            }
          }
        ),
      ),
    );
  }

  void showSnackBar(BuildContext context, String text) {
    ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(text,
            textAlign: TextAlign.center,
          ),
          duration: const Duration(seconds: 2),
        )
    );
  }

  Future<void> _logout() async {
    return showDialog<void>(
      //다이얼 로그 위젯 소환
      context: context,
      barrierDismissible: false, // 다이얼 로그 이외의 바탕 눌러도 안꺼지도록 설정
      builder: (BuildContext context) {
        return AlertDialog(
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12.0)
          ),
          content: SingleChildScrollView(
              child: Center(
                  child: Text('로그아웃 하시겠습니까?')
              )
          ),
          actionsAlignment: MainAxisAlignment.center,
          actions: [
            TextButton(
              child: Text('확인'),
              // style: ElevatedButton.styleFrom(
              //   shape: RoundedRectangleBorder(
              //     borderRadius: BorderRadius.circular(12),
              //   ),
              //   backgroundColor: const Color(0xff00C220),
              //   elevation: 0.0,
              // ),
              onPressed: () async {
                var completelyLogout = await _deleteFCMTokenFromDB();
                if (completelyLogout) {

                  var jwt = await LocalStorage.save("jwt", "");
                  var fcmToken = await LocalStorage.save("fcmToken", "");

                  if (jwt&&fcmToken) {
                    Provider.of<JwtDto>(context, listen: false).jwtMemberDto = null;
                    Provider.of<JwtDto>(context, listen: false).machineDtoList = null;
                    Provider.of<JwtDto>(context, listen: false).jwt = null;

                    Navigator.of(context).pushAndRemoveUntil(
                        MaterialPageRoute(
                            builder: (context) => const LoginPage()
                        ), (route) => false
                    );
                    return;
                  }
                }
                showSnackBar(context, '로그아웃에 실패했습니다. 네트워크 연결을 확인해주세요.');
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: Text('취소'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }
}


