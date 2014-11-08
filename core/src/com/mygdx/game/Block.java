package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.*;

public abstract class Block extends Rectangle {
    public Block() { }

    public void onEntityCollision(Entity e) { }
    public abstract Texture getTexture();
}
