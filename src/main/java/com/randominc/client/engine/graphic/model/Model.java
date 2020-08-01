package com.randominc.client.engine.graphic.model;

import com.randominc.client.engine.graphic.texture.Texture;
import java.util.Objects;

public class Model {

  private Texture texture;
  private Mesh mesh;

  public Model(Mesh mesh, Texture texture) {
    this.mesh = Objects.requireNonNull(mesh);
    this.texture = Objects.requireNonNull(texture);
  }

  public Mesh getMesh() {
    return mesh;
  }

  public Texture getTexture() {
    return texture;
  }
}
