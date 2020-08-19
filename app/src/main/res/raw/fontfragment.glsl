#version 300 es

precision mediump float;

uniform sampler2D FontTexture;

in vec2 pass_TextureCoord;
in vec4 TextureColor;
//out vec4 out_Color;
out vec4 Color;
void main(void) {
  vec4 Temp;
	// Override out_Color with our texture pixel
	//out_Color = texture(texture_diffuse, pass_TextureCoord.st);
		  //Temp = texture(FontTexture, pass_TextureCoord.st) * TextureColor;
		  //Color = vec4(1.0,1.0,1.0,Temp.r);
    //Temp = texture(FontTexture, pass_TextureCoord.st) * TextureColor;
    //Color = vec4(1.0,1.0,1.0,Temp.r);
    Color = vec4(1, 1, 1, texture(FontTexture, pass_TextureCoord).r) * TextureColor;
	  //Color.a = 0.0;
}
