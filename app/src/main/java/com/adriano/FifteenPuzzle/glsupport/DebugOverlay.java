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

import com.adriano.FifteenPuzzle.glsupport.glgui.GLString;
import com.adriano.FifteenPuzzle.glsupport.renderengine.FPSUpdatedObserver;
import com.adriano.FifteenPuzzle.glsupport.renderengine.PreferenceChangedObserver;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.SurfaceChangedObserver;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.math.BigDecimal;
import java.math.RoundingMode;

import timber.log.Timber;

public class DebugOverlay implements SurfaceChangedObserver, FPSUpdatedObserver,
        PreferenceChangedObserver {
    private RenderEngine RenderUtils;
    private GLString FPSString;
    private boolean Active;

    private boolean GetPreferenceValue() {
        return RenderUtils.Settings.Preferences.getBoolean("ShowDebugInfo",false);
    }
    @Override
    public void PreferenceChanged(String Key) {
        Timber.d("PreferenceChanged: " + Key);
        if( Key.equals("ShowDebugInfo") ) {
            this.Active = GetPreferenceValue();
        }
    }

    @Override
    public void GetFPS() {
        BigDecimal Milliseconds;
        BigDecimal DeltaB;

        if( !Active ) {
            return;
        }

        Milliseconds = new BigDecimal((1000.f / RenderUtils.Time.Fps)).setScale(2, RoundingMode.HALF_UP);
        DeltaB = new BigDecimal(RenderUtils.Time.Delta).setScale(2, RoundingMode.HALF_UP);
        FPSString.SetText("FPS:" + RenderUtils.Time.Fps + " | Ms: " + Milliseconds.doubleValue() + " ms" +
                " | Delta: " + DeltaB.doubleValue());
    }

    @Override
    public void SurfaceChanged() {
        //int FPSStringWidth;
        //FPSStringWidth = RenderUtils.FontSystem.Font[FontSize.FONT_SMALL.Value].
        //      GetStringWidth("FPS:0 | Ms: 0.00 ms   ");
        FPSString.SetPosition(new Vec2((10.f * RenderUtils.Width) / 100.f,0));
    }


    public void Draw() {
        if( !Active ) {
            return;
        }
        FPSString.Draw();
    }

    public DebugOverlay(RenderEngine RenderUtils) {
        this.RenderUtils = RenderUtils;
        FPSString = new GLString(RenderUtils, FontSize.FONT_SMALL,Color4.Black);
        RenderUtils.RegisterSurfaceChangedObserver(this);
        RenderUtils.Time.RegisterFPSChangedObserver(this);
        RenderUtils.Settings.RegisterPreferenceChangedObserver(this);
        Active = GetPreferenceValue();
    }
}
