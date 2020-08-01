package com.randominc.client.component.ui;

import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class UIPosition extends EntityComponent {
  private final Vector3f position;
  private static final Vector3f tempPosition = new Vector3f();

  public UIPosition(Vector2f position) {
    Objects.requireNonNull(position);
    this.position = new Vector3f().set(position, 0);
  }

  public Vector3f getPosition() {
    return tempPosition.set(position);
  }

  @Override
  protected void removeComponent() {}

  public void setPosition(double x, double y) {
    position.set(x, y, 0);
  }

  public void setPosition(Vector3f newPosition) {
    position.set(newPosition.x(), newPosition.y, 0);
  }
}
