package com.randominc.client.engine.graphic.render.shader;

import com.randominc.client.engine.graphic.utility.ShaderProgram;
import org.joml.Vector2f;

public class UIShader extends ShaderProgram {

  private int locationTranslation;

  public UIShader() {
    super(
        "src/main/java/com/randominc/client/resources/shader/ui.vert",
        "src/main/java/com/randominc/client/resources/shader/ui.frag");
  }

  @Override
  protected void getAllUniformLocations() {
    locationTranslation = getUniformLocation("translation");
  }

  @Override
  protected void bindAttributes() {
    super.bindAttribute(0, "position");
  }

  protected void loadTranslation(Vector2f translation) {
    super.loadVector2f(locationTranslation, translation);
  }
}
