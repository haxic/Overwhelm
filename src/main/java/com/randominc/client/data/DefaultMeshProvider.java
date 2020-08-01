package com.randominc.client.data;

import com.randominc.client.engine.graphic.model.Mesh;
import java.util.Objects;

public class DefaultMeshProvider implements MeshProvider {
  private Mesh quad;
  private Mesh cube;

  @Override
  public Mesh getQuad() {
    return quad;
  }

  @Override
  public Mesh getCube() {
    return cube;
  }

  public void setQuad(Mesh quad) {
    this.quad = Objects.requireNonNull(quad);
  }

  public void setCube(Mesh cube) {
    this.cube = Objects.requireNonNull(cube);
  }
}
