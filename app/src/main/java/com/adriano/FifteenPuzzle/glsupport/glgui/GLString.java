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
package com.adriano.FifteenPuzzle.glsupport.glgui;

import android.annotation.SuppressLint;
import android.opengl.GLES30;
import android.util.TimingLogger;

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.TextureQuad;
import com.adriano.FifteenPuzzle.glsupport.VBO;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontInfo;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.TexCoord;
import com.adriano.FifteenPuzzle.utils.Vec2;

import timber.log.Timber;

/*
* TODO:
* Load GrayScale Image as seen in ARenderer/JVoxelEngine...
* */
public class GLString {
    private RenderEngine RenderUtils;
    private TextureQuad FontQuad;
    private VBO Vbo;
    private FontInfo Font;
    private String Text;
    private Vec2 Position;
    private Color4 Color;
    private Rect Container;
    private GLTextAlign Alignment;

    public int GetLineHeight() {
        return Font.LineHeight;
    }

    public void Append(String Text) {
        this.Text += Text;
    }

    public void Append(char c) {
        this.Text += c;
    }
    public void SetText(String Text) {
        this.Text = Text;
    }

    public void SetPosition(Vec2 Position) {
        this.Position = new Vec2(Position);
    }

    public Vec2 GetPosition() {
        return Position;
    }

    public void SetContainer(Rect Container) {
        if( Alignment == GLTextAlign.ALIGN_NONE ) {
            Timber.d("SetContainer with no alignment specified called");
        }
        this.Container = new Rect(Container);
    }

    private Vec2 GetAlignedPosition() {
        float TextLength;
        Vec2 Result;

        switch ( Alignment ) {
            case ALIGN_CENTER:
                TextLength = Font.GetStringWidth(Text);
                Result = new Vec2(Container.Position);
                Result.x += (Container.Width - TextLength) / 2;
                Result.y += (Container.Height - Font.LineHeight ) / 2;
                return Result;
            case ALIGN_LEFT:
                Result = new Vec2(Container.Position);
                Result.y += (Container.Height - Font.LineHeight ) / 2;
                return Result;
            case ALIGN_NONE:
            default:
                return Position;
        }
    }

    private void DrawChar(Vec2 Position,char c) {
        TexCoord Texel;
        Texel = new TexCoord();
        Texel.u = (float) Font.Character[c].x / Font.Width;
        Texel.v = (float) Font.Character[c].y / Font.Height;
        Texel.Width = (float) Font.Character[c].Width /
                Font.Width;
        Texel.Height = (float) Font.Character[c].Height /
                Font.Height;

        Vbo.UpdateVertices(new Rect(Position,Font.Character[c].Width,Font.Character[c].Height),
                Texel);

        GLES30.glUseProgram(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.fontshader));
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, RenderUtils.TextureUtils.GetTextureID(Font.Name));
        int MatrixID = GLES30.glGetUniformLocation(
                RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.fontshader),
                "OrthoMatrix");
        int ColorID =  GLES30.glGetUniformLocation(
                RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.fontshader),
                "in_TextureColor");

        GLES30.glUniformMatrix4fv(MatrixID,1, false, RenderUtils.MVPMatrix,0);
        GLES30.glUniform4f(ColorID,Color.r,Color.g,Color.b,Color.a);
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        Vbo.Draw();
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,0);
        GLES30.glDisable(GLES30.GL_BLEND);
        GLES30.glUseProgram(0);
        //FontQuad.SetRect(Rectangle,Font.Character[c].Width,Font.Character[c].Height,Texel);

        //FontQuad.Draw(Color);
        //GLES30.glUseProgram(0);
    }

    public void Draw() {
        Vec2 TempPosition;
        float DeltaX;
        int TextLength;

        //TODO:Do alignment (ALIGN_CENTER | ALIGN_LEFT | ALIGN_NONE)
        Position = GetAlignedPosition();
        DeltaX = Position.x;
        TempPosition = new Vec2(Position);
        for (int i = 0; i < Text.length(); i++) {
            if (Text.charAt(i) == '\n') {
                TempPosition.y += Font.LineHeight;
                DeltaX = TempPosition.x;
                // Skip it
                continue;
            }
            char Character = Text.charAt(i);
            DeltaX += Font.Character[Character].BearingX;
            DrawChar(new Vec2(DeltaX,TempPosition.y),Character);
            DeltaX += Font.Character[Character].AdvanceX - Font.Character[Character].BearingX;
        }

    }


    @SuppressLint("BinaryOperationInTimber")
    public void Init() {
        int TextureID;
        short[] Indices = {
                0, 1, 2,
                3, 0, 2
        };
        TextureID = RenderUtils.TextureUtils.Cache(Font.Name, Font.Image/*,GLES30.GL_RGBA*/);
        FontQuad = new TextureQuad(RenderUtils,TextureID,new Vec2(0,0), 0,0,
                new TexCoord(0,0,0,0));
        Vbo = new VBO();
        Vbo.InitXYUV(new Rect(),new TexCoord(), Indices);
        RenderUtils.CacheShader(R.id.fontshader, R.raw.fontvertex,R.raw.fontfragment);
        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.fontshader),
                0, "in_Position");
        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.fontshader),
                1, "in_TextureCoord");
    }

    public GLString(RenderEngine RenderUtils, FontSize Size,String Text,Vec2 Position, Color4 Color) {
        this.RenderUtils = RenderUtils;
        this.Text = Text;
        this.Position = Position;
        this.Color = Color;
        this.Alignment = GLTextAlign.ALIGN_NONE;
        Font = RenderUtils.FontSystem.Font[Size.Value];
        Init();
    }

    /**
     * Build a GLString that will be centered across the given rect.
     * @param RenderUtils
     * @param Size
     * @param Text
     * @param Color
     */
    public GLString(RenderEngine RenderUtils, FontSize Size,String Text,Rect Container,Color4 Color,
                    GLTextAlign Alignment) {
        this.RenderUtils = RenderUtils;
        this.Text = Text;
        this.Position = new Vec2(0,0);
        this.Container = Container;
        this.Color = Color;
        this.Alignment = Alignment;
        Font = RenderUtils.FontSystem.Font[Size.Value];
        Init();
    }
    /**
     * Build an empty GLString.
     * @param RenderUtils
     * @param Size
     * @param Color
     */
    public GLString(RenderEngine RenderUtils,FontSize Size, Color4 Color) {
        this.RenderUtils = RenderUtils;
        this.Text = "";
        this.Color = Color;
        this.Alignment = GLTextAlign.ALIGN_NONE;
        this.Position = new Vec2(Vec2.ZeroVector());
        Font = RenderUtils.FontSystem.Font[Size.Value];
        Init();
    }
}
