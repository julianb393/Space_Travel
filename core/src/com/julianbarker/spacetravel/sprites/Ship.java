package com.julianbarker.spacetravel.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/*
 * Ship class that extends Sprite (from LIBGDX).
 */
public class Ship extends Sprite{

    private static final int MOVEMENT = 150;

    public Ship(String name){
        super(new Texture(name));
    }

    /**
     * Updates the position of this Ship.
     * @param delta float delta time.
     */
    public void update(float delta){

        setPosition(getX(), getY() + (MOVEMENT * delta));
    }

    /**
     * Sets the x-coordinate of this Ship given a float x.
     * @param x float to be set as x-coordinate.
     */
    public void move(float x){
        setX(x);
    }

    /**
     * Disposes the texture used for this Ship.
     */
    public void dispose(){
        getTexture().dispose();
    }

    /**
     * Detects of this Ship collided with a Wall object.
     * @param wall Wall to check collision with this Ship
     * @return true if there is a collision, false otherwise
     */
    public boolean collidedWithWall(Wall wall){

        WallBlock[] wbs = wall.getBody();
        for(int i = 0; i < Wall.SIZE; i++){
            if(((wbs[i].getX() < getX()) && (wbs[i].getX() + wbs[i].getWidth() > getX()))
                    || ((wbs[i].getX() < getX() + getWidth()) && (wbs[i].getX() + wbs[i].getWidth() > getX() + getWidth()))) {
                if ((wbs[i].getY() < getY() && (wbs[i].getY() + wbs[i].getHeight() > getY()))
                        || (wbs[i].getY() < getY() + getHeight() && (wbs[i].getY() + wbs[i].getHeight() > getY() + getHeight()))){

                    return true;
                }
            }
        }
        return false;
    }

}