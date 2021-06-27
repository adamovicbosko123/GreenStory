package greenstory.game.objects.harmlesobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import greenstory.game.GreenStory;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Healer extends Sprite implements Harmles {
    private PlayScreen playScreen;
    private Vector2 position;
    private float statetimer = 0;
    private TextureRegion platform;
    private TextureRegion healerRegion;
    private Animation<TextureRegion> healerAnimation;

    public Healer(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        Array<TextureRegion> frames = new Array<>();
        position = new Vector2(x, y);
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(64, 64);
        healerRegion = tiles[28][10];
        frames.add(healerRegion);
        healerRegion = tiles[28][11];
        frames.add(healerRegion);
        healerRegion = tiles[28][12];
        frames.add(healerRegion);
        healerRegion = tiles[28][13];
        frames.add(healerRegion);
        healerRegion = tiles[28][14];
        frames.add(healerRegion);
        healerRegion = tiles[28][15];
        frames.add(healerRegion);
        healerRegion = tiles[28][16];
        frames.add(healerRegion);
        healerAnimation = new Animation<TextureRegion>(0.2f, frames);
        setRegion(frames.get(0));
        frames.clear();
        setBounds(0, 0, 64f / GreenStory.PPM, 64f / GreenStory.PPM);
        setPosition(position.x - 0.25f, position.y);

    }

    public void update(float delta) {
        statetimer += delta % 5;
        setRegion(healerAnimation.getKeyFrame(statetimer, true));
        sensorRectangle.set(getBoundingRectangle().x + 0.4f, getBoundingRectangle().y, getBoundingRectangle().width - 0.8f, getBoundingRectangle().height - 0.75f);
    }

  //  private ShapeRenderer render = new ShapeRenderer();
    private Rectangle sensorRectangle = new Rectangle();

    public void draw(Batch batch) {
        super.draw(batch);
        // batch.end();
        // render.setProjectionMatrix(batch.getProjectionMatrix());
        // render.begin(ShapeRenderer.ShapeType.Line);
        // render.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
        // render.rect(sensorRectangle.x, sensorRectangle.y, sensorRectangle.width, sensorRectangle.height);
        //    render.end();
        //  batch.begin();
    }

    public Rectangle getSensorRectangle() {
        return sensorRectangle;
    }


    public void dispose() {
        platform.getTexture().dispose();
        healerRegion.getTexture().dispose();
    }
}
