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
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import java.util.ArrayList;

import timber.log.Timber;

public class SoundManager {
    private Context AndroidContext;
    private SettingsUtils Settings;
    private SoundPool soundPool;
    private ArrayList<Sound> SoundList;


    public int GetSoundID(String Name) {
        if( !Exists(Name) ){
            return -1;
        }
        for( Sound Temp : SoundList){
            if( Temp.Name.equals(Name) ){
                return Temp.ID;
            }
        }
        return -1;
    }

    public boolean Exists(String Name) {
        if( SoundList.isEmpty() ){
            return false;
        }
        for( Sound Temp : SoundList ){
            if( Temp.Name.equals(Name) ){
                return true;
            }
        }
        return false;
    }

    public int Cache(String Name,int ResourceID) {
        Sound Temp;
        int SoundID;
        SoundID = soundPool.load(AndroidContext,ResourceID,1);
        Temp = new Sound(SoundID,Name);
        SoundList.add(Temp);
        return SoundID;
    }

    public void Play(String Name) {
        int SoundID;

        //Either set volume to 0 or just use this flag
        if( !Settings.Preferences.getBoolean("SoundsEffects",true) ) {
            return;
        }

        SoundID = GetSoundID(Name);

        if( SoundID == -1 ) {
            Timber.d("Sound not found " + Name);
            return;
        }
        soundPool.play(SoundID, 1, 1, 1, 0, 1.0f);
    }

    private void Init() {
        Activity Main;
        SoundPool.Builder SpBuilder;
        AudioAttributes Attributes;

        SoundList = new ArrayList<>();

        Main = (Activity) AndroidContext;
        Main.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if( android.os.Build.VERSION.SDK_INT == 21){
            Attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SpBuilder = new SoundPool.Builder();
            SpBuilder.setMaxStreams(5);
            SpBuilder.setAudioAttributes(Attributes);
            soundPool = SpBuilder.build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

    }
    public SoundManager(Context AndroidContext,SettingsUtils Settings) {
        this.AndroidContext = AndroidContext;
        this.Settings = Settings;
        Init();
    }
}
