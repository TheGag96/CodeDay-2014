package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class Block extends com.badlogic.gdx.math.Rectangle {
    public final static int TOP = 0;
    public final static int BOTTOM = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    public final static int PASSABLE_ID = 0;
    public final static int SOLID_ID = 1;
    public final static int PLATFORM_ID = 2;
    public final static int COLLECTABLE_ID = 3;

    public int id;

    public Block(int x, int y, int id) {
        this.x = x;
        this.y = y;
        width = 1;
        height = 1;
        this.id = id;
    }

    public void onEntityCollision(Entity e) { }

    @Override
    public boolean overlaps(Rectangle r) {
        if (x < r.x+r.width && x+width > r.x) {
            if (y < r.y+r.height && y+height > r.y) {
                return true;
            }
        }
        return false;
    }
}
