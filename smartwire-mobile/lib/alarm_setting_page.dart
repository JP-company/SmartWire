import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:smartwire_mobile/dto/jwt_dto.dart';
import 'package:smartwire_mobile/local_storage/local_storage.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'package:smartwire_mobile/alarm_setting_page.dart';

import 'dto/alarm_setting_dto.dart';

class AlarmSettingPage extends StatefulWidget {
  const AlarmSettingPage({Key? key}) : super(key: key);

  @override
  State<AlarmSettingPage> createState() => _AlarmSettingPageState();
}

class _AlarmSettingPageState extends State<AlarmSettingPage> {
  var startTime;
  var lastTime;
  var isAlarmTrue;
  bool isInit = false;

  String formatTimeOfDay(TimeOfDay time) {
    final hour = time.hourOfPeriod;
    final minute = time.minute;
    final period = time.period == DayPeriod.am ? '오전' : '오후';
    return '$period ${hour.toString().padLeft(2, '0')}:${minute.toString().padLeft(2, '0')}';
  }
  String makeAlarmSettingStr(bool isTrue, TimeOfDay time1, TimeOfDay time2) {
    int hour1 = time1.hourOfPeriod;
    final minute1 = time1.minute;
    int hour2 = time2.hourOfPeriod;
    final minute2 = time2.minute;
    String isAlarm = isTrue? 'y' : 'n';

    if (time1.period == DayPeriod.am && hour1 == 12) {
      hour1 = 0;
    }
    if (time1.period == DayPeriod.pm && hour1 != 12) {
      hour1 += 12;
    }
    if (time2.period == DayPeriod.am && hour2 == 12) {
      hour2 = 0;
    }
    if (time2.period == DayPeriod.pm && hour2 != 12) {
      hour2 += 12;
    }
    return '$isAlarm$hour1:$minute1,$hour2:$minute2';
  }
  TimeOfDay getTimeOfDayFromStr(String timeStr) {
    return TimeOfDay(
        hour: int.parse(timeStr.split(":")[0]),
        minute: int.parse(timeStr.split(":")[1])
    );
  }

  Future<void> getAlarmSettingFromLocalStorage() async {
    if (!isInit) {
      String alarmSettingStr = await LocalStorage.load("alarmSetting");
      var providerAlarmSetting = Provider.of<AlarmSettingDto>(context, listen: false);
      providerAlarmSetting.isAlarmTrue = alarmSettingStr[0] == 'y' ? true : false;
      providerAlarmSetting.startTime = getTimeOfDayFromStr(alarmSettingStr.substring(1).split(",")[0]);
      providerAlarmSetting.lastTime = getTimeOfDayFromStr(alarmSettingStr.substring(1).split(",")[1]);
      isInit = true;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.5,
        backgroundColor: Colors.white,
        iconTheme: IconThemeData(color: Colors.black),
        title: Text(
          '알림 설정',
          style: TextStyle(
            color: Colors.black,
            fontSize: 18.0,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: FutureBuilder(
        future: getAlarmSettingFromLocalStorage(),
        builder: (context, snapshot) {
            return Column(
              children: [
                Container(
                  margin: EdgeInsets.fromLTRB(24.0, 24.0, 24.0, 0.0),
                  decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(8.0),
                      boxShadow: [
                        BoxShadow(
                          color: Color(0xffD9D9D9),
                          blurRadius: 8,
                        )
                      ]
                  ),
                  child: Container(
                    padding: EdgeInsets.fromLTRB(24, 8, 24, 8),
                    child: Column(
                      children: [
                        Container(
                            height: 56.0,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("알림 허용",
                                  style: const TextStyle(
                                    fontSize: 14.0,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                Switch(
                                  value: Provider.of<AlarmSettingDto>(context).isAlarmTrue,
                                  onChanged: (value) {
                                    Provider.of<AlarmSettingDto>(context, listen: false).changeAlarmBool(value);
                                  },
                                )
                              ],
                            )
                        ),
                        Divider(
                          thickness: 1.0,
                          height: 1.0,
                          color: Color(0xffF4F4F4),
                        ),
                        Container(
                            height: 56.0,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("알림 시간",
                                  style: const TextStyle(
                                    fontSize: 14.0,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                Row(
                                  children: [
                                    ElevatedButton(
                                      onPressed: () {
                                        if (!Provider.of<AlarmSettingDto>(context, listen: false).isAlarmTrue) {
                                          return null;
                                        }
                                        Future<TimeOfDay?> selectedTime = showTimePicker(
                                          context: context,
                                          initialTime: Provider.of<AlarmSettingDto>(context, listen: false).startTime,
                                          confirmText: '확인',
                                          cancelText: '취소',
                                          errorInvalidText: '',
                                          hourLabelText: '',
                                          minuteLabelText: '',
                                          helpText: '알림 시작 시각',
                                          initialEntryMode: TimePickerEntryMode.input,
                                        );

                                        selectedTime.then((timeOfDay) {
                                          if (timeOfDay != null) {
                                            Provider.of<AlarmSettingDto>(context, listen: false).changeStartTime(timeOfDay);
                                          }
                                        });
                                      },
                                      style: ElevatedButton.styleFrom(
                                          minimumSize: Size(56, 32),
                                          backgroundColor: Color(0xffF4F4F4),
                                          elevation: 0.0,
                                          shape: RoundedRectangleBorder(
                                              borderRadius: BorderRadius
                                                  .circular(999.0)
                                          )
                                      ),
                                      child: Text(formatTimeOfDay(Provider.of<AlarmSettingDto>(context).startTime),
                                        style: TextStyle(
                                            color: Provider.of<AlarmSettingDto>(context).isAlarmTrue ? Colors.black : Color(0xffCCCCCC)
                                        ),
                                      ),
                                    ),
                                    SizedBox(
                                      width: 6.0,
                                    ),
                                    Text('-'),
                                    SizedBox(
                                      width: 6.0,
                                    ),
                                    ElevatedButton(
                                      onPressed: () {
                                        if (!Provider.of<AlarmSettingDto>(context, listen: false).isAlarmTrue) {
                                          return null;
                                        }
                                        Future selectedTime = showTimePicker(
                                          context: context,
                                          initialTime: Provider.of<AlarmSettingDto>(context, listen: false).lastTime,
                                          confirmText: '확인',
                                          cancelText: '취소',
                                          hourLabelText: '',
                                          errorInvalidText: '',
                                          minuteLabelText: '',
                                          helpText: '알림 종료 시각',
                                          initialEntryMode: TimePickerEntryMode.input,
                                        );

                                        selectedTime.then((timeOfDay) {
                                          if (timeOfDay != null) {
                                            Provider.of<AlarmSettingDto>(context, listen: false).changeLastTime(timeOfDay);
                                          }
                                        });
                                      },

                                      style: ElevatedButton.styleFrom(
                                          minimumSize: Size(56, 32),
                                          backgroundColor: Color(0xffF4F4F4),
                                          elevation: 0.0,
                                          shape: RoundedRectangleBorder(
                                              borderRadius: BorderRadius
                                                  .circular(999.0)
                                          )
                                      ),
                                      child: Text(formatTimeOfDay(Provider.of<AlarmSettingDto>(context).lastTime),
                                        style: TextStyle(
                                            color: Provider.of<AlarmSettingDto>(context).isAlarmTrue ? Colors.black : Color(0xffCCCCCC)
                                        ),
                                      ),
                                    ),
                                  ],
                                )
                              ],
                            )
                        ),
                      ],
                    ),
                  ),
                ),
                SizedBox(height: 32.0,),
                ElevatedButton(
                  onPressed: () async {
                    String alarmSettingStr = makeAlarmSettingStr(
                        Provider.of<AlarmSettingDto>(context, listen: false).isAlarmTrue,
                        Provider.of<AlarmSettingDto>(context, listen: false).startTime,
                        Provider.of<AlarmSettingDto>(context, listen: false).lastTime
                    );

                    var jwt = Provider.of<JwtDto>(context, listen: false).jwt;
                    var fcmToken = Provider.of<AlarmSettingDto>(context, listen: false).fcmToken;

                    bool savedServer = await saveAlarmSettingToServer(jwt, fcmToken, alarmSettingStr);
                    if (savedServer) {
                      bool savedLocalStorage = await LocalStorage.save("alarmSetting", alarmSettingStr);
                      if (savedLocalStorage){
                        showSnackBar(context, "저장되었습니다.");
                      }
                    }

                  },
                  child: Text("저장하기",
                    style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w500
                    ),
                  ),
                  style: ElevatedButton.styleFrom(
                      minimumSize: Size(MediaQuery.of(context).size.width * 0.9, 46),
                      backgroundColor: Colors.black,
                      elevation: 0.0,
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(999.0)
                      )
                  ),

                ),
                SizedBox(height: 40,),
                Container(
                  height: 80,
                  width: 240,
                  decoration: BoxDecoration(
                    color: Color(0xffF4F4F4),
                    borderRadius: BorderRadius.circular(8.0),
                  ),
                  alignment: Alignment.center,
                  child: Text(
                    '설정하신 시간대 내에서만\n기계 상태 알림이 발송됩니다.',
                    style: TextStyle(
                        color: Color(0xff999999),
                        fontSize: 14.0
                    ),
                    textAlign: TextAlign.center,
                  ),
                ),

                Expanded(child: SizedBox()),
                SizedBox(height: 80,)
              ],
            );

        }
      ),

    );
  }


  void showSnackBar(BuildContext context, String text) {
    final overlay = Overlay.of(context);
    final overlayEntry = OverlayEntry(
      builder: (context) => Positioned(
        top: MediaQuery.of(context).size.height * 0.5 - 20, // 화면 중앙에서 약간 위쪽에 배치
        left: MediaQuery.of(context).size.width * 0.2, // 화면의 25%부터 시작
        child: Material(
          color: Colors.transparent,
          child: Container(
            width: MediaQuery.of(context).size.width * 0.6, // 화면의 80% 너비
            height: 80,
            padding: EdgeInsets.symmetric(horizontal: 24.0, vertical: 12.0),
            decoration: BoxDecoration(
              color: Colors.black,
              borderRadius: BorderRadius.circular(8), // 모서리 둥글게
              border: Border.all(color: Colors.black, width: 2)
            ),
            child: Center(
              child: Text(
                text,
                textAlign: TextAlign.center,
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 16
                ),
              ),
            ),
          ),
        ),
      ),
    );

    overlay.insert(overlayEntry);

    Future.delayed(Duration(seconds: 2), () {
      overlayEntry.remove();
    });
  }


  Future<bool> saveAlarmSettingToServer(var jwt, var fcmToken, String alarmSetting) async {

    Map<String, String> headers = {
      'Content-Type' : 'application/json',
      'Authorization': jwt
    };
    var data = jsonEncode({
      "fcmToken": fcmToken,
      "alarmSetting" : alarmSetting,
    });

    final response = await http.post(
      Uri.parse(
          'https://smartwire-backend-f39394ac6218.herokuapp.com/api/fcm_token/update'),
      headers: headers,
      body: data,
    );

    print('response= ${response.statusCode}, ${response.body}');

    if (response.statusCode == 200) {
      return true;
    }
    return false;
  }

}
