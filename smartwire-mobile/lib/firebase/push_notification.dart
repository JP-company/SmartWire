// import 'package:flutter_local_notifications/flutter_local_notifications.dart';
//
// class PushNotification {
//   static final _notifications = FlutterLocalNotificationsPlugin();
//
//   static Future _notificationDetails() async {
//     return const NotificationDetails(
//       android: AndroidNotificationDetails(
//           'channel id',
//           'channel name',
//           importance: Importance.max
//       ),
//       iOS: DarwinNotificationDetails(
//         presentSound : true,
//         presentAlert: true,
//         presentBadge: true,
//       ),
//     );
//   }
//
//   static Future showNotification({
//     int id = 0,
//     String? title,
//     String? body,
//   }) async =>
//       _notifications.show(
//         id,
//         title,
//         body,
//         await _notificationDetails(),
//       );
//
// }