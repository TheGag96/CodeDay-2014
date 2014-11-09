package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainGame extends ApplicationAdapter {
	static SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;

	private OrthogonalTiledMapRenderer mapRenderer;
	public static TiledMap map;
	private TmxMapLoader mapLoader;

	private BitmapFont font;

	public static HashMap<Integer, Boolean> puzzleFlags = new HashMap<Integer, Boolean>();

	public static int collectablesCollected = 0;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	public static boolean resultsScreen = false;

	public int levelNum = 1;

	private Matrix4 normalProjection;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 80, 45);
		camera.update();

		//Load font
		font = new BitmapFont();
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());



		//Load Sounds

		//Load level
		mapLoader = new TmxMapLoader();
		loadLevel(1);
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/16f);

		//
		//Gdx.gl20.glEnable(GL20.GL_BLEND);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		if (resultsScreen) {
			Gdx.gl.glClearColor(0, 0.5f, 0.8f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();

			batch.setProjectionMatrix(camera.combined);
			//TODO: star shit


			batch.setProjectionMatrix(normalProjection);
			font.draw(batch, "You made it!", 140, 400);
			font.draw(batch, "Press Enter to continue.", 140, 100);

			batch.end();

			if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
				resultsScreen = false;
				levelNum++;
				loadLevel(levelNum);
			}
		}
		else {
			runLogicChecksAndCollisions(deltaTime);

			Gdx.gl.glClearColor(0, 0.5f, 0.8f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			camera.update();

			mapRenderer.setView(camera);
			mapRenderer.render();

			batch.setProjectionMatrix(camera.combined);
			batch.begin();

			for (Sprite sprite : sprites) {
				sprite.draw(batch);
			}

			batch.setProjectionMatrix(normalProjection);
			font.draw(batch, "Level: "+levelNum, 10, 720-font.getCapHeight());
			font.draw(batch, "Collectables: " + collectablesCollected, 1150, 720-font.getCapHeight());

			batch.end();
		}
	}

	private void runLogicChecksAndCollisions(float deltaTime) {
		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).performLogic(deltaTime);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).updatePosition(false, deltaTime);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).checkBlockCollisionsX(map);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).checkEntityCollisionsX(sprites);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).finalizePosition(false);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).updatePosition(true, deltaTime);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).checkBlockCollisionsY(map);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).checkEntityCollisionsY(sprites);
			}
		}

		for (Sprite sprite : sprites) {
			if (sprite instanceof Entity) {
				((Entity)sprite).finalizePosition(true);
			}
		}
	}

	public void loadLevel(int n) {
		map = mapLoader.load("levels/level"+n+".tmx");
		sprites.clear();
		puzzleFlags.clear();
		collectablesCollected = 0;
		try {
			Scanner scanner = new Scanner(Gdx.files.internal("levels/level"+n+".cfg").file());
			while (scanner.hasNext()) {
				String spriteName = scanner.next();
				float x = scanner.nextFloat();
				float y = scanner.nextFloat();
				if (spriteName.equals("Player")) {
					sprites.add(new Player(x, y));
				}
				else if (spriteName.equals("Button")) {
					int puzzleFlag = scanner.nextInt();
					sprites.add(new Button(x, y , puzzleFlag));
				}
				else if (spriteName.equals("Lever")) {
					int puzzleFlag = scanner.nextInt();
					sprites.add(new Lever(x, y , puzzleFlag));
				}
				else if (spriteName.equals("MovableBox")) {
					sprites.add(new MovableBox(x, y));
				}
				else if (spriteName.equals("Door")) {
					int puzzleFlag = scanner.nextInt();
					sprites.add(new Door(x, y, puzzleFlag));
				}
				else if (spriteName.equals("PushButton")) {
					int puzzleFlag = scanner.nextInt();
					float activationTime = scanner.nextFloat();
					sprites.add(new PushButton(x, y, puzzleFlag, activationTime));
				}
				else if (spriteName.equals("Wall")) {
					int puzzleFlag = scanner.nextInt();
					sprites.add(new Wall(x, y, puzzleFlag));
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
