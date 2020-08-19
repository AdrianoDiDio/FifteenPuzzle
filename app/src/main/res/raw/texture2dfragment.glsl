#version 300 es

precision mediump float;

uniform sampler2D FontTexture;

in vec2 pass_TextureCoord;
in vec4 TextureColor;
//out vec4 out_Color;
out vec4 Out_Color;
void main(void) {
	vec4 Temp;
	// Override out_Color with our texture pixel
	//out_Color = texture(texture_diffuse, pass_TextureCoord.st);
	Out_Color = texture(FontTexture, pass_TextureCoord.st) * TextureColor;
}