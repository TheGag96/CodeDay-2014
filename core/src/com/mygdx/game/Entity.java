package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Entity extends Sprite {
    public int state;
    public int health;
    public static int maxHealth;

    public Entity(int x, int y) {

    }

    public abstract void performLogic(float deltaTime);

    public void onBlockCollision(int direction, Block block) { }
}
