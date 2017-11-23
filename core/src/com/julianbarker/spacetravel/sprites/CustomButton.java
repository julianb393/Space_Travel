package com.julianbarker.spacetravel.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class CustomButton extends Sprite {


    private boolean isVisible;


    public CustomButton(String imagePath, float x, float y, float width, float height, Boolean center){
        super(new Texture(imagePath));
        setSize(width, height);
        if(center) {
            setPosition(x - width / 2, y); //Centers appropriately.
        } else {
            setPosition(x, y);
        }
        isVisible = true;
    }

    public boolean isClicked(float inputX, float inputY){

        if((inputX >= getX()) && (inputX <= (getX() + getWidth()))){
            if((inputY >= getY()) && (inputY <= (getY() + getHeight()))){
                return true;
            }


        }
        return false;
    }

    public void dispose(){
      getTexture().dispose();
    }

    public boolean isVisible(){
        return isVisible;
    }

    public void setVisible(boolean visible){
        isVisible = visible;
    }
}
