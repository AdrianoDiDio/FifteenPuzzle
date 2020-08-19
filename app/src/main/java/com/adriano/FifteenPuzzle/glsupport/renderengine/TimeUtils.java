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

import java.util.ArrayList;

public class TimeUtils {
    public int	  Fps;
    public float Delta;
    //Frame to frame time
    //updated each second.
    public float LastFpsTime;
    private long  LastLoopTime;
    private float OptimalTime;
    private long  UpdateLength;
    private ArrayList<FPSUpdatedObserver> FPSChangedObserverList;


    private void NotifyAllObservers() {
        for( FPSUpdatedObserver Observer : FPSChangedObserverList ) {
            Observer.GetFPS();
        }
    }

    public void RegisterFPSChangedObserver(FPSUpdatedObserver Observer) {
        FPSChangedObserverList.add(Observer);
    }

    public void RemoveFPSChangedObserver(FPSUpdatedObserver Observer) {
        FPSChangedObserverList.remove(Observer);
    }

    public void Tick() {
        long Now;

        Now = RenderEngine.SysMilliseconds();
        UpdateLength = Now - LastLoopTime; //Ms
        OptimalTime = 1000.f / 60.f; // Ms
        Delta = UpdateLength / OptimalTime;
        LastFpsTime += UpdateLength;
        Fps++;
        LastLoopTime = Now;
        if( LastFpsTime >= 1000 ) {
            NotifyAllObservers();
            LastFpsTime = 0;
            Fps = 0;
        }
    }


    public TimeUtils() {
        Fps = 0;
        Delta = 0.f;
        LastFpsTime = 0.f;
        OptimalTime = 0.f;
        UpdateLength = 0;
        LastLoopTime = RenderEngine.SysMilliseconds();
        FPSChangedObserverList = new ArrayList<>();
    }
}
