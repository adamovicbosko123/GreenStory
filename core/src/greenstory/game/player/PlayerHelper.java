package greenstory.game.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.AnimationHelper;

public class PlayerHelper {
    private ObjectMap<String, Animation<TextureRegion>> playerAnimations;
    private AnimationHelper animationHelper;
    private AssetManager manager = StageLoadingScreen.manager;

    public PlayerHelper() {
        playerAnimations = new ObjectMap<>();
        animationHelper = new AnimationHelper(manager, "player/run", "run", 6, 0.1f, Animation.PlayMode.LOOP);
        playerAnimations.put("run", animationHelper.getAnimation());
        ///////
        animationHelper = new AnimationHelper(manager, "player/runsword", "run", 6, 0.1f, Animation.PlayMode.LOOP);
        playerAnimations.put("runWithSword", animationHelper.getAnimation());
////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/idle", "idle", 4, 0.3f, Animation.PlayMode.LOOP);
        playerAnimations.put("idle", animationHelper.getAnimation());
        //////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/jump", "jump", 4, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("jump", animationHelper.getAnimation());
//////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/falling", "fall", 2, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("fall", animationHelper.getAnimation());
//////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/attack1", "attack1-", 5, 0.12f, Animation.PlayMode.NORMAL);
        playerAnimations.put("attack1", animationHelper.getAnimation());
//////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/attack2", "attack2-", 6, 0.12f, Animation.PlayMode.NORMAL);
        playerAnimations.put("attack2", animationHelper.getAnimation());
//////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/attack3", "attack3-", 5, 0.12f, Animation.PlayMode.NORMAL);
        playerAnimations.put("attack3", animationHelper.getAnimation());
//////////////////////////////////

        animationHelper = new AnimationHelper(manager, "player/idle1", "idle1-", 4, 0.3f, Animation.PlayMode.NORMAL);
        playerAnimations.put("idle1", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/hurt", "hurt", 1, 0.3f, Animation.PlayMode.NORMAL);
        playerAnimations.put("hurt", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/die", "die", 6, 0.2f, Animation.PlayMode.NORMAL);
        playerAnimations.put("die", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/wallSlide", "wallslide", 2, 0.1f, Animation.PlayMode.LOOP);
        playerAnimations.put("slideDownTheWall", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/ladder", "ladder", 4, 0.2f, Animation.PlayMode.LOOP);
        playerAnimations.put("ladderClimb", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/smrslt", "smrslt", 4, 0.1f, Animation.PlayMode.LOOP);
        playerAnimations.put("smrslt", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/bow", "bow", 9, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("bow", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/bowjump", "bow", 6, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("bowjump", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/grab", "grab", 4, 0.2f, Animation.PlayMode.NORMAL);
        playerAnimations.put("grab", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/pullup", "pull", 5, 0.05f, Animation.PlayMode.NORMAL);
        playerAnimations.put("pull", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/airattack1", "air", 4, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("airattack1", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/airattack2", "air", 3, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("airattack2", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/airattack3", "air", 6, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("airattack3", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/swordReady", "swordReady", 4, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("swordReady", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/swordBack", "swordBack", 4, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("swordBack", animationHelper.getAnimation());
///////////////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/punch1", "punch", 8, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("punch1", animationHelper.getAnimation());
        //////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/punch2", "punch", 13, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("punch2", animationHelper.getAnimation());
        //////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/punch3", "punch", 7, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("punch3", animationHelper.getAnimation());
        //////////////////////////////////
        animationHelper = new AnimationHelper(manager, "player/itemTake", "item", 3, 0.1f, Animation.PlayMode.NORMAL);
        playerAnimations.put("item", animationHelper.getAnimation());

    }


    public Animation<TextureRegion> get(String name) {
        return playerAnimations.get(name);
    }
}
