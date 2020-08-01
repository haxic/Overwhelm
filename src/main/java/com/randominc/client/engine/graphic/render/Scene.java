package com.randominc.client.engine.graphic.render;

import com.randominc.client.component.Actor;
import com.randominc.client.component.Camera;
import com.randominc.client.component.Light;
import com.randominc.client.component.Position;
import com.randominc.client.engine.graphic.model.Model;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityContainer;
import com.randominc.shared.hecs.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Scene implements EntityContainer {

  private final DebugLog debugLog;
  private List<Entity> lights;
  private Map<Model, List<Entity>> entities;
  private Model skybox;
  //  private ParticleManager particleManager;
  private final EntityManager entityManager;
  private Entity cameraEntity;
  private List<Terrain> terrains;
  private Map<Model, List<Entity>> uiElements;

  public Scene(EntityManager entityManager, Entity cameraEntity) {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    this.entityManager = Objects.requireNonNull(entityManager);
    this.cameraEntity = Objects.requireNonNull(cameraEntity);
    terrains = new ArrayList<>();

    Position position = (Position) entityManager.getComponent(cameraEntity, Position.class);
    Camera camera = (Camera) entityManager.getComponent(cameraEntity, Camera.class);
    if (position == null || camera == null) {
      debugLog.error(
          String.format(
              "Created scene with a camera entity that is missing necessary component: [WorldObjectComponent='%s', CameraComponent='%s'].",
              position, camera));
    }

    lights = new ArrayList<>();
    uiElements = new HashMap<>();
    entities = new HashMap<>();
    cameraEntity.attach(this);
  }

  public void addTerrain(Terrain terrain) {
    terrains.add(Objects.requireNonNull(terrain));
  }

  public void addEntity(Entity entity) {
    Objects.requireNonNull(entity);
    Actor actor = (Actor) entityManager.getComponent(entity, Actor.class);
    Model model = actor.getModel();
    List<Entity> batch = entities.get(model);
    if (batch != null) {
      batch.add(entity);
    } else {
      List<Entity> newBatch = new ArrayList<>();
      newBatch.add(entity);
      entities.put(model, newBatch);
    }
    entity.attach(this);
  }

  public void addUIElement(Entity entity) {
    Objects.requireNonNull(entity);
    Actor actor = (Actor) entityManager.getComponent(entity, Actor.class);
    Objects.requireNonNull(actor);
    Model model = actor.getModel();
    List<Entity> batch = uiElements.get(model);
    if (batch != null) {
      batch.add(entity);
    } else {
      List<Entity> newBatch = new ArrayList<>();
      newBatch.add(entity);
      uiElements.put(model, newBatch);
    }
    entity.attach(this);
  }

  @Override
  public void detach(Entity entity) {
    Objects.requireNonNull(entity);
    Actor actor = (Actor) entityManager.getComponent(entity, Actor.class);
    if (actor != null) {
      Model model = actor.getModel();
      List<Entity> batch = entities.get(model);
      if (batch == null || batch != null && !batch.contains(entity)) {
        System.out.println(
            "WARNING: trying to remove an entity that isn't in the rendering list! Entity id: "
                + entity.getEntityId()
                + ".");
        return;
      }
      batch.remove(entity);
      if (batch.isEmpty()) entities.remove(model);
    }
    Light light = (Light) entityManager.getComponent(entity, Light.class);
    if (light != null) {
      lights.remove(entity);
    }
  }

  public Entity getCameraEntity() {
    return cameraEntity;
  }

  public Map<Model, List<Entity>> getUiElements() {
    return uiElements;
  }

  public Map<Model, List<Entity>> getEntities() {
    return entities;
  }

  public List<Terrain> getTerrain() {
    return terrains;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }
}
