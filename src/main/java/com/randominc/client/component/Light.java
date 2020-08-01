package com.randominc.client.component;

import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;
import org.joml.Vector3f;

public class Light extends EntityComponent {
  private Vector3f color;
  private Vector3f attenuation;

  public Light(Vector3f color, Vector3f attenuation) {
    this.setColor(Objects.requireNonNull(color));
    this.setAttenuation(Objects.requireNonNull(attenuation));
  }

  private void setAttenuation(Vector3f attenuation) {
    this.attenuation = Objects.requireNonNull(attenuation);
  }

  public Vector3f getColor() {
    return color;
  }

  public void setColor(Vector3f color) {
    this.color = Objects.requireNonNull(color);
  }

  public Vector3f getAttenuation() {
    return attenuation;
  }

  @Override
  protected void removeComponent() {}
}
