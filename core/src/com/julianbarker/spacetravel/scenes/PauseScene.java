package com.julianbarker.spacetravel.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.julianbarker.spacetravel.GameInfo;
import com.julianbarker.spacetravel.SpaceTravelMain;
import com.julianbarker.spacetravel.sprites.CustomButton;

class PauseScene implements Screen {

    private SpaceTravelMain st;
    private OrthographicCamera guiCam;

    private Texture bg;

    private CustomButton resumeBtn;



    private GameScene gs;


    PauseScene(SpaceTravelMain st, OrthographicCamera guiCam, GameScene gs){

        this.st = st;
        this.guiCam = guiCam;
        this.gs = gs;
        resumeBtn = new CustomButton("resume_btn.png", this.guiCam.viewportWidth/2, this.guiCam.position.y, 60f, 60f, true);

        bg = new Texture("paused_background.png");

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.75f ,0.75f ,0.75f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.gs.render(delta);


        SpriteBatch sb = this.st.getBatch();
        // cam is locked onto the ship.
        sb.setProjectionMatrix(this.guiCam.combined);

        sb.begin();
        sb.draw(bg, 0,0, this.guiCam.viewportWidth, this.guiCam.viewportHeight);
        resumeBtn.draw(sb);
        sb.end();

        if(Gdx.input.isTouched()){
            // project input
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            this.guiCam.unproject(touchPos);
            if(resumeBtn.isClicked(touchPos.x, touchPos.y)) {
                this.st.setScreen(this.gs);
                GameInfo.scene = GameInfo.Scene.GAME; // Game is resumed.
                GameInfo.isRunning = true;
            }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bg.dispose();
        resumeBtn.dispose();
    }
}
