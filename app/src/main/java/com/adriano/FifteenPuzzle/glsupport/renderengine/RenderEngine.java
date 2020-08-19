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
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.adriano.FifteenPuzzle.glsupport.renderengine.assets.AssetUtils;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontManager;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.util.ArrayList;

import timber.log.Timber;

public class RenderEngine {
    private float VirtualWidth = 1080;
    private float VirtualHeight = 2160;
    private float TargetAspectRatio = VirtualWidth / VirtualHeight;

    public AssetUtils Assets;
    public ShaderManager ShaderUtils;
    public FontManager FontSystem;
    public TextureManager TextureUtils;
    public SoundManager SoundUtils;
    public TimeUtils Time;
    public SettingsUtils Settings;
    public Context AndroidContext;
    public GLSurfaceView Surface;


    public int Width;
    public int Height;
    public float[] ModelMatrix;
    public float[] ViewMatrix;
    public float[] ProjectionMatrix;
    public float[] MVPMatrix;

    private ArrayList<SurfaceChangedObserver> SurfaceChangedObserverList;

    public static long SysMilliseconds() {
        return SystemClock.uptimeMillis();
        //return (System.nanoTime() / 1000000);
    }

    public void CleanUp() {
        SurfaceChangedObserverList.clear();
        Settings.CleanUp();
    }

    public void QueueEvent(Runnable Job) {
        Surface.queueEvent(Job);
    }

    public void CacheShader(int ResourceID,int VertexResourceID,int FragmentResourceID) {
        ShaderUtils.Cache(AndroidContext,ResourceID,VertexResourceID,FragmentResourceID);
    }

    private void NotifyAllObservers() {
        for( SurfaceChangedObserver Observer : SurfaceChangedObserverList ) {
            Observer.SurfaceChanged();
        }
    }


    public void RegisterSurfaceChangedObserver(SurfaceChangedObserver Observer) {
        SurfaceChangedObserverList.add(Observer);
    }

    public void RemoveSurfaceChangedObserver(SurfaceChangedObserver Observer) {
        SurfaceChangedObserverList.remove(Observer);
    }

    public void ResetModelMatrix() {
        Matrix.setIdentityM(ModelMatrix,0);
        UpdateMVPMatrix();
    }

    public Vec2 GetScreenCenter(float RequiredWidth, float RequiredHeight) {
        float x;
        float y;
        x = (Width - RequiredWidth) / 2.f;
        y = (Height - RequiredHeight) / 2.f;
        return new Vec2(x,y);
    }

    public void UpdateMVPMatrix() {
        float[] ModelViewMatrix;

        ModelViewMatrix = new float[16];


        Matrix.setLookAtM(ViewMatrix, 0, 0f, 0f, 1f,
                0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.multiplyMM(ModelViewMatrix,0,ViewMatrix,0,ModelMatrix,0);

        Matrix.orthoM(ProjectionMatrix, 0, 0,Width,Height,0, -1, 1);

        Matrix.multiplyMM(
                MVPMatrix, 0,
                ProjectionMatrix, 0,
                ModelViewMatrix, 0);
    }

    public void OnSurfaceChanged(int Width,int Height) {


        this.Width = Width;
        this.Height = Height;

        UpdateMVPMatrix();
        NotifyAllObservers();
    }

    public void TickTime() {
        Time.Tick();
    }
    public void PrepareToRender() {
        int ViewportX;
        int ViewportY;
        int ViewportWidth;
        int ViewportHeight;

        ViewportWidth = this.Width;
        ViewportHeight = (int) (this.Width / TargetAspectRatio + 0.5f);

        if( ViewportHeight > Height ) {
            ViewportHeight = Height;
            ViewportWidth = (int) (Height * TargetAspectRatio + 0.5f);
        }

        ViewportX = (Width / 2) - (ViewportWidth / 2);
        ViewportY = (Height / 2) - (ViewportHeight / 2);

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glClearColor( 0.f, 0.f, 0.f, 0.f );
        //GLES30.glClearColor( 1.0f, 1.0f, 1.0f, 0.0f );
        GLES30.glViewport(0,0, Width, Height);
    }


    private void InitMatrix() {
        ModelMatrix = new float[16];
        ViewMatrix = new float[16];
        ProjectionMatrix = new float[16];
        MVPMatrix = new float[16];
        Matrix.setIdentityM(ModelMatrix,0);
    }

    public RenderEngine(Context AndroidContext, GLSurfaceView Surface) {
        Timber.d("Starting up....");
        this.AndroidContext = AndroidContext;
        this.Surface = Surface;
        Assets = new AssetUtils(AndroidContext);
        ShaderUtils = new ShaderManager(AndroidContext);
        FontSystem = new FontManager(AndroidContext,Assets);
        TextureUtils = new TextureManager(AndroidContext);
        Time = new TimeUtils();
        Settings = new SettingsUtils(AndroidContext);
        SoundUtils = new SoundManager(AndroidContext,Settings);
        SurfaceChangedObserverList = new ArrayList<>();
        InitMatrix();
    }
}
