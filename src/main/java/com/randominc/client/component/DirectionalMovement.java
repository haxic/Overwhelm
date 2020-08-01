package com.randominc.client.component;

import com.randominc.shared.hecs.EntityComponent;
import org.joml.Vector3i;

public class DirectionalMovement extends EntityComponent {

  private final Vector3i direction;
  private float pitch;
  private float yaw;

  public DirectionalMovement() {
    this.direction = new Vector3i();
  }

  public void reset() {
    direction.set(0);
    pitch = 0;
    yaw = 0;
  }

  public void setDirection(int forwardDirection, int horizontalDirection, int verticalDirection) {
    direction.set(forwardDirection, horizontalDirection, verticalDirection);
  }

  public void setDirection(Vector3i direction) {
    this.direction.set(direction);
  }

  public Vector3i getDirection() {
    return direction;
  }

  public void setRotation(float pitch, float yaw) {
    this.pitch = pitch;
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public float getYaw() {
    return yaw;
  }

  @Override
  protected void removeComponent() {}
}
