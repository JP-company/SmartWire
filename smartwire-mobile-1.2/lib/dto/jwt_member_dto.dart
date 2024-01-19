import 'package:flutter/material.dart';

class JwtMemberDto with ChangeNotifier{
  final int id;
  final String loginId;
  final String companyName;
  final String email;
  final String phoneNumber;
  final String role;

  JwtMemberDto({
    required this.id,
    required this.loginId,
    required this.companyName,
    required this.email,
    required this.phoneNumber,
    required this.role
  });

  factory JwtMemberDto.fromJson(Map<dynamic, dynamic> json) {
    return JwtMemberDto(
        id: json['id'],
        loginId: json['loginId'],
        companyName: json['companyName'],
        email: json['email'],
        phoneNumber: json['phoneNumber'],
        role: json['role'],
    );
  }
}