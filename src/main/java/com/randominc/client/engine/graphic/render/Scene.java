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
  private Map<Model, List<Entity>> actors;
  private Model skybox;
  //  private ParticleManager particleManager;
  private final EntityManager entityManager;
  private Entity cameraEntity;
  private List<Terrain> terrains;

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
              "Created scene with a camera entity that is missing necessary component: [WorldObjectComponent=%s, CameraComponent=%S].",
              position, camera));
    }

    lights = new ArrayList<>();
    actors = new HashMap<>();
    cameraEntity.attach(this);
    //    particleManager = new ParticleManager(entityManager);
  }

  //  public void update(float dt) {
  //    particleManager.update(camera, dt);
  //  }

  //  public void addParticleEntity(Entity entity) {
  //    particleManager.addEntity(entity);
  //  }

  //  public void removeParticleEntity(Entity entity) {
  //    particleManager.removeEntity(entity);
  //  }

  public void addLightEntity(Entity entity) {
    Objects.requireNonNull(entity);
    entity.attach(this);
    lights.add(entity);
  }

  public void removeLightEntity(Entity entity) {
    Objects.requireNonNull(entity);
    lights.remove(entity);
    entity.detach(this);
  }

  public void setSkybox(Model skybox) {
    this.skybox = Objects.requireNonNull(skybox);
  }

  public void addTerrain(Terrain terrain) {
    terrains.add(Objects.requireNonNull(terrain));
  }

  public void addEntity(Entity entity) {
    Objects.requireNonNull(entity);
    Actor actor = (Actor) entityManager.getComponent(entity, Actor.class);
    Model model = actor.getModel();
    List<Entity> batch = actors.get(model);
    if (batch != null) {
      batch.add(entity);
    } else {
      List<Entity> newBatch = new ArrayList<>();
      newBatch.add(entity);
      actors.put(model, newBatch);
    }
    entity.attach(this);
  }

  public void removeActorEntity(Entity entity) {
    detach(entity);
    entity.detach(this);
  }

  @Override
  public void detach(Entity entity) {
    Objects.requireNonNull(entity);
    Actor actor = (Actor) entityManager.getComponent(entity, Actor.class);
    if (actor != null) {
      Model model = actor.getModel();
      List<Entity> batch = actors.get(model);
      if (batch == null || batch != null && !batch.contains(entity)) {
        System.out.println(
            "WARNING: trying to remove an entity that isn't in the rendering list! Entity id: "
                + entity.getEntityId()
                + ".");
        return;
      }
      batch.remove(entity);
      if (batch.isEmpty()) actors.remove(model);
    }
    Light light = (Light) entityManager.getComponent(entity, Light.class);
    if (light != null) {
      lights.remove(entity);
    }
  }

  public Entity getCameraEntity() {
    return cameraEntity;
  }

  public List<Entity> getLightEntities() {
    return lights;
  }

  public Map<Model, List<Entity>> getEntities() {
    return actors;
  }

  public Model getSkybox() {
    return skybox;
  }

  //  public ParticleManager getParticleManager() {
  //    return particleManager;
  //  }

  public List<Terrain> getTerrain() {
    return terrains;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }
}
