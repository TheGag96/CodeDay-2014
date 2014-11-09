package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Door extends Entity {
    private static final Texture closedTexture = new Texture(Gdx.files.internal("entities/door_closed.png"));
    private static final Texture openTexture = new Texture(Gdx.files.internal("entities/door_open.png"));

    public int puzzleFlag;
    public boolean open = false;
    public boolean alwaysOpen = false;

    public Door(float x, float y, int puzzleFlag) {
        super(x, y);
        this.puzzleFlag = puzzleFlag;
        if (puzzleFlag == -1) alwaysOpen = true;
        setTexture(closedTexture);
        setSize(2, 2);
        setRegionWidth(32);
        setRegionHeight(32);
    }

    @Override
    public void performLogic(float deltaTime) {
        if (MainGame.puzzleFlags.get(puzzleFlag) || alwaysOpen) {
            open = true;
            setTexture(openTexture);
        }
        else {
            open = false;
            setTexture(closedTexture);
        }
    }
}
