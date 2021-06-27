package greenstory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.player.Wife;
import greenstory.game.utilities.ContactListenerClass;
import greenstory.game.utilities.GroundCreator;

public class HappyEndScreen extends AbstractScreen {
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;
    private Player player;
    private Wife wife;
    private ShapeRenderer renderer = new ShapeRenderer();
    private Rectangle wifeSeePlayer;
    private TheEndHud theEndHud;
    private Music theEndMusic;


    public HappyEndScreen() {
        batch = GreenStory.mainGame.batch;
        camera = new OrthographicCamera();

        viewport = new FitViewport(GreenStory.SCENE_WIDTH, GreenStory.SCENE_HEIGHT, camera);
        camera.setToOrtho(false);
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("happyend.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1 / GreenStory.PPM);
        world = new World(new Vector2(0, -9.81f), false);
        box2DDebugRenderer = new Box2DDebugRenderer();
        player = new Player(world, this) {
            public void listenInput() {
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playerBody.getLinearVelocity().x <= 2 && !player.isMetWithTheWife()) {
                    playerBody.applyForceToCenter(25, 0, true);
                }
            }
        };
        wife = new Wife(this, world);
        player.playerBody.setTransform(1, 2.1f, 0);
        new GroundCreator(this);
        wifeSeePlayer = new Rectangle();
        world.setContactListener(new ContactListenerClass(this));
        theEndHud = new TheEndHud(GreenStory.mainGame.batch, this);
        theEndMusic = Gdx.audio.newMusic(Gdx.files.internal("music/theEnd.ogg"));
        theEndMusic.setVolume(MainMenuScreen.musicVolume);
        theEndMusic.play();
        theEndMusic.setPosition(MainMenuScreen.positionOfSong);
        theEndMusic.setLooping(true);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        player.update(delta);
        wife.update(delta);
        wifeSeePlayer.set(3, 2, 6, 1);
        if (player.getPlayerRectangle().overlaps(wifeSeePlayer)) {
            wife.flip(true, false);
            if (wife.waitABit()) {
                wife.setPlayerHasBeenSeen(true);
            }
        }
        theEndHud.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        camera.position.set(GreenStory.SCENE_WIDTH * 0.4f, GreenStory.SCENE_HEIGHT * 0.5f, 0);
        camera.update();
        tiledMapRenderer.setView(camera);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.render();
        world.step(1 / 60f, 12, 6);
        //box2DDebugRenderer.render(world, camera.combined);
        batch.begin();
        wife.draw(batch);
        player.draw(batch);

        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        // renderer.rect(wifeSeePlayer.x, wifeSeePlayer.y, wifeSeePlayer.width, wifeSeePlayer.height);
        renderer.end();
        GreenStory.mainGame.batch.setProjectionMatrix(theEndHud.getStage().getCamera().combined);
        theEndHud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isEndOfStage() {
        return false;
    }

    public World getWorld() {
        return this.world;
    }

    public TiledMap getMap() {
        return this.map;
    }

    public Batch getBatch() {
        return this.batch;
    }

    public void dispose() {
        map.dispose();
        tiledMapRenderer.dispose();
        batch.dispose();
        renderer.dispose();
        theEndHud.dispose();
        theEndMusic.stop();
        theEndMusic.dispose();
    }
}
