import 'package:flutter_test/flutter_test.dart';
import 'package:unvrsl_biometric_detector/unvrsl_biometric_detector.dart';
import 'package:unvrsl_biometric_detector/unvrsl_biometric_detector_platform_interface.dart';
import 'package:unvrsl_biometric_detector/unvrsl_biometric_detector_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockUnvrslBiometricDetectorPlatform
    with MockPlatformInterfaceMixin
    implements UnvrslBiometricDetectorPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final UnvrslBiometricDetectorPlatform initialPlatform = UnvrslBiometricDetectorPlatform.instance;

  test('$MethodChannelUnvrslBiometricDetector is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelUnvrslBiometricDetector>());
  });

  test('getPlatformVersion', () async {
    UnvrslBiometricDetector unvrslBiometricDetectorPlugin = UnvrslBiometricDetector();
    MockUnvrslBiometricDetectorPlatform fakePlatform = MockUnvrslBiometricDetectorPlatform();
    UnvrslBiometricDetectorPlatform.instance = fakePlatform;

    expect(await unvrslBiometricDetectorPlugin.getPlatformVersion(), '42');
  });
}
