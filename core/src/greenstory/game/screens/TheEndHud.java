package greenstory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;

public class TheEndHud implements Disposable {

    private Viewport viewport;
    private BitmapFont font;
    private Label theEndLabel;
    private Stage stage;
    private AbstractScreen screen;
    private Player player;
    private Table table;

    public TheEndHud(Batch batch, AbstractScreen screen) {
        this.screen = screen;
        this.player = screen.getPlayer();

        viewport = new ExtendViewport(GreenStory.SCENE_WIDTH * GreenStory.PPM, GreenStory.SCENE_HEIGHT * GreenStory.PPM, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        font = new BitmapFont(Gdx.files.internal("titlefont.fnt"));
        Label.LabelStyle theEndLabelStyle = new Label.LabelStyle(font, Color.GREEN);

        theEndLabel = new Label("", theEndLabelStyle);
        theEndLabel.setFontScale(1f);

        table = new Table();
        table.top();
        table.center();
        table.setFillParent(true);
        table.pad(5);
        table.add().expandX().fillX();

        table.row();
        table.add();
        table.add(theEndLabel).bottom().expand().colspan(7).padBottom(250);


        stage.addActor(table);
        // table.debug();
    }


    private float timer = 0;

    public void update(float delta) {
        stage.act(delta);

        if (player.isMetWithTheWife()) {
            theEndLabel.setText("The End...");
            if ((timer += Gdx.graphics.getDeltaTime()) >= 5) {
                timer = 0;
                Gdx.app.exit();
            }
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
