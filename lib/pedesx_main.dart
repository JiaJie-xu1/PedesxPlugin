import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:pedesxplugin/pedesx_response.dart';

import 'package:pedesxplugin/pedesxplugin.dart';

MethodChannel _channel = MethodChannel('pedesxplugin')
  ..setMethodCallHandler(_methodHandler);

StreamController<BasePedesxResponse> _pedesxResponseEventHandlerController =
    new StreamController.broadcast();

Stream<BasePedesxResponse> get pedesxResponseEventHandler =>
    _pedesxResponseEventHandlerController.stream;

Future _methodHandler(MethodCall methodCall) {
  var response =
      BasePedesxResponse.create(methodCall.method, methodCall.arguments);
  _pedesxResponseEventHandlerController.add(response);
  return Future.value();
}

Future<bool> initPedesxSdk({
  @required String appId, //矩步引领SDK APPId
  @required String shelf_id, //任务货架ID
  @required String csj_appId, //穿山甲APPID
  @required String csj_video_id, //穿山甲激励视频ID
  @required String ylh_appId, //优量汇APPID
  @required String ylh_video_id //优量汇激励视频ID

}) async {
  return await _channel.invokeMethod("registerPedesx", {
    "appId": appId,
    "shelf_id": shelf_id,
    "csj_appId": csj_appId,
    "csj_video_id": csj_video_id,
    "ylh_appId":ylh_appId,
    "ylh_video_id":ylh_video_id
  });
}

Future<bool> initPedesxSdkUser({
  @required String uid,
  @required String oaid,
}) async {
  return await _channel
      .invokeMethod("registerPedesxUser", {"uid": uid, "oaid": oaid});
}

Future<bool> startPedesxVideoActivity() async {
  return await _channel.invokeMethod("startPedesxVideoActivity");
}

Future<String> get platformVersion async {
  final String version = await _channel.invokeMethod('getPlatformVersion');
  return version;
}
