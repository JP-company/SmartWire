import 'package:flutter/material.dart';

import 'jwt_member_dto.dart';
import 'machine_dto.dart';

class JwtDto with ChangeNotifier {
  List<MachineDto>? _machineDtoList;
  JwtMemberDto? _jwtMemberDto;

  JwtDto({
    List<MachineDto>? machineDtoList,
    JwtMemberDto? jwtMemberDto,
  }) {
    _machineDtoList = machineDtoList;
    _jwtMemberDto = jwtMemberDto;
  }

  factory JwtDto.fromJson(Map<dynamic, dynamic> json) {
    List<dynamic> body = json['machineDtoList'];
    List<MachineDto> allInfo = body.map((dynamic item) => MachineDto.fromJson(item)).toList();
    return JwtDto(
      machineDtoList: allInfo,
      jwtMemberDto: JwtMemberDto.fromJson(json['jwtMemberDto']),
    );
  }

  List<MachineDto>? get machineDtoList => _machineDtoList;

  set machineDtoList(List<MachineDto>? input_machineDtoList) {
    _machineDtoList = input_machineDtoList;
    notifyListeners();
  }

  JwtMemberDto? get jwtMemberDto => _jwtMemberDto;

  set jwtMemberDto(JwtMemberDto? input_jwtMemberDto) {
    _jwtMemberDto = input_jwtMemberDto;
    notifyListeners();
  }
}
