package com.mygdx.game;

public class Button extends Entity {
    public int puzzleFlag;

    public Button(float x, float y, int puzzleFlag) {
        super(x, y);
        this.puzzleFlag = puzzleFlag;
    }

    @Override
    public void performLogic(float deltaTime) {

    }
}
