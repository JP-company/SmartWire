import 'dart:async';
import 'dart:convert';

import 'package:provider/provider.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:smartwire_mobile/dto/jwt_dto.dart';
import 'package:smartwire_mobile/home_page.dart';
import 'package:smartwire_mobile/local_storage/local_storage.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {

  TextEditingController ctrId = TextEditingController();
  TextEditingController ctrPw = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Builder(
          builder: (context) {
                return GestureDetector(
                  onTap: () {
                    FocusScope.of(context).unfocus();
                  },
                  child: Center(
                    child: SingleChildScrollView(
                        child: Column(
                          children: [
                            const CircleAvatar(
                              backgroundImage: AssetImage('assets/images/smartwire.png'),
                              radius: 32.0,
                            ),
                            const SizedBox(
                              height: 16.0,
                            ),
                            const Text(
                              'SMART WIRE',
                              style: TextStyle(
                                  color: Color(0xff00C220),
                                  fontSize: 24.0,
                                  fontWeight: FontWeight.bold
                              ),
                            ),
                            const SizedBox(
                              height: 54.0,
                            ),
                            Form(
                              child: Theme(
                                data: ThemeData(
                                    primaryColor: Colors.black,
                                    inputDecorationTheme: InputDecorationTheme(
                                        labelStyle: TextStyle(
                                            color: Colors.teal,
                                            fontSize: 14.0
                                        )
                                    )
                                ),
                                child: Column(
                                  children: [
                                    SizedBox(
                                      width: 320.0,
                                      height: 48.0,
                                      child: TextField(
                                        autofocus: true,
                                        controller: ctrId,
                                        style: TextStyle(
                                            fontSize: 14.0
                                        ),
                                        decoration: const InputDecoration(
                                          labelText: '아이디',
                                          labelStyle: TextStyle(
                                              color: Colors.grey),
                                          border: OutlineInputBorder(
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(8.0)),
                                            borderSide: BorderSide(width: 1,
                                                color: Color(0xffCACACA)),
                                          ),
                                          focusedBorder: OutlineInputBorder(
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(8.0)),
                                            borderSide: BorderSide(width: 2,
                                                color: Color(0xff000000)),
                                          ),
                                        ),
                                        keyboardType: TextInputType.text,
                                      ),
                                    ),
                                    SizedBox(
                                      height: 12.0,
                                    ),
                                    SizedBox(
                                      width: 320.0,
                                      height: 48.0,
                                      child: TextField(
                                        controller: ctrPw,
                                        style: TextStyle(
                                            fontSize: 14.0
                                        ),
                                        decoration: const InputDecoration(
                                          labelText: '비밀번호',
                                          labelStyle: TextStyle(
                                              color: Colors.grey),
                                          border: OutlineInputBorder(
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(8.0)),
                                            borderSide: BorderSide(width: 1,
                                                color: Color(0xffCACACA)),
                                          ),
                                          focusedBorder: OutlineInputBorder(
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(8.0)),
                                            borderSide: BorderSide(width: 2,
                                                color: Color(0xff000000)),
                                          ),
                                        ),
                                        keyboardType: TextInputType.text,
                                        obscureText: true,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                            SizedBox(
                              height: 12.0,
                            ),
                            ElevatedButton(
                              onPressed: () {
                                if (ctrId.text.isEmpty) {
                                  showSnackBar(context, '아이디를 입력해주세요');
                                  return;
                                }
                                if (ctrPw.text.isEmpty) {
                                  showSnackBar(context, '비밀번호를 입력해주세요');
                                  return;
                                }
                                login(context, ctrId.text, ctrPw.text);
                              },
                              style: ElevatedButton.styleFrom(
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(800),
                                ),
                                minimumSize: const Size(320, 50),
                                backgroundColor: const Color(0xff00C220),
                                elevation: 0.0,
                              ),
                              child: const Text(
                                '로그인',
                                style: TextStyle(
                                    fontSize: 16.0,
                                    fontWeight: FontWeight.w600
                                ),
                              ),
                            ),
                          ],
                        )
                    ),
                  ),
                );
              }
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

  Future<void> login(BuildContext context, String loginId, String loginPassword) async {
    final Map<String, String> headers = {
      'Content-Type' : 'application/json',
      'Accept' : 'application/json',
    };
    final data = jsonEncode({
      "loginId": loginId,
      "loginPassword": loginPassword,
    });

    final response = await http.post(
        Uri.parse('https://smartwire-backend-f39394ac6218.herokuapp.com/api/login'),
        headers: headers,
        body: data
    );

    if (response.statusCode == 200) {
        String? jwt = response.headers['authorization'];

        /// 로그인 성공 시
        /// Local Storage - JWT Token 저장
        await LocalStorage.save("jwt", jwt);

        /// 받은 정보를 JwtDto 로 변환
        JwtDto jwtDto = JwtDto.fromJson(json.decode(response.body));

        /// Provider - JwtDto( List<MachineDto>, JwtMemberDto, String jwt )저장
        JwtDto jwtDtoProvider = Provider.of<JwtDto>(context, listen: false);
        jwtDtoProvider.jwt = jwt;
        jwtDtoProvider.machineDtoList = jwtDto.machineDtoList;
        jwtDtoProvider.jwtMemberDto = jwtDto.jwtMemberDto;

        /// HomePage 이동
        Navigator.of(context).pushAndRemoveUntil(
            MaterialPageRoute(
                builder: (context) => const HomePage()
            ),
                (route) => false
        );
        return;
    }

    showSnackBar(context, '아이디 혹은 비밀번호를 확인해주세요.');
  }

}




