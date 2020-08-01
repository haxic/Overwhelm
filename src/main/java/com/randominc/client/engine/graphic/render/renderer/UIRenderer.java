package com.randominc.client.engine.graphic.render.renderer;

import com.randominc.client.component.ui.UIPosition;
import com.randominc.client.component.ui.UIRotation;
import com.randominc.client.component.ui.UIScale;
import com.randominc.client.data.MeshProvider;
import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.render.shader.UIShader;
import com.randominc.client.engine.graphic.utility.MatrixFactory;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class UIRenderer {

  private final DebugLog debugLog;
  private final UIShader uiShader;
  private final MeshProvider meshProvider;
  private EntityManager entityManager;

  public UIRenderer(MeshProvider meshProvider) {
    this.meshProvider = Objects.requireNonNull(meshProvider);
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    uiShader = new UIShader();
  }

  public void render(EntityManager entityManager, Map<Model, List<Entity>> entities) {
    this.entityManager = Objects.requireNonNull(entityManager);
    prepareShader();
    for (Model model : entities.keySet()) {
      prepareModel(model);
      List<Entity> batch = entities.get(model);
      System.out.println("000");
      for (Entity entity : batch) {
        System.out.println("111");
        prepareInstance(entity);
        System.out.println("222 " + model.getMesh().getIndicesSize());
        GL11.glDrawElements(
            GL11.GL_TRIANGLES, model.getMesh().getIndicesSize(), GL11.GL_UNSIGNED_INT, 0);
        System.out.println("3333");
      }
      unbindModel();
    }
  }

  private void prepareShader() {
    uiShader.start();
  }

  private void prepareModel(Model model) {
    Mesh mesh = model.getMesh();
    GL30.glBindVertexArray(mesh.getVaoId());
    // Bind vao attributes.
    GL20.glEnableVertexAttribArray(0);
    boolean hasTexture = model.getTexture().getTextureID() >= 0;

    if (hasTexture) {
      GL20.glEnableVertexAttribArray(1);
    }
    //    GL20.glEnableVertexAttribArray(2);

    uiShader.loadHasTexture(hasTexture);
  }

  private void prepareInstance(Entity entity) {
    UIPosition position = (UIPosition) entityManager.getComponent(entity, UIPosition.class);
    UIScale scale = (UIScale) entityManager.getComponent(entity, UIScale.class);
    UIRotation rotation = (UIRotation) entityManager.getComponent(entity, UIRotation.class);
    Matrix4f modelMatrix =
        MatrixFactory.createModelMatrix(
            position.getPosition(), scale.getScale(), rotation.getRotation());
    uiShader.loadModelMatrix(modelMatrix);
    uiShader.loadColor(new Vector4f(1, 0, 1, 1));
  }

  private void unbindModel() {
    // Unbind vao attributes.
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL20.glDisableVertexAttribArray(2);
    // Unbind vao.
    GL30.glBindVertexArray(0);
  }
}
