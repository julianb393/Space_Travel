package com.julianbarker.spacetravel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.julianbarker.spacetravel.scenes.GameScene;

public class SpaceTravelMain extends Game {
	SpriteBatch batch;

	private OrthographicCamera gameCam;


	@Override
	public void create () {
		this.batch = new SpriteBatch();

		setScreen(new GameScene(this));

	}

	@Override
	public void render () {
		super.render();
	}



	@Override
	public void dispose () {
		batch.dispose();
	}

	public SpriteBatch getBatch(){
		return this.batch;
	}
}
