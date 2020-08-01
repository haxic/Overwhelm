package com.randominc.client.component;

import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends EntityComponent {
  float fov;
  float aspectRatio;
  float near;
  float far;

  private final Matrix4f projectionMatrix;
  private final Matrix4f viewMatrix;

  private static final Vector3f tempVector = new Vector3f();

  public Camera() {
    projectionMatrix = new Matrix4f();
    viewMatrix = new Matrix4f();
    fov = 70;
    near = 1;
    far = 20000;
  }

  public float getFov() {
    return fov;
  }

  public void setFov(float fov) {
    this.fov = fov;
  }

  public float getNear() {
    return near;
  }

  public void setNear(float near) {
    this.near = near;
  }

  public float getFar() {
    return far;
  }

  public void setFar(float far) {
    this.far = far;
  }

  public void setProjectionMatrix(float fov, float aspectRatio, float near, float far) {
    this.fov = fov;
    this.aspectRatio = aspectRatio;
    this.near = near;
    this.far = far;
    projectionMatrix
        .identity()
        .perspective((float) Math.toRadians(getFov()), aspectRatio, getNear(), getFar());
  }

  public void updateProjectionMatrix(float aspectRatio) {
    if (this.aspectRatio == aspectRatio) return;
    this.aspectRatio = aspectRatio;
    projectionMatrix
        .identity()
        .perspective((float) Math.toRadians(getFov()), aspectRatio, getNear(), getFar());
  }

  public void updateViewMatrix(Vector3f position, Vector3f forward, Vector3f up) {
    Objects.requireNonNull(position);
    Objects.requireNonNull(forward);
    Objects.requireNonNull(up);
    viewMatrix.identity().lookAt(position, position.add(forward, tempVector), up);
  }

  public Matrix4f getProjectionMatrix() {
    return projectionMatrix;
  }

  public Matrix4f getViewMatrix() {
    return viewMatrix;
  }

  @Override
  protected void removeComponent() {}
}
