#version 150

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec4 color;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.1);
    vec3 diffuse = brightness * vec3(1.0, 1.0, 0.9);

    vec4 tex_color = texture(modelTexture, pass_textureCoordinates);

	out_Color =  tex_color;
}