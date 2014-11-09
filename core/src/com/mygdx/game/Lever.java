package com.mygdx.game;

public class Lever extends Entity {
    public int puzzleFlag;

    public Lever(float x, float y, int puzzleFlag) {
        super(x, y);
        this.puzzleFlag = puzzleFlag;
    }

    @Override
    public void performLogic(float deltaTime) {

    }
}
