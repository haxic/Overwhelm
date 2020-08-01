package com.randominc.client.component;

import com.randominc.shared.hecs.EntityComponent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Direction extends EntityComponent {

  private final Vector3f forward;
  private final Vector3f up;
  private final Vector3f right;
  private final Quaternionf orientation;

  private static final Vector3f FORWARD = new Vector3f(0, 0, -1);
  private static final Vector3f UP = new Vector3f(1, 0, 0);
  private static final Vector3f RIGHT = new Vector3f(0, 1, 0);

  private static final Vector3f tempVector1 = new Vector3f();
  private static final Vector3f tempVector2 = new Vector3f();
  private static final Matrix4f tempRotationMatrix = new Matrix4f();

  public Direction() {
    forward = new Vector3f(0, 0, -1);
    right = new Vector3f(0, 1, 0);
    up = new Vector3f(1, 0, 0);
    orientation = new Quaternionf();
    updateOrientation();
  }

  public void rotate(float pitch, float yaw, float roll) {
    orientation.rotateXYZ(pitch, yaw, roll);
    forward.set(FORWARD).rotate(orientation);
    right.set(RIGHT).rotate(orientation);
    up.set(UP).rotate(orientation);
  }

  public void rotateAroundZAxis(float angle) {
    orientation.rotateAxis(angle, new Vector3f(1, 0, 0));
    updateOrientation();
    //    forward.rotateZ(angle);
    //    right.rotateZ(angle);
  }

  // Hack
  //  private final Vector3f forwardTest = new Vector3f();

  public void rotateAroundRightAxis(float angle) {
    //    orientation.rotateAxis(angle, right);
    //    forwardTest.set(forward);
    //    forwardTest.rotateAxis(angle, right.x, right.y, right.z);
    //    if (forwardTest.z < 0.95f && forwardTest.z > -0.95f) {
    //      forward.rotateAxis(angle, right.x, right.y, right.z);
    //    }
    //    up.rotateAxis(angle, right.x, right.y, right.z);
    updateOrientation();
  }

  public void updateOrientation() {
    forward.set(FORWARD).rotate(orientation);
    right.set(RIGHT).rotate(orientation);
    up.set(UP).rotate(orientation);
  }

  public Vector3f getForward(Vector3f dest) {
    return dest.set(forward);
  }

  public Vector3f getForward() {
    return forward;
  }

  public Vector3f getUp(Vector3f dest) {
    return dest.set(up);
  }

  public Vector3f getUp() {
    return up;
  }

  public Vector3f getRight(Vector3f dest) {
    return dest.set(right);
  }

  public Vector3f getRight() {
    return right;
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
