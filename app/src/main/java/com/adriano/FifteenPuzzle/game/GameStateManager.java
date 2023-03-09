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
package com.adriano.FifteenPuzzle.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.adriano.FifteenPuzzle.game.gamestates.IntroState;
import com.adriano.FifteenPuzzle.glsupport.DebugOverlay;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.ContextUtils;

import timber.log.Timber;


public class GameStateManager {
    private GameState CurrentState;
    private RenderEngine RenderUtils;
    private Context AndroidContext;
    private GLSurfaceView Surface;
    private DebugOverlay DebugSystem;
    private static final long TOUCH_DEBOUNCE_DELAY_MS = 250;
    private long LastInputEvent = 0;

    public String GetStringFromResources(int ResourceID) {
        return ContextUtils.GetStringFromResources(AndroidContext,ResourceID);
    }


    public void CleanUp() {
        RenderUtils.CleanUp();
    }

    public boolean OnTouch(MotionEvent Event) {
        //CurrentState
        if( CurrentState == null ) {
            return false;
        }
        if( CurrentState.Debounce ) {
            long Now;
            long ElapsedTime;

            Now = RenderEngine.SysMilliseconds();
            ElapsedTime = Now - LastInputEvent;
            LastInputEvent = Now;
            //Avoid double-clicks.
            if( ElapsedTime < TOUCH_DEBOUNCE_DELAY_MS ) {
                Timber.d("Debouncing touch too fast...");
                return false;
            }
            return CurrentState.OnTouch(Event);
        } else {
            return CurrentState.OnTouch(Event);
        }
    }

    /*
        In order to switch a state we need to wait for the next GL draw
        because otherwise all GL operation called by the new state will be
        out-of-sync from the thread and it would crash.
     */
    public void ChangeState(GameState NewState) {
        if( CurrentState != null) {
            CurrentState.OnDestroy();
        }
        CurrentState = NewState;
        RenderUtils.QueueEvent(() -> {
            Timber.d("Requested state switch...");
            CurrentState.Init(RenderUtils);
        });
    }

    public void Draw() {
        if( CurrentState == null ) {
            return;
        }
        RenderUtils.TickTime();
        RenderUtils.PrepareToRender();
        CurrentState.Draw();
        DebugSystem.Draw();
    }

    public void OnSurfaceChanged(int Width,int Height) {
        RenderUtils.OnSurfaceChanged(Width,Height);
    }

    public void Init() {
        Timber.d("Initializing OpenGL Utils.");
        RenderUtils = new RenderEngine(AndroidContext,Surface);
        ChangeState( new IntroState(this) );
        DebugSystem = new DebugOverlay(RenderUtils);
    }

    /*
    * Unfortunately context is not available on the AppGLRenderer Class...
    * So we need to take it from AppGLSurfaceView and then use it for the late init.
    * */
    public GameStateManager(Context AndroidContext, GLSurfaceView Surface) {
        this.AndroidContext = AndroidContext;
        this.Surface = Surface;
    }
}
