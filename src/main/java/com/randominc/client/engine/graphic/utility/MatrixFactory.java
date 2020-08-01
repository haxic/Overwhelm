package com.randominc.client.engine.graphic.utility;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MatrixFactory {
  private static Matrix4f tempModelMatrix = new Matrix4f();
  private static Matrix4f tempViewMatrix = new Matrix4f();
  private static Matrix4f tempProjectionMatrix = new Matrix4f();
  private static Matrix4f mvpMatrix = new Matrix4f();

  public static Matrix4f createModelMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
    tempModelMatrix.identity();
    tempModelMatrix.translate(position);
    tempModelMatrix.rotateX((float) Math.toRadians(rotation.x));
    tempModelMatrix.rotateY((float) Math.toRadians(rotation.y));
    tempModelMatrix.rotateZ((float) Math.toRadians(rotation.z));
    tempModelMatrix.scale(scale);
    return tempModelMatrix;
  }

  public static Matrix4f createModelMatrix(
      Vector3f position, Vector3f scale, Matrix4f rotationMatrix) {
    tempModelMatrix.identity();
    tempModelMatrix.translate(position);
    tempModelMatrix.mul(rotationMatrix);
    tempModelMatrix.scale(scale);
    return tempModelMatrix;
  }

  public static Matrix4f createViewMatrix(Vector3f position, Vector3f direction, Vector3f up) {
    tempViewMatrix.identity().lookAt(position, direction, up);
    return tempViewMatrix;
  }

  public static Matrix4f createViewMatrix(Quaternionf rotation) {
    tempViewMatrix.set(rotation);
    return tempViewMatrix;
  }

  public static Matrix4f createProjectionMatrix(float fov, float aspect, float near, float far) {
    tempProjectionMatrix.identity().perspective(fov, aspect, near, far);
    return tempProjectionMatrix;
  }

  public static Matrix4f createMVPMatrix(
      Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
    return projectionMatrix.mul(viewMatrix, mvpMatrix).mul(modelMatrix);
  }
}
