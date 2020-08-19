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
package com.adriano.FifteenPuzzle.glsupport.renderengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.TimingLogger;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import timber.log.Timber;

public class TextureManager {
    private ArrayList<Texture> TextureList;
    private Context AndroidContext;

    public int GetTextureID(String Name) {
        if( !Exists(Name) ){
            return -1;
        }
        for( Texture Temp : TextureList){
            if( Temp.Name.equals(Name) ){
                return Temp.ID;
            }
        }
        return -1;
    }

    public boolean Exists(String Name) {
        if( TextureList.isEmpty() ){
            return false;
        }
        for( Texture Temp : TextureList ){
            if( Temp.Name.equals(Name) ){
                return true;
            }
        }
        return false;
    }

    public int Cache(String Name, Bitmap Image) {
        Texture Temp;
        int[] ResultTextureID;
        int Result;

        Result = GetTextureID(Name);
        if( Result != -1 ) {
            return Result;
        }
        Timber.d("Texture " + Name + " not found caching it...");
        Temp = new Texture();
        ResultTextureID = new int[1];
        GLES30.glGenTextures(1,ResultTextureID,0);

        Temp.Name = Name;
        Temp.ID = ResultTextureID[0];


        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, ResultTextureID[0]);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER,
                GLES30.GL_LINEAR);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0,Image, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,0);
        TextureList.add(Temp);
        return Temp.ID;
    }

    public int Cache(String Name,int ResourceID) {
        Bitmap Temp;
        Temp = BitmapFactory.decodeResource(AndroidContext.getResources(),
                ResourceID);
        return Cache(Name,Temp);
    }

    public TextureManager(Context AndroidContext) {
        TextureList = new ArrayList<>();
        this.AndroidContext = AndroidContext;
    }
}
