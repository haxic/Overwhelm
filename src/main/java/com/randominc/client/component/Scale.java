package com.randominc.client.component;

import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;
import org.joml.Vector3f;

public class Scale extends EntityComponent {
  private final Vector3f scale;

  public Scale(Vector3f scale) {
    this.scale = Objects.requireNonNull(scale);
  }

  public Vector3f getScale(Vector3f dest) {
    return dest.set(scale);
  }

  public Vector3f getScale() {
    return scale;
  }

  @Override
  protected void removeComponent() {}
}
