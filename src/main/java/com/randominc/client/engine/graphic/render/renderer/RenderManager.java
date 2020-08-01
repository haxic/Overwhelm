package com.randominc.client.engine.graphic.render.renderer;

import com.randominc.client.component.Camera;
import com.randominc.client.component.Direction;
import com.randominc.client.component.Position;
import com.randominc.client.engine.graphic.render.Scene;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;

public class RenderManager {
  private final DebugLog debugLog;
  private final EntityRenderer entityRenderer;
  private final TerrainRenderer terrainRenderer;
  private final UIRenderer uiRenderer;

  public RenderManager() {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    entityRenderer = new EntityRenderer();
    terrainRenderer = new TerrainRenderer();
    uiRenderer = new UIRenderer();
  }

  public void initialize() {
    entityRenderer.initialize();
    terrainRenderer.initialize();
    uiRenderer.initialize();
  }

  public void render(Scene scene) {
    EntityManager entityManager = scene.getEntityManager();
    Entity cameraEntity = scene.getCameraEntity();

    Camera camera = (Camera) entityManager.getComponent(cameraEntity, Camera.class);
    Position position = (Position) entityManager.getComponent(cameraEntity, Position.class);
    Direction direction = (Direction) entityManager.getComponent(cameraEntity, Direction.class);

    camera.updateProjectionMatrix(800 / 600);
    camera.updateViewMatrix(position.getPosition(), direction.getForward(), direction.getUp());

    terrainRenderer.render(
        scene.getTerrain(),
        position.getPosition(),
        camera.getProjectionMatrix(),
        camera.getViewMatrix());

    entityRenderer.render(
        entityManager,
        scene.getLightEntities(),
        scene.getEntities(),
        position.getPosition(),
        camera.getProjectionMatrix(),
        camera.getViewMatrix());
  }
}
