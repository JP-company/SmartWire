import 'package:flutter/material.dart';

class LogDto with ChangeNotifier{
  final String log;
  final String date;
  final String logTime;
  final String file;
  final String machineName;

  LogDto({
    required this.log,
    required this.date,
    required this.logTime,
    required this.file,
    required this.machineName,
  });

  factory LogDto.fromJson(Map<dynamic, dynamic> json) {
    return LogDto(
      log: json['log'],
      date: json['date'],
      logTime: json['logTime'],
      file: json['file'],
      machineName: json['machineName'],
    );
  }
}