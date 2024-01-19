import 'package:flutter/material.dart';


class AlarmSettingDto with ChangeNotifier { // with 는 기능만 가져와 쓸 수 있게 해줌
  int? id;
  String? fcmToken;

  var isAlarmTrue;
  var startTime;
  var lastTime;

  AlarmSettingDto({
    String? fcmToken,
    bool? isAlarmTrue,
    TimeOfDay? startTime,
    TimeOfDay? lastTime,
  }) {
    fcmToken = fcmToken;
    isAlarmTrue = isAlarmTrue;
    startTime = startTime;
    lastTime = lastTime;
  }

  void changeStartTime(TimeOfDay time) {
    startTime = time;
    notifyListeners();
  }

  void changeLastTime(TimeOfDay time) {
    lastTime = time;
    notifyListeners();
  }

  void changeAlarmBool(bool changeBool) {
    isAlarmTrue = changeBool;
    notifyListeners();
  }

  // void changeAlarmSettingStr(String alarmSetting) {
  //   alarmSettingStr = alarmSetting;
  //   notifyListeners();
  // }
}