package com.randominc.client.component.ui;

import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class UIScale extends EntityComponent {
  private final Vector3f scale;
  private static final Vector3f tempScale = new Vector3f();

  public UIScale(Vector2f scale) {
    Objects.requireNonNull(scale);
    this.scale = new Vector3f().set(scale, 0);
  }

  public Vector3f getScale() {
    return tempScale.set(scale);
  }

  @Override
  protected void removeComponent() {}
}
