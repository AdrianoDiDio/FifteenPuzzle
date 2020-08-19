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
import com.adriano.FifteenPuzzle.game.gamestates.fifteenpuzzlestate.MainGameState;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLButton;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLClickEvent;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLHorizontalPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLString;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLTextAlign;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLWidgetTouchCallback;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.SurfaceChangedObserver;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import timber.log.Timber;

/*
* FIXME:Button Panel not showing...
* */
public class GameOverState extends GameState implements SurfaceChangedObserver,GLWidgetTouchCallback {
    private String GameOverString;
    private int CurrentAdvanceCharIndex;
    private GLString GameOverText;
    private long LastTextDrawTime;
    private GLHorizontalPanel ButtonsPanel;
    private boolean HasWin;
    private int PreviousGridSize;
    private GLPanel BackgroundPanel;

    private boolean DoneAnimateText() {
        return CurrentAdvanceCharIndex >= GameOverString.length();
    }


    private void UpdateLayout() {
        float StringWidth;
        float IdealButtonWidth;
        float IdealButtonHeight;
        Vec2 ScreenCenter;

        BackgroundPanel.SetRectangle(new Rect(Vec2.ZeroVector(),RenderUtils.Width,RenderUtils.Height));

        GameOverText.SetContainer(new Rect(new Vec2(0,0),
                RenderUtils.Width,RenderUtils.Height));



        IdealButtonWidth = (60.f * RenderUtils.Width) / 100.f;
        IdealButtonHeight = (10.f * RenderUtils.Height) / 100.f;
        StringWidth = RenderUtils.FontSystem.GetStringWidth(GameOverString,FontSize.FONT_BIG);

        if( StringWidth >= IdealButtonWidth ) {
            IdealButtonWidth = StringWidth;
        }

        ScreenCenter = RenderUtils.GetScreenCenter(IdealButtonWidth,IdealButtonHeight);
        ScreenCenter.y += GameOverText.GetLineHeight();
        ButtonsPanel.SetRectangle(new Rect(ScreenCenter,IdealButtonWidth,IdealButtonHeight));
        ButtonsPanel.UpdateLayout();
    }

    @Override
    public void SurfaceChanged() {
        UpdateLayout();
    }

    private void AdvanceText() {
        if( DoneAnimateText() ) {
            Timber.d("Advance done");
            return;
        }
        GameOverText.Append(GameOverString.charAt(CurrentAdvanceCharIndex));
        CurrentAdvanceCharIndex++;
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
        Button = (GLButton) ButtonsPanel.GetWidgetByID(Event.SourceID);

        switch ( Button.GetResourceID() ) {
            case R.id.BackToMainButton:
                ChangeState(new MainMenuState(Manager));
                break;
            case R.id.RetryButton:
                ChangeState(new MainGameState(Manager,PreviousGridSize));
                break;
            default:
                Timber.d("Unknown button " + Button.GetName());
        }
    }

    @Override
    public boolean OnTouch(MotionEvent Event) {
        if( !Initialized ) {
            return false;
        }
        ButtonsPanel.OnTouch(new Vec2(Event.getX(),Event.getY()));
        return false;
    }

    @Override
    public void Draw() {
        if( !Initialized ) {
            Timber.d("Not init");
            return;
        }
        BackgroundPanel.Draw();
        GameOverText.Draw();
        ButtonsPanel.Draw();

        if( !DoneAnimateText() ) {
            LastTextDrawTime += RenderUtils.Time.Delta;
            if (LastTextDrawTime > 5) {
                Timber.d("Advancing it");
                //Advance it!
                AdvanceText();
                LastTextDrawTime = 0;
            }
        } else {
            ButtonsPanel.SetActive(true);
        }
        //GameOverText.Draw();

        //GameOverText.Draw();
    }


    private void InitPanels() {
        BackgroundPanel = new GLPanel(RenderUtils,new Rect(),
                Color4.LightBlue,R.id.BackgroundPanel);

        ButtonsPanel = new GLHorizontalPanel(RenderUtils,new Rect(),
                Color4.TransparentWhite,R.id.ButtonsPanel);

        ButtonsPanel.Add(new GLButton(RenderUtils,R.id.RetryButton,
                Manager.GetStringFromResources(R.string.Retry),this));
        ButtonsPanel.Add(new GLButton(RenderUtils,R.id.BackToMainButton,
                Manager.GetStringFromResources(R.string.Back),this));
        ButtonsPanel.SetActive(false);
    }

    private void InitUI() {
        GameOverText = new GLString(RenderUtils, FontSize.FONT_BIG,
                "",
                new Rect(),Color4.White, GLTextAlign.ALIGN_CENTER);
        if( HasWin ) {
            GameOverString = Manager.GetStringFromResources(R.string.GameWin);
        } else {
            GameOverString = Manager.GetStringFromResources(R.string.GameOver);
        }
        CurrentAdvanceCharIndex = 0;
        LastTextDrawTime = 0;
        InitPanels();
        AdvanceText();
        UpdateLayout();
        RenderUtils.RegisterSurfaceChangedObserver(this);
        Initialized = true;
    }
    @Override
    public void Init(RenderEngine RenderUtils) {
        super.Init(RenderUtils, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,true);
        InitUI();
    }

    public GameOverState(GameStateManager Manager,boolean HasWin,int PreviousGridSize) {
        super(Manager);
        this.HasWin = HasWin;
        this.PreviousGridSize = PreviousGridSize;
    }

}
