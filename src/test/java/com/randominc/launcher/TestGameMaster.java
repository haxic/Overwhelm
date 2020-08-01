package com.randominc.launcher;

import com.randominc.client.component.Actor;
import com.randominc.client.component.Camera;
import com.randominc.client.component.Direction;
import com.randominc.client.component.DirectionalMovement;
import com.randominc.client.component.Position;
import com.randominc.client.component.Scale;
import com.randominc.client.component.ui.UIPosition;
import com.randominc.client.component.ui.UIRotation;
import com.randominc.client.component.ui.UIScale;
import com.randominc.client.data.DefaultGameModelFactory;
import com.randominc.client.data.DefaultMeshProvider;
import com.randominc.client.data.DefaultModelProvider;
import com.randominc.client.engine.core.GameController;
import com.randominc.client.engine.core.GameMaster;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.client.engine.graphic.render.Scene;
import com.randominc.client.engine.graphic.render.Terrain;
import com.randominc.client.engine.window.WindowInputProvider;
import com.randominc.client.system.CameraFreeLookControlSystem;
import com.randominc.client.system.DirectionalMovementSystem;
import com.randominc.client.system.UITextSystem;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;
import java.util.Objects;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TestGameMaster implements GameMaster {

  private final EntityManager entityManager;
  private UITextSystem uiTextManager;
  private final DefaultGameModelFactory defaultGameModelFactory;
  private final DefaultMeshProvider defaultMeshProvider;
  private final DefaultModelProvider defaultModelProvider;

  private GameController gameController;

  private Scene currentScene;
  private WindowInputProvider windowInputProvider;

  // Systems
  private CameraFreeLookControlSystem cameraFreeLookController;
  private DirectionalMovementSystem directionalMovementSystem;

  public TestGameMaster(
      EntityManager entityManager,
      UITextSystem uiTextManager,
      DefaultGameModelFactory defaultGameModelFactory,
      DefaultMeshProvider defaultMeshProvider,
      DefaultModelProvider defaultModelProvider,
      WindowInputProvider windowInputProvider) {

    this.entityManager = Objects.requireNonNull(entityManager);
    this.uiTextManager = uiTextManager;
    this.defaultGameModelFactory = Objects.requireNonNull(defaultGameModelFactory);
    this.defaultMeshProvider = Objects.requireNonNull(defaultMeshProvider);
    this.defaultModelProvider = Objects.requireNonNull(defaultModelProvider);
    this.windowInputProvider = Objects.requireNonNull(windowInputProvider);

    currentScene = createScene();
  }

  @Override
  public void initialize(GameController gameController) {
    this.gameController = Objects.requireNonNull(gameController);

    defaultGameModelFactory.initialize();

    cameraFreeLookController =
        new CameraFreeLookControlSystem(entityManager, windowInputProvider, gameController);
    directionalMovementSystem = new DirectionalMovementSystem(entityManager);

    createStartingCubes();
    createUIElement();
    createUITextElement();

    currentScene.addTerrain(generateTerrain());
    gameController.showCursor();
  }

  private void createUITextElement() {

    Entity entity = uiTextManager.createUITextElement("Hellow World!", 0.25f, 0.25f, 1, 1, 0);

    currentScene.addUIElement(entity);
  }

  private void createUIElement() {
    UIPosition position = new UIPosition(new Vector2f(-0.5f, 0.5f));
    UIScale scale = new UIScale(new Vector2f(0.10f, 0.10f));
    UIRotation rotation = new UIRotation(0);
    Model model = defaultModelProvider.getDefaultQuad();
    Actor actor = new Actor(model);
    Entity testEntity = entityManager.createEntity(position, scale, rotation, actor);
    currentScene.addUIElement(testEntity);
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
    return new Terrain(defaultModelProvider.getTestTerrain());
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
    Actor actor = new Actor(defaultModelProvider.getDefaultCube());
    Entity testEntity = entityManager.createEntity(position, scale, direction, actor);
    scene.addEntity(testEntity);
  }
}
