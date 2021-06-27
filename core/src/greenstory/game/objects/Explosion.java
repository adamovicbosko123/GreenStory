package greenstory.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Explosion extends Sprite implements Disposable {
    private TextureRegion platform;
    private Animation<TextureRegion> explosionAnimation;
    private TextureRegion explosionRegion;
    private float stateTimer = 0;
    private PlayScreen playScreen;
    private boolean draw = true;
    private boolean readyToDrawCollectible = false;

    public Explosion(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(64, 32);
        Array<TextureRegion> explosionFrames = new Array<>();
        explosionRegion = tiles[8][10];
        explosionFrames.add(explosionRegion);

        explosionRegion = tiles[8][11];
        explosionFrames.add(explosionRegion);

        explosionRegion = tiles[8][12];
        explosionFrames.add(explosionRegion);

        explosionRegion = tiles[8][13];
        explosionFrames.add(explosionRegion);


        explosionAnimation = new Animation<TextureRegion>(0.2f, explosionFrames);

        setRegion(explosionRegion);
        setBounds(0, 0, 64 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(x, y);

    }


    public void update(float delta) {
        stateTimer += delta % 5;
        setRegion(explosionAnimation.getKeyFrame(stateTimer, true));
        if (explosionAnimation.isAnimationFinished(stateTimer)) {
            draw = false;
            readyToDrawCollectible = true;
        }
    }

    public boolean isReadyToDrawCollectible() {
        return readyToDrawCollectible;
    }


    public void dispose() {
        platform.getTexture().dispose();
        explosionRegion.getTexture().dispose();
    }

    public void draw(Batch batch) {
        if (draw)
            super.draw(batch);
    }

}
