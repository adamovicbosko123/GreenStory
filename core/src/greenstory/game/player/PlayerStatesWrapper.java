package greenstory.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import greenstory.game.objects.harmlesobjects.Barrel;
import greenstory.game.player.assets.Bow;
import greenstory.game.screens.StageLoadingScreen;

public class PlayerStatesWrapper {


    public static Animation<TextureRegion> runAnimation;
    private static Animation<TextureRegion> runWithSwordAnimation;
    private static Animation<TextureRegion> jumpAnimation;
    private static Animation<TextureRegion> idleAnimation;
    public static Animation<TextureRegion> idle1Animation;
    private static Animation<TextureRegion> fallAnimation;
    private static Animation<TextureRegion> attack1Animation;
    private static Animation<TextureRegion> attack2Animation;
    private static Animation<TextureRegion> attack3Animation;
    private static Animation<TextureRegion> swordReadyAnimation;
    private static Animation<TextureRegion> swordBackAnimation;
    private static Animation<TextureRegion> hurtAnimation;
    private static Animation<TextureRegion> dieAnimation;
    private static Animation<TextureRegion> slideDownWallAnimation;
    private static Animation<TextureRegion> ladderClimbAnimation;
    private static Animation<TextureRegion> smrsltAnimation;
    private static Animation<TextureRegion> bowAnimation;
    private static Animation<TextureRegion> bowjumpAnimation;
    private static Animation<TextureRegion> grabAnimation;
    private static Animation<TextureRegion> pullAnimation;
    private static Animation<TextureRegion> airattack1Animation;
    private static Animation<TextureRegion> airattack2Animation;
    private static Animation<TextureRegion> airattack3Animation;
    private static Animation<TextureRegion> itemAnimation;
    private static Animation<TextureRegion> punch1Animation;
    private static Animation<TextureRegion> punch2Animation;
    private static Animation<TextureRegion> punch3Animation;
    private PlayerHelper playerHelper;
    /// private final Player player;

    public PlayerStatesWrapper() {
        // this.player = player;
        playerHelper = new PlayerHelper();
        runAnimation = playerHelper.get("run");
        runWithSwordAnimation = playerHelper.get("runWithSword");
        jumpAnimation = playerHelper.get("jump");
        idleAnimation = playerHelper.get("idle");
        idle1Animation = playerHelper.get("idle1");
        fallAnimation = playerHelper.get("fall");
        attack1Animation = playerHelper.get("attack1");
        attack2Animation = playerHelper.get("attack2");
        attack3Animation = playerHelper.get("attack3");
        swordReadyAnimation = playerHelper.get("swordReady");
        swordBackAnimation = playerHelper.get("swordBack");
        hurtAnimation = playerHelper.get("hurt");
        dieAnimation = playerHelper.get("die");
        slideDownWallAnimation = playerHelper.get("slideDownTheWall");
        ladderClimbAnimation = playerHelper.get("ladderClimb");
        smrsltAnimation = playerHelper.get("smrslt");
        bowAnimation = playerHelper.get("bow");
        bowjumpAnimation = playerHelper.get("bowjump");
        grabAnimation = playerHelper.get("grab");
        pullAnimation = playerHelper.get("pull");
        airattack1Animation = playerHelper.get("airattack1");
        airattack2Animation = playerHelper.get("airattack2");
        airattack3Animation = playerHelper.get("airattack3");
        swordReadyAnimation = playerHelper.get("swordReady");
        itemAnimation = playerHelper.get("item");
        punch1Animation = playerHelper.get("punch1");
        punch2Animation = playerHelper.get("punch2");
        punch3Animation = playerHelper.get("punch3");

    }

    public enum PlayerStates implements State<Player> {

        IDLE() {
            @Override
            public void enter(Player entity) {

            }

            @Override
            public void update(Player player) {
                if (player.swordHolding) {
                    player.setRegion(idle1Animation.getKeyFrame(player.getStateTimer(), true));
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(ATTACK1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(ATTACK2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(ATTACK3);
                    }
                } else {
                    player.setRegion(idleAnimation.getKeyFrame(player.getStateTimer(), true));
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(PUNCH1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(PUNCH2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(PUNCH3);
                    }
                }
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }


                if ((player.getPlayerBody().getLinearVelocity().x > 0 || player.getPlayerBody().getLinearVelocity().x < 0) && player.playerBody.getLinearVelocity().y == 0) {
                    player.getFSM().changeState(RUNNING);
                }
                if (player.getPlayerBody().getLinearVelocity().y > 0) {
                    player.getFSM().changeState(JUMPING);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }
                if (player.getLinearVelocity().y < 0) {
                    player.getFSM().changeState(FALLING);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.C) && player.getArrowCount() > 0 && !player.swordHolding) {
                    player.getFSM().changeState(BOW);
                }
                if (player.swordHolding && !player.swordAlreadyHolding) {
                    player.getFSM().changeState(SWORD_READY);
                }

                if (!player.swordHolding && player.swordAlreadyHolding) {
                    player.getFSM().changeState(SWORD_BACK);
                }


            }

            @Override
            public void exit(Player entity) {
                if (entity.getDirection() < 0) {
                    entity.flip(true, false);
                }
            }


            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        },

        RUNNING() {
            @Override
            public void enter(Player entity) {
            }

            @Override
            public void update(Player player) {

                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }


                if (player.swordHolding) {
                    player.setRegion(runWithSwordAnimation.getKeyFrame(player.getStateTimer(), true));
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(ATTACK1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(ATTACK2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(ATTACK3);
                    }
                } else {

                    player.setRegion(runAnimation.getKeyFrame(player.getStateTimer(), true));

                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(PUNCH1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(PUNCH2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(PUNCH3);
                    }
                }
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }

                if (player.getPlayerBody().getLinearVelocity().x == 0) {
                    player.getFSM().changeState(IDLE);
                }
                if (player.getPlayerBody().getLinearVelocity().y > 0) {
                    player.getFSM().changeState(JUMPING);
                }
                if (player.getLinearVelocity().y < 0) {
                    player.getFSM().changeState(FALLING);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

                if (player.swordHolding && !player.swordAlreadyHolding) {
                    player.getFSM().changeState(SWORD_READY);
                }
                if (!player.swordHolding && player.swordAlreadyHolding) {
                    player.getFSM().changeState(SWORD_BACK);
                }

            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        },

        JUMPING() {
            @Override
            public void enter(Player player) {

            }

            @Override
            public void update(Player player) {
                player.setRegion(jumpAnimation.getKeyFrame(player.getStateTimer(), false));

                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }

                if (player.playerBody.getLinearVelocity().y < 0) {
                    player.getFSM().changeState(FALLING);
                }
                if (player.swordHolding) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(AIR_ATTACK2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(AIR_ATTACK1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(AIR_ATTACK3);
                    }
                } else {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(PUNCH1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(PUNCH2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(PUNCH3);
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.C) && player.getArrowCount() > 0 && !player.swordHolding) {
                    player.getFSM().changeState(BOW_JUMP);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }
                if (player.playerBody.getLinearVelocity().y == 0.0f) {
                    player.getFSM().changeState(IDLE);
                }


            }

            @Override
            public void exit(Player entity) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        },

        FALLING() {
            @Override
            public void enter(Player player) {

            }

            @Override
            public void update(Player player) {
                player.setRegion(fallAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }

                if (player.swordHolding) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(AIR_ATTACK2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(AIR_ATTACK1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(AIR_ATTACK3);
                    }
                } else {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        player.getFSM().changeState(PUNCH1);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        player.getFSM().changeState(PUNCH2);
                    }
                    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        player.getFSM().changeState(PUNCH3);
                    }
                }
                if (player.playerBody.getLinearVelocity().y == 0.0f) {
                    player.getFSM().changeState(IDLE);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }
                if (player.isSlidingDownTheWall()) {
                    player.getFSM().changeState(SLIDING_DOWN_THE_WALL);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.C) && player.getArrowCount() > 0 && !player.swordHolding) {
                    player.getFSM().changeState(BOW_JUMP);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.G) && player.getDirection() > 0 && player.isRightHandGrabbed() && !player.swordHolding) {
                    player.grabRight();
                    player.getFSM().changeState(RIGHT_GRAB);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.G) && player.getDirection() < 0 && player.isLeftHandGrabbed() && !player.swordHolding) {
                    player.grabLeft();
                    player.getFSM().changeState(LEFT_GRAB);
                }

            }

            @Override
            public void exit(Player entity) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        },

        ATTACK1() {

            int counter = 0;
            int swishCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRunningForce(1);
                player.setRegion(attack1Animation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (swishCounter < 1 && attack1Animation.getKeyFrameIndex(player.getStateTimer()) == 2) {
                    long id = player.swish.play();
                    player.swish.setVolume(id, 0.03f);
                    player.swish.setLooping(id, false);
                    swishCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }

                // long id = player.barrelImpact.play();
                //player.barrelImpact.setLooping(id, false);
                // player.barrelImpact.setVolume(id, 0.1f);

                if (attack1Animation.getKeyFrameIndex(player.getStateTimer()) == 3 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) {
                    player.punchBody();
                }


                if (attack1Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;


                    if (counter == 1) {

                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                swishCounter = 0;
                player.setRunningForce(25);
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        },

        ATTACK2() {
            private int counter = 0;
            private int swishCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRunningForce(1);
                player.setRegion(attack2Animation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (swishCounter < 1 && attack2Animation.getKeyFrameIndex(player.getStateTimer()) == 3) {
                    long id = player.swish.play();
                    player.swish.setVolume(id, 0.03f);
                    player.swish.setLooping(id, false);
                    swishCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }

                if (attack2Animation.getKeyFrameIndex(player.getStateTimer()) == 3 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) {
                    player.punchBody();
                }

                if (attack2Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;
                    player.playerBody.setLinearVelocity(0, player.getLinearVelocity().y);
                    if (counter == 1) {
                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                swishCounter = 0;
                player.setRunningForce(25);
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        },

        ATTACK3() {
            private int counter = 0;
            private int swishCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }


            @Override
            public void update(Player player) {
                player.setRunningForce(40);
                player.setRegion(attack3Animation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (swishCounter < 1 && attack3Animation.getKeyFrameIndex(player.getStateTimer()) == 2) {
                    long id = player.swish.play();
                    player.swish.setVolume(id, 0.03f);
                    player.swish.setLooping(id, false);
                    swishCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }

                if (attack3Animation.getKeyFrameIndex(player.getStateTimer()) >= 2 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) {
                    player.punchBody();
                }
                if (attack3Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;

                    if (counter == 1) {
                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }

                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                player.setRunningForce(25);
                swishCounter = 0;
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, HURT() {
            float time = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRegion(hurtAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                time += Gdx.graphics.getDeltaTime();
                if (time >= 0.3f) {
                    player.getFSM().changeState(IDLE);
                    time = 0;
                    player.setHit(false);
                }
                if (player.getHealth() <= 0) {
                    player.getFSM().changeState(DEATH);
                }

            }

            @Override
            public void exit(Player player) {
                player.damageHealth();
            }

            @Override
            public boolean onMessage(Player player, Telegram telegram) {
                return false;
            }
        }, DEATH() {
            @Override
            public void enter(Player player) {
                    //long id = player.getGameOverSound().play();
                  //  player.getGameOverSound().setLooping(id, false);
                   // player.getGameOverSound().setVolume(id, 0.3f);

            }

            @Override
            public void update(Player player) {
                player.setRegion(dieAnimation.getKeyFrame(player.getStateTimer(), false));
                player.arrows.clear();
                player.setHealth(0);
                player.setStrength(0);

            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }

        }, LADDER_CLIMB() {
            float stateTimer = 0;

            @Override
            public void enter(Player player) {
                stateTimer = player.getStateTimer();
            }

            @Override
            public void update(Player player) {
                //player.playerBody.setGravityScale(0);
                player.setRegion(ladderClimbAnimation.getKeyFrame(0.2f));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    stateTimer += Gdx.graphics.getDeltaTime();
                    player.setRegion(ladderClimbAnimation.getKeyFrame(stateTimer, false));
                    player.getPlayerBody().setLinearVelocity(0, 3f);
                    //  player.getPlayerBody().setGravityScale(0);
                } else {
                    player.playerBody.setLinearVelocity(0, 0);
                }
                if (!player.isPlayerOnLadder()) {
                    player.getFSM().changeState(IDLE);
                    // player.playerBody.setGravityScale(1);
                }


            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, SLIDING_DOWN_THE_WALL() {
            @Override
            public void enter(Player player) {
            }

            @Override
            public void update(Player player) {
                player.setRegion(slideDownWallAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (player.playerBody.getLinearVelocity().y == 0) {
                    player.getFSM().changeState(IDLE);
                }
                if (player.getPlayerBody().getLinearVelocity().y < 0 && !player.isSlidingDownTheWall()) {
                    player.getFSM().changeState(FALLING);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    player.getFSM().changeState(SMRSLT);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.G) && player.getDirection() > 0 && player.isRightHandGrabbed() && !player.swordHolding) {
                    player.grabRight();
                    player.getFSM().changeState(RIGHT_GRAB);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.G) && player.getDirection() < 0 && player.isLeftHandGrabbed() && !player.swordHolding) {
                    player.grabLeft();
                    player.getFSM().changeState(LEFT_GRAB);
                }
            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, SMRSLT() {
            float time = 0;

            @Override
            public void enter(Player player) {
                time = 0;
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRegion(smrsltAnimation.getKeyFrame(player.getStateTimer(), true));
                if (player.getDirection() < 0) {
                    player.playerBody.applyForceToCenter(new Vector2(3f, 4), true);
                } else {
                    player.playerBody.applyForceToCenter(new Vector2(-3f, 4), true);
                }

                if ((time += Gdx.graphics.getDeltaTime()) >= 0.5f) {
                    player.getFSM().changeState(FALLING);
                }
                if (player.swordHolding) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                        time = 1;
                        player.getFSM().changeState(ATTACK1);
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        time = 1;
                        player.getFSM().changeState(ATTACK2);
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                        time = 1;
                        player.getFSM().changeState(ATTACK3);
                    }
                }

            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, BOW() {
            private float time = 0;
            private int counter = 0;
            private float bowFireCounter = 0;
            Bow bow;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();

            }

            @Override
            public void update(Player player) {
                player.setRegion(bowAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (bowFireCounter < 1 && bowAnimation.getKeyFrameIndex(player.getStateTimer()) == 6) {
                    long id = player.bow.play();
                    player.bow.setVolume(id, 0.05f);
                    player.bow.setLooping(id, false);
                    bowFireCounter++;
                }

                time += Gdx.graphics.getDeltaTime();
                if (time >= 0.9f) {
                    player.getFSM().changeState(IDLE);
                    time = 0;
                }

                if (bowAnimation.getKeyFrameIndex(player.getStateTimer()) == 7) {
                    counter++;
                    if (counter == 1) {
                        player.setArrowCount(player.getArrowCount() - 1);
                        player.arrows.add(bow = new Bow(player.getPlayerBody().getWorld(), player));
                    }

                }


                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                bowFireCounter = 0;

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, BOW_JUMP() {
            private float time = 0;
            private int counter = 0;
            private Bow bow;
            private int bowFireCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();

            }

            @Override
            public void update(Player player) {
                player.setRegion(bowjumpAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (bowFireCounter < 1 && bowAnimation.getKeyFrameIndex(player.getStateTimer()) == 3) {
                    long id = player.bow.play();
                    player.bow.setVolume(id, 0.05f);
                    player.bow.setLooping(id, false);
                    bowFireCounter++;
                }

                time += Gdx.graphics.getDeltaTime();
                if (time >= 0.6f) {
                    player.getFSM().changeState(IDLE);
                    time = 0;
                }

                if (bowjumpAnimation.getKeyFrameIndex(player.getStateTimer()) == 4) {
                    counter++;
                    if (counter == 1) {
                        player.setArrowCount(player.getArrowCount() - 1);
                        player.arrows.add(bow = new Bow(player.getPlayerBody().getWorld(), player));
                    }

                }

                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }


            }

            @Override
            public void exit(Player player) {
                counter = 0;
                bowFireCounter = 0;
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, RIGHT_GRAB() {
            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRegion(grabAnimation.getKeyFrame(player.getStateTimer(), true));

                if (!player.isRightHandGrabbed()) {
                    player.getFSM().changeState(IDLE);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    player.getFSM().changeState(PULL_UP);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, LEFT_GRAB() {
            @Override
            public void enter(Player player) {
                //player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRegion(grabAnimation.getKeyFrame(player.getStateTimer(), true));
                player.flip(true, false);
                if (!player.isLeftHandGrabbed()) {
                    player.getFSM().changeState(IDLE);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    player.getFSM().changeState(PULL_UP);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }
            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, PULL_UP() {
            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {

                player.setRegion(pullAnimation.getKeyFrame(player.getStateTimer(), true));
                if (pullAnimation.isAnimationFinished(player.getStateTimer())) {
                    player.getFSM().changeState(IDLE);
                }
                if (player.getDirection() > 0)
                    player.playerBody.setTransform(player.playerBody.getPosition().x + 0.01f, player.playerBody.getPosition().y + 0.1f, 0);
                else {
                    player.flip(true, false);
                    player.playerBody.setTransform(player.playerBody.getPosition().x - 0.01f, player.playerBody.getPosition().y + 0.1f, 0);

                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, AIR_ATTACK1() {

            private int counter = 0;
            private int swishCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {

                player.setRegion(airattack1Animation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (swishCounter < 1 && airattack1Animation.getKeyFrameIndex(player.getStateTimer()) == 1) {
                    long id = player.swish.play();
                    player.swish.setVolume(id, 0.03f);
                    player.swish.setLooping(id, false);
                    swishCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                        player.punchBody();
                    }
                }

                if (airattack1Animation.getKeyFrameIndex(player.getStateTimer()) == 1 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) {
                    player.punchBody();
                }


                if (airattack1Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;

                    if (counter == 1) {

                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                swishCounter = 0;
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }

        }, AIR_ATTACK2() {
            private int counter = 0;
            private int swishCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRegion(airattack2Animation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (swishCounter < 1 && airattack2Animation.getKeyFrameIndex(player.getStateTimer()) == 0) {
                    long id = player.swish.play();
                    player.swish.setVolume(id, 0.03f);
                    player.swish.setLooping(id, false);
                    swishCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }

                if (airattack2Animation.getKeyFrameIndex(player.getStateTimer()) == 0 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) {
                    player.punchBody();
                }

                if (airattack2Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;
                    if (counter == 1) {
                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                swishCounter = 0;
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        },

        AIR_ATTACK3() {
            private int counter = 0;
            private int swishCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }


            @Override
            public void update(Player player) {

                player.setRegion(airattack3Animation.getKeyFrame(player.getStateTimer(), false));

                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (swishCounter < 1 && airattack3Animation.getKeyFrameIndex(player.getStateTimer()) == 3) {
                    long id = player.swish.play();
                    player.swish.setVolume(id, 0.03f);
                    player.swish.setLooping(id, false);
                    swishCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }
                if (airattack3Animation.getKeyFrameIndex(player.getStateTimer()) == 3 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) {
                    player.punchBody();
                }
                if (airattack3Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;

                    if (counter == 1) {
                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }

                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                swishCounter = 0;
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, SWORD_READY() {
            float time = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRunningForce(1);
                player.setRegion(swordReadyAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }
                if ((time += Gdx.graphics.getDeltaTime()) >= 0.4f) {
                    player.getFSM().changeState(IDLE);
                    time = 0;
                    player.swordAlreadyHolding = true;
                }
            }

            @Override
            public void exit(Player player) {
                player.setRunningForce(25);
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, SWORD_BACK() {
            float time = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRunningForce(1);
                player.setRegion(swordBackAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }
                if ((time += Gdx.graphics.getDeltaTime()) >= 0.4f) {
                    player.getFSM().changeState(IDLE);
                    time = 0;
                    player.swordHolding = false;
                    player.swordAlreadyHolding = false;
                }
            }

            @Override
            public void exit(Player player) {
                player.setRunningForce(25);
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, ITEM() {
            float time = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();

            }

            @Override
            public void update(Player player) {
                player.setRegion(itemAnimation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }

                time += Gdx.graphics.getDeltaTime();
                if (time >= 0.3f) {
                    player.getFSM().changeState(IDLE);
                    time = 0;
                }

                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {

            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, PUNCH1() {

            private int counter = 0;
            private int punchCounter = 0;
            private int punch1Counter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRunningForce(1);
                player.setRegion(punch1Animation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }

                if ((punchCounter < 1 && punch1Animation.getKeyFrameIndex(player.getStateTimer()) == 2)) {
                    long id = player.punch.play();
                    player.punch.setVolume(id, 0.02f);
                    player.punch.setLooping(id, false);
                    punchCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }
                if ((punch1Counter < 1 && punch1Animation.getKeyFrameIndex(player.getStateTimer()) == 5)) {
                    long id = player.punch.play();
                    player.punch.setVolume(id, 0.02f);
                    player.punch.setLooping(id, false);
                    punch1Counter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }

                if ((punch1Animation.getKeyFrameIndex(player.getStateTimer()) == 2 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) || (punch1Animation.getKeyFrameIndex(player.getStateTimer()) == 5 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch()))) {
                    player.punchBody();
                }


                if (punch1Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;
                    if (counter == 1) {

                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                punchCounter = 0;
                punch1Counter = 0;
                player.setRunningForce(25);
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, PUNCH2() {
            private int counter = 0;
            private int punchCounter = 0;
            private int punch1Counter = 0;
            private int punch2Counter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }

            @Override
            public void update(Player player) {
                player.setRunningForce(1);
                player.setRegion(punch2Animation.getKeyFrame(player.getStateTimer(), false));
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }

                if ((punchCounter < 1 && punch2Animation.getKeyFrameIndex(player.getStateTimer()) == 2)) {
                    long id = player.punch.play();
                    player.punch.setVolume(id, 0.02f);
                    player.punch.setLooping(id, false);
                    punchCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }
                if ((punch1Counter < 1 && punch2Animation.getKeyFrameIndex(player.getStateTimer()) == 6)) {
                    long id = player.punch.play();
                    player.punch.setVolume(id, 0.02f);
                    player.punch.setLooping(id, false);
                    punch1Counter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }
                if ((punch2Counter < 1 && punch2Animation.getKeyFrameIndex(player.getStateTimer()) == 10)) {
                    long id = player.punch.play();
                    player.punch.setVolume(id, 0.02f);
                    player.punch.setLooping(id, false);
                    punch2Counter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }


                if ((punch2Animation.getKeyFrameIndex(player.getStateTimer()) == 2 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) || (punch2Animation.getKeyFrameIndex(player.getStateTimer()) == 6 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) || (punch2Animation.getKeyFrameIndex(player.getStateTimer()) == 10 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch()))) {
                    player.punchBody();
                }

                if (punch2Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;
                    if (counter == 1) {
                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }
                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                punchCounter = 0;
                punch1Counter = 0;
                punch2Counter = 0;
                player.setRunningForce(25);
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }, PUNCH3() {
            private int counter = 0;
            private int punchCounter = 0;

            @Override
            public void enter(Player player) {
                player.resetStateTimer();
            }


            @Override
            public void update(Player player) {
                player.setRunningForce(40);
                player.setRegion(punch3Animation.getKeyFrame(player.getStateTimer(), false));

                if ((punchCounter < 1 && punch3Animation.getKeyFrameIndex(player.getStateTimer()) == 3)) {
                    long id = player.punch.play();
                    player.punch.setVolume(id, 0.02f);
                    player.punch.setLooping(id, false);
                    punchCounter++;
                    if (player.getBodyToPunch() != null && player.getBodyToPunch().getUserData() instanceof Barrel && !player.distancingHimself(player.getBodyToPunch())) {
                        long id1 = player.barrelImpact.play();
                        player.barrelImpact.setVolume(id1, 0.1f);
                        player.barrelImpact.setLooping(id1, false);
                    }
                }
                if (player.getDirection() < 0) {
                    player.flip(true, false);
                }
                if (punch3Animation.getKeyFrameIndex(player.getStateTimer()) == 3 && player.getBodyToPunch() != null && !player.distancingHimself(player.getBodyToPunch())) {
                    player.punchBody();
                }
                if (punch3Animation.isAnimationFinished(player.getStateTimer())) {
                    counter++;

                    if (counter == 1) {
                        if (player.getFSM().getPreviousState() == JUMPING || player.getFSM().getPreviousState() == FALLING)
                            player.getFSM().changeState(IDLE);
                        else
                            player.getFSM().revertToPreviousState();
                    }

                }
                if (player.isHit() && !player.isOnElevator()) {
                    player.getFSM().changeState(HURT);
                }

            }

            @Override
            public void exit(Player player) {
                counter = 0;
                punchCounter = 0;
                player.setRunningForce(25);
            }

            @Override
            public boolean onMessage(Player entity, Telegram telegram) {
                return false;
            }
        }


    }
}