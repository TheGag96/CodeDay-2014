package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class MovableBox extends Entity {

    private static final Texture normalTexture = new Texture(Gdx.files.internal("entities/button_normal.png"));
    private static final Texture pressedTexture = new Texture(Gdx.files.internal("entities/button_pressed.png"));

    public MovableBox(float x, float y) {
        super(x, y);
        setTexture(normalTexture);
    }

    @Override
    public void performLogic(float deltaTime) {

    }
}
