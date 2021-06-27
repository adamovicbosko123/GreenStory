package greenstory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import greenstory.game.GreenStory;


public class MainMenuScreen extends ScreenAdapter {
    private static final int VIRTUAL_WIDTH = 640;
    private static final int VIRTUAL_HEIGHT = 360;
    public static float musicVolume = 0.1f;
    public static float positionOfSong = 0;

    private Viewport viewport;

    private Slider slider;

    private Button startButton;
    private Button.ButtonStyle startButtonStyle = new Button.ButtonStyle();

    private Button helpButton;
    private Button.ButtonStyle helpButtonStyle = new Button.ButtonStyle();

    private Button exitButton;
    private Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();


    private Table table;
    private Stage stage;

    private Texture sliderBackgroundTex, sliderKnobTex;
    private TextureRegion menuButtonRegions;
    private BitmapFont bitmapFont;

    private Label titleLabel;
    private Label.LabelStyle labelStyle;
    private Sound selectSound;
    private Music mainMenuMusic;

    public void show() {

        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        bitmapFont = new BitmapFont(Gdx.files.internal("scene2d/titlefont.fnt"));
        labelStyle = new Label.LabelStyle(bitmapFont, Color.GREEN);
        titleLabel = new Label("Green Story", labelStyle);
        titleLabel.setFontScale(1.5f);
        selectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/cautious.wav"));

        menuButtonRegions = new TextureRegion(new Texture("scene2d/buttons.png"));
        TextureRegion[][] buttonRegions = menuButtonRegions.split(197, 54);

        TextureRegion startButtonRegion = buttonRegions[0][1];
        TextureRegion startButtonRegionOver = buttonRegions[0][0];

        TextureRegion helpButtonRegion = buttonRegions[4][2];
        TextureRegion helpButtonRegionOver = buttonRegions[4][3];
        TextureRegion exitButtonRegion = buttonRegions[5][2];
        TextureRegion exitButtonRegionOver = buttonRegions[5][1];
        startButtonStyle.up = new TextureRegionDrawable(startButtonRegion);
        startButtonStyle.over = new TextureRegionDrawable(startButtonRegionOver);
        startButtonStyle.down = new TextureRegionDrawable(startButtonRegion);
        startButton = new Button(startButtonStyle);
        helpButtonStyle.up = new TextureRegionDrawable(helpButtonRegion);
        helpButtonStyle.over = new TextureRegionDrawable(helpButtonRegionOver);
        helpButtonStyle.down = new TextureRegionDrawable(helpButtonRegion);
        helpButton = new Button(helpButtonStyle);
        exitButtonStyle.up = new TextureRegionDrawable(exitButtonRegion);
        exitButtonStyle.over = new TextureRegionDrawable(exitButtonRegionOver);
        exitButtonStyle.down = new TextureRegionDrawable(exitButtonRegion);
        exitButton = new Button(exitButtonStyle);


        //Slider
        sliderBackgroundTex = new Texture(Gdx.files.internal("scene2d/slider_background.png"));
        sliderKnobTex = new Texture(Gdx.files.internal("scene2d/knob.png"));
        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTex));
        ss.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTex));
        slider = new Slider(0f, 1f, .1f, false, ss);
        slider.scaleBy(0.1f, 0.1f);
        slider.setValue(musicVolume);
        musicVolume = slider.getValue();
        mainMenuMusic.play();
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(musicVolume);
        mainMenuMusic.setPosition(positionOfSong);

        // Create table
        table = new Table();
        //  table.debug(); //Enables debug

        // Set table structure
        table.row();
        //table.add(gameTitle).padTop(30f).colspan(2).expand();
        table.add(titleLabel).padTop(30f).colspan(2).expand();
        table.row();
        table.add(startButton).padTop(10f).colspan(2);
        table.row();
        table.add(helpButton).padTop(10f).colspan(2);
        table.row();
        table.add(exitButton).padTop(10f).colspan(2);
        table.row();
        table.add(slider).pad(10f).colspan(2).expandY();
        // table.padBottom(30f);

        // Pack table
        table.setFillParent(true);
        table.pack();

        // Set table's alpha to 0
        table.getColor().a = 0f;

        // Play button listener
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectSound.play();
                mainMenuMusic.stop();
                GreenStory.mainGame.setScreen(new StageLoadingScreen("stage1"));
                dispose();
            }


        });

        // Settings button listener
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenuMusic.stop();
                selectSound.play();
                GreenStory.mainGame.setScreen(new HelpScreen());
                dispose();
            }


        });

        // Exit button listener
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectSound.play();
                Gdx.app.exit();
            }

            ;
        });

        // Slider listener
        slider.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                mainMenuMusic.setVolume(slider.getValue());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            ;
        });

        // Adds created table to stage
        stage.addActor(table);

        // To make the table appear smoothly
        table.addAction(Actions.fadeIn(2f));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        sliderBackgroundTex.dispose();
        sliderKnobTex.dispose();
        bitmapFont.dispose();
        mainMenuMusic.dispose();
        selectSound.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        musicVolume = slider.getValue();
        positionOfSong = mainMenuMusic.getPosition();


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }
}
