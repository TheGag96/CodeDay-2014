package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public abstract class Entity extends Sprite {
    public int state;
    public int health;
    public int maxHealth;

    public float velX;
    public float velY;

    protected float newX;
    protected float newY;

    public final static int TOP = 0;
    public final static int BOTTOM = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;

    public static final float GRAVITY = -0.4f;

    protected String texturePath;
    protected int textureWidth;
    protected int getTextureHeight;

    public boolean checksOtherEntities = false;

    public boolean blockedDown, blockedUp, blockedRight, blockedLeft;

    public Entity(float x, float y) {
        super();
        setPosition(x, y);
    }

    public abstract void performLogic(float deltaTime);

    public void onBlockCollision(int direction, Block block) {
        int tileID = block.id;
        switch (tileID) {
            case Block.SOLID_ID:
                switch (direction) {
                    case BOTTOM:
                        newY = block.y + 1;
                        velY = 0;
                        blockedDown = true;
                        break;
                    case TOP:
                        newY = block.y - getHeight();
                        velY = 0;
                        blockedUp = true;
                        break;
                    case LEFT:
                        newX = block.x + 1;
                        velX = 0;
                        blockedLeft = true;
                        break;
                    case RIGHT:
                        newX = block.x - getWidth();
                        velX = 0;
                        blockedRight = true;
                        break;
                }
                break;
            case Block.COLLECTABLE_ID:
                TiledMapTileLayer layer = (TiledMapTileLayer)MainGame.map.getLayers().get(0);
                layer.setCell((int)block.x, (int)block.y, null);
                MainGame.collectablesCollected++;
                break;
        }
    }

    public void onEntityCollision(int direction, Entity e) { }
    public void onEntityColliding(int direction, Entity e) { }

    public void updatePosition(boolean xorY, float deltaTime) {
        if (xorY) {
            blockedDown = false;
            blockedUp = false;
            newY = getY() + velY * deltaTime;
        }
        else {
            blockedLeft = false;
            blockedRight = false;
            newX = getX()+ velX * deltaTime;
        }
    }

    public void checkBlockCollisionsX(TiledMap map) {
        if (newX == getX()) return;

        Rectangle collisionBounds = newX > getX() ? new Rectangle(getX(),getY(),newX-getX()+getWidth(),getHeight())
                : new Rectangle(newX,getY(),getX()-newX+getWidth(),getHeight());

        ArrayList<Block> collisionBlocks = getCollisionTiles(map, false);

        float brandNewX = newX;
        for (Block block : collisionBlocks) {
            if (block.overlaps(collisionBounds)) {
                if (newX > getX()) {
                    onBlockCollision(RIGHT, block);
                }
                else {
                    onBlockCollision(LEFT, block);
                }
            }
            if (brandNewX != newX)
                break;
        }

    }

    public void checkBlockCollisionsY(TiledMap map) {
        if (newY == getY()) return;

        com.badlogic.gdx.math.Rectangle collisionBounds = newY > getY() ? new Rectangle(getX(),getY(),getWidth(),newY-getY()+getHeight())
                : new Rectangle(getX(),newY,getWidth(),getY()-newY+getHeight());

        ArrayList<Block> collisionBlocks = getCollisionTiles(map, true);
        float brandNewY = newY;
        for (Block block : collisionBlocks) {
            if (block.overlaps(collisionBounds)) {
                if (newY > getY()) {
                    onBlockCollision(TOP, block);
                }
                else {
                    onBlockCollision(BOTTOM, block);
                }
            }
            if (brandNewY != newY)
                break;
        }
    }

    public void checkEntityCollisionsX(ArrayList<Sprite> entities) {
        if (newX == getX()) return;

        for (Sprite s : entities) {
            if (s instanceof Entity) {
                Entity entity = (Entity)s;
                if (getBoundingRectangle().overlaps(entity.getBoundingRectangle())) {
                    if (newX > getX()) {
                        onEntityCollision(RIGHT, entity);
                        entity.onEntityColliding(LEFT, this);
                    }
                    else {
                        onEntityCollision(LEFT, entity);
                        entity.onEntityColliding(RIGHT, this);
                    }
                }
            }
        }
    }

    public void checkEntityCollisionsY(ArrayList<Sprite> entities) {
        if (newY == getY()) return;

        for (Sprite s : entities) {
            if (s instanceof Entity) {
                Entity entity = (Entity)s;
                if (getBoundingRectangle().overlaps(entity.getBoundingRectangle())) {
                    if (newY > getY()) {
                        onEntityCollision(TOP, entity);
                        entity.onEntityCollision(BOTTOM, this);
                    }
                    else {
                        onEntityCollision(BOTTOM, entity);
                        entity.onEntityCollision(TOP, this);
                    }
                }
            }
        }
    }

    public void finalizePosition(boolean xOrY) {
        if (xOrY)
            setY(newY);
        else
            setX(newX);
    }

    //false for x, true for y
    private ArrayList<Block> getCollisionTiles(TiledMap map, boolean xOrY) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        float oldX = getX(), oldY = getY();
        int roundX = Math.round(oldX);
        int roundY = Math.round(oldY);
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);

        float delta;
        int direction, initial, condition, offset;

        if (xOrY) {
            delta = newY-oldY;
            direction = delta < 0 ? -1 : 1;
            initial = -direction;
            offset = (newY<oldY) ? -1 : Math.round(getHeight());
            condition = (int)Math.round(delta);

            for (int j = initial; (direction == 1 && j <= condition) || (direction == -1 && j >= condition); j+=direction) {
                for (int i = -1; i <= Math.ceil(getWidth()); i++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(roundX+i, roundY+offset+j);
                    if (cell != null) {
                        int behaviorID = Integer.parseInt((String)cell.getTile().getProperties().get("behavior"));
                        if (behaviorID != 0)
                            blocks.add(new Block(roundX+i, roundY+offset+j, behaviorID));
                    }
                }
            }
            return blocks;
        }

        delta = newX-oldX;
        direction = delta < 0 ? -1 : 1;
        initial = -direction;
        offset = (newX<oldX) ? -1 : Math.round(getWidth());
        condition = (int)Math.round(delta);

        for (int j = initial; (direction == 1 && j <= condition) || (direction == -1 && j >= condition); j+=direction) {    //fixes high speed collision issues
            for (int i = -1; i <= Math.ceil(getHeight()); i++) {
                TiledMapTileLayer.Cell cell = layer.getCell(roundX+offset+j, roundY+i);
                if (cell != null) {
                    int behaviorID = Integer.parseInt((String)cell.getTile().getProperties().get("behavior"));
                    if (behaviorID != 0)
                        blocks.add(new Block(roundX+offset+j, roundY+i, behaviorID));

                }
            }
        }
        return blocks;
    }
}
