package com.julianbarker.spacetravel.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.julianbarker.spacetravel.GameInfo;
import com.julianbarker.spacetravel.SpaceTravelMain;
import com.julianbarker.spacetravel.sprites.CustomButton;
import com.julianbarker.spacetravel.sprites.Ship;
import com.julianbarker.spacetravel.sprites.Wall;
import com.julianbarker.spacetravel.sprites.WallBlock;

import java.util.Random;

public class GameScene implements Screen{

    public static final int INITIAL_CAM_Y = 140;

    private static final int WALL_COUNT = 3;
    private static final int WALL_GAP = 128;

    private SpaceTravelMain st;
    private OrthographicCamera gameCam;
    private OrthographicCamera guiCam;
    private OrthographicCamera textCam;
    private MenuScene ms;
    private PauseScene ps;
    private GameOverScene gos;
    private Texture bg;
    private Texture leftBorder;
    private Texture rightBorder;
    private Vector2 leftBorderPos1, leftBorderPos2;
    private Vector2 rightBorderPos1, rightBorderPos2;
    private Wall[] wallArray;
    private Random rand;
    private CustomButton pauseBtn;
    private Ship ship;
    private int score;

    private OrthographicCamera bgCam;

    private BitmapFont font;

    public GameScene(SpaceTravelMain st){

        this.st = st;

        score = 0;
        textCam = new OrthographicCamera();
        textCam.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);

        // Cam that contains the objects that make up the game.
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, GameInfo.WIDTH/2, GameInfo.HEIGHT/2);

        // Game objects
        ship = new Ship("ship.png");
        ship.setPosition(gameCam.viewportWidth/2 - ship.getWidth()/2, 0);

        wallArray = new Wall[WALL_COUNT];
        // Every other wall goes in the other direction.
        for(int i = 0; i < WALL_COUNT; i++) { // Ensures they start off-screen.
            if(i % 2 == 0) {
                wallArray[i] = new Wall(-1 * WallBlock.WIDTH, gameCam.viewportHeight * 2 + (i * WALL_GAP), false);
            } else {
                wallArray[i] = new Wall(Wall.WALLBLOCK_GAP, gameCam.viewportHeight * 2 + (i * WALL_GAP), true);
            }
        }

        leftBorder = new Texture("border.png");
        rightBorder = new Texture("border.png");

        leftBorderPos1 = new Vector2(0, (gameCam.position.y - gameCam.viewportHeight));
        leftBorderPos2 = new Vector2(0, (gameCam.position.y - gameCam.viewportHeight) + leftBorder.getHeight());
        rightBorderPos1 = new Vector2(gameCam.viewportWidth - rightBorder.getWidth(), gameCam.position.y - gameCam.viewportHeight);
        rightBorderPos2 = new Vector2(gameCam.viewportWidth - rightBorder.getWidth(), (gameCam.position.y - gameCam.viewportHeight) + leftBorder.getHeight());
        rand = new Random();


        bg = new Texture("sp_background.png");

        // Cam which contains objects that the user interacts with.
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, GameInfo.WIDTH/2, GameInfo.HEIGHT/2);

        pauseBtn = new CustomButton("pause_btn.png", guiCam.viewportWidth - 40f, guiCam.viewportHeight - 40f, 40f, 40f, false);

        // Cam used to smoothly render the background "In one spot"
        bgCam = new OrthographicCamera();
        bgCam.setToOrtho(false, GameInfo.WIDTH/2, GameInfo.HEIGHT/2);

        // All scenes that will be used (Only created once).
        ps = new PauseScene(this.st, guiCam, this);
        gos = new GameOverScene(this.st, guiCam, textCam, this);
        ms = new MenuScene(this.st, guiCam, this);

        /* Sets up the custom font.
         */


        GameInfo.parameter.size = 64;
        GameInfo.parameter.characters = GameInfo.FONT_CHARACTERS;
        font = GameInfo.generator.generateFont(GameInfo.parameter);
    }

    @Override
    public void show() {
    }


    /**
     * Implemented method from the interface Screen.
     * Called at each frame. When the game is running,
     * the GameScene continuously renders. Similarly, when the game
     * is paused, the GameScene is continuously rendering but the logic
     * is paused.
     * @param delta delta time
     */
    @Override
    public void render(float delta) {

        drawGame(); // Always renders to screen.

        if(GameInfo.isRunning) { // Used as a method to pause logic.

            switch (GameInfo.scene) {
                case GAME:

                    if(!pauseBtn.isVisible()){
                        pauseBtn.setVisible(true);
                    }
                    updateGame(delta);
                    if(Gdx.input.isTouched()) {
                        // project input
                        Vector3 touchPos = new Vector3();
                        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                        guiCam.unproject(touchPos);

                        if (pauseBtn.isClicked(touchPos.x, touchPos.y)) {
                            pauseBtn.setVisible(false);
                            pause();
                        }
                    }
                    break;
                case PAUSE:
                    GameInfo.isRunning = false;
                    this.st.setScreen(ps);
                    break;
                case GAMEOVER:
                    gos.setScoreMsg(score);
                    pauseBtn.setVisible(false);
                    GameInfo.isRunning = false;
                    this.st.setScreen(gos);
                    break;
                case MENU:
                    gameCam.update(); // updates gameCam because of the newGame() call.
                    pauseBtn.setVisible(false);
                    GameInfo.isRunning = false;
                    this.st.setScreen(ms);
                    break;

            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        GameInfo.scene = GameInfo.Scene.PAUSE;
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /**
     * Implemented method from the interface Screen.
     * Disposes all textures used in GameScene.
     */
    @Override
    public void dispose() {
        ship.dispose();
        bg.dispose();
        leftBorder.dispose();
        rightBorder.dispose();
        for(Wall w: wallArray){
            w.dispose();
        }
        pauseBtn.dispose();
    }

    /**
     * Updates the y-coordinate of both borders
     * (continuously raises them when they go off-screen).
     */
    private void updateBorder(){
        if(gameCam.position.y - (gameCam.viewportHeight / 2)> leftBorderPos1.y + leftBorder.getHeight()){
            leftBorderPos1.add(0, leftBorder.getHeight() * 2);
            rightBorderPos1.add(0, rightBorder.getHeight() * 2);
        }
        if(gameCam.position.y - (gameCam.viewportHeight / 2)> leftBorderPos2.y + leftBorder.getHeight()){
            leftBorderPos2.add(0, leftBorder.getHeight() * 2);
            rightBorderPos2.add(0, rightBorder.getHeight() * 2);
        }
    }

    /**
     * Updates the position of the ship. If the left or right buttons are pressed,
     * then the ship moves in the respective direction. The ship is also continuously
     * moving in the positive y direction.
     * @param delta delta time
     */
    private void updateShip(float delta){

        if(Gdx.input.isTouched()) {
            // project input
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            guiCam.unproject(touchPos);
            if(touchPos.y < guiCam.viewportHeight/2) {
                ship.move(touchPos.x - ship.getWidth() / 2); // ship is centered at touch.
            }
        }

        for(Wall wall: wallArray){
            if(ship.collidedWithWall(wall)){
                GameInfo.scene = GameInfo.Scene.GAMEOVER;
                break;
            } else if(wall.getY() <= ship.getY() && !wall.getScored()){
                wall.scored();
                score++;
                break;

            }
        }
        ship.update(delta); // continuously move upwards.

    }

    /**
     * Updates the position of the walls. Each wall moves in the opposite
     * direction of the one before it. When off-screen, they are repositioned
     * in the positive y direction.
     *
     * @param delta delta time
     */
    private void updateWalls(float delta){
        int wallDirSwitch;

        for(int i = 0; i < WALL_COUNT; i++) {
            if(wallArray[i].getY() < ship.getY() - 50){
                wallDirSwitch = rand.nextInt(2);

                if(wallDirSwitch == 0) {
                    wallArray[i].setDirection(true); // towards left
                    wallArray[i].reposition(Wall.WALLBLOCK_GAP, wallArray[i].getY() + gameCam.viewportHeight);
                } else if(wallDirSwitch == 1) {
                    wallArray[i].setDirection(false); // towards right
                    wallArray[i].reposition(-1 * WallBlock.WIDTH, wallArray[i].getY() + gameCam.viewportHeight);
                }
                wallArray[i].resetScored();
            }
            wallArray[i].update(delta, gameCam);
        }
    }

    /**
     *
     */
    private void collidedWithBoarder(){

        if(leftBorderPos1.x + leftBorder.getWidth() > ship.getX() ||
                leftBorderPos2.x  + leftBorder.getWidth() > ship.getX()){
            ship.setPosition(leftBorderPos1.x + leftBorder.getWidth(), ship.getY());
        }
        if(rightBorderPos1.x < ship.getX() + ship.getWidth() ||
                rightBorderPos2.x < ship.getX() + ship.getWidth()){
            ship.setPosition(rightBorderPos1.x - ship.getWidth(), ship.getY());
        }
    }

    private void drawGame(){
        SpriteBatch sb = this.st.getBatch();
        // Draws in view of the gameCam.
        // Resets screen.
        Gdx.gl.glClearColor(0.75f ,0.75f ,0.75f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        sb.setProjectionMatrix(bgCam.combined);
        sb.begin();
        sb.draw(bg, 0,0, bgCam.viewportWidth, bgCam.viewportHeight);
        sb.end();



        // gameCam is locked onto the ship.
        gameCam.position.set(gameCam.viewportWidth/2, ship.getY() + INITIAL_CAM_Y, 0);
        sb.setProjectionMatrix(gameCam.combined);


        sb.begin();
        for(int i = 0; i < WALL_COUNT; i++){
            wallArray[i].draw(sb);
        }

        sb.draw(leftBorder, leftBorderPos1.x, leftBorderPos1.y);
        sb.draw(leftBorder, leftBorderPos2.x, leftBorderPos2.y);
        sb.draw(rightBorder, rightBorderPos1.x, rightBorderPos1.y);
        sb.draw(rightBorder, rightBorderPos2.x, rightBorderPos2.y);
        collidedWithBoarder();
        ship.draw(sb);
        sb.end();



        sb.setProjectionMatrix(guiCam.combined);
        sb.begin();

        if(pauseBtn.isVisible()) {
            pauseBtn.draw(sb);
        }

        sb.end();

        sb.setProjectionMatrix(textCam.combined);
        sb.begin();

        if (GameInfo.scene == GameInfo.Scene.GAME) {
            font.draw(sb, Integer.toString(score), textCam.viewportWidth / 2, textCam.viewportHeight);
        }
        sb.end();
    }

    private void updateGame(float delta){
        updateBorder();
        updateWalls(delta);
        updateShip(delta);
        gameCam.update();
    }

    void newGame(){

        score = 0;
        ship.setPosition(gameCam.viewportWidth/2 - ship.getWidth()/2, INITIAL_CAM_Y);
        gameCam.position.y = ship.getY() + INITIAL_CAM_Y;
        leftBorderPos1.set(0, gameCam.position.y - gameCam.viewportHeight);
        leftBorderPos2.set(0, (gameCam.position.y - gameCam.viewportHeight) + leftBorder.getHeight());
        rightBorderPos1.set(gameCam.viewportWidth - rightBorder.getWidth(), gameCam.position.y - gameCam.viewportHeight);
        rightBorderPos2.set(gameCam.viewportWidth - rightBorder.getWidth(), (gameCam.position.y - gameCam.viewportHeight) + leftBorder.getHeight());

        for(int i = 0; i < WALL_COUNT; i++) {
            if (i % 2 == 0) {
                wallArray[i].setDirection(false);
            } else {
                wallArray[i].setDirection(true);
            }
            wallArray[i].reposition(wallArray[i].getInitialX(), wallArray[i].getInitialY());
            wallArray[i].resetScored();
        }
    }
}
