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

import android.app.Activity;
import android.view.MotionEvent;

import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;


public abstract class GameState {
    protected boolean Initialized;
    protected RenderEngine RenderUtils;
    private int ScreenOrientation;
    protected GameStateManager Manager;
    protected boolean Debounce;


    //TODO: Called before changing state.
    //TODO: Add Transient GameState (Overlay on top of an existing one).
    public abstract void OnDestroy();
    public abstract void Pause();
    public abstract void Draw();
    public abstract boolean OnTouch(MotionEvent Event);

    public void SetScreenOrientation(int ScreenOrientation) {
        Activity Main;
        this.ScreenOrientation = ScreenOrientation;
        Main = (Activity) RenderUtils.AndroidContext;
        Main.setRequestedOrientation(ScreenOrientation);

    }

    public int GetScreenOrientation() {
        return ScreenOrientation;
    }

    public void ChangeState(GameState NewState) {
        Manager.ChangeState(NewState);
    }

    /*
    * Init is called every time we have a valid GL context.
    * Constructor can be used to initialized non-GL-related things.
    * */

    public void Init(RenderEngine RenderUtils,int ScreenOrientation,boolean Debounce) {
        this.RenderUtils = RenderUtils;
        this.Debounce = Debounce;
        SetScreenOrientation(ScreenOrientation);
    }

    public void Init(RenderEngine RenderUtils,int ScreenOrientation) {
        this.RenderUtils = RenderUtils;
        this.Debounce = false;
        SetScreenOrientation(ScreenOrientation);
    }

    public void Init(RenderEngine RenderUtils,boolean Debounce) {
        this.RenderUtils = RenderUtils;
        this.Debounce = Debounce;
    }
    public void Init(RenderEngine RenderUtils) {
        this.RenderUtils = RenderUtils;
        this.Debounce = false;
    }

    public GameState(GameStateManager Manager) {
        this.Manager = Manager;
    }
}
