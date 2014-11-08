package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;

	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMap map;
	private TmxMapLoader mapLoader;

	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 90, 45);
		camera.update();

		//Load tetures
		img = new Texture("badlogic.jpg");

		//Load Sounds

		//Load level
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("level1.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/16f);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).performLogic();
			}
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for (Sprite sprite : sprites) {
			sprite.draw(batch);
		}

		batch.draw(img, 0, 0);

		batch.end();
	}
}
