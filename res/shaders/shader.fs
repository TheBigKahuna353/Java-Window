#version 330

uniform sampler2D sample;

varying vec2 texCoords;

layout(location = 0) out vec4 diffuseColor;

void main() {
    // gl_FragColor = texture2D(sample, texCoords);
    // gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
    diffuseColor = texture2D(sample, texCoords);
    // diffuseColor = vec4(1.0, 0.0, 0.0, 1.0);
}