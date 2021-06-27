package greenstory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import greenstory.game.GreenStory;
import greenstory.game.objects.HealthBar;
import greenstory.game.objects.icons.ArrowIcon;
import greenstory.game.objects.icons.CanIcon;
import greenstory.game.objects.icons.FistIcon;
import greenstory.game.objects.icons.SwordIcon;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;
import greenstory.game.player.assets.StaminaBar;


public class Hud implements Disposable {

    private Viewport viewport;
    private BitmapFont font;
    private Label healthLabel;
    private Label staminaLabel;
    private Label bowLabel;
    private Label canLabel;
    private Label gameOverLabel;
    private Label countOfEnemiesLabel;
    private Label countOfEnemiesLabelDesc;
    private HealthBar healthBar;
    private StaminaBar staminaBar;
    private Stage stage;
    private PlayScreen playScreen;
    private Player player;
    private Table table;
    private ArrowIcon arrowIcon;
    private CanIcon canIcon;
    private FistIcon fistIcon;
    private SwordIcon swordIcon;

    public Hud(Batch batch, PlayScreen playScreen) {
        this.playScreen = playScreen;
        this.player = playScreen.getPlayer();

        viewport = new ExtendViewport(GreenStory.SCENE_WIDTH * GreenStory.PPM, GreenStory.SCENE_HEIGHT * GreenStory.PPM, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        healthBar = new HealthBar(player, 0.8f);
        staminaBar = new StaminaBar(player, 0.8f);
        arrowIcon = new ArrowIcon();
        canIcon = new CanIcon();
        fistIcon = new FistIcon();
        swordIcon = new SwordIcon();

        font = new BitmapFont(Gdx.files.internal("titlefont.fnt"));

        Label.LabelStyle healthBarLabelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label.LabelStyle staminaBarLabelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label.LabelStyle bowLabelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label.LabelStyle canLabelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label.LabelStyle gameOverStyle = new Label.LabelStyle(font, Color.GREEN);

        healthLabel = new Label("Health", healthBarLabelStyle);
        staminaLabel = new Label("Strength", staminaBarLabelStyle);
        bowLabel = new Label("0x", bowLabelStyle);
        canLabel = new Label("0", canLabelStyle);
        gameOverLabel = new Label("", gameOverStyle);
        countOfEnemiesLabel = new Label("" + playScreen.getCountOfEnemies(), bowLabelStyle);
        countOfEnemiesLabelDesc = new Label("Enemies", bowLabelStyle);
        gameOverLabel.setFontScale(1f);
        bowLabel.setFontScale(0.25f);
        healthLabel.setFontScale(0.25f);
        staminaLabel.setFontScale(0.25f);
        canLabel.setFontScale(0.25f);
        countOfEnemiesLabel.setFontScale(0.25f);
        countOfEnemiesLabelDesc.setFontScale(0.25f);

        table = new Table();
        table.top();
        table.left();
        table.setFillParent(true);
        table.pad(5);
        table.add(healthLabel);
        if (healthBar.getVulnerable() instanceof Player) {
            table.add(healthBar).left().fillX().padRight(40);
        }
        table.add(fistIcon).padRight(40);
        table.add(swordIcon).padRight(40);
        table.add(bowLabel).right();
        table.add(arrowIcon).left().padRight(40);
        table.add(canLabel).right();
        table.add(canIcon).left();
        table.add(countOfEnemiesLabelDesc);
        table.add(countOfEnemiesLabel).padLeft(10);
        table.add().expandX().fillX();

        table.row();
        table.pad(5);
        table.add(staminaLabel);
        table.add(staminaBar).left();
        table.row();
        table.add();
        table.add(gameOverLabel).bottom().expand().colspan(7).padBottom(250);


        stage.addActor(table);
        // table.debug();
    }


    public void update(float delta) {
        stage.act(delta);
        healthBar.update(delta);
        staminaBar.update(delta);
        bowLabel.setText(player.getArrowCount() + "x");
        fistIcon.act(delta);
        swordIcon.act(delta);
        if (player.swordHolding) {
            fistIcon.clearActions();
            fistIcon.addAction(Actions.hide());
            swordIcon.clearActions();
            swordIcon.addAction(Actions.visible(true));
        } else {
            fistIcon.clearActions();
            fistIcon.addAction(Actions.visible(true));
            swordIcon.clearActions();
            swordIcon.addAction(Actions.hide());
        }
        if (player.getEnergyTime() >= 0) {
            canLabel.setText(player.getEnergyTime());
        }

        if (player.getFSM().isInState(PlayerStatesWrapper.PlayerStates.DEATH)) {
            gameOverLabel.setText("Game Over...");
        }
        if (playScreen.getCountOfEnemies() > 0) {
            countOfEnemiesLabel.setText("" + playScreen.getCountOfEnemies());
        } else {
            countOfEnemiesLabel.setText("cleared...");
        }
    }

    public Viewport getViewport() {
        return this.viewport;
    }

    public Stage getStage() {
        return this.stage;
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

}