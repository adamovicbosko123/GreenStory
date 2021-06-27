package greenstory.game.enemies.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import greenstory.game.GreenStory;
import greenstory.game.utilities.Vulnerable;
import greenstory.game.screens.StageLoadingScreen;

public class Blood extends Sprite {
    private TextureRegion platform;
    private Array<TextureRegion> bloodFrames = new Array<>();
    private TextureRegion blood;
    private Animation<TextureRegion> bloodAnimation;
    private float statetimer = 0;

    public Blood() {
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] fallingDropRegions = platform.split(64, 96);
        blood = fallingDropRegions[1][16];
        blood.setRegionWidth(64);
        blood.setRegionHeight(64);
        bloodFrames.add(blood);

        blood = fallingDropRegions[1][17];
        blood.setRegionWidth(64);
        blood.setRegionHeight(64);
        bloodFrames.add(blood);

        blood = fallingDropRegions[1][18];
        blood.setRegionWidth(64);
        blood.setRegionHeight(64);
        bloodFrames.add(blood);

        blood = fallingDropRegions[1][19];
        blood.setRegionWidth(64);
        blood.setRegionHeight(64);
        bloodFrames.add(blood);

        blood = fallingDropRegions[1][20];
        blood.setRegionWidth(64);
        blood.setRegionHeight(64);
        bloodFrames.add(blood);

        blood = fallingDropRegions[1][21];
        blood.setRegionWidth(64);
        blood.setRegionHeight(64);
        bloodFrames.add(blood);
        bloodAnimation = new Animation<TextureRegion>(0.1f, bloodFrames, Animation.PlayMode.NORMAL);
        bloodFrames.clear();
        setBounds(0, 0, 32f / GreenStory.PPM, 32f / GreenStory.PPM);
    }

    public void update(float delta) {
        statetimer += delta % 5;
        setRegion(bloodAnimation.getKeyFrame(statetimer, false));
    }
}
