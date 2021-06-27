package greenstory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import greenstory.game.GreenStory;

public class StageLoadingScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Label titleLabel;
    private Label.LabelStyle labelStyle;
    private Label continueLabel;

    private Label descriptionLabel;

    private Texture progressBarImg, progressBarBaseImg;
    private TiledMap map;

    private float unitScale;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    // in case of android app, just reload the managers
    public static AssetManager manager;
    public static AssetManager enemyAssetManager;
    private BitmapFont bitmapFont;
    private Table table;
    private Stage stage;
    private String flag;
    private Music mainMenuMusic;

    public StageLoadingScreen(String flag) {
        this.flag = flag;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 60, 35);
        camera.update();
        table = new Table();
        table.top();
        table.row();
        table.padTop(40);
        table.row();
        table.debug();
        batch = new SpriteBatch();
        stage = new Stage();
        camera.update();
        if (flag.equals("stage1")) {
            mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/cautious.wav"));
            mainMenuMusic.play();
            mainMenuMusic.setPosition(MainMenuScreen.positionOfSong);
            mainMenuMusic.setLooping(true);
            mainMenuMusic.setVolume(MainMenuScreen.musicVolume);
        }
        manager = new AssetManager();
        enemyAssetManager = new AssetManager();
        bitmapFont = new BitmapFont(Gdx.files.internal("scene2d/titlefont.fnt"));
        labelStyle = new Label.LabelStyle(bitmapFont, Color.GREEN);
        continueLabel = new Label("", labelStyle);
        continueLabel.setFontScale(0.25f);
        if (flag.equals("stage1")) {
            titleLabel = new Label("Green Story", labelStyle);
            descriptionLabel = new Label("Must remove all cultists\n they spread awful virus\n which makes our people mindless\n slaves in their factories and mines......", labelStyle);
            titleLabel.setFontScale(2f);
            descriptionLabel.setFontScale(0.25f);
        } else if (flag.equals("happyend")) {
            titleLabel = new Label("End of Green Story...", labelStyle);
            descriptionLabel = new Label("And so, the time of cultists is over.....\n" +
                    "Peace is restored again in our little village\n" +
                    "and our hero is on the way back home.\n" +
                    "His family is in front of house and worried....", labelStyle);
            titleLabel.setFontScale(1f);
            descriptionLabel.setFontScale(0.5f);
        }
        table.add(titleLabel);
        table.row();
        table.padTop(60);
        table.add(descriptionLabel);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("loading_screen/map.tmx", TiledMap.class);
        unitScale = 1 / 16f;
        manager.load("arrow.png", Texture.class);
        manager.load("loading_screen/background.png", Texture.class);
        manager.load("loading_screen/progress_bar.png", Texture.class);
        manager.load("loading_screen/progress_bar_base.png", Texture.class);
        manager.finishLoading(); // Blocks until all resources are loaded into memory
        map = manager.get("loading_screen/map.tmx");
        progressBarImg = manager.get("loading_screen/progress_bar.png");
        progressBarBaseImg = manager.get("loading_screen/progress_bar_base.png");

        renderer = new OrthogonalTiledMapRenderer(map, unitScale);


        // Load assets for the next screen
        manager.load("platform.png", Texture.class);
        manager.load("fist.png", Texture.class);
        manager.load("bow.png", Texture.class);
        manager.load("can1.png", Texture.class);
        manager.load("can2.png", Texture.class);
        manager.load("can3.png", Texture.class);
        manager.load("sword.png", Texture.class);
        manager.load("arrow.png", Texture.class);
        manager.load("crap1.png", Texture.class);
        manager.load("crap2.png", Texture.class);
        manager.load("crap3.png", Texture.class);
        manager.load("crap4.png", Texture.class);
        manager.load("crap5.png", Texture.class);
        manager.load("crap6.png", Texture.class);
        manager.load("crap7.png", Texture.class);
        manager.load("barrel1.png", Texture.class);
        manager.load("barrel2.png", Texture.class);
        manager.load("barrel3.png", Texture.class);
        manager.load("barrel4.png", Texture.class);
        manager.load("sounds/swish.wav", Sound.class);
        manager.load("sounds/bow.wav", Sound.class);
        manager.load("sounds/barrelImpact.wav", Sound.class);
        manager.load("sounds/punch.wav", Sound.class);
        manager.load("sounds/explosion.wav", Sound.class);
        manager.load("sounds/cultistScream.wav", Sound.class);
        manager.load("sounds/fireCultistScream.wav", Sound.class);
        manager.load("sounds/bigCultistScream.wav", Sound.class);
        manager.load("sounds/skeletonScream.wav", Sound.class);
        manager.load("sounds/assassinScream.wav", Sound.class);
        manager.load("sounds/cultistDie.wav", Sound.class);
        manager.load("sounds/fireCultistDie.wav", Sound.class);
        manager.load("sounds/bigCultistDie.wav", Sound.class);
        manager.load("sounds/assassinDie.wav", Sound.class);
        manager.load("sounds/skeletonDie.wav", Sound.class);
        manager.load("sounds/footstep.wav", Sound.class);
        manager.load("sounds/drink.wav", Sound.class);
        manager.load("sounds/arrow.wav", Sound.class);
        manager.load("sounds/crap.wav", Sound.class);
        manager.load("sounds/cultistPush.wav", Sound.class);
        manager.load("sounds/fireCultistFire.wav", Sound.class);
        manager.load("sounds/skeletonSwing.wav", Sound.class);
        manager.load("music/theEnd.ogg", Sound.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("stage1.tmx", TiledMap.class);

        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element root = reader.parse(Gdx.files.internal("player.xml"));
            Array<XmlReader.Element> elements = root.getChildrenByNameRecursively("Path");

            for (XmlReader.Element el : elements) {
                manager.load(el.getAttribute("path") + ".png", Texture.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!flag.equals("happyend")) {

            try {
                XmlReader reader = new XmlReader();
                XmlReader.Element root = reader.parse(Gdx.files.internal("enemies.xml"));
                Array<XmlReader.Element> elements = root.getChildrenByNameRecursively("Path");

                for (XmlReader.Element el : elements) {
                    enemyAssetManager.load(el.getAttribute("path") + ".png", Texture.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        table.row();
        table.add(continueLabel).bottom().padTop(40);

        // Pack table
        table.setFillParent(true);
        table.pack();
        stage.addActor(table);

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        renderer.dispose();
        map.dispose();
        progressBarImg.dispose();
        progressBarBaseImg.dispose();
        bitmapFont.dispose();
    }

    private float time = 0;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
        renderer.setView(camera);
        renderer.render();

        batch.begin();
        titleLabel.draw(batch, 1);
        descriptionLabel.draw(batch, 1);
        if (!flag.equals("happyend")) {
            //batch.draw(progressBarBaseImg, titleLabel.getX() + 50, titleLabel.getY() - 160);
            batch.setColor(Color.SCARLET);
            if (!manager.update()) {
                batch.draw(progressBarImg, titleLabel.getX() + 50, titleLabel.getY() - 160, progressBarImg.getWidth() * manager.getProgress(), progressBarImg.getHeight());
            }
            if (!enemyAssetManager.update())
                batch.draw(progressBarImg, titleLabel.getX() + 50, titleLabel.getY() - 160, progressBarImg.getWidth() * enemyAssetManager.getProgress(), progressBarImg.getHeight());
        }
        continueLabel.draw(batch, 1);
        batch.end();
        batch.setColor(GreenStory.mainGame.mainColor);
        if (enemyAssetManager.update() && manager.update()) {
            continueLabel.setText("Press enter to continue...");

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (flag.equals("stage1")) {
                    mainMenuMusic.stop();
                    mainMenuMusic.dispose();
                    GreenStory.mainGame.setScreen(new PlayScreen());
                } else if (flag.equals("happyend")) {
                    GreenStory.mainGame.setScreen(new HappyEndScreen());
                }
            }
        }


    }

}
