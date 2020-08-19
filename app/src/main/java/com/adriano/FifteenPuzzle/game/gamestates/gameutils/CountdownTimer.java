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
package com.adriano.FifteenPuzzle.game.gamestates.gameutils;

import com.adriano.FifteenPuzzle.glsupport.glgui.GLString;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.util.Locale;
import java.util.Timer;

public class CountdownTimer implements TimeTickCallback {
    protected RenderEngine RenderUtils;
    protected GLString TimeLeft;
    protected int Seconds;
    protected int StartingSeconds;
    protected TimeOutCallback Callback;
    protected Vec2 Position;
    protected FontSize TextSize;
    private Timer CTimer;
    private CountdownTimerTask CTimerTask;


    public void Stop() {
        if( !CTimerTask.IsActive ) {
            return;
        }
        CTimer.cancel();
    }
    /*
    * Reset the timer but does not start it.
    * */
    public void Reset() {
        if( CTimerTask.IsActive ) {
            CTimer.cancel();
        }
        Seconds = StartingSeconds;
    }

    public void Resume() {
        Init();
        Start();
    }
    public void Pause() {
        CTimer.cancel();
    }

    public void Start() {
        CTimer.scheduleAtFixedRate(CTimerTask, 0, 1000);
    }

    public void SetTextSize(FontSize TextSize) {
        this.TextSize = TextSize;
    }

    public FontSize GetTextSize() {
        return TextSize;
    }

    public String GetTimeMMSS() {
        return String.format(Locale.getDefault(),"%02d:%02d", Seconds / 60, Seconds % 60);
    }

    @Override
    public void OnTick(int ElapsedTime) {
        Seconds = ElapsedTime;
    }

    public void Draw() {
        TimeLeft.SetText(GetTimeMMSS());
        TimeLeft.Draw();
    }

    public void CenterText(Rect Container) {
        float Center;
        float TextWidth;
        TextWidth = RenderUtils.FontSystem.GetStringWidth(GetTimeMMSS(),TextSize);
        Center = Container.Width / 2.f;
        TimeLeft.SetPosition(new Vec2(Center - TextWidth,Container.Position.y));
    }

    private void Init() {
        CTimer = new Timer();
    }

    public CountdownTimer(RenderEngine RenderUtils, Vec2 Position, int Seconds,
                          FontSize TextSize, TimeOutCallback Callback) {
        this.RenderUtils = RenderUtils;
        this.Position = Position;
        this.Seconds = Seconds;
        this.TextSize = TextSize;
        this.Callback = Callback;
        this.StartingSeconds = Seconds;
        CTimerTask = new CountdownTimerTask(Seconds,this,Callback);
        TimeLeft = new GLString(RenderUtils,TextSize, Color4.Black);
        Init();
    }
}
