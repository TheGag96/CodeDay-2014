package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity extends Sprite {
    public int state;
    public int health;
    public static int maxHealth;

    public float velX;
    public float velY;

    private float newX;
    private float newY;

    private float oldX;
    private float oldY;

    public final static int TOP = 0;
    public final static int BOTTOM = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    public Entity(float x, float y) {
        setPosition(x, y);
        oldX = x;
        oldY = y;
    }

    public abstract void performLogic(float deltaTime);

    public void onBlockCollision(int direction, TiledMapTile tile) {
        int tileID = tile.getId();
        switch (tileID) {

        }
    }

    public void updatePosition(boolean xorY) {

    }

    public void checkBlockCollisionsX(TiledMap map) {
        ArrayList<Block> collisionBlocks = getCollisionTiles(map, false);

        for (Block block : collisionBlocks) {
            if (block.overlaps(getBoundingRectangle())) {

            }
        }

        setX(newX);
    }

    public void checkBlockCollisionsY(TiledMap map) {
        ArrayList<Block> collisionBlocks = getCollisionTiles(map, true);


        setY(newY);
    }

    //false for x, true for y
    private ArrayList<Block> getCollisionTiles(TiledMap map, boolean xOrY) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        float oldX = getX(), oldY = getY();
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);

        float delta;
        int direction, initial, condition;

        if (xOrY) {
            delta = getY()-oldY;
            direction = velY < 0 ? -1 : 1;
            initial = -direction;
            condition = (int)Math.round(delta);

            for (int j = initial; (direction == 1 && j <= condition) || (direction == -1 && j >= condition); j+=direction) {
                for (int i = -1; i <= Math.ceil(getWidth()); i++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                    if (cell != null) {
                        blocks.add(new Block(i, j, cell.getTile().getId()));
                    }
                }
            }
            return blocks;
        }

        delta = getX()-oldX;
        direction = velX < 0 ? -1 : 1;
        initial = -direction;
        condition = (int)Math.round(delta);

        for (int j = initial; (direction == 1 && j <= condition) || (direction == -1 && j >= condition); j+=direction) {    //fixes high speed collision issues
            for (int i = -1; i <= Math.ceil(getHeight()); i++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if (cell != null) {
                    blocks.add(new Block(i, j, cell.getTile().getId()));
                }
            }
        }
        return blocks;
    }
}
