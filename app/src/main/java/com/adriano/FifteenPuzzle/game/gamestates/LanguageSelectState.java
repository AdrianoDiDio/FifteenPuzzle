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
import android.content.res.Configuration;
import android.view.MotionEvent;

import com.adriano.FifteenPuzzle.game.GameState;
import com.adriano.FifteenPuzzle.game.GameStateManager;
import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.ColorQuad;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLButton;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLClickEvent;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLString;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLTextAlign;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLVerticalPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLWidgetTouchCallback;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.SurfaceChangedObserver;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.util.Locale;

import timber.log.Timber;

/*
*
* STUB FOR NOW UNTIL I DECIDE IF IT'S WORTH IT.
*
* */

public class LanguageSelectState extends GameState implements SurfaceChangedObserver,GLWidgetTouchCallback {
    private GLPanel BackgroundPanel;
    private GLVerticalPanel MainPanel;
    private ColorQuad Rect;
    private GLString LanguageText;

    private void SetLocale() {
        Locale locale = new Locale("ru");
        Locale.setDefault(locale);
        Configuration config = RenderUtils.AndroidContext.getResources().getConfiguration();
        config.locale = locale;
        RenderUtils.AndroidContext.getResources().updateConfiguration(config,
                RenderUtils.AndroidContext.getResources().getDisplayMetrics());
    }
    @Override
    public void SurfaceChanged() {
        UpdateLayout();
    }

    @Override
    public boolean OnTouch(MotionEvent Event) {
        if( !Initialized ) {
            return false;
        }
        MainPanel.OnTouch(new Vec2(Event.getX(),Event.getY()));
        return false;
    }

    @Override
    public void OnDestroy() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void OnClick(GLClickEvent Event) {
       GLButton Button;

       Button = (GLButton) MainPanel.GetWidgetByID(Event.SourceID);

       switch ( Button.GetResourceID() ) {
           case R.id.ItalianLanguageButton:
               Timber.d("Selected Italian");
               break;
           case R.id.EnglishLanguageButton:
               Timber.d("Selected English");
               break;
           default:
               Timber.d("Unknown button id " + Button.GetResourceID());
       }
    }

    private void UpdateLayout() {
        BackgroundPanel.SetRectangle(new Rect(Vec2.ZeroVector(),RenderUtils.Width,RenderUtils.Height));
        Vec2 ScreenCenter;
        float ButtonWidth;
        float ButtonHeight;

        ButtonWidth = (40.f * RenderUtils.Width) / 100.f;
        ButtonHeight = (30.f * RenderUtils.Height) / 100.f;

        ScreenCenter = RenderUtils.GetScreenCenter(ButtonWidth,ButtonHeight);
        LanguageText.SetContainer(new Rect(new Vec2(0,0),
                RenderUtils.Width,(20 * RenderUtils.Height) / 100));
        MainPanel.SetRectangle(new Rect(ScreenCenter,ButtonWidth,ButtonHeight));
        MainPanel.UpdateLayout();
    }

    @Override
    public void Draw() {
        if( !Initialized ) {
            return;
        }
        LanguageText.Draw();
        BackgroundPanel.Draw();
        MainPanel.Draw();
    }

    private void InitLanguageList() {
        int ItalianFlagID;
        int EnglishFlagID;
        MainPanel = new GLVerticalPanel(RenderUtils,
                new Rect(),
                Color4.TransparentWhite, R.id.MainPanel);
        //ItalianFlagID = RenderUtils.TextureUtils.Cache("ItalianFlag",R.raw.itflag);
        //EnglishFlagID = RenderUtils.TextureUtils.Cache("EnglishFlag",R.raw.enflag);

        //MainPanel.Add(new GLButton(RenderUtils,R.id.ItalianLanguageButton,ItalianFlagID,this));
        // MainPanel.Add(new GLButton(RenderUtils,R.id.EnglishLanguageButton,EnglishFlagID,this));

    }
    @Override
    public void Init(RenderEngine RenderUtils) {
        super.Init(RenderUtils, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        Timber.d("Init Language selection screen.");

        BackgroundPanel = new GLPanel(RenderUtils,new Rect(),
                Color4.LightBlue,R.id.BackgroundPanel);

        InitLanguageList();

        LanguageText = new GLString(RenderUtils, FontSize.FONT_BIG,
                Manager.GetStringFromResources(R.string.LanguageSelect),
                new Rect(),Color4.White, GLTextAlign.ALIGN_CENTER);
        UpdateLayout();
        RenderUtils.RegisterSurfaceChangedObserver(this);
        Initialized = true;
    }

    public LanguageSelectState(GameStateManager Manager) {
        super(Manager);
    }
}
