package greenstory.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import greenstory.game.GreenStory;

public class HelpScreen extends ScreenAdapter {
    private static final int SCENE_WIDTH = 1280;
    private static final int SCENE_HEIGHT = 720;
    private Viewport viewport;
    private BitmapFont font;
    private TextureRegion menuButtonRegions;

    private Button startButton;
    private Button.ButtonStyle startButtonStyle = new Button.ButtonStyle();

    private Button backButton;
    private Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();

    private Stage stage;
    private Table table;
    private Sound selectSound;
    private Music mainMenuMusic;

    @Override
    public void show() {
        super.show();

        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        selectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/cautious.wav"));
        mainMenuMusic.play();
        mainMenuMusic.setPosition(MainMenuScreen.positionOfSong);
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(MainMenuScreen.musicVolume);
        font = new BitmapFont(Gdx.files.internal("scene2d/titlefont.fnt"));

        menuButtonRegions = new TextureRegion(new Texture("scene2d/buttons.png"));
        TextureRegion[][] buttonRegions = menuButtonRegions.split(197, 54);

        TextureRegion startButtonRegion = buttonRegions[0][1];
        TextureRegion startButtonRegionOver = buttonRegions[0][0];
        startButtonStyle.up = new TextureRegionDrawable(startButtonRegion);
        startButtonStyle.over = new TextureRegionDrawable(startButtonRegionOver);
        startButtonStyle.down = new TextureRegionDrawable(startButtonRegion);
        startButton = new Button(startButtonStyle);

        TextureRegion backButtonRegion = buttonRegions[6][3];
        TextureRegion backButtonRegionOver = buttonRegions[6][2];
        backButtonStyle.up = new TextureRegionDrawable(backButtonRegion);
        backButtonStyle.over = new TextureRegionDrawable(backButtonRegionOver);
        backButtonStyle.down = new TextureRegionDrawable(backButtonRegion);
        backButton = new Button(backButtonStyle);
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label title = new Label("Controller mapping", titleLabelStyle);
        title.setFontScale(2);

        Label.LabelStyle headStyle = new Label.LabelStyle(font, Color.GREEN);

        Label.LabelStyle actionStyle = new Label.LabelStyle(font, Color.GREEN);
        actionStyle.font.getData().setScale(0.5f);

        Label.LabelStyle inputStyle = new Label.LabelStyle(font, Color.RED);
        inputStyle.font.getData().setScale(0.5f);

        Label actionHeadLabel = new Label("Action", headStyle);
        actionHeadLabel.setFontScale(1);
        Label inputHeadLabel = new Label("Input button", headStyle);
        inputHeadLabel.setFontScale(1);
        Label swordReady = new Label("Prepare sword or put sword back\n(can't climb or shoot arrow if sword is in hand)...", actionStyle);
        Label swordReadyInput = new Label("F", inputStyle);

        Label swingUp = new Label("Attack version 1...", actionStyle);
        Label swingUpInput = new Label("A", inputStyle);

        Label swingDown = new Label("Attack version 2...", actionStyle);
        Label swingDownInput = new Label("S", inputStyle);

        Label swingMiddle = new Label("Attack version 3...", actionStyle);
        Label swingMiddleInput = new Label("D", inputStyle);

        Label runLeft = new Label("Run left...", actionStyle);
        Label runLeftInput = new Label("LEFT", inputStyle);

        Label runRight = new Label("Run right...", actionStyle);
        Label runRightInput = new Label("RIGHT", inputStyle);

        Label jump = new Label("Jump...", actionStyle);
        Label jumpInput = new Label("SPACE", inputStyle);

        Label grab = new Label("Hand grab...", actionStyle);
        Label grabInput = new Label("G", inputStyle);

        Label grabPull = new Label("Hand grab and pull\nor pull up...", actionStyle);
        Label grabPullInput = new Label("UP", inputStyle);

        Label bow = new Label("Bow shoot...", actionStyle);
        Label bowInput = new Label("C", inputStyle);


        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectSound.play();
                mainMenuMusic.stop();
                GreenStory.mainGame.setScreen(new MainMenuScreen());
                dispose();
            }

            ;
        });

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectSound.play();
                mainMenuMusic.stop();
                GreenStory.mainGame.setScreen(new StageLoadingScreen("stage1"));
                dispose();
            }

            ;
        });


        // container.debug();

        table = new Table();
        //  table.debug();
        table.add(title).padTop(10f).colspan(2).expandX().uniform();
        table.row();
        table.add(actionHeadLabel);
        table.add(inputHeadLabel);
        table.row();
        table.add(swordReady);
        table.add(swordReadyInput);
        table.row();
        table.add(swingUp);
        table.add(swingUpInput);
        table.row();
        table.add(swingDown);
        table.add(swingDownInput);
        table.row();
        table.add(swingMiddle);
        table.add(swingMiddleInput);
        table.row();
        table.add(runLeft);
        table.add(runLeftInput);
        table.row();
        table.add(runRight);
        table.add(runRightInput);
        table.row();
        table.add(jump);
        table.add(jumpInput);
        table.row();
        table.add(grab);
        table.add(grabInput);
        table.row();
        table.add(grabPull);
        table.add(grabPullInput);
        table.row();
        table.add(bow);
        table.add(bowInput);
        table.row();
        table.add(backButton).expand();
        table.add(startButton).expand();
        stage.addActor(table);
        table.setFillParent(true);
        table.getColor().a = 0f;
        table.addAction(Actions.fadeIn(3f));

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        font.dispose();
        mainMenuMusic.dispose();
        selectSound.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        MainMenuScreen.positionOfSong = mainMenuMusic.getPosition();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }


}
