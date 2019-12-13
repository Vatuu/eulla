#version 400 core

uniform sampler2D portal;

in vec2 uv;

out vec4 colour;

void main() {
    colour = texture(portal, uv);
}