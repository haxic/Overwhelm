package com.randominc.client.engine.graphic.render.renderer;

import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.render.shader.UIShader;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import com.randominc.shared.hecs.EntityManager;
import java.util.Objects;

public class UIRenderer {

  private final DebugLog debugLog;
  private final UIShader uiShader;
  private EntityManager entityManager;

  public UIRenderer() {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    uiShader = new UIShader();
  }

  public void render(EntityManager entityManager, Model model) {
    this.entityManager = Objects.requireNonNull(entityManager);
    prepareShader();
    prepareModel(model);
  }


  private void prepareShader() {
    uiShader.start();
  }

  private void prepareModel(Model model) {
  }

  public void initialize() {
    uiShader.initialize();
  }
}
