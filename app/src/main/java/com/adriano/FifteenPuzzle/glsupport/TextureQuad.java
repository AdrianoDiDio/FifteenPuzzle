/*
===========================================================================
    Copyright (C) 2019 Adriano Di Dio.
    
    FifteenPuzzle is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FifteenPuzzle is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FifteenPuzzle.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================
*/ 
package com.adriano.FifteenPuzzle.glsupport;

import android.opengl.GLES30;
import android.util.TimingLogger;

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.TexCoord;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class TextureQuad extends Quad {
    protected TexCoord TextureCoordinates;
    private int TextureID;

    public void SetTextureID(int TextureID) {
        this.TextureID = TextureID;
    }

    public void setTextureCoordinates(TexCoord TextureCoordinates) {
        this.TextureCoordinates = TextureCoordinates;
        UpdateData();
    }

    public TexCoord getTextureCoordinates() {
        return TextureCoordinates;
    }

    @Override
    public void UpdateData() {
        Vbo.UpdateVertices(Vec2.ZeroVector(),GetRectangle().Width,GetRectangle().Height,TextureCoordinates);
    }

    public void Draw(Color4 Color) {
        int MatrixID;
        int ColorID;

        super.Draw();

        GLES30.glUseProgram(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.textureshader2d));
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TextureID);

        MatrixID = GLES30.glGetUniformLocation(
                RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.textureshader2d),
                "OrthoMatrix");
        ColorID =  GLES30.glGetUniformLocation(
                RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.textureshader2d),
                "in_TextureColor");



        GLES30.glUniformMatrix4fv(MatrixID,1, false, RenderUtils.MVPMatrix,0);
        GLES30.glUniform4f(ColorID,Color.r,Color.g,Color.b,Color.a);
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        Vbo.Draw();
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,0);
        GLES30.glDisable(GLES30.GL_BLEND);
        GLES30.glUseProgram(0);

        RenderUtils.ResetModelMatrix();
    }

    public void Draw() {
        Draw(new Color4(1.f,1.f,1.f,1.f));
    }

    public void Init(Vec2 Position,float Width,float Height, TexCoord TextureCoordinates) {
        short[] Indices = {
                0, 1, 2,
                3, 0, 2
        };
        Vbo = new VBO();
        //Build a VBO with no position set.
        Vbo.InitXYUV(Width,Height,TextureCoordinates, Indices);

        RenderUtils.CacheShader(R.id.textureshader2d, R.raw.texture2dvertex,R.raw.texture2dfragment);

        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.textureshader2d),
                0, "in_Position");
        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.textureshader2d),
                1, "in_TextureCoord");
        SetRectangle(Position, Width, Height);
    }

    public TextureQuad(RenderEngine RenderUtils,int TextureID,
                       Vec2 Position, float Width, float Height,TexCoord TextureCoordinates) {
        this.RenderUtils = RenderUtils;
        this.TextureID = TextureID;
        Init(Position,Width,Height,TextureCoordinates);
    }
    public TextureQuad(RenderEngine RenderUtils,int TextureID,Rect Rectangle,TexCoord TextureCoordinates) {
        this.RenderUtils = RenderUtils;
        this.TextureID = TextureID;
        this.TextureCoordinates = TextureCoordinates;
        Init(Rectangle.Position,Rectangle.Width,Rectangle.Height,TextureCoordinates);
    }
}
