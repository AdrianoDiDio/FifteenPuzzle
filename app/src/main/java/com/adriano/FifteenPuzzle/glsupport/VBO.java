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

import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.TexCoord;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * @author Adriano di Dio
 */
public class VBO {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;
    private int[] VaoID;
    private int[] VBOID;
    private int[] IBOID;

    private FloatBuffer VertexBuffer;
    private ShortBuffer IndexBuffer;
    private int IndicesLength;
    private boolean Initialized;


    private void SetVertexBuffer(float[] Array) {
        ByteBuffer Temp;
        Temp = ByteBuffer.allocateDirect(Array.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder());
        if( VertexBuffer != null ) {
            VertexBuffer.clear();
        }
        VertexBuffer = Temp.asFloatBuffer();
        VertexBuffer.put(Array).flip();
    }

    private void SetIndexBuffer(short[] Array) {
        ByteBuffer Temp;
        Temp = ByteBuffer.allocateDirect(Array.length * BYTES_PER_SHORT).order(ByteOrder.nativeOrder());
        IndexBuffer = Temp.asShortBuffer();
        IndexBuffer.put(Array).flip();
    }

    private float[] GetVertexArray(Rect Rectangle, TexCoord TextureCoordinates) {
        float[] Vertices = {
                Rectangle.Position.x,Rectangle.Position.y,
                TextureCoordinates.u,TextureCoordinates.v,
                Rectangle.Position.x,Rectangle.Position.y + Rectangle.Height,
                TextureCoordinates.u,TextureCoordinates.v + TextureCoordinates.Height,
                Rectangle.Position.x + Rectangle.Width, Rectangle.Position.y + Rectangle.Height,
                TextureCoordinates.u + TextureCoordinates.Width,TextureCoordinates.v + TextureCoordinates.Height,
                Rectangle.Position.x + Rectangle.Width,Rectangle.Position.y,
                TextureCoordinates.u + TextureCoordinates.Width,TextureCoordinates.v
        };
        return Vertices;
    }

    private float[] GetVertexArray(Rect Rectangle, Color4 Color) {
        float[] Vertices = {
                Rectangle.Position.x,Rectangle.Position.y,
                Color.r,Color.g,Color.b,Color.a,
                Rectangle.Position.x,Rectangle.Position.y + Rectangle.Height,
                Color.r,Color.g,Color.b,Color.a,
                Rectangle.Position.x + Rectangle.Width,Rectangle.Position.y + Rectangle.Height,
                Color.r,Color.g,Color.b,Color.a,
                Rectangle.Position.x + Rectangle.Width,Rectangle.Position.y,
                Color.r,Color.g,Color.b,Color.a
        };
        return Vertices;
    }

    private float[] GetVertexArray(Vec2 Start, Vec2 End,Color4 Color) {
        float[] Vertices = {
                Start.x,Start.y,
                Color.r,Color.g,Color.b,Color.a,
                End.x,End.y,
                Color.r,Color.g,Color.b,Color.a,
        };
        return Vertices;
    }


    public void UpdateVertices(Rect Rectangle,TexCoord TextureCoordinates) {
        float[] Vertices;
        Vertices = GetVertexArray(Rectangle,TextureCoordinates);
        UpdateVertices(Vertices);
    }

    public void UpdateVertices(Vec2 Position,float Width,float Height,TexCoord TextureCoordinates) {
        UpdateVertices(new Rect(Position,Width,Height),TextureCoordinates);
    }

    public void UpdateVertices(Rect Rectangle,Color4 Color) {
        float[] Vertices;
        Vertices = GetVertexArray(Rectangle,Color);
        UpdateVertices(Vertices);
    }

    public void UpdateVertices(Vec2 Start,Vec2 End,Color4 Color) {
        float[] Vertices;
        Vertices = GetVertexArray(Start,End,Color);
        UpdateVertices(Vertices);
    }

    public void UpdateVertices(float[] Vertices) {
        SetVertexBuffer(Vertices);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBOID[0]);
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0,Vertices.length * BYTES_PER_FLOAT,
                VertexBuffer);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    public void Draw(int Mode) {
        if( !Initialized ) {
            System.err.println("VBO:Draw call without initialization.");
            return;
        }
        GLES30.glBindVertexArray(VaoID[0]);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, IBOID[0]);
        GLES30.glDrawElements(Mode, IndicesLength, GLES30.GL_UNSIGNED_SHORT, 0);
        GLES30.glBindVertexArray(0);
        /*
         */
    }
    /*
    * Draw the VBO using GL_TRIANGLES.
    * If we need another shape call Draw and pass the appropriate mode there.
    * */
    public void Draw() {
        Draw(GLES30.GL_TRIANGLES);
    }

    public void DrawAsLine() {
        if( !Initialized ) {
            System.err.println("VBO:Draw call without initialization.");
            return;
        }
        GLES30.glBindVertexArray(VaoID[0]);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, IBOID[0]);
        GLES30.glDrawElements(GLES30.GL_LINES, IndicesLength, GLES30.GL_UNSIGNED_BYTE, 0);
        GLES30.glBindVertexArray(0);

    }

    public void InitXYRGBA(Rect Rectangle,Color4 Color,short[] Indices) {
        InitXYRGBA(GetVertexArray(Rectangle,Color),Indices);
    }

    /*
    * Build a VBO without position so that it can be translated later.
    * */
    public void InitXYRGBA(float Width,float Height,Color4 Color,short[] Indices) {
        InitXYRGBA(GetVertexArray(new Rect(Vec2.ZeroVector(),Width,Height),Color),Indices);
    }

    /**
     * @param Vertices:An array of Vertices/Colors packed as XYRGBA
     * @param Indices: The indices to draw the shape.
     */
    public void InitXYRGBA(float[] Vertices, short[] Indices) {
        int Stride;
        int VertexOffset;
        int ColorOffset;
        //If data is packed as XYRGBA then stride will be 6 (Number of attributes per vertex)
        // times the size of a float.
        Stride = 6 * BYTES_PER_FLOAT;
        //Vertex starts at 0.
        VertexOffset = 0;
        //Color starts after the first vertex which is 2 (X,Y) times the size of a float.
        ColorOffset = 2 * BYTES_PER_FLOAT;

        GLES30.glGenVertexArrays(1,VaoID,0);
        GLES30.glBindVertexArray(VaoID[0]);

        GLES30.glGenBuffers(1,VBOID,0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,VBOID[0]);
        SetVertexBuffer(Vertices);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, Vertices.length * BYTES_PER_FLOAT,
                VertexBuffer, GLES30.GL_DYNAMIC_DRAW);
        GLES30.glVertexAttribPointer(0,2,GLES30.GL_FLOAT,false,Stride,VertexOffset);
        GLES30.glVertexAttribPointer(1,4,GLES30.GL_FLOAT,false,Stride,ColorOffset);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);

        GLES30.glGenBuffers(1,IBOID,0);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,IBOID[0]);
        SetIndexBuffer(Indices);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, Indices.length * BYTES_PER_SHORT,
                IndexBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0);
        GLES30.glBindVertexArray(0);

        IndicesLength = Indices.length;

        Initialized = true;
    }

    public void InitXYUV(Rect Rectangle,TexCoord TextureCoord,short[] Indices) {
        InitXYUV(GetVertexArray(Rectangle,TextureCoord),Indices);
    }

    /*
     * Build a VBO without position so that it can be translated later.
     * */
    public void InitXYUV(float Width,float Height,TexCoord TextureCoord,short[] Indices) {
        InitXYUV(GetVertexArray(new Rect(Vec2.ZeroVector(),Width,Height),TextureCoord),Indices);
    }

    /**
     * @param Vertices:An array of Vertices/Colors packed as XYUV
     * @param Indices: The indices to draw the shape.
     */
    public void InitXYUV(float[] Vertices, short[] Indices) {
        int Stride;
        int VertexOffset;
        int TextureOffset;
        //If data is packed as XYUV then stride will be 4 (Number of attributes per vertex)
        // times the size of a float.
        Stride = 4 * BYTES_PER_FLOAT;
        //Vertex starts at 0.
        VertexOffset = 0;
        //Texture starts after the first vertex which is 2 (X,Y) times the size of a float.
        TextureOffset = 2 * BYTES_PER_FLOAT;

        GLES30.glGenVertexArrays(1,VaoID,0);
        GLES30.glBindVertexArray(VaoID[0]);

        GLES30.glGenBuffers(1,VBOID,0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,VBOID[0]);
        SetVertexBuffer(Vertices);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, Vertices.length * BYTES_PER_FLOAT,
                VertexBuffer, GLES30.GL_DYNAMIC_DRAW);
        GLES30.glVertexAttribPointer(0,2,GLES30.GL_FLOAT,false,Stride,VertexOffset);
        GLES30.glVertexAttribPointer(1,2,GLES30.GL_FLOAT,false,Stride,TextureOffset);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);

        GLES30.glGenBuffers(1,IBOID,0);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,IBOID[0]);
        SetIndexBuffer(Indices);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, Indices.length * BYTES_PER_SHORT,
                IndexBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0);
        GLES30.glBindVertexArray(0);

        IndicesLength = Indices.length;

        Initialized = true;
    }

    public VBO() {
        VaoID = new int[1];
        VBOID = new int[1];
        IBOID = new int[1];
        IndicesLength = 0;
        Initialized = false;
    }

}
