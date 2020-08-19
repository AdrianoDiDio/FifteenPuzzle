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

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.MotionEvent;

import com.adriano.FifteenPuzzle.game.GameState;
import com.adriano.FifteenPuzzle.game.GameStateManager;
import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.game.gamestates.fifteenpuzzlestate.MainGameState;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLButton;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLClickEvent;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLString;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLCheckBox;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLTextAlign;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLVerticalPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLWidgetCheckBoxChangeCallback;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLWidgetTouchCallback;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.SurfaceChangedObserver;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.util.ArrayList;
import java.util.UUID;

import timber.log.Timber;

public class MainMenuState extends GameState implements SurfaceChangedObserver,
        GLWidgetTouchCallback, GLWidgetCheckBoxChangeCallback {
    private GLPanel BackgroundPanel;
    //private GLVerticalPanel MainPanel;
    //private GLVerticalPanel SkillPanel;
    //private GLVerticalPanel SettingsPanel;
    private GLString PanelHeader;
    private ArrayList<GLVerticalPanel> MenuPanels;


    private void SetActivePanel(int ResourceID) {
        for( GLPanel Panel : MenuPanels) {
            if( Panel.GetResourceID() == ResourceID ) {
                Panel.SetActive(true);
            } else {
                Panel.SetActive(false);
            }
        }
    }


    private GLPanel GetPanelByID(UUID ID) {
        for( GLPanel Panel : MenuPanels) {
            if( Panel.GetID().compareTo(ID) == 0 ) {
                return Panel;
            }
        }
       /* if( ID.compareTo(MainPanel.GetID()) == 0 ) {
            return MainPanel;
        }
        if( ID.compareTo(SkillPanel.GetID()) == 0 ) {
            return SkillPanel;
        }*/
        return null;
    }

    private void UpdateLayout() {
        BackgroundPanel.SetRectangle(new Rect(Vec2.ZeroVector(),RenderUtils.Width,RenderUtils.Height));
        Vec2 ScreenCenter;
        float ButtonWidth;
        float ButtonHeight;

        ButtonWidth = (40.f * RenderUtils.Width) / 100.f;
        ButtonHeight = (80.f * RenderUtils.Height) / 100.f;

        PanelHeader.SetContainer(new Rect(new Vec2(0,0),
                RenderUtils.Width,(20 * RenderUtils.Height) / 100));

        ScreenCenter = RenderUtils.GetScreenCenter(ButtonWidth,ButtonHeight);
        ScreenCenter.y += PanelHeader.GetLineHeight();
        for( GLVerticalPanel Panel : MenuPanels ) {
            Panel.SetRectangle(new Rect(ScreenCenter,ButtonWidth,ButtonHeight));
            Panel.UpdateLayout();
        }
    }
    @Override
    public void SurfaceChanged() {
        UpdateLayout();
    }

    @Override
    public boolean OnTouch(MotionEvent Event) {
        Vec2 EventPosition;

        EventPosition = new Vec2(Event.getX(),Event.getY());
        for( GLPanel Panel : MenuPanels) {
            if( Panel.IsActive() ) {
                return Panel.OnTouch(EventPosition);
            }
        }
        return false;
    }

    @Override
    public void OnChange(GLClickEvent Event,boolean Checked) {
        GLCheckBox Temp;

        GLVerticalPanel Panel;

        Panel = (GLVerticalPanel) GetPanelByID(Event.ParentID);
        Temp = (GLCheckBox) Panel.GetWidgetByID(Event.SourceID);
        switch ( Temp.GetResourceID() ) {
            case R.id.ShowDebugInfoLabel:
                RenderUtils.Settings.Preferences.edit().putBoolean("ShowDebugInfo",Checked).apply();
                break;
            case R.id.SetSmoothTileMotionLabel:
                RenderUtils.Settings.Preferences.edit().putBoolean("SmoothTileMotion",Checked).apply();
                break;
            case R.id.SetSoundEffectsLabel:
                RenderUtils.Settings.Preferences.edit().putBoolean("SoundsEffects",Checked).apply();
                break;
            default:
                Timber.d("Unknown checkbox " + Temp.GetName());
                break;
        }
    }

    @Override
    public void OnClick(GLClickEvent Event) {
        GLButton Temp;

        GLVerticalPanel Panel;

        Panel = (GLVerticalPanel) GetPanelByID(Event.ParentID);
        Temp = (GLButton) Panel.GetWidgetByID(Event.SourceID);

        switch ( Temp.GetResourceID() ) {
            case R.id.PlayButton:
                PanelHeader.SetText(Manager.GetStringFromResources(R.string.Difficulty));
                SetActivePanel(R.id.SkillPanel);
                break;
            case R.id.SettingsButton:
            case R.id.BackToSettingsButton:
                PanelHeader.SetText(Manager.GetStringFromResources(R.string.Settings));
                SetActivePanel(R.id.SettingsPanel);
                break;
            case R.id.GeneralSettingsButton:
                PanelHeader.SetText(Manager.GetStringFromResources(R.string.GeneralSettings));
                SetActivePanel(R.id.GeneralSettingsPanel);
                break;
            case R.id.GridSize9Button:
                ChangeState(new MainGameState(Manager,3));
                break;
            case R.id.GridSize16Button:
                ChangeState(new MainGameState(Manager,4));
                break;
            case R.id.QuitButton:
                Activity Main;
                Main = (Activity) RenderUtils.AndroidContext;
                Main.finishAffinity();
                //ChangeState(new GameOverState(Manager,true,3));
                break;
            case R.id.BackToMainButton:
                PanelHeader.SetText(Manager.GetStringFromResources(R.string.MainMenu));
                SetActivePanel(R.id.MainPanel);
                break;
            default:
                Timber.d("Unknown button " + Temp.GetName());
                break;
        }
    }

    @Override
    public void OnDestroy() {
        //Goodbye state.
    }

    @Override
    public void Pause() {

    }

    @Override
    public void Draw() {
        if( !Initialized ) {
            Timber.d("I'm Ready but i cannot show.");
            return;
        }

        BackgroundPanel.Draw();
        PanelHeader.Draw();

        for( GLPanel Panel : MenuPanels) {
            Panel.Draw();
        }
        /*
        BackgroundPanel.Draw();
        PanelHeader.Draw();
        MainPanel.Draw();
        SkillPanel.Draw();*/
    }

    private void InitGeneralSettingsMenu() {
        boolean ShowDebugInfo;
        boolean SetSoundEffects;

        GLVerticalPanel GeneralSettingsMenu;

        GeneralSettingsMenu = new GLVerticalPanel(RenderUtils,
                new Rect(),
                Color4.TransparentWhite, R.id.GeneralSettingsPanel);

        ShowDebugInfo = RenderUtils.Settings.Preferences.getBoolean("ShowDebugInfo",false);
        SetSoundEffects = RenderUtils.Settings.Preferences.getBoolean("SoundsEffects",false);

        GeneralSettingsMenu.Add(new GLCheckBox(RenderUtils,R.id.ShowDebugInfoLabel,
                Manager.GetStringFromResources(R.string.ShowDebugInfo),ShowDebugInfo,this));
        GeneralSettingsMenu.Add(new GLCheckBox(RenderUtils,R.id.SetSoundEffectsLabel,
                Manager.GetStringFromResources(R.string.SoundEffects),SetSoundEffects,this));
        GeneralSettingsMenu.Add(new GLButton(RenderUtils,R.id.BackToSettingsButton,
                Manager.GetStringFromResources(R.string.Back),this));
        MenuPanels.add(GeneralSettingsMenu);
    }
    private void InitSettingsMenu() {
        GLVerticalPanel SettingsMenu = new GLVerticalPanel(RenderUtils,
                new Rect(),
                Color4.TransparentWhite, R.id.SettingsPanel);
        SettingsMenu.Add(new GLButton(RenderUtils,R.id.GeneralSettingsButton,
                Manager.GetStringFromResources(R.string.GeneralSettings),this));
        SettingsMenu.Add(new GLButton(RenderUtils, R.id.BackToMainButton,
                Manager.GetStringFromResources(R.string.Back),this));
        MenuPanels.add(SettingsMenu);
    }
    private void InitSkillSelectMenu() {
        GLVerticalPanel SkillPanel = new GLVerticalPanel(RenderUtils,
                new Rect(),
                Color4.TransparentWhite, R.id.SkillPanel);
        SkillPanel.Add(new GLButton(RenderUtils,R.id.GridSize9Button,
                Manager.GetStringFromResources(R.string.ThreeTimesThree),this));
        SkillPanel.Add(new GLButton(RenderUtils, R.id.GridSize16Button,
                Manager.GetStringFromResources(R.string.FourTimesFour),this));
        SkillPanel.Add(new GLButton(RenderUtils,R.id.BackToMainButton,
                Manager.GetStringFromResources(R.string.Back),this));
        MenuPanels.add(SkillPanel);
    }

    private void InitMainPanel() {
        GLVerticalPanel MainPanel = new GLVerticalPanel(RenderUtils,
                    new Rect(),
                    Color4.TransparentWhite, R.id.MainPanel);
        GLButton Button = new GLButton(RenderUtils,R.id.PlayButton,
                Manager.GetStringFromResources(R.string.Play),this);
        MainPanel.Add(Button);

        MainPanel.Add(new GLButton(RenderUtils,R.id.SettingsButton,
                Manager.GetStringFromResources(R.string.Settings),this));
        MainPanel.Add(new GLButton(RenderUtils,R.id.QuitButton,
                Manager.GetStringFromResources(R.string.Quit),this));
        MenuPanels.add(MainPanel);
    }
    private void InitGLUi() {
        Vec2 ScreenCenter;
        float ButtonWidth;
        float ButtonHeight;
        float Spacing;
        Rect PanelHeaderRect;

        ButtonWidth = 500;
        ButtonHeight = 250;
        Spacing = 20;

        MenuPanels = new ArrayList<>();
        ScreenCenter = new Vec2((RenderUtils.Width - ButtonWidth) / 2,
                (RenderUtils.Height - (ButtonHeight * 3)) / 2);

        BackgroundPanel = new GLPanel(RenderUtils,new Rect(),
                Color4.LightBlue,R.id.BackgroundPanel);
        PanelHeader = new GLString(RenderUtils, FontSize.FONT_BIG,
                Manager.GetStringFromResources(R.string.MainMenu),
                new Rect(),Color4.White, GLTextAlign.ALIGN_CENTER);
        InitMainPanel();
        InitSkillSelectMenu();
        InitSettingsMenu();
        InitGeneralSettingsMenu();
        SetActivePanel(R.id.MainPanel);
/*
        ScreenCenter = new Vec2((RenderUtils.Width - ButtonWidth) / 2,
                (RenderUtils.Height - (ButtonHeight * 3)) / 2);

        Buttons.add(new GLButton(RenderUtils,"PlayButton",new Rect(new Vec2(ScreenCenter.x,ScreenCenter.y),
                ButtonWidth,ButtonHeight),"PLAY",this));
        ScreenCenter.y += ButtonHeight + Spacing;

        Buttons.add(new GLButton(RenderUtils,"SettingsButton",new Rect(new Vec2(ScreenCenter.x,ScreenCenter.y),
                ButtonWidth,ButtonHeight),"SETTINGS",this));
        ScreenCenter.y += ButtonHeight + Spacing;

        Buttons.add(new GLButton(RenderUtils,"QuitButton",new Rect(new Vec2(ScreenCenter.x,ScreenCenter.y),
                ButtonWidth,ButtonHeight),"QUIT",this));
*/
        UpdateLayout();
        RenderUtils.RegisterSurfaceChangedObserver(this);
    }


    @Override
    public void Init(RenderEngine RenderUtils) {
        super.Init(RenderUtils, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,true);
        InitGLUi();
        Initialized = true;
    }

    public MainMenuState(GameStateManager Manager) {
        super(Manager);
    }
}
