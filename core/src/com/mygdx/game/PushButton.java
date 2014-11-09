package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class PushButton extends Entity {
    private static final Texture normalTexture = new Texture(Gdx.files.internal("entities/pushbutton_normal.png"));
    private static final Texture pushedTexture = new Texture(Gdx.files.internal("entities/pushbutton_pushed.png"));

    public boolean pushed = false;
    public int puzzleFlag;

    public float timer = 0;
    private float activationLength;

    public PushButton(float x, float y, int puzzleFlag, float activationLength) {
        super(x, y);
        setSize(1, 1);
        setRegionWidth(16);
        setRegionHeight(16);
        setTexture(normalTexture);
        this.puzzleFlag = puzzleFlag;
        this.activationLength = activationLength;
    }

    @Override
    public void performLogic(float deltaTime) {
        if (pushed) {
            setTexture(pushedTexture);
            MainGame.puzzleFlags.put(puzzleFlag, true);
            timer += deltaTime;
            if (timer > activationLength) {
                timer = 0;
                pushed = false;
            }
        }
        else {
            setTexture(normalTexture);
            MainGame.puzzleFlags.put(puzzleFlag, false);
        }
    }
}
