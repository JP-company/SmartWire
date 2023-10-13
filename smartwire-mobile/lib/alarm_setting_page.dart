import 'package:flutter/material.dart';

class AlarmSettingPage extends StatelessWidget {
  const AlarmSettingPage({Key? key}) : super(key: key);

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
            // height: 128.0,
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
                  container('알림 허용', false, context),
                  Divider(
                    thickness: 1.0,
                    height: 1.0,
                    color: Color(0xffF4F4F4),
                  ),
                  container('알림 시간', true, context),
                ],
              ),
            ),
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



Widget container(String title, bool time, BuildContext context) {
  Row row = Row(
    mainAxisAlignment: MainAxisAlignment.spaceBetween,
    children: [
      Text(title,
        style: const TextStyle(
          fontSize: 14.0,
          fontWeight: FontWeight.bold,
        ),
      ),
    ],
  );
  if (!time) {
    row.children.add(
        Switch(
          value: true,
          onChanged: (value) {},
        )
    );
  } else {
    row.children.add(
        Row(
          children: [
            ElevatedButton(
              onPressed: () {
                Future selectedTime = showTimePicker(
                  context: context,
                  initialTime: TimeOfDay(hour: 21, minute: 0),
                  confirmText: '확인',
                  cancelText: '취소',
                  errorInvalidText: '오류',
                  hourLabelText: '',
                  minuteLabelText: '',
                  helpText: '알림 시작 시각',
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
              child: Text('오후 09:00',
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
                  initialTime: TimeOfDay(hour: 0, minute: 0),
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
              child: Text('오전 12:00',
                style: TextStyle(
                    color: Colors.black
                ),
              ),
            ),
          ],
        )
    );
  }

  return Container(
      height: 56.0,
      child: row
  );
}