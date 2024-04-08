import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'unvrsl_biometric_detector_method_channel.dart';

abstract class UnvrslBiometricDetectorPlatform extends PlatformInterface {
  /// Constructs a UnvrslBiometricDetectorPlatform.
  UnvrslBiometricDetectorPlatform() : super(token: _token);

  static final Object _token = Object();

  static UnvrslBiometricDetectorPlatform _instance = MethodChannelUnvrslBiometricDetector();

  /// The default instance of [UnvrslBiometricDetectorPlatform] to use.
  ///
  /// Defaults to [MethodChannelUnvrslBiometricDetector].
  static UnvrslBiometricDetectorPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [UnvrslBiometricDetectorPlatform] when
  /// they register themselves.
  static set instance(UnvrslBiometricDetectorPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
