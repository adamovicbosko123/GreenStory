package greenstory.game.objects.enemyobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Lightning extends Sprite implements Menace {
    private PlayScreen playScreen;
    private Vector2 position;
    private Vector2 initialPosition;
    private float statetimer = 0;
    private TextureRegion platform;
    private TextureRegion lightningRegion;
    private Animation<TextureRegion> lightningAnimation;


    public Lightning(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        Array<TextureRegion> frames = new Array<>();
        initialPosition = new Vector2(x, y);
        position = new Vector2(x, y);
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(32, 736);
        lightningRegion = tiles[1][32];
        lightningRegion.setRegionHeight(64);
        frames.add(lightningRegion);
        lightningRegion = tiles[1][33];
        lightningRegion.setRegionHeight(64);

        frames.add(lightningRegion);
        lightningRegion = tiles[1][34];
        lightningRegion.setRegionHeight(64);

        frames.add(lightningRegion);
        lightningRegion = tiles[1][35];
        lightningRegion.setRegionHeight(64);

        frames.add(lightningRegion);
        lightningAnimation = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();
        setBounds(0, 0, 32f / GreenStory.PPM, 64 / GreenStory.PPM);
        setPosition(position.x, position.y);
        rotate(180f);
    }

    public void update(float delta) {
        statetimer += delta % 5;
        setRegion(lightningAnimation.getKeyFrame(statetimer, true));
        strikeDown();

    }


    public void strikeDown() {
        setPosition(position.x, position.y -= 0.05f);
        if (position.y <= -3) {
            setPosition(initialPosition.x, initialPosition.y);
            position.set(initialPosition.x, initialPosition.y);
        }
    }

    // private ShapeRenderer renderer = new ShapeRenderer();

    public void draw(Batch batch) {
        super.draw(batch);
        batch.end();
        //renderer.begin(ShapeRenderer.ShapeType.Line);
        // renderer.setProjectionMatrix(batch.getProjectionMatrix());
        // renderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
        //   renderer.end();
        batch.begin();
    }

    private Rectangle lightningRectangle() {
        return this.getBoundingRectangle();
    }

    public void dispose() {
        //renderer.dispose();
        platform.getTexture().dispose();
        lightningRegion.getTexture().dispose();
    }

}
