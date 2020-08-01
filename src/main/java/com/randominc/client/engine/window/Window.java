package com.randominc.client.engine.window;

import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.util.Objects;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {

  private final DebugLog debugLog;
  private final String title;
  private int width;
  private int height;
  private int positionX;
  private int positionY;
  private DefaultWindowInputProvider defaultWindowInputProvider;
  private final Vector2f cursorPosition;
  private long window;
  private GLFWVidMode videoMode;
  private int frames;
  private int displayedFps;
  private long time;
  private boolean isResized;
  private boolean isFullscreen;
  private boolean isCursorShown;

  public Window(String title, int width, int height) {
    this.debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    this.title = Objects.requireNonNull(title);
    this.width = width;
    this.height = height;
    cursorPosition = new Vector2f();
    create();
  }

  public Window(String title) {
    this.debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    this.title = Objects.requireNonNull(title);
    this.width = 800;
    this.height = 600;
    isFullscreen = true;
    cursorPosition = new Vector2f();
    create();
  }

  private void create() {
    GLFWErrorCallback.createPrint(debugLog.getPrintStream()).set();

    if (!GLFW.glfwInit()) {
      debugLog.error("Could not initialize glfw.");
      return;
    }

    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

    window =
        GLFW.glfwCreateWindow(
            width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
    if (window == 0) {
      debugLog.error("Could not create window.");
      return;
    }

    defaultWindowInputProvider = new DefaultWindowInputProvider(window);
    videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    positionX = (videoMode.width() - width) / 2;
    positionY = (videoMode.height() - height) / 2;

    GLFW.glfwSetWindowPos(window, positionX, positionY);
    GLFW.glfwMakeContextCurrent(window);

    setWindowCallbacks();

    GLFW.glfwSwapInterval(1);

    GL.createCapabilities();
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
  }

  private void setWindowCallbacks() {
    GLFWWindowSizeCallback windowSizeCallback =
        new GLFWWindowSizeCallback() {
          @Override
          public void invoke(long window, int width, int height) {
            if (Window.this.width != width || Window.this.height != height) {
              isResized = true;
            }
            Window.this.width = width;
            Window.this.height = height;
          }
        };

    GLFW.glfwSetKeyCallback(window, defaultWindowInputProvider.getKeyCallback());
    GLFW.glfwSetMouseButtonCallback(window, defaultWindowInputProvider.getMouseButtonCallback());
    GLFW.glfwSetScrollCallback(window, defaultWindowInputProvider.getScrollCallback());
    GLFW.glfwSetCursorPosCallback(window, defaultWindowInputProvider.getCursorPosCallback());
    GLFW.glfwSetWindowSizeCallback(window, windowSizeCallback);
  }

  public void show() {
    GLFW.glfwShowWindow(window);
    hideCursor();
  }

  public void update() {
    if (isResized) {
      GL11.glViewport(0, 0, width, height);
      isResized = false;
    }
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    defaultWindowInputProvider.update();
    GLFW.glfwPollEvents();
    if (!isCursorShown()) {
      GLFW.glfwSetCursorPos(window, width / 2.0f, height / 2.0f);
    }
  }

  public void render() {
    GLFW.glfwSwapBuffers(window);
    frames++;
    if (System.currentTimeMillis() > time + 1000) {
      displayedFps = frames;
      time = System.currentTimeMillis();
      frames = 0;
    }
    cursorPosition.set(defaultWindowInputProvider.getCursorPosition(cursorPosition));
    float xpos = cursorPosition.x();
    float ypos = cursorPosition.y();
    GLFW.glfwSetWindowTitle(
        window,
        String.format("Overwhelm | FPS: %d | Cursor: %d:%d", displayedFps, (int) xpos, (int) ypos));
  }

  public boolean isClosing() {
    return GLFW.glfwWindowShouldClose(window);
  }

  public void cleanUp() {
    defaultWindowInputProvider.cleanUp();
    GLFW.glfwDestroyWindow(window);
    GLFW.glfwTerminate();
    GLFW.glfwSetErrorCallback(null).free();
    debugLog.debug("Window clean up successful.");
  }

  public WindowInputProvider getInputProvider() {
    if (defaultWindowInputProvider == null) {
      debugLog.error("Tried to get input provider before creating the window.");
    }
    return defaultWindowInputProvider;
  }

  public boolean toggleFullscreen() {
    isFullscreen = !isFullscreen;
    isResized = true;
    if (isFullscreen) {
      final int[] xpos = new int[1];
      final int[] ypos = new int[1];
      GLFW.glfwGetWindowPos(window, xpos, ypos);
      positionX = xpos[0];
      positionY = ypos[0];
      GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
    } else {
      GLFW.glfwSetWindowMonitor(window, 0, positionX, positionY, width, height, 0);
    }
    return isFullscreen;
  }

  public void showCursor() {
    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    isCursorShown = true;
  }

  public void hideCursor() {
    GLFW.glfwSetCursorPos(window, width / 2.0f, height / 2.0f);
    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    isCursorShown = false;
  }

  public void toggleCursor() {
    if (isCursorShown) {
      hideCursor();
    } else {
      showCursor();
    }
  }

  public boolean isCursorShown() {
    return isCursorShown;
  }

  public void centerCursor() {
    GLFW.glfwSetCursorPos(window, width / 2.0f, height / 2.0f);
  }

  public Vector2f getCenter(Vector2f destination) {
    return destination.set(width / 2.0f, height / 2.0f);
  }
}
