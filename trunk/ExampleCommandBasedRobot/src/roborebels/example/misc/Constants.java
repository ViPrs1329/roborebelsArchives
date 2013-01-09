/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.misc;

/**
 *
 */
public interface Constants {

    public static final int kTeamNumber = 1329;
    public static final double kMaxBatteryVoltage = 13.0;
    public static final double kArmSpeed = 1.0;
    public static final double kArmSlowSpeed = 0.4;
    public static final double kArmLatchSpeed = 0.8;
    public static final double kTimeFeedOneBall = 0.4;
    public static final double kShooterUp = 1.0;
    public static final double kShooterDown = -1.0;
    public static final boolean kShooterForceFeed = true;
    public static final double kRobotWidth = 38;
    public static final double kRobotLength = 28;
    public static final double kRobotBumpers = 3 + 3;
    public static final double kRobotWidthWithBumpers = kRobotWidth + kRobotBumpers;
    public static final double kRobotLengthWithBumpers = kRobotLength + kRobotBumpers;
    public static final double kRobotEdgeToSonar = 4;
    public static final double kWheelRadius = 3.0;
    public static final double kWheelCircumference = 2 * Math.PI * kWheelRadius;
    public static final double kShooterHeight = 43;
    public static final double kHoopHeightTop = 98;
    public static final double kHoopHeightMid = 61;
    public static final double kHoopHeightLow = 28;
    public static final double kHoopInnerRadius = 9;
    public static final double kHoopInnerDiameter = kHoopInnerRadius * 2;
    public static final double kJoystickThreshold = 0.2;
// Gamepad axes
    public static final int kGamepadAxisLeftStickX = 1;
    public static final int kGamepadAxisLeftStickY = 2;
    public static final int kGamepadAxisTrigger = 3;
    public static final int kGamepadAxisRightStickX = 4;
    public static final int kGamepadAxisRightStickY = 5;
    public static final int kGamepadAxisDpadX = 6;
// Gamepad buttons
    public static final int kGamepadButtonA = 1;	// / Y \
    public static final int kGamepadButtonB = 2;	// | X + B |
    public static final int kGamepadButtonX = 3;	// \ A /
    public static final int kGamepadButtonY = 4;
    public static final int kGamepadButtonLB = 5;
    public static final int kGamepadButtonRB = 6;
    public static final int kGamepadButtonBack = 7;
    public static final int kGamepadButtonStart = 8;
    public static final int kGamepadButtonLeftStick = 9;
    public static final int kGamepadButtonRightStick = 10;
// Joystick (Extreme 3D Pro) axes
    public static final int kJoystickAxisX = 1;
    public static final int kJoystickAxisY = 2;
    public static final int kJoystickAxisZ = 3;
    public static final int kJoystickAxisThrottle = 4;
    public static final int kJoystickAxisTopX = 5;
    public static final int kJoystickAxisTopY = 6;
// Joystick (Attack 3) axes
    public static final int kJoystick2AxisX = 1;
    public static final int kJoystick2AxisY = 2;
    public static final int kJoystick2AxisThrottle = 3;
// Joystick buttons
    public static final int kJoystickButton1 = 1;
    public static final int kJoystickButton2 = 2;
    public static final int kJoystickButton3 = 3;
    public static final int kJoystickButton4 = 4;
    public static final int kJoystickButton5 = 5;
    public static final int kJoystickButton6 = 6;
    public static final int kJoystickButton7 = 7;
    public static final int kJoystickButton8 = 8;
    public static final int kJoystickButton9 = 9;
    public static final int kJoystickButton10 = 10;
    public static final int kJoystickButton11 = 11;
    public static final int kJoystickButton12 = 12;
// Joystick (Extreme 3D Pro) buttons
    public static final int kJoystickButtonTrigger = 1;
    public static final int kJoystickButtonThumb = 2;
    public static final int kJoystickButtonThumbBottomLeft = 3;
    public static final int kJoystickButtonThumbBottomRight = 4;
    public static final int kJoystickButtonThumbTopLeft = 5;
    public static final int kJoystickButtonThumbTopRight = 6;
    public static final int kJoystickButtonTopLeft = 7;
    public static final int kJoystickButtonTopRight = 8;
    public static final int kJoystickButtonMidLeft = 9;
    public static final int kJoystickButtonMidRight = 10;
    public static final int kJoystickButtonBottomLeft = 11;
    public static final int kJoystickButtonBottomRight = 12;
// Joystick (Attack 3) buttons
    public static final int kJoystick2ButtonTrigger = 1;
    public static final int kJoystick2ButtonThumbBottom = 2;
    public static final int kJoystick2ButtonThumbTop = 3;
    public static final int kJoystick2ButtonThumbLeft = 4;
    public static final int kJoystick2ButtonThumbRight = 5;
    public static final int kJoystick2ButtonLeftTop = 6;
    public static final int kJoystick2ButtonLeftBottom = 7;
    public static final int kJoystick2ButtonBottomLeft = 8;
    public static final int kJoystick2ButtonBottomRight = 9;
    public static final int kJoystick2ButtonRightBottom = 10;
    public static final int kJoystick2ButtonRightTop = 11;
// Driver Station Digital Inputs
    public static final int kDSDigitalInputShooterOnJ2 = 1;
    public static final int kDSDigitalInputShooterAI1 = 2;
    public static final int kDSDigitalInputArcadeDrive = 3;
    public static final int kDSDigitalInputDisableDrive = 4;
    public static final int kDSDigitalInputSlowDrive = 5;
    public static final int kDSDigitalInput6 = 6;
    public static final int kDSDigitalInput7 = 7;
    public static final int kDSDigitalInputDebugMode = 8;
// Driver Station Digital Outputs
    public static final int kDSDigitalOutputSensorExtracted = 1;
    public static final int kDSDigitalOutputSensorRetracted = 2;
    public static final int kDSDigitalOutputSensorLatch = 3;
// Driver Station Analog Inputs
    public static final int kDSAnalogInput1 = 1;
    public static final int kDSAnalogInput2 = 2;
    public static final int kDSAnalogInput3 = 3;
    public static final int kDSAnalogInput4 = 4;
}
