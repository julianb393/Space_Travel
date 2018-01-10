package com.julianbarker.spacetravel.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.julianbarker.spacetravel.GameInfo;
import com.julianbarker.spacetravel.SpaceTravelMain;
import com.julianbarker.spacetravel.sprites.CustomButton;

class MenuScene implements Screen {

    private SpaceTravelMain st;
    private OrthographicCamera guiCam;
    private OrthographicCamera textCam;
    private GameScene gs;
    private CustomButton startBtn;

    private Texture titleTexture;



    private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    MenuScene(SpaceTravelMain st, OrthographicCamera guiCam, GameScene gs){

        this.st = st;
        this.guiCam = guiCam;
        this.gs = gs;

        titleTexture = new Texture("st_title.png");

        textCam = new OrthographicCamera();
        textCam.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);

        startBtn = new CustomButton("start_btn.png", this.guiCam.viewportWidth/2, this.guiCam.viewportHeight/2, 150f, 60f, true);

        /* Sets up the custom font.
         */

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Stfont-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 54;
        parameter.characters = FONT_CHARACTERS;
        BitmapFont font = generator.generateFont(parameter);

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, "Space Travel");







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
        // guiCam is locked onto the ship.
        sb.setProjectionMatrix(this.guiCam.combined);

        sb.begin();
        startBtn.draw(sb);
        sb.end();

        if(Gdx.input.isTouched()){
            // project input
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            this.guiCam.unproject(touchPos);
            if(startBtn.isClicked(touchPos.x, touchPos.y)) {
                this.st.setScreen(this.gs);
                GameInfo.scene = GameInfo.Scene.GAME; // Game is resumed.
                GameInfo.isRunning = true;
            }
        }

        sb.setProjectionMatrix(textCam.combined);
        sb.begin();
        sb.draw(titleTexture, textCam.viewportWidth/2 - titleTexture.getWidth()/2, textCam.viewportHeight - 200);
        sb.end();

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

    }
}
