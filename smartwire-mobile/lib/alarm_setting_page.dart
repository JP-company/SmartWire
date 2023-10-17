import 'package:flutter/material.dart';
import 'package:smartwire_mobile/local_storage/local_storage.dart';

class AlarmSettingPage extends StatefulWidget {
  const AlarmSettingPage({Key? key}) : super(key: key);

  @override
  State<AlarmSettingPage> createState() => _AlarmSettingPageState();
}

class _AlarmSettingPageState extends State<AlarmSettingPage> {
  // late TimeOfDay _alarmStartTime;
  // late TimeOfDay _alarmLastTime;

  late bool _isAlarmTrue;
  late String _alarmStartTimeStr;
  late String _alarmLastTimeStr;

  Future<String> _getAlarmTime() async {
    var alarmSetting = await LocalStorage.load("alarmSetting");
    return alarmSetting;
  }

  void _setAlarmTime(String savedAlarmTime) {
    // y21:00,23:00
    _isAlarmTrue = savedAlarmTime[0] == "y" ? true : false;
    _alarmStartTimeStr = savedAlarmTime.substring(1).split(",")[0];
    _alarmLastTimeStr = savedAlarmTime.substring(1).split(",")[1];
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
        future: _getAlarmTime(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return CircularProgressIndicator(); // 로딩 표시
          } else if (snapshot.hasError) {
            return Text("데이터를 불러오는 중 오류가 발생했습니다.");
          } else {
            _setAlarmTime(snapshot.data.toString());
            return Column(
              children: [
                Container(
                  margin: EdgeInsets.fromLTRB(24.0, 24.0, 24.0, 32.0),
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
                                  value: _isAlarmTrue,
                                  onChanged: (value) {
                                    _isAlarmTrue = value;
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
                                        Future<
                                            TimeOfDay?> selectedTime = showTimePicker(
                                          context: context,
                                          initialTime: TimeOfDay(
                                              hour: 21, minute: 00),
                                          confirmText: '확인',
                                          cancelText: '취소',
                                          errorInvalidText: '오류',
                                          hourLabelText: '',
                                          minuteLabelText: '',
                                          helpText: '알림 시작 시각',
                                          initialEntryMode: TimePickerEntryMode
                                              .inputOnly,
                                        );
                                        selectedTime.then((timeOfDay) {
                                          if (timeOfDay != null) {
                                            setState(() {
                                              // _alarmStartTime = timeOfDay;
                                            });
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
                                      child: Text(_alarmStartTimeStr,
                                        style: TextStyle(
                                            color: Colors.black
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
                                        Future selectedTime = showTimePicker(
                                          context: context,
                                          initialTime: TimeOfDay(
                                              hour: 23, minute: 00),
                                          confirmText: '확인',
                                          cancelText: '취소',
                                          errorInvalidText: '오류',
                                          hourLabelText: '',
                                          minuteLabelText: '',
                                          helpText: '알림 종료 시각',
                                          initialEntryMode: TimePickerEntryMode
                                              .inputOnly,
                                        );
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
                                      child: Text(_alarmLastTimeStr,
                                        style: TextStyle(
                                            color: Colors.black
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
                ElevatedButton(
                    onPressed: () {},
                    child: Text("저장하기")
                ),
                Text('설정하신 시간대 내에서만\n기계 상태 알림이 발송됩니다.',
                  style: TextStyle(
                    color: Color(0xff999999),
                  ),
                  textAlign: TextAlign.center,
                )
              ],
            );
          }
        }
      ),
    );
  }
}
