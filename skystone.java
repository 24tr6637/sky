package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SkystoneKent (Blocks to Java)", group = "")
public class SkystoneKent extends LinearOpMode {

  private DcMotor FL;
  private DcMotor BL;
  private DcMotor FR;
  private DcMotor BR;
  private DcMotor lift;
  private Servo flag;
  private Servo gripper;
  private Servo topper;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    FL = hardwareMap.dcMotor.get("FL");
    BL = hardwareMap.dcMotor.get("BL");
    FR = hardwareMap.dcMotor.get("FR");
    BR = hardwareMap.dcMotor.get("BR");
    lift = hardwareMap.dcMotor.get("lift");
    flag = hardwareMap.servo.get("flag");
    gripper = hardwareMap.servo.get("gripper");
    topper = hardwareMap.servo.get("topper");

    // Put initialization blocks here.
    FL.setDirection(DcMotorSimple.Direction.REVERSE);
    BL.setDirection(DcMotorSimple.Direction.REVERSE);
    FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    lift.setDirection(DcMotorSimple.Direction.REVERSE);
    flag.setPosition(0.5);
    gripper.setPosition(0.12);
    topper.setPosition(0.004);
    slow = 0;
    topperDeployed = false;
    waitForStart();
    if (opModeIsActive()) {
      // Wait till Active
      lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      while (opModeIsActive()) {
        // Put loop blocks here.
        if (gamepad1.y && flag.getPosition() > 0.25) {
          // Starts slow mode
          flag.setPosition(0);
          slow = 1;
          telemetry.addData("slow", slow);
        } else if (gamepad1.x && flag.getPosition() < 0.25) {
          flag.setPosition(0.5);
          slow = 0;
        }
        if (slow == 1) {
          // If 1 start slowmode
          vertical = -(gamepad1.left_stick_y * 0.25);
          horizontal = gamepad1.left_stick_x * 0.25;
          spin = gamepad1.right_stick_x * 0.25;
          telemetry.addData("slowIndicator", slow);
        } else {
          vertical = -(gamepad1.left_stick_y * 0.4);
          horizontal = gamepad1.left_stick_x * 0.4;
          spin = gamepad1.right_stick_x * 0.4;
        }
        FR.setPower(-spin + (vertical - horizontal));
        BR.setPower(-spin + vertical + horizontal);
        FL.setPower(spin + vertical + horizontal);
        BL.setPower(spin + (vertical - horizontal));
        if (gamepad1.left_trigger > 0 && lift.getCurrentPosition() < 7600) {
          if (topperDeployed) {
            mountTopper();
          } else {
            lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lift.setPower(1 * gamepad1.left_trigger);
          }
        } else if (gamepad1.right_trigger > 0 && lift.getCurrentPosition() > 50) {
          lift.setPower(-1 * gamepad1.right_trigger);
        } else {
          lift.setPower(0);
          lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (gamepad1.left_bumper) {
          gripper.setPosition(0.05);
        }
        if (gamepad1.right_bumper) {
          gripper.setPosition(0.2);
        }
        if (gamepad1.a) {
          topper.setPosition(0.074);
          topperDeployed = true;
        }
        if (gamepad1.b) {
          topper.setPosition(0.004);
          topperDeployed = false;
        }
        telemetry.update();
      }
      // Put run blocks here.
    }
  }

  /**
   * Describe this function...
   */
  private void mountTopper() {
    telemetry.addData("mount", "function");
    telemetry.update();
    lift.setTargetPosition(900);
    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    lift.setPower(-1);
    while (lift.isBusy()) {
      telemetry.addData("Lift is going", "up");
      telemetry.update();
    }
    topper.setPosition(0.004);
    topperDeployed = false;
    for (int count = 0; count < 100; count++) {
      telemetry.addData("Lift is going", "up");
      telemetry.update();
    }
    lift.setTargetPosition(0);
    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    lift.setPower(1);
    while (lift.isBusy()) {
      telemetry.addData("Lift is going", "down");
      telemetry.update();
    }
  }
}
