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
package com.adriano.FifteenPuzzle.game.gamestates;

import android.content.pm.ActivityInfo;
import android.view.MotionEvent;

import com.adriano.FifteenPuzzle.game.GameState;
import com.adriano.FifteenPuzzle.game.GameStateManager;
import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLString;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLTextAlign;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class IntroState extends GameState {
    private GLString CopyrightText;

    @Override
    public boolean OnTouch(MotionEvent Event) {
        ChangeState(new MainMenuState(Manager));
        //ChangeState(new LanguageSelectState(Manager));
        return false;
    }

    @Override
    public void OnDestroy() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Draw() {
        if( CopyrightText == null ) {
            return;
        }
        CopyrightText.Draw();
    }

    @Override
    public void Init(RenderEngine RenderUtils) {
        Rect ScreenCenter;
        super.Init(RenderUtils, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        ScreenCenter = new Rect(Vec2.ZeroVector(),RenderUtils.Width,RenderUtils.Height);
        CopyrightText = new GLString(RenderUtils, FontSize.FONT_BIG,
                Manager.GetStringFromResources(R.string.Copyright),
                ScreenCenter,Color4.White, GLTextAlign.ALIGN_CENTER);
    }

    public IntroState(GameStateManager Manager) {
        super(Manager);
    }
}
