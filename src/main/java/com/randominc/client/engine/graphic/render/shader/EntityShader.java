package com.randominc.client.engine.graphic.render.shader;

import com.randominc.client.engine.graphic.utility.ShaderProgram;
import org.joml.Matrix4f;

public class EntityShader extends ShaderProgram {

  //  public static final int INSTANCE_DATA_LENGTH = 48;
  //  public static final int MAX_LIGHTS = 64;

  private int mvpLocation;
  //  private int location_modelView;
  //  private int location_model;
  //
  //  private int location_view;
  //  private int location_projection;
  //  private int location_cameraPosition;
  // Lighting
  //  private int location_ambientLight;
  // Lighting lights
  //  private int location_numberOfLights;
  //  private int[] location_lightPosition;
  //  private int[] location_lightColor;
  //  private int[] location_attenuation;
  // Lighting material
  //  private int location_materialShininess;
  //  private int location_materialSpecularColor;

  public EntityShader() {
    super(
        "src/main/java/com/randominc/client/resources/shader/entity.vert",
        "src/main/java/com/randominc/client/resources/shader/entity.frag");
  }

  @Override
  protected void getAllUniformLocations() {
    mvpLocation = super.getUniformLocation("mvp");
    //    location_modelView = super.getUniformLocation("modelView");
    //    location_model = super.getUniformLocation("model");
    //    location_view = super.getUniformLocation("view");
    //    location_projection = super.getUniformLocation("projection");
    //    location_cameraPosition = super.getUniformLocation("cameraPosition");
    // Lighting
    //    location_ambientLight = super.getUniformLocation("ambientLight");
    // Lighting lights
    //    location_numberOfLights = super.getUniformLocation("numberOfLights");
    //    location_lightPosition = new int[MAX_LIGHTS];
    //    location_lightColor = new int[MAX_LIGHTS];
    //    location_attenuation = new int[MAX_LIGHTS];
    //    for (int i = 0; i < MAX_LIGHTS; i++) {
    //      location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
    //      location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
    //      location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
    //    }
    // Lighting material
    //    location_materialShininess = super.getUniformLocation("materialShininess");
    //    location_materialSpecularColor = super.getUniformLocation("materialSpecularColor");
  }

  @Override
  protected void bindAttributes() {
    super.bindAttribute(0, "position");
  }

  public void loadModelViewProjectionMatrix(Matrix4f mvp) {
    super.loadMatrixf(mvpLocation, mvp);
  }

  //  public void loadModelViewMatrix(Matrix4f modelView) {
  //    super.loadMatrixf(location_modelView, modelView);
  //  }

  //  public void loadModelMatrix(Matrix4f model) {
  //    super.loadMatrixf(location_model, model);
  //  }

  //  public void loadViewMatrix(Matrix4f view) {
  //    super.loadMatrixf(location_view, view);
  //  }

  //  public void loadProjectionMatrix(Matrix4f projection) {
  //    super.loadMatrixf(location_projection, projection);
  //  }

  //  public void loadCameraPosition(Vector3f vector3f) {
  //    super.loadVector3f(location_cameraPosition, vector3f);
  //  }

  //  public void loadAmbientLight(float ambientLight) {
  //    super.loadFloat(location_ambientLight, ambientLight);
  //  }

  //  public void loadLights(EntityManager entityManager, List<Entity> lights) {
  //    int numberOfLights = lights.size() < MAX_LIGHTS ? lights.size() : MAX_LIGHTS;
  //    super.loadInt(location_numberOfLights, numberOfLights);
  //    for (int i = 0; i < MAX_LIGHTS && i < numberOfLights; i++) {
  //      Entity entity = lights.get(i);
  //      Position objectComponent =
  //          (Position) entityManager.getComponent(entity, Position.class);
  //      Light light =
  //          (Light) entityManager.getComponent(entity, Light.class);
  //      super.loadVector3f(location_lightPosition[i], objectComponent.getPosition());
  //      super.loadVector3f(location_lightColor[i], light.getColor());
  //      super.loadVector3f(location_attenuation[i], light.getAttenuation());
  //    }
  //  }

  //  public void loadSpecularLighting(Model model) {
  //    super.loadFloat(location_materialShininess, model.getShininess());
  //    super.loadVector3f(location_materialSpecularColor, model.getSpecularColor());
  //  }
}
