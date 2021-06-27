package greenstory.game.objects.enemyobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Thunder extends Sprite implements Menace {
    private PlayScreen playScreen;
    private Vector2 position;
    private float statetimer = 0;
    private TextureRegion platform;
    private TextureRegion thunderRegion;
    private Animation<TextureRegion> thunderAnimation;

    public Thunder(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        Array<TextureRegion> frames = new Array<>();
        position = new Vector2(x, y);
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(32, 64);
        thunderRegion = tiles[13][32];
        frames.add(thunderRegion);
        thunderRegion = tiles[13][33];
        frames.add(thunderRegion);
        thunderRegion = tiles[13][34];
        frames.add(thunderRegion);
        thunderRegion = tiles[13][35];
        frames.add(thunderRegion);
        thunderAnimation = new Animation<TextureRegion>(0.2f, frames);

        setRegion(frames.get(0));
        frames.clear();
        setBounds(0, 0, 32f / GreenStory.PPM, 64 / GreenStory.PPM);
        setPosition(position.x, position.y);
        rotate(180f);
    }

    public void update(float delta) {
        statetimer += delta % 5;
        setRegion(thunderAnimation.getKeyFrame(statetimer, true));
    }

    public void dispose() {
        platform.getTexture().dispose();
        thunderRegion.getTexture().dispose();
    }
}
