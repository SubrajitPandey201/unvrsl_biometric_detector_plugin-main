import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:unvrsl_biometric_detector/unvrsl_biometric_detector_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelUnvrslBiometricDetector platform = MethodChannelUnvrslBiometricDetector();
  const MethodChannel channel = MethodChannel('unvrsl_biometric_detector');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
