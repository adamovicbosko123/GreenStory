package greenstory.game.objects.harmlesobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.screens.AbstractScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Aureol extends Sprite implements Harmles {
    private AbstractScreen screen;
    private Vector2 position;
    private float statetimer = 0;
    private TextureRegion platform;
    private TextureRegion aureolRegion;
    private Animation<TextureRegion> aureolAnimation;

    public Aureol(AbstractScreen screen, float x, float y) {
        this.screen = screen;
        Array<TextureRegion> frames = new Array<>();
        position = new Vector2(x, y);
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(64, 1696);

        aureolRegion = tiles[1][10];
        aureolRegion.setRegionHeight(64);
        frames.add(aureolRegion);

        aureolRegion = tiles[1][11];
        aureolRegion.setRegionHeight(64);
        frames.add(aureolRegion);

        aureolRegion = tiles[1][12];
        aureolRegion.setRegionHeight(64);
        frames.add(aureolRegion);

        aureolRegion = tiles[1][13];
        aureolRegion.setRegionHeight(64);
        frames.add(aureolRegion);

        aureolRegion = tiles[1][14];
        aureolRegion.setRegionHeight(64);
        frames.add(aureolRegion);

        aureolAnimation = new Animation<TextureRegion>(0.2f, frames);

        // setRegion(frames.get(0));
        frames.clear();
        setBounds(0, 0, 32f / GreenStory.PPM, 64 / GreenStory.PPM);
        setPosition(position.x, position.y);
    }

    private boolean animationActive = false;
    private int counter = 0;

    public void resetHealerCounter() {
        counter = 0;
    }

    public void update(float delta) {
        if (counter < 1) {
            if (animationActive) {
                statetimer += delta % 5;
                setRegion(aureolAnimation.getKeyFrame(statetimer, false));

                if (aureolAnimation.isAnimationFinished(statetimer)) {
                    counter += 1;
                    animationActive = false;
                    statetimer = 0;
                    healPlayer(screen.getPlayer());
                }
            }
        }

    }


    public void startAureol() {
        animationActive = true;
    }

    public boolean isAnimationActive() {
        return animationActive;
    }

    public void healPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setStrength(player.getMaxStrength());
    }

    public void draw(Batch batch) {
        if (animationActive && counter < 1)
            super.draw(batch);
    }


    public void dispose() {
        platform.getTexture().dispose();
        aureolRegion.getTexture().dispose();
    }


}
