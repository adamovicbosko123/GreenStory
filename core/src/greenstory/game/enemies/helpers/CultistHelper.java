package greenstory.game.enemies.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.AnimationHelper;

public class CultistHelper {
    private static CultistHelper instance;
    private ObjectMap<String, Animation<TextureRegion>> cultistAnimations;
    private AnimationHelper animationHelper;
    private AssetManager manager = StageLoadingScreen.enemyAssetManager;

    private CultistHelper() {
        cultistAnimations = new ObjectMap<>();
        //
        animationHelper = new AnimationHelper(manager, "cultist/cultist/idle", "cultist", 6, 0.1f, Animation.PlayMode.LOOP);
        cultistAnimations.put("idle", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/cultist/attack", "attack", 7, 0.1f, Animation.PlayMode.NORMAL);
        cultistAnimations.put("attack", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/cultist/walk", "walk", 8, 0.1f, Animation.PlayMode.LOOP);
        cultistAnimations.put("walk", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/cultist/hit", "hit", 3, 0.1f, Animation.PlayMode.NORMAL);
        cultistAnimations.put("hit", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/cultist/death", "death", 12, 0.1f, Animation.PlayMode.NORMAL);
        cultistAnimations.put("death", animationHelper.getAnimation());
        //

    }

    public Animation<TextureRegion> get(String name) {
        return cultistAnimations.get(name);
    }

    public static CultistHelper getInstance() {
        if (instance == null) {
            instance = new CultistHelper();
        }
        return instance;
    }

}
