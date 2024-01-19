import 'package:flutter/material.dart';

class MachineDto with ChangeNotifier{
  final int id;
  final String machineName;
  final int sequence;
  final bool selected;
  // final String memberId;
  // final String machineModel;
  // final String dateManufacture;


  MachineDto({
    required this.id,
    required this.machineName,
    required this.sequence,
    required this.selected,
    // required this.machineModel,
    // required this.dateManufacture,
    // required this.memberId
  });

  factory MachineDto.fromJson(Map<dynamic, dynamic> json) {
    return MachineDto(
      id: json['id'],
      machineName: json['machineName'],
      sequence: json['sequence'],
      selected: json['selected'],
      // machineModel: json['machineModel'],
      // dateManufacture: json['dateManufacture'],
      // memberId: json['memberId'],
    );
  }
}