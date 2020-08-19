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


import java.util.TimerTask;


public class CountdownTimerTask extends TimerTask  {
    private int Seconds;
    private TimeTickCallback TickCallback;
    private TimeOutCallback FinishCallback;

    public boolean IsActive;

    @Override
    public void run() {
        IsActive = true;
        Seconds--;
        if( Seconds < 0 ) {
            IsActive = false;
            FinishCallback.OnTimeOut();
            cancel();
        } else {
            TickCallback.OnTick(Seconds);
        }
    }

    public CountdownTimerTask(int Seconds,TimeTickCallback TickCallback,TimeOutCallback FinishCallback) {
        this.Seconds = Seconds;
        this.TickCallback = TickCallback;
        this.FinishCallback = FinishCallback;
    }
}
