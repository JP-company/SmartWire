import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'dto/jwt_dto.dart';

class MemberPage extends StatelessWidget {
  const MemberPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.5,
        backgroundColor: Colors.white,
        iconTheme: IconThemeData(color: Colors.black),
        title: Text(
          '계정 정보',
          style: TextStyle(
            color: Colors.black,
            fontSize: 18.0,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: Container(
        padding: EdgeInsets.all(24.0),
        child: Column(
          children: [
            accountBlock('아이디', Provider.of<JwtDto>(context).jwtMemberDto!.loginId),
            accountBlock('이메일', Provider.of<JwtDto>(context).jwtMemberDto!.email),
            accountBlock('회사명', Provider.of<JwtDto>(context).jwtMemberDto!.companyName),
            accountBlock('연락처', Provider.of<JwtDto>(context).jwtMemberDto!.phoneNumber),
            Container(
              margin: EdgeInsets.only(top: 32.0),
              height: 62,
              width: 286,
              decoration: BoxDecoration(
                color: Color(0xffF4F4F4),
                borderRadius: BorderRadius.circular(8.0),
              ),
              alignment: Alignment.center,
              child: Text(
                '계정 정보 변경은 PC에서 가능합니다.',
                style: TextStyle(
                    color: Color(0xff999999),
                    fontSize: 14.0
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}

Widget accountBlock(String leading, String value) {
  return Column(
    children: [
      Row(
        children: [
          Container(
            padding: EdgeInsets.all(18.0),
            child: Text(
              leading,
              style: TextStyle(
                fontSize: 14.0,
                color: Color(0xff999999),
              ),
            ),
          ),
          Text(
            value,
            style: TextStyle(
              fontSize: 14.0,
              color: Colors.black,
            ),
          ),
        ],
      ),
      Divider(
        thickness: 1.0,
        height: 1.0,
        color: Color(0xffF4F4F4),
      ),
    ],
  );
}
