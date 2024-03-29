package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Button extends Entity {
    public int puzzleFlag;

    private static final Texture normalTexture = new Texture(Gdx.files.internal("entities/button_normal.png"));
    private static final Texture pressedTexture = new Texture(Gdx.files.internal("entities/button_pressed.png"));

    public boolean activated = false;
    public boolean pressed = false;

    public float timer = 0;
    public float turnoffDelay = 1;

    public Button(float x, float y, int puzzleFlag) {
        super(x, y);
        this.puzzleFlag = puzzleFlag;
        setSize(2, 1);
        setTexture(normalTexture);
        setRegionWidth(32);
        setRegionHeight(16);
    }

    @Override
    public void performLogic(float deltaTime) {
        if (pressed) {
            activated = true;
            timer = 0;
            setTexture(pressedTexture);
        }
        else {
            if (activated) {
                timer += deltaTime;
                if (timer > turnoffDelay) {
                    activated = false;
                    setTexture(normalTexture);
                }
            }
        }
        MainGame.puzzleFlags.put(puzzleFlag, activated);

        pressed = false;
    }
}
