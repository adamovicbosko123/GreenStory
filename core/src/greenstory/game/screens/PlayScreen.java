package greenstory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import greenstory.game.GreenStory;
import greenstory.game.enemies.Enemy;
import greenstory.game.enemies.FireCultist;
import greenstory.game.enemies.assets.FireBall;
import greenstory.game.objects.enemyobjects.Menace;
import greenstory.game.objects.harmlesobjects.Harmles;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;
import greenstory.game.player.assets.Bow;
import greenstory.game.utilities.ContactListenerClass;
import greenstory.game.utilities.GroundCreator;
import greenstory.game.utilities.MapBodiesCreator;
import greenstory.game.utilities.WallCreator;
import greenstory.game.objects.enemyobjects.*;
import greenstory.game.objects.harmlesobjects.*;

public class PlayScreen extends AbstractScreen implements Disposable {
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    public Player player;
    private MapBodiesCreator mapBodiesCreator;
    private Array<Enemy> enemies;
    private World world;
    private Hud hud;
    private final Array<Menace> menaces;
    private Array<Harmles> harmles;
    private Box2DDebugRenderer box2DDebugRenderer;
    private boolean endOfStage = false;
    private int countOfEnemies;
    private Music ghostSound, music;

    public PlayScreen() {
        batch = GreenStory.mainGame.batch;

        camera = new OrthographicCamera();
        viewport = new FitViewport(GreenStory.SCENE_WIDTH, GreenStory.SCENE_HEIGHT, camera);
        camera.setToOrtho(false);
        camera.position.set(GreenStory.SCENE_WIDTH * 2, GreenStory.SCENE_HEIGHT * 0.5f, 0);

        map = StageLoadingScreen.manager.get("stage1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1 / GreenStory.PPM);

        world = new World(new Vector2(0, -9.81f), false);
        box2DDebugRenderer = new Box2DDebugRenderer();

        player = new Player(world, this);
        new GroundCreator(this);
        new WallCreator(this);
        mapBodiesCreator = new MapBodiesCreator(this);
        enemies = mapBodiesCreator.getEnemies();
        menaces = mapBodiesCreator.getMenaces();
        harmles = mapBodiesCreator.getHarmles();
        world.setContactListener(new ContactListenerClass(this));
        hud = new Hud(GreenStory.mainGame.batch, this);
        ghostSound = Gdx.audio.newMusic(Gdx.files.internal("music/ghost.ogg"));
        ghostSound.setVolume(0.009f);
        ghostSound.play();
        ghostSound.setLooping(true);
        music = Gdx.audio.newMusic(Gdx.files.internal("music/cautious.wav"));
        music.setVolume(MainMenuScreen.musicVolume);
        music.play();
        music.setPosition(MainMenuScreen.positionOfSong);
        music.setLooping(true);
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public int getCountOfEnemies() {
        return countOfEnemies;
    }

    public void update(float delta) {
        hud.update(delta);
        countOfEnemies = enemies.size;
        for (Enemy enemy : enemies) {
            if (!enemy.isDestroyed()) {
                enemy.update(delta);
            } else {
                countOfEnemies--;
            }

        }
        for (Menace m : menaces) {
            if (m instanceof PoisonDrop) {
                PoisonDrop p = (PoisonDrop) m;
                p.checkCollisionWithPlayer(player);
            }
            if (m instanceof Lightning) {
                Lightning l = (Lightning) m;
                if (l.getBoundingRectangle().overlaps(player.getPlayerRectangle())) {
                    player.setHealth(0);
                    player.getFSM().changeState(PlayerStatesWrapper.PlayerStates.DEATH);
                }
            }
            if (m instanceof Smoke) {
                Smoke s = (Smoke) m;
                if (s.overlapsSmokeRectangle(player) && (s.getIndex() == 0 || s.getIndex() == 1)) {
                    player.setHit(true);
                }
            }
            m.update(delta);

        }

        for (Harmles h : harmles) {

            if (h instanceof Healer) {
                Healer healer = (Healer) h;
                if (player.playerOverlapsHealer(healer.getSensorRectangle())) {
                    player.startHealing();

                }
            }
            if (h instanceof Ladder) {
                Ladder ladder = (Ladder) h;
                if (ladder.getLadderRectangle().overlaps(player.getPlayerRectangle()) && Gdx.input.isKeyPressed(Input.Keys.UP) && !player.swordHolding) {
                    player.setPlayerOnLadder(true);
                    player.getFSM().changeState(PlayerStatesWrapper.PlayerStates.LADDER_CLIMB);
                } else {
                    player.setPlayerOnLadder(false);
                }
            }

            if (h instanceof Door) {
                Door door = (Door) h;
                if (door.playerOnTheDoor(player) && getCountOfEnemies() <= 0) {
                    door.setPlayerCameToDoor(true);
                    endOfStage = true;
                }
            }
            h.update(delta);
        }


        player.update(delta);

        camera.position.x = player.playerBody.getPosition().x;
        camera.position.y = player.playerBody.getPosition().y + 1.2f;
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        float cameraMinX = viewport.getWorldWidth() * 0.5f;
        float cameraMinY = viewport.getWorldHeight() * 0.5f;
        float cameraMaxX = (layer.getWidth() / GreenStory.PPM) * layer.getTileWidth() - cameraMinX;
        float cameraMaxY = (layer.getHeight() / GreenStory.PPM) * layer.getTileHeight() - cameraMinY;

        camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX, cameraMaxX);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraMinY, cameraMaxY);
        camera.update();

        if (player.getFSM().isInState(PlayerStatesWrapper.PlayerStates.DEATH)) {
            if ((timeToTryAgain += delta) >= 3) {
                this.dispose();
                GreenStory.mainGame.setScreen(new MainMenuScreen());

            }
        }
        if (countOfEnemies <= 0) {
            ghostSound.stop();
            ghostSound.dispose();
        }


    }

    private float timeToTryAgain = 0;

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        update(delta);
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        world.step(1 / 60f, 9, 5);
        ///box2DDebugRenderer.render(world, camera.combined);
        batch.begin();


        for (Menace m : menaces) {
            m.draw(batch);
        }
        for (Harmles h : harmles) {
            h.draw(batch);
        }

        for (Enemy enemy : enemies) {
            if (enemy instanceof FireCultist) {
                FireCultist fireCultist = (FireCultist) enemy;
                if (!fireCultist.fireBalls.isEmpty()) {
                    for (FireBall fb : fireCultist.fireBalls) {
                        fb.draw(batch);
                    }
                }

            }
            enemy.draw(batch);
        }
        player.draw(batch);
        if (!player.arrows.isEmpty()) {
            for (Bow bow : player.arrows) {
                bow.draw(batch);
            }
        }


        batch.end();
        GreenStory.mainGame.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

    }


    @Override
    public void dispose() {
        hud.dispose();
        for (Menace menace : menaces) {
            menace.dispose();
        }

        for (int i = 0; i < harmles.size; i++) {
            harmles.get(i).dispose();
        }

        music.stop();
        music.dispose();
        ghostSound.stop();
        ghostSound.dispose();
    }

    public World getWorld() {
        return this.world;
    }

    public TiledMap getMap() {
        return this.map;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isEndOfStage() {
        return endOfStage;
    }

}
