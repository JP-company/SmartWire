import 'package:shared_preferences/shared_preferences.dart';

class LocalStorage {

  static Future<dynamic> save(String key, String? value) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    var bool = await preferences.setString(key, value!);
    return bool;
  }

  static Future<dynamic> load(String key) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    var str = preferences.getString(key);
    return str;
  }
}