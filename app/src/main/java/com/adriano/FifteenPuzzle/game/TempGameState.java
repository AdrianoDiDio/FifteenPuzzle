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
package com.adriano.FifteenPuzzle.game;


public abstract class TempGameState extends GameState {
    protected GameState PreviousGameState;

    @Override
    public void OnDestroy() {
        Manager.ChangeState(PreviousGameState);
    }

    public TempGameState(GameStateManager Manager, GameState PreviousGameState) {
        super(Manager);
        this.PreviousGameState = PreviousGameState;
    }
}
