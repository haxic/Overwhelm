package com.randominc.client.component;

import com.randominc.shared.debug.DefaultDebugLogProvider;
import com.randominc.shared.hecs.EntityComponent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Direction extends EntityComponent {
  public static final Vector3f FORWARD = new Vector3f(0, -1, 0);
  public static final Vector3f RIGHT = new Vector3f(-1, 0, 0);
  public static final Vector3f UP = new Vector3f(0, 0, 1);

  private static final Matrix4f tempRotationMatrix = new Matrix4f();
  private static final Vector3f tempForward = new Vector3f();
  private static final Vector3f tempUp = new Vector3f();
  private static final Vector3f tempRight = new Vector3f();
  private static final Quaternionf tempOrientation = new Quaternionf();

  private final Vector3f forward;
  private final Vector3f up;
  private final Vector3f right;
  private final Quaternionf orientation;

  public Direction() {
    forward = new Vector3f(FORWARD);
    right = new Vector3f(RIGHT);
    up = new Vector3f(UP);
    orientation = new Quaternionf();
    updateOrientation();
  }

  public void rotate(float pitch, float yaw, float roll) {
    pitch = pitch % 360.0f;
    yaw = yaw % 360.0f;
    roll = roll % 360.0f;
    orientation.rotateAxis(yaw, UP);
    orientation.rotateAxis(pitch, RIGHT);
    updateOrientation();

    NumberFormat format = new DecimalFormat();
    format.setMaximumFractionDigits(3);
    new DefaultDebugLogProvider().getDebugLog(this).info(right.toString(format));

    float debugSumXY = orientation.x() + orientation.y();
    String debugOrientation = format.format(debugSumXY) + "             ";
    new DefaultDebugLogProvider().getDebugLog(this).info(debugOrientation.substring(0, 10));
    new DefaultDebugLogProvider()
        .getDebugLog(this)
        .info(orientation.getEulerAnglesXYZ(tempForward).toString(format));
  }

  private void updateOrientation() {
    forward.set(FORWARD).rotate(orientation);
    right.set(RIGHT).rotate(orientation);
    up.set(UP).rotate(orientation);
  }

  public Vector3f getForward() {
    return tempForward.set(forward);
  }

  public Vector3f getUp() {
    return tempUp.set(up);
  }

  public Vector3f getRight() {
    return tempRight.set(right);
  }

  public Quaternionf getOrientation() {
    return tempOrientation.set(orientation);
  }

  public Matrix4f getRotationMatrix() {
    return tempRotationMatrix.set(
        right.x, right.y, right.z, 0, // Vector 1
        forward.x, forward.y, forward.z, 0, // Vector 2
        up.x, up.y, up.z, 0, 0, // Vector 3
        0, 0, 1); // Vector 4
  }

  @Override
  protected void removeComponent() {}
}
