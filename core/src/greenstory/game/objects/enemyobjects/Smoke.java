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
import greenstory.game.player.Player;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Smoke extends Sprite implements  Menace {
    private PlayScreen playScreen;
    private Vector2 position;
    private Vector2 initialPosition;
    private float statetimer = 0;
    private TextureRegion platform;
    private TextureRegion smokeRegion;
    private Animation<TextureRegion> smokeAnimation;
    private Rectangle smokeRectangle;


    public Smoke(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        Array<TextureRegion> frames = new Array<>();
        initialPosition = new Vector2(x, y);
        position = new Vector2(x, y);
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(64, 96);


        smokeRegion = tiles[6][17];
        frames.add(smokeRegion);

        smokeRegion = tiles[6][16];
        frames.add(smokeRegion);

        smokeRegion = tiles[6][18];
        frames.add(smokeRegion);

        smokeRegion = tiles[6][19];
        frames.add(smokeRegion);

        smokeRegion = tiles[6][20];
        frames.add(smokeRegion);

        smokeRegion = tiles[6][21];
        frames.add(smokeRegion);

        smokeRegion = tiles[6][22];
        frames.add(smokeRegion);

        smokeAnimation = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();
        setBounds(0, 0, 64 / GreenStory.PPM, 96 / GreenStory.PPM);
        setPosition(position.x, position.y);
        smokeRectangle = new Rectangle();
        smokeRectangle.set(getBoundingRectangle().x - 0.04f, getBoundingRectangle().y + 0.3f, getBoundingRectangle().width, getBoundingRectangle().height - 0.2f);
    }

    public Rectangle getSmokeRectangle() {
        return this.smokeRectangle;
    }

    public boolean overlapsSmokeRectangle(Player player) {
        if (player.getPlayerRectangle().overlaps(getSmokeRectangle())) {
            return true;
        } else {
            return false;
        }
    }

    private int index = 0;

    public void update(float delta) {
        setScale(2, 2);
        setPosition(position.x - 0.6f, position.y - 0.5f);
        statetimer += delta;
        setRegion(smokeAnimation.getKeyFrame(statetimer, true));
        index = smokeAnimation.getKeyFrameIndex(statetimer);
        if (index == ((Object[])smokeAnimation.getKeyFrames()).length-1 ) {
            statetimer = 0;
        }


    }

    public int getIndex(){
        return index;
    }


  //  private ShapeRenderer renderer = new ShapeRenderer();

    public void draw(Batch batch) {
        super.draw(batch);
        batch.end();
       // renderer.begin(ShapeRenderer.ShapeType.Line);
       // renderer.setProjectionMatrix(batch.getProjectionMatrix());
       // renderer.rect(smokeRectangle.x, smokeRectangle.y, smokeRectangle.width, smokeRectangle.height);
       // renderer.end();
        batch.begin();
    }

    private Rectangle lightningRectangle() {
        return this.getBoundingRectangle();
    }

    public void dispose() {
       // renderer.dispose();
        platform.getTexture().dispose();
        smokeRegion.getTexture().dispose();
    }

}
