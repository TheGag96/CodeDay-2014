package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Entity extends Sprite {
    public int state;
    public int health;

    public Entity(int x, int y) {

    }

    public abstract void performLogic();
    public int getMaxHealth() { return 10; }

    public void onBlockCollision(int direction, Block block) { }
}
