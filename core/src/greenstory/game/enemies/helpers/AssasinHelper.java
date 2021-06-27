package greenstory.game.enemies.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.AnimationHelper;

public class AssasinHelper {
    private static AssasinHelper instance;
    private ObjectMap<String, Animation<TextureRegion>> assasinAnimations;
    private AnimationHelper animationHelper;
    private AssetManager manager = StageLoadingScreen.enemyAssetManager;

    private AssasinHelper() {
        assasinAnimations = new ObjectMap<>();

        assasinAnimations = new ObjectMap<>();
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/idle", "assassin", 8, 0.2f, Animation.PlayMode.LOOP);
        assasinAnimations.put("idle", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/ambush", "ambush", 13, 0.1f, Animation.PlayMode.NORMAL);
        assasinAnimations.put("ambush", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/attack", "attack", 13, 0.08f, Animation.PlayMode.LOOP);
        assasinAnimations.put("attack", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/run", "run", 8, 0.1f, Animation.PlayMode.LOOP);
        assasinAnimations.put("run", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/blinkRun", "bRun", 8, 0.1f, Animation.PlayMode.LOOP);
        assasinAnimations.put("blinkRun", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/vanish", "vanish", 9, 0.2f, Animation.PlayMode.NORMAL);
        assasinAnimations.put("vanish", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/hit", "hit", 3, 0.2f, Animation.PlayMode.NORMAL);
        assasinAnimations.put("hit", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/assassin/death", "death", 16, 0.1f, Animation.PlayMode.NORMAL);
        assasinAnimations.put("death", animationHelper.getAnimation());
        //

    }

    public Animation<TextureRegion> get(String name) {
        return assasinAnimations.get(name);
    }

    public static AssasinHelper getInstance() {
        if (instance == null) {
            instance = new AssasinHelper();
        }
        return instance;
    }

}
