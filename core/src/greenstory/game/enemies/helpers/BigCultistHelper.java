package greenstory.game.enemies.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.AnimationHelper;

public class BigCultistHelper {
    private static BigCultistHelper instance;
    private ObjectMap<String, Animation<TextureRegion>> bigCultistAnimations;
    private AnimationHelper animationHelper;
    private AssetManager manager = StageLoadingScreen.enemyAssetManager;


    private BigCultistHelper() {
        bigCultistAnimations = new ObjectMap<>();
        //
        animationHelper = new AnimationHelper(manager, "cultist/bigcultist/idle", "idle", 8, 0.1f, Animation.PlayMode.LOOP);
        bigCultistAnimations.put("idle", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/bigcultist/attack", "attack", 20, 0.1f, Animation.PlayMode.NORMAL);
        bigCultistAnimations.put("attack", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/bigcultist/walk", "walk", 8, 0.1f, Animation.PlayMode.LOOP);
        bigCultistAnimations.put("walk", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/bigcultist/hit", "hit", 3, 0.05f, Animation.PlayMode.NORMAL);
        bigCultistAnimations.put("hit", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/bigcultist/death", "death", 12, 0.1f, Animation.PlayMode.NORMAL);
        bigCultistAnimations.put("death", animationHelper.getAnimation());
        //

    }

    public Animation<TextureRegion> get(String name) {
        return bigCultistAnimations.get(name);
    }

    public static BigCultistHelper getInstance() {
        if (instance == null) {
            instance = new BigCultistHelper();
        }
        return instance;
    }

}
