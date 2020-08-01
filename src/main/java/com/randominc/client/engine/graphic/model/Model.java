package com.randominc.client.engine.graphic.model;

import org.joml.Vector3f;

public class Model {
  private Mesh mesh;
  private boolean hasTransparency;

  private float materialShininess = 5f;
  private Vector3f materialSpecularColor = new Vector3f(0.5f, 0.5f, 0.5f);

  public Model(Mesh mesh) {
    this.mesh = mesh;
  }

  public Mesh getMesh() {
    return mesh;
  }

  public boolean hasTransparency() {
    return hasTransparency;
  }

  public void setHasTransparency(boolean hasTransparency) {
    this.hasTransparency = hasTransparency;
  }

  public float getShininess() {
    return materialShininess;
  }

  public Vector3f getSpecularColor() {
    return materialSpecularColor;
  }
}
