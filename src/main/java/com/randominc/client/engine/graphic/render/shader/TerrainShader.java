package com.randominc.client.engine.graphic.render.shader;

import com.randominc.client.engine.graphic.utility.ShaderProgram;
import org.joml.Matrix4f;

public class TerrainShader extends ShaderProgram {
  private int mvpLocation;

  public TerrainShader() {
    super(
        "src/main/java/com/randominc/client/resources/shader/terrain.vert",
        "src/main/java/com/randominc/client/resources/shader/terrain.frag");
  }

  @Override
  protected void getAllUniformLocations() {
    mvpLocation = super.getUniformLocation("mvp");
  }

  @Override
  protected void bindAttributes() {
    super.bindAttribute(0, "position");
  }

  public void loadModelViewProjectionMatrix(Matrix4f mvp) {
    super.loadMatrixf(mvpLocation, mvp);
  }
}
