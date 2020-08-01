package com.randominc.client.game.control;

public class Binding {

  private final ControllerType controllerType;
  private final int binding;

  public Binding(ControllerType controllerType, int binding) {
    this.controllerType = controllerType;
    this.binding = binding;
  }

  public ControllerType getControllerType() {
    return controllerType;
  }

  public int getBinding() {
    return binding;
  }
}
