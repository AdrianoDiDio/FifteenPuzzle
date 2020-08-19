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
import android.opengl.GLES30;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Adriano di Dio
 */
public class ShaderManager {
    private ArrayList<ShaderProgram> ShaderList;
    private Context AndroidContext;

    private String GetResourceNameByID(int ResourceID) {
        return AndroidContext.getResources().getResourceName(ResourceID);
    }


    private int GetShader(Context AndroidContext,int ShaderID, int Type) {
        int ID;
        StringBuilder Buffer = new StringBuilder();
        String CLine;
        int CompileStatus;
        int MaxLength;
        String Error;

        CompileStatus = 0;
        MaxLength = 0;

        try {
            InputStream ShaderStream = AndroidContext.getResources().openRawResource(ShaderID);
            InputStreamReader InReader = new InputStreamReader(ShaderStream);
            BufferedReader BReader = new BufferedReader(InReader);
            while( (CLine = BReader.readLine() ) != null ) {
                Buffer.append(CLine);
                Buffer.append("\n");
            }
            BReader.close();
        } catch (IOException e) {
            Timber.d("ID " + GetResourceNameByID(ShaderID) + " was not found.");
            e.printStackTrace();
        }
        ID = GLES30.glCreateShader(Type);
        GLES30.glShaderSource(ID, Buffer.toString());
        GLES30.glCompileShader(ID);

        int Compiled[] = new int[1];
        GLES30.glGetShaderiv(ID, GLES30.GL_COMPILE_STATUS,Compiled,0);
        if(Compiled[0] == GLES30.GL_FALSE)
        {
            //GLES30.glGetShaderiv(ID, GLES30.GL_COMPILE_STATUS,Compiled,0);
            Error = GLES30.glGetShaderInfoLog(ID);
            Timber.e("Failed when compiling " + GetResourceNameByID(ShaderID) + "...");
            Timber.e(Error);
            GLES30.glDeleteShader(ID);
            return -1;
        }
        return ID;
    }

    /**
     * @param ResourceID
     * @return
     */
    public int GetProgramIdByResourceID(int ResourceID) {
        for( ShaderProgram Temp : ShaderList ) {
            if( Temp.ResourceID == ResourceID ) {
                return Temp.ProgramID;
            }
        }
        Timber.e("Shader with ID" + GetResourceNameByID(ResourceID) + " not found.");
        return -1;
    }

    public boolean ShaderExists(int ResourceID) {
        for( ShaderProgram Temp : ShaderList ) {
            if( Temp.ResourceID == ResourceID ) {
                return true;
            }
        }
        Timber.d("Shader with ID " + GetResourceNameByID(ResourceID) + " Doesn't exists.");
        return false;
    }

    /**
     *
     * @param ShaderResourceID Needed for fast lookup.
     * @param VertexResourceID VertexShader Path.
     * @param FragmentResourceID FragmentShader Path.
     */
    public void Cache(Context AndroidContext,int ShaderResourceID,int VertexResourceID,int FragmentResourceID) {
        ShaderProgram Temp = new ShaderProgram();
        int CompileStatus;
        int MaxLength;
        String Error;

        if( ShaderExists(ShaderResourceID) ) {
            return;
        }

        CompileStatus = 0;
        MaxLength = 0;
        Temp.ResourceID = ShaderResourceID;
        Temp.Name = GetResourceNameByID(ShaderResourceID);
        Temp.VertexID = GetShader(AndroidContext,VertexResourceID,GLES30.GL_VERTEX_SHADER);
        if( Temp.VertexID == -1 ) {
            Timber.e("Aborting...an error has occurred while compiling Vertex shader.");
            return;
        }
        Temp.FragmentID = GetShader(AndroidContext,FragmentResourceID,GLES30.GL_FRAGMENT_SHADER);
        if( Temp.FragmentID == -1 ) {
            Timber.e("Aborting...an error has occurred while compiling Fragment shader.");
            return;
        }

        Temp.ProgramID = GLES30.glCreateProgram();

        GLES30.glAttachShader(Temp.ProgramID, Temp.VertexID);
        GLES30.glAttachShader(Temp.ProgramID, Temp.FragmentID);
        GLES30.glLinkProgram(Temp.ProgramID);
        int Compiled[] = new int[1];
        GLES30.glGetProgramiv(Temp.ProgramID, GLES30.GL_LINK_STATUS,Compiled,0);
        if(Compiled[0] == GLES30.GL_FALSE) {
            Error = GLES30.glGetProgramInfoLog(Temp.ProgramID);
            Timber.e("Failed when Linking " + Temp.Name + "...");
            Timber.e(Error);
            GLES30.glDeleteProgram(Temp.ProgramID);
            return;
        }
        GLES30.glValidateProgram(Temp.ProgramID);
        Timber.d("Raised error " + GLES30.glGetError());
        GLES30.glGetProgramiv(Temp.ProgramID, GLES30.GL_VALIDATE_STATUS,Compiled,0);
        if(Compiled[0] == GLES30.GL_FALSE) {
            Error = GLES30.glGetProgramInfoLog(Temp.ProgramID);
            Timber.e("Failed when Validating program " + Temp.Name + "...");
            Timber.e(Error);
            GLES30.glDeleteProgram(Temp.ProgramID);
            return;
        }
        Timber.d("Created program ID " + Temp.ProgramID);
        ShaderList.add(Temp);
        return;
    }

    public ShaderManager(Context AndroidContext) {
        Timber.d("ShaderManager Init...");
        ShaderList = new ArrayList<>();
        this.AndroidContext = AndroidContext;
    }
}