package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity{

    public Player(float x, float y) {
        super(x, y);
        setTexture(new Texture(Gdx.files.internal("entities/player.png")));
    }

    @Override
    public void performLogic(float deltaTime) {
        velY -= GRAVITY;
    }

}
