package com.randominc.client.component;

import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;
import org.joml.Vector3f;

public class Position extends EntityComponent {
  private final Vector3f position;

  public Position(Vector3f position) {
    this.position = Objects.requireNonNull(position);
  }

  public Vector3f getPosition(Vector3f dest) {
    return dest.set(position);
  }

  public Vector3f getPosition() {
    return position;
  }

  @Override
  protected void removeComponent() {}

  public void setPosition(double x, double y, double z) {
    position.set(x, y, z);
  }

  public void setPosition(Vector3f newPosition) {
    position.set(newPosition);
  }
}
