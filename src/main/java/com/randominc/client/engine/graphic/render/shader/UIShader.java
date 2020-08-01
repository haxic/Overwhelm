package com.randominc.client.engine.graphic.render.shader;

import com.randominc.client.engine.graphic.utility.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class UIShader extends ShaderProgram {

  private int locationModelMatrix;
  private int locationColor;
  private int locationHasTexture;

  public UIShader() {
    super(
        "src/main/java/com/randominc/client/resources/shader/ui.vert",
        "src/main/java/com/randominc/client/resources/shader/ui.frag");
  }

  @Override
  protected void getAllUniformLocations() {
    locationModelMatrix = getUniformLocation("modelMatrix");
    locationColor = getUniformLocation("color");
    locationHasTexture = getUniformLocation("hasTexture");
  }

  @Override
  protected void bindAttributes() {
    super.bindAttribute(0, "position");
    super.bindAttribute(1, "texCoord");
    super.bindAttribute(2, "normal");
  }

  public void loadModelMatrix(Matrix4f modelMatrix) {
    super.loadMatrixf(locationModelMatrix, modelMatrix);
  }

  public void loadColor(Vector4f color) {
    super.loadVector4f(locationColor, color);
  }

  public void loadHasTexture(boolean hasTexture) {
    super.loadInt(locationHasTexture, hasTexture ? 1 : 0);
  }
}
