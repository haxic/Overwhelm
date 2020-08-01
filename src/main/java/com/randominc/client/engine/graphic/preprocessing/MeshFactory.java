package com.randominc.client.engine.graphic.preprocessing;

import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class MeshFactory {

  private final DebugLog debugLog;
  private List<Integer> vaoIds = new ArrayList<>();
  private Map<Integer, List<Integer>> vboIdReferences = new HashMap<>();
  private List<Integer> vboIds = new ArrayList<>();
  private List<Integer> textures = new ArrayList<>();

  public MeshFactory() {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
  }

  public Mesh createMeshFromModelData(ModelData modelData) {
    return createMesh(
        modelData.getVertices(),
        modelData.getUVs(),
        modelData.getNormals(),
        modelData.getIndices());
  }

  public Mesh createMesh(float[] vertices, float[] uVs, float[] normals, int[] indices) {
    int vaoId = createVao();
    // Bind index buffer to vao.
    int indexBufferVbo = storeIndexBuffer(vaoId, indices);
    // Bind attributes to vao.
    storeVertexAttribute(vaoId, 0, 3, vertices);
    // Unbind vao.
    GL30.glBindVertexArray(0);
    Mesh mesh = new Mesh(vaoId, indices.length);
    mesh.setIndexBufferVbo(indexBufferVbo);
    return mesh;
  }

  public Mesh createSimpleMesh(float[] vertices, int dimensions) {
    int vaoId = createVao();
    // Bind vertices attribute to vao.
    storeVertexAttribute(vaoId, 0, dimensions, vertices);
    // Unbind vao.
    GL30.glBindVertexArray(0);
    return new Mesh(vaoId, vertices.length / dimensions);
  }

  private int createVao() {
    int vaoId = GL30.glGenVertexArrays();
    vaoIds.add(vaoId);
    GL30.glBindVertexArray(vaoId);
    return vaoId;
  }

  /**
   * Binds the index buffer to the vao and stores it in the special index buffer storage.
   *
   * @param vaoId
   * @param indices
   * @return
   */
  private int storeIndexBuffer(int vaoId, int[] indices) {
    int vboId = GL15.glGenBuffers();
    storeVboIdReference(vaoId, vboId);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, getIntBuffer(indices), GL15.GL_STATIC_DRAW);
    //    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    return vboId;
  }

  /**
   * Binds the vertex attribute to the vao and stores it in the attribute list.
   *
   * @param vaoId
   * @param attributeNumber
   * @param size
   * @param data
   */
  private void storeVertexAttribute(int vaoId, int attributeNumber, int size, float[] data) {
    int vboId = GL15.glGenBuffers();
    storeVboIdReference(vaoId, vboId);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, getFloatBuffer(data), GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }

  /**
   * Store vbo id references for clean up.
   *
   * @param vaoId
   * @param vboId
   */
  private void storeVboIdReference(int vaoId, int vboId) {
    vboIdReferences.computeIfAbsent(vaoId, k -> new ArrayList<>());
    vboIdReferences.get(vaoId).add(vboId);
    vboIds.add(vboId);
  }

  private IntBuffer getIntBuffer(int[] data) {
    IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
    intBuffer.put(data);
    intBuffer.flip();
    return intBuffer;
  }

  private FloatBuffer getFloatBuffer(float[] data) {
    FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
    floatBuffer.put(data);
    floatBuffer.flip();
    return floatBuffer;
  }

  public void removeMesh(int vaoId) {
    String debugVboList = "";
    for (Integer vboId : vboIdReferences.get(vaoId)) {
      debugVboList += vboId + ", ";
      GL30.glDeleteVertexArrays(vboId);
    }
    vboIdReferences.remove(vaoId);
    GL30.glDeleteVertexArrays(vaoId);
    vaoIds.remove(vaoId);
    debugLog.info(String.format("Mesh removed. VaoId=[%d], VboIds=[%s]", vaoId, debugVboList));
  }

  public void cleanUp() {
    int vbosCounted = 0;
    for (Entry<Integer, List<Integer>> entry : vboIdReferences.entrySet())
      for (Integer vboID : entry.getValue()) {
        vbosCounted++;
        GL30.glDeleteVertexArrays(vboID);
      }
    for (int vaoID : vaoIds) GL30.glDeleteVertexArrays(vaoID);
    for (int textureID : textures) GL11.glDeleteTextures(textureID);
    if (vbosCounted != vboIds.size()) {
      debugLog.error(
          String.format(
              "Mesh clean up unsuccessful. Counted [%d] removed vbos, but [%d] existed.",
              vbosCounted, vboIds.size()));

    } else {
      debugLog.info("Mesh clean up successful.");
    }
  }
}
