package greenstory.game.objects.enemyobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Fire extends Sprite implements Menace {
    private TextureRegion platform;
    private Animation<TextureRegion> fireAnimation;
    private TextureRegion fireTile;
    private float stateTimer = 0;
    private PlayScreen playScreen;

    public Fire(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(64, 32);
        Array<TextureRegion> fireFrames = new Array<>();
        fireTile = tiles[8][10];
        fireFrames.add(fireTile);

        fireTile = tiles[8][11];
        fireFrames.add(fireTile);

        fireTile = tiles[8][12];
        fireFrames.add(fireTile);

        fireTile = tiles[8][13];
        fireFrames.add(fireTile);


        fireAnimation = new Animation<TextureRegion>(0.2f, fireFrames);

        setRegion(fireTile);
        setBounds(0, 0, 64 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(x, y);
    }

    public void update(float delta) {
        stateTimer += delta % 5;
        setRegion(fireAnimation.getKeyFrame(stateTimer, true));
    }

    public void dispose() {
        fireTile.getTexture().dispose();
        platform.getTexture().dispose();
    }

}
