import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'unvrsl_biometric_detector_platform_interface.dart';

/// An implementation of [UnvrslBiometricDetectorPlatform] that uses method channels.
class MethodChannelUnvrslBiometricDetector
    extends UnvrslBiometricDetectorPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('unvrsl_biometric_detector');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
