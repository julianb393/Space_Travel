package com.julianbarker.spacetravel.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.julianbarker.spacetravel.scenes.GameScene;
import java.util.Random;

/*
 * Wall class that is made up of WallBlocks.
 */
public class Wall{

    static final int SIZE = 3;
    public static final int WALLBLOCK_GAP = 48;

    private WallBlock[] body;

    private Random rand;
    private int speed;
    private float initialX,initialY;
    private boolean leftDirection;
    private boolean scored;

    public Wall(float x, float y, boolean leftDirection){

        // Coordinates of this Wall
        this.initialX = x;
        this.initialY = y;

        this.leftDirection = leftDirection;

        // Array of WallBlocks to make up the Wall.
        body = new WallBlock[SIZE];

        // The speed of a Wall is pseudo-random.
        rand = new Random();
        speed = rand.nextInt(6) + 4;
        speed *= 10;

        if(this.leftDirection){
            speed *= -1;
        }

        // Initialize every WallBlock in the body.
        for(int i = 0; i < SIZE; i++){
            body[i] = new WallBlock("wall_block.png", x + (i * (WALLBLOCK_GAP + WallBlock.WIDTH)), y, speed);
        }

        scored = false;
    }

    /**
     * Updates the position of each WallBlock in this Wall.
     * @param delta float delta time.
     * @param c OrthographicCamera camera view of game world
     */
    public void update(float delta, OrthographicCamera c){

        for(int i = 0; i < SIZE; i++){
            if(body[i].getY() < c.position.y - GameScene.INITIAL_CAM_Y + c.viewportHeight) {
                body[i].update(delta);
            }
            if(!this.leftDirection && body[i].getX() >= c.viewportWidth){
                body[i].setPosition(-1 * body[i].getWidth(), getY());

            } else if (this.leftDirection && (body[i].getX() + body[i].getWidth()) <= 0){
                body[i].setPosition(c.viewportWidth, getY());
            }
        }
    }

    /**
     * Calls Draw method for each WallBlock in this Wall's body.
     * @param batch SpriteBatch batch where each WallBlock is drawn.
     */
    public void draw(SpriteBatch batch){
        for(WallBlock wb: body){
            wb.draw(batch);
        }
    }

    /**
     * Returns the Y position of this Wall.
     * Every WallBlock has the same Y Position, so only
     * the first one's Y is returned.
     * @return float Y Position of this Wall.
     */
    public float getY(){
        return body[0].getY();
    }

    /**
     * Returns the body of this Wall.
     * @return WallBlock[] array of WallBlocks.
     */
    WallBlock[] getBody(){
        return body;
    }

    /**
     * When this wall is out of the screen (from the bottom),
     * it's position is repositioned to the top.
     */
    public void reposition(float x, float y){

        // New random speed.
        speed = rand.nextInt(6) + 5;
        speed *= 10;

        if(this.leftDirection){
            speed *= -1;
        }

        for(int i = 0; i < SIZE; i++) {
            body[i].setPosition(x + (i * (WALLBLOCK_GAP + WallBlock.WIDTH)), y);
            body[i].setSpeed(speed);
        }
    }

    public void setDirection(boolean newDirection){ // Left is true, Right is false.
        this.leftDirection = newDirection;

    }

    public float getInitialX(){
        return this.initialX;
    }

    public float getInitialY(){
        return this.initialY;
    }

    public void dispose(){
        for(WallBlock wb: body){
            wb.dispose();
        }
    }

    public void scored(){
        scored = true;
    }

    public void resetScored(){
        scored = false;
    }

    public boolean getScored(){
        return scored;
    }





}
