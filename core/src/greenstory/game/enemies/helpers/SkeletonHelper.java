package greenstory.game.enemies.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.AnimationHelper;

public class SkeletonHelper {
    private ObjectMap<String, Animation<TextureRegion>> skeletonAnimations;
    private TextureRegion idle;
    private static SkeletonHelper instance;
    private AssetManager manager = StageLoadingScreen.enemyAssetManager;
    private AnimationHelper animationHelper;


    private SkeletonHelper() {
        skeletonAnimations = new ObjectMap<>();
        //Makes idle frame, only one frame!
        Texture texture = new Texture("skeleton/idle.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int count = 11;
        TextureRegion textureRegion = new TextureRegion(texture);
        int frameWidth = textureRegion.getRegionWidth() / count;
        int frameHeight = textureRegion.getRegionHeight();
        TextureRegion[][] textureRegions = textureRegion.split(frameWidth, frameHeight);
        idle = textureRegions[0][0];
        //
        animationHelper = new AnimationHelper(manager, "skeleton/idle.png", 11, 0.3f, Animation.PlayMode.LOOP);
        skeletonAnimations.put("idle", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "skeleton/walk.png", 13, 0.06f, Animation.PlayMode.LOOP);
        skeletonAnimations.put("walk", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "skeleton/react.png", 4, 0.1f, Animation.PlayMode.NORMAL);
        skeletonAnimations.put("react", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "skeleton/attack.png", 18, 0.025f, Animation.PlayMode.NORMAL);
        skeletonAnimations.put("attack", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "skeleton/hit.png", 8, 0.1f, Animation.PlayMode.NORMAL);
        skeletonAnimations.put("hit", animationHelper.getAnimation());
        //
        animationHelper = new AnimationHelper(manager, "skeleton/dead.png", 15, 0.1f, Animation.PlayMode.NORMAL);
        skeletonAnimations.put("death", animationHelper.getAnimation());
        //

    }


    public TextureRegion getIdle() {
        return idle;
    }


    public Animation<TextureRegion> get(String name) {
        return skeletonAnimations.get(name);
    }

    public static SkeletonHelper getInstance() {
        if (instance == null) {
            instance = new SkeletonHelper();
        }
        return instance;
    }

}
