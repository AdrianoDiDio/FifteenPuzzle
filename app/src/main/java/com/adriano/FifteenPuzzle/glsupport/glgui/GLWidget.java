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
package com.adriano.FifteenPuzzle.glsupport.glgui;

import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.util.UUID;

public abstract class GLWidget  {
    protected UUID ID;
    protected UUID ParentID;
    protected int ResourceID; // Can be used to retrieve it from resources ids.xml
    protected boolean Active;
    protected RenderEngine RenderUtils;
    protected Rect Rectangle;
    protected GLWidgetTouchCallback Callback;

    public abstract boolean OnTouch(Vec2 Position);
    public abstract void Draw();


    public UUID GetID() {
        return ID;
    }

    public String GetName() {
        return RenderUtils.AndroidContext.getResources().getResourceEntryName(ResourceID);
    }

    public void SetActive(boolean Active) {
        this.Active = Active;
    }

    public boolean IsActive() {
        return Active;
    }

    public Rect GetRectangle() {
        return Rectangle;
    }

    public void SetRectangle(Rect Rectangle) {
        this.Rectangle = new Rect(Rectangle);
    }

    public int GetResourceID() {
        return ResourceID;
    }

    public GLWidget(RenderEngine RenderUtils, Rect Rectangle,int ResourceID,GLWidgetTouchCallback Callback) {
        this.RenderUtils = RenderUtils;
        this.Rectangle = new Rect(Rectangle);
        this.ID = UUID.randomUUID();
        this.ResourceID = ResourceID;
        this.Callback = Callback;
        this.Active = true;
    }

    public GLWidget(RenderEngine RenderUtils, Rect Rectangle,int ResourceID) {
        this.RenderUtils = RenderUtils;
        this.Rectangle = new Rect(Rectangle);
        this.ID = UUID.randomUUID();
        this.ResourceID = ResourceID;
        this.Active = true;
    }


    public GLWidget(RenderEngine RenderUtils,int ResourceID,GLWidgetTouchCallback Callback) {
        this.RenderUtils = RenderUtils;
        this.Rectangle = new Rect();
        this.ID = UUID.randomUUID();
        this.Callback = Callback;
        this.Active = true;
        this.ResourceID = ResourceID;
    }


}
