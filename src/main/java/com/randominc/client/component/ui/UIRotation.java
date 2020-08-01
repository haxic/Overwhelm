package com.randominc.client.component.ui;

import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;

public class UIRotation extends EntityComponent {
  private final float rotation;

  public UIRotation(float rotation) {
    this.rotation = Objects.requireNonNull(rotation);
  }

  public float getRotation() {
    return rotation;
  }

  @Override
  protected void removeComponent() {}
}
