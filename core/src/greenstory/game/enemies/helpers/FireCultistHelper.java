package greenstory.game.enemies.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.AnimationHelper;

public class FireCultistHelper {
    private static FireCultistHelper instance;
    private ObjectMap<String, Animation<TextureRegion>> fireCultistAnimations;
    private AnimationHelper animationHelper;
    private AssetManager manager = StageLoadingScreen.enemyAssetManager;
    private FireCultistHelper() {
        fireCultistAnimations = new ObjectMap<>();
        //
        animationHelper = new AnimationHelper(manager, "cultist/firecultist/idle", "idle", 6, 0.1f, Animation.PlayMode.LOOP);
        fireCultistAnimations.put("idle", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/firecultist/attack", "attack", 7, 0.1f, Animation.PlayMode.NORMAL);
        fireCultistAnimations.put("attack", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/firecultist/walk", "walk", 8, 0.1f, Animation.PlayMode.LOOP);
        fireCultistAnimations.put("walk", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/firecultist/hit", "hit", 3, 0.1f, Animation.PlayMode.NORMAL);
        fireCultistAnimations.put("hit", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "cultist/firecultist/death", "death", 12, 0.1f, Animation.PlayMode.NORMAL);
        fireCultistAnimations.put("death", animationHelper.getAnimation());
        //
    }

    public Animation<TextureRegion> get(String name) {
        return fireCultistAnimations.get(name);
    }

    public static FireCultistHelper getInstance() {
        if (instance == null) {
            instance = new FireCultistHelper();
        }
        return instance;
    }

}
