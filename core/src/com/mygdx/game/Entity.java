package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity extends Sprite {
    public int state;
    public int health;
    public int maxHealth;

    public float velX;
    public float velY;

    private float newX;
    private float newY;

    public final static int TOP = 0;
    public final static int BOTTOM = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    public static final float GRAVITY = -0.4f;

    protected String texturePath;
    protected int textureWidth;
    protected int getTextureHeight;

    public Entity(float x, float y) {
        setPosition(x, y);
    }

    public abstract void performLogic(float deltaTime);

    public void translateX(float dist) {
        newX += dist;
    }

    public void translateY(float dist) {
        newY += dist;
    }

    public void onBlockCollision(int direction, Block block) {
        int tileID = block.id;
        switch (tileID) {
            case Block.SOLID_ID:
                switch (direction) {
                    case BOTTOM:
                        newY = block.y + 1;
                        velY = 0;
                        break;
                    case TOP:
                        newY = block.y - getHeight();
                        velY = 0;
                        break;
                    case LEFT:
                        newX = block.x + 1;
                        velX = 0;
                        break;
                    case RIGHT:
                        newX = block.x - getWidth();
                        velX = 0;
                        break;
                }
                break;
        }
    }

    public void updatePosition(boolean xorY, float deltaTime) {
        if (xorY) {
            newY = getY()+velY*deltaTime;
        }
        else {
            newX = getX()+velX*deltaTime;
        }
    }

    public void checkBlockCollisionsX(TiledMap map) {
        ArrayList<Block> collisionBlocks = getCollisionTiles(map, false);

        for (Block block : collisionBlocks) {
            if (block.overlaps(getBoundingRectangle())) {
                if (newX > getX()) {
                    onBlockCollision(RIGHT, block);
                }
                else {
                    onBlockCollision(LEFT, block);
                }
            }
        }

        setX(newX);
    }

    public void checkBlockCollisionsY(TiledMap map) {
        ArrayList<Block> collisionBlocks = getCollisionTiles(map, true);

        for (Block block : collisionBlocks) {
            if (block.overlaps(getBoundingRectangle())) {
                if (newY > getY()) {
                    onBlockCollision(TOP, block);
                }
                else {
                    onBlockCollision(BOTTOM, block);
                }
            }
        }

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
            delta = newY-oldY;
            direction = velY < 0 ? -1 : 1;
            initial = -direction;
            condition = (int)Math.round(delta);

            for (int j = initial; (direction == 1 && j <= condition) || (direction == -1 && j >= condition); j+=direction) {
                for (int i = -1; i <= Math.ceil(getWidth()); i++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                    if (cell != null) {
                        int behaviorID = Integer.parseInt((String)cell.getTile().getProperties().get("behavior"));
                        blocks.add(new Block(i, j, behaviorID));
                    }
                }
            }
            return blocks;
        }

        delta = newX-oldX;
        direction = velX < 0 ? -1 : 1;
        initial = -direction;
        condition = (int)Math.round(delta);

        for (int j = initial; (direction == 1 && j <= condition) || (direction == -1 && j >= condition); j+=direction) {    //fixes high speed collision issues
            for (int i = -1; i <= Math.ceil(getHeight()); i++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if (cell != null) {
                    int behaviorID = Integer.parseInt((String)cell.getTile().getProperties().get("behavior"));
                    blocks.add(new Block(i, j, behaviorID));

                }
            }
        }
        return blocks;
    }
}
