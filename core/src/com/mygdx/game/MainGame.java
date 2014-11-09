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
import com.badlogic.gdx.physics.box2d.Box2D;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;

	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMap map;
	private TmxMapLoader mapLoader;
	public static HashMap<Integer, Boolean> puzzleFlags = new HashMap<Integer, Boolean>();

	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 80, 45);
		camera.update();

		//Load tetures
		//img = new Texture("badlogic.jpg");

		//Load Sounds

		//Load level
		mapLoader = new TmxMapLoader();
		loadLevel(1);
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/16f);

		//
		Gdx.gl20.glEnable(GL20.GL_BLEND);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		runLogicChecksAndCollisions(deltaTime);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for (Sprite sprite : sprites) {
			//sprite.draw(batch);
		}

		batch.end();
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
				((Entity)sprite).checkBlockCollisionsX(map);
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
				scanner.nextLine();
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
