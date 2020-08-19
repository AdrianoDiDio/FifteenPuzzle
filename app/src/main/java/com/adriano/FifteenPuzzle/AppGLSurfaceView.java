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
package com.adriano.FifteenPuzzle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.adriano.FifteenPuzzle.game.GameStateManager;

import timber.log.Timber;


public class AppGLSurfaceView extends GLSurfaceView {
    private AppGLRenderer Renderer;
    private GameStateManager Game;
    private long LastInputEvent;

    public void OnDestroy() {
        Timber.d("Destroying Game");
        Game.CleanUp();
    }

    @Override
    public boolean onTouchEvent(MotionEvent Event) {
        return Game.OnTouch(Event);
    }

    public AppGLSurfaceView(Context AndroidContext){
        super(AndroidContext);

        Game = new GameStateManager(AndroidContext,this);

        setEGLContextClientVersion(3);
        setPreserveEGLContextOnPause(true);
        Renderer = new AppGLRenderer(Game);

        setRenderer(Renderer);
        LastInputEvent = 0;
    }

}
