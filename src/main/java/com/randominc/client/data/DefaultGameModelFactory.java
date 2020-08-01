package com.randominc.client.data;

import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.preprocessing.MeshFactory;
import com.randominc.client.engine.graphic.preprocessing.ModelData;
import com.randominc.client.engine.graphic.preprocessing.TextureFactory;
import com.randominc.client.engine.graphic.texture.Texture;
import java.util.Objects;

public class DefaultGameModelFactory {

  private final MeshFactory meshFactory;
  private final TextureFactory textureFactory;
  private final DefaultMeshProvider defaultMeshProvider;
  private final DefaultTextureProvider defaultTextureProvider;
  private final DefaultModelProvider defaultModelProvider;
  private final FontTextureFactory fontTextureFactory;

  public DefaultGameModelFactory(
      MeshFactory meshFactory,
      TextureFactory textureFactory,
      DefaultMeshProvider defaultMeshProvider,
      DefaultTextureProvider defaultTextureProvider,
      DefaultModelProvider defaultModelProvider) {

    this.meshFactory = Objects.requireNonNull(meshFactory);
    this.textureFactory = Objects.requireNonNull(textureFactory);
    this.defaultMeshProvider = Objects.requireNonNull(defaultMeshProvider);
    this.defaultTextureProvider = Objects.requireNonNull(defaultTextureProvider);
    this.defaultModelProvider = Objects.requireNonNull(defaultModelProvider);
    this.fontTextureFactory = new FontTextureFactory(textureFactory);
  }

  public void initialize() {
    createCube();
    createQuad();
    createTestTerrain();
    createDefaultFrontTexture();
  }

  private void createDefaultFrontTexture() {
    defaultTextureProvider.setDefaultFontTexture(fontTextureFactory.createDefaultFrontTexture());
  }

  private void createCube() {
    float[] vertices =
        new float[] {
          -1.0f, -1.0f, -1.0f, //
          1.0f, -1.0f, -1.0f, //
          1.0f, 1.0f, -1.0f, //
          -1.0f, 1.0f, -1.0f, //
          -1.0f, -1.0f, 1.0f, //
          1.0f, -1.0f, 1.0f, //
          1.0f, 1.0f, 1.0f, //
          -1.0f, 1.0f, 1.0f //
        };
    int[] indices =
        new int[] {
          0, 1, 3, 3, 1, 2, //
          1, 5, 2, 2, 5, 6, //
          5, 4, 6, 6, 4, 7, //
          4, 0, 7, 7, 0, 3, //
          3, 2, 7, 7, 2, 6, //
          4, 5, 0, 0, 5, 1 //
        };
    ModelData modelData = new ModelData(vertices, null, null, indices, 0);
    Mesh mesh = meshFactory.createMeshFromModelData(modelData);
    Model model = new Model(mesh, Texture.EMPTY);
    defaultMeshProvider.setCube(mesh);
    defaultModelProvider.setDefaultCube(model);
  }

  private void createQuad() {
    float[] vertices = {
      -1f, 1f, 0, // V0
      -1f, -1f, 0, // V1
      1f, -1f, 0, // V2
      1f, 1f, 0 // V3
    };

    int[] indices = {
      0, 1, 3, // Top left triangle (V0,V1,V3)
      3, 1, 2 // Bottom right triangle (V3,V1,V2)
    };

    Mesh mesh = meshFactory.createMesh(vertices, null, null, indices);
    Model model = new Model(mesh, Texture.EMPTY);
    defaultMeshProvider.setQuad(mesh);
    defaultModelProvider.setDefaultQuad(model);
  }

  private void createTestTerrain() {
    int vertexCount = 128;
    float size = 100;
    int count = vertexCount * vertexCount;
    float[] vertices = new float[count * 3];
    float[] normals = new float[count * 3];
    float[] textureCoords = new float[count * 2];
    int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
    int vertexPointer = 0;
    for (int i = 0; i < vertexCount; i++) {
      for (int j = 0; j < vertexCount; j++) {
        vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * size;
        vertices[vertexPointer * 3 + 1] = (float) i / ((float) vertexCount - 1) * size;
        vertices[vertexPointer * 3 + 2] = 0;
        normals[vertexPointer * 3] = 0;
        normals[vertexPointer * 3 + 1] = 0;
        normals[vertexPointer * 3 + 2] = 1;
        textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
        textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
        vertexPointer++;
      }
    }
    int pointer = 0;
    for (int gz = 0; gz < vertexCount - 1; gz++) {
      for (int gx = 0; gx < vertexCount - 1; gx++) {
        int topLeft = (gz * vertexCount) + gx;
        int topRight = topLeft + 1;
        int bottomLeft = ((gz + 1) * vertexCount) + gx;
        int bottomRight = bottomLeft + 1;
        indices[pointer++] = topLeft;
        indices[pointer++] = bottomLeft;
        indices[pointer++] = topRight;
        indices[pointer++] = topRight;
        indices[pointer++] = bottomLeft;
        indices[pointer++] = bottomRight;
      }
    }
    Mesh mesh = meshFactory.createMesh(vertices, textureCoords, normals, indices);
    Model model = new Model(mesh, Texture.EMPTY);

    defaultModelProvider.setTestTerrain(model);
  }
}
