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

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import timber.log.Timber;

public class MainActivity extends Activity {
    private GLSurfaceView GLView;

    @Override
    public void onWindowFocusChanged(boolean HasFocus) {
        super.onWindowFocusChanged(HasFocus);
        if( HasFocus ) {
            HideSystemUI();
        }
    }

    private void HideSystemUI() {
        View DecorView = getWindow().getDecorView();
        DecorView.setSystemUiVisibility(
                          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        AppGLSurfaceView View;
        super.onDestroy();
        View = (AppGLSurfaceView) GLView;
        View.OnDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GLView.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( BuildConfig.DEBUG ) {
            Timber.plant(new Timber.DebugTree());
        }
        GLView = new AppGLSurfaceView(this);
        setContentView(GLView);

    }
}
