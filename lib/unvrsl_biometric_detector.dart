import 'unvrsl_biometric_detector_platform_interface.dart';

class UnvrslBiometricDetector {
  Future<String?> getPlatformVersion() {
    return UnvrslBiometricDetectorPlatform.instance.getPlatformVersion();
  }
}
