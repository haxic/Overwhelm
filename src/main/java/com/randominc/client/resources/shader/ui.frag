#version 330

in vec2 outTexCoord;

out vec4 fragment;

uniform sampler2D textureSampler;
uniform vec4 color;
uniform int hasTexture;

void main() {
  if (hasTexture == 1)  {
    fragment = color * texture(textureSampler, outTexCoord);
  } else {
    fragment = color;
  }
}
