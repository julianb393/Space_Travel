package com.julianbarker.spacetravel.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/*
 * WallBlock class that extends Sprite (from libgdx).
 */
public class WallBlock extends Sprite {

    public static final int WIDTH = 48;
    private int speed;


    WallBlock(String name, float x, float y, int speed){
        super(new Texture(name));
        setPosition(x,y);
        this.speed = speed;
    }

    /**
     * Updates the position of this WallBlock.
     * Generally, each WallBlok in a Wall will have the same Y Position.
     * @param delta float delta time
     */
    void update(float delta){
        setPosition(getX() + (this.speed * delta), getY());
    }

    /**
     * Disposes the texture used for this WallBlock.
     */
    void dispose(){
        getTexture().dispose();
    }

    /**
     * Sets the speed of this WallBlock.
     * The speed is the same for all WallBlocks in the same Wall.
     * @param setSpeed int speed to be set.
     */
    void setSpeed(int setSpeed){
        this.speed = setSpeed;
    }
}
