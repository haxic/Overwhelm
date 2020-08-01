package com.randominc.client.engine.graphic.render.renderer;

import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.render.Terrain;
import com.randominc.client.engine.graphic.render.shader.TerrainShader;
import com.randominc.client.engine.graphic.utility.MatrixFactory;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.util.List;
import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TerrainRenderer {

  private final DebugLog debugLog;
  private final TerrainShader terrainShader;
  private final Vector3f rotationVector = new Vector3f(90, 0, 0);
  private final Vector3f scaleVector = new Vector3f(1);

  public TerrainRenderer() {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    terrainShader = new TerrainShader();
  }

  public void render(
      List<Terrain> terrains,
      Vector3f cameraPosition,
      Matrix4f projectionMatrix,
      Matrix4f viewMatrix) {
    Objects.requireNonNull(terrains);
    Objects.requireNonNull(cameraPosition);
    Objects.requireNonNull(projectionMatrix);
    Objects.requireNonNull(viewMatrix);

    prepareShader();
    for (Terrain terrain : terrains) {
      prepareModel(terrain.getModel());
      prepareInstance(terrain, projectionMatrix, viewMatrix);
      // Draw model.
      GL11.glDrawElements(
          GL11.GL_TRIANGLES,
          terrain.getModel().getMesh().getIndicesSize(),
          GL11.GL_UNSIGNED_INT,
          0);
    }

    unbindModel();
    terrainShader.stop();
  }

  private void prepareShader() {
    terrainShader.start();
  }

  private void prepareModel(Model model) {
    // Bind vao.
    Mesh mesh = model.getMesh();
    GL30.glBindVertexArray(mesh.getVaoId());
    // Bind vao attributes.
    GL20.glEnableVertexAttribArray(0);
  }

  private void prepareInstance(Terrain terrain, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
    Matrix4f modelMatrix =
        MatrixFactory.createModelMatrix(terrain.getPosition(), rotationVector, scaleVector);

    Matrix4f mvpMatrix = MatrixFactory.createMVPMatrix(projectionMatrix, viewMatrix, modelMatrix);

    terrainShader.loadModelViewProjectionMatrix(mvpMatrix);
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
    terrainShader.initialize();
  }
}
