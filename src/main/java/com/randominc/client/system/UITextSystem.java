package com.randominc.client.system;

import com.randominc.client.component.Actor;
import com.randominc.client.component.ui.UIPosition;
import com.randominc.client.component.ui.UIRotation;
import com.randominc.client.component.ui.UIScale;
import com.randominc.client.component.ui.UIText;
import com.randominc.client.data.TextureProvider;
import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.preprocessing.MeshFactory;
import com.randominc.client.engine.graphic.texture.Texture;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.joml.Vector2f;

public class UITextSystem {

  private final EntityManager entityManager;
  private final MeshFactory meshFactory;
  private final TextureProvider textureProvider;

  public UITextSystem(
      EntityManager entityManager, MeshFactory meshFactory, TextureProvider TextureProvider) {
    this.entityManager = Objects.requireNonNull(entityManager);
    this.meshFactory = Objects.requireNonNull(meshFactory);
    this.textureProvider = Objects.requireNonNull(TextureProvider);
  }

  public Entity createUITextElement(
      String text, float xpos, float ypos, float scaleX, float scaleY, float rotation) {
    UIPosition uiPosition = new UIPosition(new Vector2f(-xpos, ypos));
    UIScale uiScale = new UIScale(new Vector2f(0.10f, 0.10f));
    UIRotation uiRotation = new UIRotation(0);
    UIText uiText = new UIText(text, text.length(), 1);
    Texture texture = textureProvider.getDefaultFontTexture();
    Mesh mesh = buildMesh(texture, uiText);
    uiText.setMesh(mesh);
    Model model = new Model(mesh, texture);
    Actor actor = new Actor(model);
    return entityManager.createEntity(uiPosition, uiScale, uiRotation, uiText, actor);
  }

  private Mesh buildMesh(Texture texture, UIText uiText) {
    byte[] chars = uiText.getText().getBytes(Charset.forName("ISO-8859-1"));
    int numChars = chars.length;

    List<Float> vertices = new ArrayList();
    List<Float> textCoords = new ArrayList();
    float[] normals = new float[0];
    List<Integer> indices = new ArrayList();

    new DefaultDebugLogProvider().getDebugLog(this).info("THIS IS WHERE IT GOES WRONG, texture width/height never sets and thus fails to create vertices nad texcoords -> " + texture.getWidth() + " " + texture.getHeight());
    float tileWidth = (float) texture.getWidth() / (float) uiText.getColumns();
    float tileHeight = (float) texture.getHeight() / (float) uiText.getRows();
    for (int i = 0; i < numChars; i++) {
      byte currChar = chars[i];
      int col = currChar % uiText.getColumns();
      int row = currChar / uiText.getColumns();

      // Build a character tile composed by two triangles

      // Left Top vertex
      vertices.add((float) i * tileWidth); // x
      vertices.add(0.0f); // y
      vertices.add(0.0f); // z
      textCoords.add((float) col / (float) uiText.getColumns());
      textCoords.add((float) row / (float) uiText.getRows());
      indices.add(i * 4);

      // Left Bottom vertex
      vertices.add((float) i * tileWidth); // x
      vertices.add(tileHeight); // y
      vertices.add(0.0f); // z
      textCoords.add((float) col / (float) uiText.getColumns());
      textCoords.add((float) (row + 1) / (float) uiText.getRows());
      indices.add(i * 4 + 1);

      // Right Bottom vertex
      vertices.add((float) i * tileWidth + tileWidth); // x
      vertices.add(tileHeight); // y
      vertices.add(0.0f); // z
      textCoords.add((float) (col + 1) / (float) uiText.getColumns());
      textCoords.add((float) (row + 1) / (float) uiText.getRows());
      indices.add(i * 4 + 2);

      // Right Top vertex
      vertices.add((float) i * tileWidth + tileWidth); // x
      vertices.add(0.0f); // y
      vertices.add(0.0f); // z
      textCoords.add((float) (col + 1) / (float) uiText.getColumns());
      textCoords.add((float) row / (float) uiText.getRows());
      indices.add(i * 4 + 3);

      // Add indices por left top and bottom right vertices
      indices.add(i * 4);
      indices.add(i * 4 + 2);
    }

    float[] verticesArray = new float[vertices.size()];
    int[] indicesArray = new int[indices.size()];
    for (int i = 0; i < vertices.size(); i++) {
      verticesArray[i] = vertices.get(i);
    }
    for (int i = 0; i < indices.size(); i++) {
      indicesArray[i] = indices.get(i);
    }

    return meshFactory.createMesh(verticesArray, null, null, indicesArray);
  }
}
