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
  // String _alarmStartTimeString = "";
  // late String _alarmLastTimeString;
  //
  // void _setAlarmTime() async {
  //   String alarmTime = await LocalStorage.load("alarmSetting");
  //   _alarmStartTimeString = alarmTime.split(",")[0];
  //   _alarmLastTimeString = alarmTime.split(",")[1];
  //
  //   _alarmStartTime = TimeOfDay(
  //       hour: int.parse(_alarmStartTimeString.split(":")[0]),
  //       minute: int.parse(_alarmStartTimeString.split(":")[1])
  //   );
  //
  //   _alarmLastTime = TimeOfDay(
  //       hour: int.parse(_alarmLastTimeString.split(":")[0]),
  //       minute: int.parse(_alarmLastTimeString.split(":")[1])
  //   );
  // }

  @override
  void initState() {
    // _setAlarmTime();
    super.initState();
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
      body: Column(
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
                          value: true,
                          onChanged: (value) {},
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
                                  Future<TimeOfDay?> selectedTime = showTimePicker(
                                    context: context,
                                    initialTime: TimeOfDay(hour: 21, minute: 00),
                                    confirmText: '확인',
                                    cancelText: '취소',
                                    errorInvalidText: '오류',
                                    hourLabelText: '',
                                    minuteLabelText: '',
                                    helpText: '알림 시작 시각',
                                    initialEntryMode: TimePickerEntryMode.inputOnly,
                                  );
                                  selectedTime.then((timeOfDay){
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
                                        borderRadius: BorderRadius.circular(999.0)
                                    )
                                ),
                                child: Text("1",
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
                                    initialTime: TimeOfDay(hour: 23, minute: 00),
                                    confirmText: '확인',
                                    cancelText: '취소',
                                    errorInvalidText: '오류',
                                    hourLabelText: '',
                                    minuteLabelText: '',
                                    helpText: '알림 종료 시각',
                                    initialEntryMode: TimePickerEntryMode.inputOnly,
                                  );
                                },

                                style: ElevatedButton.styleFrom(
                                    minimumSize: Size(86, 32),
                                    backgroundColor: Color(0xffF4F4F4),
                                    elevation: 0.0,
                                    shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(999.0)
                                    )
                                ),
                                child: Text("2",
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
      ),
    );
  }
}
