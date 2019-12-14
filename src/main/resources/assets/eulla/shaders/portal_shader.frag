#version 400 core

uniform sampler2D portal;

uniform mat4 screen2tex = mat4(
0.5, 0, 0, 0,
0, 0.5, 0, 0,
0, 0, 0.5, 0,
0.5, 0.5, 0.5, 1
);

in vec4 pos1;

out vec4 colour;

void main() {
    vec4 p = screen2tex * pos1;
    vec2 uv = p.xy / p.w;
    colour = texture(portal, uv);
}