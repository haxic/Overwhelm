package com.randominc.launcher;

import com.randominc.client.component.Actor;
import com.randominc.client.component.Camera;
import com.randominc.client.component.Direction;
import com.randominc.client.component.DirectionalMovement;
import com.randominc.client.component.Position;
import com.randominc.client.component.Scale;
import com.randominc.client.engine.core.GameEngine;
import com.randominc.client.engine.core.GameMaster;
import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.preprocessing.MeshFactory;
import com.randominc.client.engine.graphic.preprocessing.ModelData;
import com.randominc.client.engine.graphic.render.Scene;
import com.randominc.client.engine.graphic.render.Terrain;
import com.randominc.client.engine.window.WindowInputProvider;
import com.randominc.client.system.CameraFreeLookControlSystem;
import com.randominc.client.system.DirectionalMovementSystem;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;
import java.util.Objects;
import org.joml.Vector3f;

public class TestGameMaster implements GameMaster {

  private final EntityManager entityManager;
  private final MeshFactory meshFactory;

  private GameEngine gameEngine;
  private Model cubeModel;
  private Scene currentScene;
  private WindowInputProvider windowInputProvider;

  // Systems
  private CameraFreeLookControlSystem cameraFreeLookController;
  private DirectionalMovementSystem directionalMovementSystem;

  public TestGameMaster(EntityManager entityManager, MeshFactory meshFactory) {

    this.entityManager = Objects.requireNonNull(entityManager);
    this.meshFactory = Objects.requireNonNull(meshFactory);
  }

  @Override
  public void initialize(GameEngine gameEngine, WindowInputProvider windowInputProvider) {
    this.gameEngine = Objects.requireNonNull(gameEngine);
    this.windowInputProvider = Objects.requireNonNull(windowInputProvider);

    gameEngine.showCursor();
    cameraFreeLookController =
        new CameraFreeLookControlSystem(entityManager, windowInputProvider, gameEngine);
    directionalMovementSystem = new DirectionalMovementSystem(entityManager);

    cubeModel = createCubeModel();
    currentScene = createScene();
    createStartingCubes();
    currentScene.addTerrain(generateTerrain());
  }

  private void createStartingCubes() {
    // Create cubes in front
    float distance = 10.0f;
    float height = 11.0f;
    createCube(currentScene, distance, 0.0f, 0.0f + height);
    createCube(currentScene, distance, distance, 0.0f + height);
    createCube(currentScene, distance, -distance, 0.0f + height);

    createCube(currentScene, distance, 0.0f, distance + height);
    createCube(currentScene, distance, distance, distance + height);
    createCube(currentScene, distance, -distance, distance + height);

    createCube(currentScene, distance, 0.0f, -distance + height);
    createCube(currentScene, distance, distance, -distance + height);
    createCube(currentScene, distance, -distance, -distance + height);

    // Create cubes in behind
    createCube(currentScene, -distance, 0.0f, 0.0f + height);
    createCube(currentScene, -distance, distance, 0.0f + height);
    createCube(currentScene, -distance, -distance, 0.0f + height);

    createCube(currentScene, -distance, 0.0f, distance + height);
    createCube(currentScene, -distance, distance, distance + height);
    createCube(currentScene, -distance, -distance, distance + height);

    createCube(currentScene, -distance, 0.0f, -distance + height);
    createCube(currentScene, -distance, distance, -distance + height);
    createCube(currentScene, -distance, -distance, -distance + height);

    // Create cubes around
    createCube(currentScene, 0.0f, distance, 0.0f + height);
    createCube(currentScene, 0.0f, -distance, 0.0f + height);

    createCube(currentScene, 0.0f, 0.0f, distance + height);
    createCube(currentScene, 0.0f, distance, distance + height);
    createCube(currentScene, 0.0f, -distance, distance + height);

    createCube(currentScene, 0.0f, 0.0f, -distance + height);
    createCube(currentScene, 0.0f, distance, -distance + height);
    createCube(currentScene, 0.0f, -distance, -distance + height);
  }

  private Terrain generateTerrain() {
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
        vertices[vertexPointer * 3 + 1] = 0;
        vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * size;
        normals[vertexPointer * 3] = 0;
        normals[vertexPointer * 3 + 1] = 1;
        normals[vertexPointer * 3 + 2] = 0;
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
    return new Terrain(new Model(mesh));
  }

  @Override
  public Scene getCurrentScene() {
    return currentScene;
  }

  @Override
  public EntityManager getEntityManager() {
    return null;
  }

  @Override
  public void update(float delta) {
    cameraFreeLookController.update();
    directionalMovementSystem.update(delta);
    //    if (windowInputProvider.getKey(GLFW.GLFW_KEY_F11) == KeyState.PRESSED) {
    //      gameController.exitGame();
    //    }
    //    KeyState key = windowInputProvider.getKey(GLFW.GLFW_KEY_ESCAPE);
    //    if (key == KeyState.PRESSED) {
    //      gameController.exitGame();
    //    }
    //    userControlSystem = new UserControlSystem(gameBuilder.getEntityManager());
  }

  private Scene createScene() {
    Position position = new Position(new Vector3f(1.0f, 1.0f, 3.0f));
    Direction direction = new Direction();
    //    direction.pitch((float) Math.toRadians(90));
    Camera camera = new Camera();
    DirectionalMovement directionalMovement = new DirectionalMovement();
    Entity cameraEntity =
        entityManager.createEntity(position, camera, direction, directionalMovement);
    return new Scene(entityManager, cameraEntity);
  }

  private void createCube(Scene scene, float x, float y, float z) {
    Position position = new Position(new Vector3f(x, y, z));
    Scale scale =
        new Scale(
            new Vector3f(
                (float) (0.5f + (Math.random() * 0.5f)),
                (float) (0.5f + (Math.random() * 0.5f)),
                (float) (0.5f + (Math.random() * 0.5f))));
    Direction direction = new Direction();
    Actor actor = new Actor(cubeModel);
    Entity testEntity = entityManager.createEntity(position, scale, direction, actor);
    scene.addEntity(testEntity);
  }

  private Model createCubeModel() {
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
    return new Model(mesh);
  }
}
