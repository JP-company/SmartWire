import 'package:shared_preferences/shared_preferences.dart';

class RememberMember {


  static Future<void> saveMemberJwt(String? jwt) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    await preferences.setString("jwt", jwt!);
  }

  static Future<dynamic> loadMemberJwt() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    var str = preferences.getString("jwt");
    return str;
  }
}