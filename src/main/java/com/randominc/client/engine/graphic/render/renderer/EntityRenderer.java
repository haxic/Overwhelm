package com.randominc.client.engine.graphic.render.renderer;

import com.randominc.client.component.Direction;
import com.randominc.client.component.Position;
import com.randominc.client.component.Scale;
import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.render.shader.EntityShader;
import com.randominc.client.engine.graphic.utility.MatrixFactory;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class EntityRenderer {

  private final DebugLog debugLog;
  private final EntityShader entityShader;
  private EntityManager entityManager;

  public EntityRenderer() {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    entityShader = new EntityShader();
  }

  public void render(
      EntityManager entityManager,
      List<Entity> lights,
      Map<Model, List<Entity>> entities,
      Vector3f cameraPosition,
      Matrix4f projectionMatrix,
      Matrix4f viewMatrix) {
    this.entityManager = Objects.requireNonNull(entityManager);
    Objects.requireNonNull(lights);
    Objects.requireNonNull(entities);
    Objects.requireNonNull(cameraPosition);
    Objects.requireNonNull(projectionMatrix);
    Objects.requireNonNull(viewMatrix);

    prepareEntityShader(cameraPosition, projectionMatrix, viewMatrix, lights);
    for (Model model : entities.keySet()) {
      prepareModel(model);
      List<Entity> batch = entities.get(model);
      for (Entity entity : batch) {
        prepareInstance(entity, projectionMatrix, viewMatrix);
        // Draw model.
        GL11.glDrawElements(
            GL11.GL_TRIANGLES, model.getMesh().getIndicesSize(), GL11.GL_UNSIGNED_INT, 0);
      }
      unbindModel();
    }
    entityShader.stop();
  }

  private void prepareEntityShader(
      Vector3f position, Matrix4f projectionMatrix, Matrix4f viewMatrix, List<Entity> lights) {
    entityShader.start();
    //    entityShader.loadCameraPosition(position);
    //    entityShader.loadProjectionMatrix(projectionMatrix);
    //    entityShader.loadViewMatrix(viewMatrix);
    //    entityShader.loadAmbientLight(0);
    //    entityShader.loadLights(entityManager, lights);
  }

  private void prepareModel(Model model) {
    // Bind vao.
    Mesh mesh = model.getMesh();
    GL30.glBindVertexArray(mesh.getVaoId());
    // Bind vao attributes.
    GL20.glEnableVertexAttribArray(0);
  }

  private void prepareInstance(Entity entity, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
    Position position = (Position) entityManager.getComponent(entity, Position.class);
    Scale scale = (Scale) entityManager.getComponent(entity, Scale.class);
    Direction direction = (Direction) entityManager.getComponent(entity, Direction.class);
    Matrix4f modelMatrix =
        MatrixFactory.createModelMatrix(
            position.getPosition(), scale.getScale(), direction.getRotationMatrix());

    Matrix4f mvpMatrix = MatrixFactory.createMVPMatrix(projectionMatrix, viewMatrix, modelMatrix);

    //    Matrix4f modelViewMatrix = viewMatrix.mul(modelMatrix, new Matrix4f());
    entityShader.loadModelViewProjectionMatrix(mvpMatrix);
    //    entityShader.loadModelViewMatrix(modelViewMatrix);
    //    entityShader.loadModelMatrix(modelMatrix);
  }

  private void unbindModel() {
    // Unbind vao attributes.
    GL20.glDisableVertexAttribArray(0);
    // Unbind vao.
    GL30.glBindVertexArray(0);
  }

  public void cleanUp() {
    // Clean up shader.
    debugLog.debug("Entity renderer clean up successful.");
  }

  public void initialize() {
    entityShader.initialize();
  }
}
