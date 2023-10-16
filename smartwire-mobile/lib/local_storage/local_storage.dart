import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LocalStorage {

  static Future<void> save(String key, String? value) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    await preferences.setString(key, value!);
  }

  static Future<dynamic> load(String key) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    var str = preferences.getString(key);
    return str;
  }
}