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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SettingsUtils implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String PreferenceName = "com.adriano.fifteenpuzzle.settings";
    public SharedPreferences Preferences;
    private Activity MainActivity;
    private ArrayList<PreferenceChangedObserver> PreferenceChangedObserverList;


    public void CleanUp() {
        Preferences.unregisterOnSharedPreferenceChangeListener(this);
        PreferenceChangedObserverList.clear();
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences Preferences, String Key) {
        for( PreferenceChangedObserver Observer : PreferenceChangedObserverList ) {
            Observer.PreferenceChanged(Key);
        }
    }

    public void RegisterPreferenceChangedObserver(PreferenceChangedObserver Observer) {
        PreferenceChangedObserverList.add(Observer);
    }

    private void Init() {
        Preferences = MainActivity.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Preferences.registerOnSharedPreferenceChangeListener(this);
        PreferenceChangedObserverList = new ArrayList<>();
    }

    public SettingsUtils(Context AndroidContext) {
        this.MainActivity = (Activity) AndroidContext;
        Init();
    }
}
