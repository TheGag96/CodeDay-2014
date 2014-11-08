package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.*;

public class Block extends com.badlogic.gdx.math.Rectangle {
    public final static int TOP = 0;
    public final static int BOTTOM = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    public final static int PASSABLE_ID = 0;
    public final static int SOLID_ID = 1;

    public int id;

    public Block(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void onEntityCollision(Entity e) { }
}
