package com.julianbarker.spacetravel.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.julianbarker.spacetravel.GameInfo;
import com.julianbarker.spacetravel.SpaceTravelMain;
import com.julianbarker.spacetravel.sprites.CustomButton;

class GameOverScene implements Screen {

    private static final int BUTTON_GAP = 60;

    private SpaceTravelMain st;
    private OrthographicCamera guiCam;
    private OrthographicCamera textCam;
    private GameScene gs;
    private CustomButton retryBtn;
    private CustomButton homeBtn;

    private BitmapFont scoreFont;
    private BitmapFont highscoreFont;
    private GlyphLayout scoreLayout;
    private GlyphLayout highscoreLayout;

    private Preferences prefs;

    private int highscore;



    GameOverScene(SpaceTravelMain st, OrthographicCamera guiCam, OrthographicCamera textCam,GameScene gs){
        this.st = st;
        this.guiCam = guiCam;
        this.textCam = textCam;
        this.gs = gs;

        prefs = Gdx.app.getPreferences("My Preferences");
        highscore = prefs.getInteger("Preferences");


        retryBtn = new CustomButton("retry_btn.png", this.guiCam.viewportWidth/2, this.guiCam.viewportHeight/2, 50, 50, true);
        homeBtn = new CustomButton("home_btn.png", this.guiCam.viewportWidth/2, this.guiCam.viewportHeight/2 - BUTTON_GAP, 50, 50, true);

        scoreLayout = new GlyphLayout();
        GameInfo.parameter.size = 74;
        GameInfo.parameter.characters = GameInfo.FONT_CHARACTERS;
        scoreFont = GameInfo.generator.generateFont(GameInfo.parameter);

        highscoreLayout = new GlyphLayout();
        GameInfo.parameter.size = 20;
        GameInfo.parameter.characters = GameInfo.FONT_CHARACTERS;
        highscoreFont = GameInfo.generator.generateFont(GameInfo.parameter);

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

        sb.setProjectionMatrix(this.guiCam.combined);

        sb.begin();
        retryBtn.draw(sb);
        homeBtn.draw(sb);
        sb.end();

        if(Gdx.input.isTouched()){
            // project input
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            this.guiCam.unproject(touchPos);

            if(retryBtn.isClicked(touchPos.x, touchPos.y)) {
                GameInfo.scene = GameInfo.Scene.GAME; // Game is resumed.
                GameInfo.isRunning = true;
                this.gs.newGame();
                this.st.setScreen(this.gs);

            } else if (homeBtn.isClicked(touchPos.x, touchPos.y)){
                this.gs.newGame();
                GameInfo.scene = GameInfo.Scene.MENU;
                GameInfo.isRunning = true;
                this.st.setScreen(this.gs);
            }
        }

        sb.setProjectionMatrix(this.textCam.combined);
        sb.begin();
        scoreFont.draw(sb, scoreLayout, this.textCam.viewportWidth/2 - scoreLayout.width/2,
                this.textCam.viewportHeight - 150);
        highscoreFont.draw(sb, highscoreLayout, this.textCam.viewportWidth/2 - highscoreLayout.width/2,
                this.textCam.viewportHeight - 250);


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

    void setScoreMsg(int currScore) {
        setHighScore(currScore);
        scoreLayout.setText(scoreFont, "score " + Integer.toString(currScore));
        highscoreLayout.setText(highscoreFont, "highscore " + Integer.toString(highscore));


    }

    private void setHighScore(int currScore){
        if(currScore > highscore){
            highscore = currScore;
            prefs.remove("Preferences");
            prefs.putInteger("Preferences", highscore);
            prefs.flush();
        }
    }
}
