package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Lever extends Entity {
    public int puzzleFlag;

    private static final Texture normalTexture = new Texture(Gdx.files.internal("entities/lever_normal.png"));
    private static final Texture flippedTexture = new Texture(Gdx.files.internal("entities/lever_flipped.png"));

    public boolean flipped = false;

    public Lever(float x, float y, int puzzleFlag) {
        super(x, y);
        this.puzzleFlag = puzzleFlag;
        setSize(2, 1);
        setTexture(normalTexture);
    }

    @Override
    public void performLogic(float deltaTime) {
        if (flipped) {
            setTexture(flippedTexture);
        }
        else {
            setTexture(normalTexture);
        }
        MainGame.puzzleFlags.put(puzzleFlag, flipped);
    }
}
