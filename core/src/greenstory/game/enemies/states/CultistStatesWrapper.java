package greenstory.game.enemies.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import greenstory.game.enemies.*;
import greenstory.game.enemies.states.*;
import greenstory.game.enemies.helpers.*;

public class CultistStatesWrapper {
    private static Animation<TextureRegion> walkAnimation;
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> hitAnimation;
    private static Animation<TextureRegion> attackAnimation;
    private static Animation<TextureRegion> deathAnimation;
    private static Animation<TextureRegion> jumpAnimation;
    private static Animation<TextureRegion> fallAnimation;
    private CultistHelper cultistHelper;

    public CultistStatesWrapper() {
        cultistHelper = CultistHelper.getInstance();
        walkAnimation = cultistHelper.get("walk");
        idleAnimation = cultistHelper.get("idle");
        hitAnimation = cultistHelper.get("hit");
        attackAnimation = cultistHelper.get("attack");
        deathAnimation = cultistHelper.get("death");
        jumpAnimation = cultistHelper.get("jump");
        fallAnimation = cultistHelper.get("fall");
    }


    public enum CultistStates implements State<Cultist> {
        IDLE() {
            @Override
            public void enter(Cultist entity) {

            }

            @Override
            public void update(Cultist entity) {
                entity.setRegion(idleAnimation.getKeyFrame(entity.getStateTimer(), true));
                if (entity.alert) {
                    entity.getFSM().changeState(CHASE);
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }


            }

            @Override
            public void exit(Cultist entity) {

            }

            @Override
            public boolean onMessage(Cultist entity, Telegram telegram) {
                return false;
            }
        },


        CHASE() {
            float time = 0;

            @Override
            public void enter(Cultist entity) {
                time = 0;
            }

            @Override
            public void update(Cultist entity) {
                entity.setRegion(walkAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayerForChase();
                if ((time += Gdx.graphics.getDeltaTime()) > 2) {
                    entity.alert = false;
                    entity.getFSM().changeState(IDLE);
                    time = 0;
                }
                if (!entity.alert) {
                    entity.getFSM().changeState(IDLE);
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }
                if (entity.alert && MathUtils.randomBoolean(0.08f) && entity.isInRange()) {
                    entity.getFSM().changeState(ATTACK);
                }
            }

            @Override
            public void exit(Cultist entity) {

            }

            @Override
            public boolean onMessage(Cultist entity, Telegram telegram) {

                return false;
            }
        },
        ATTACK() {
            float time = 0;
            private int counter = 0;

            @Override
            public void enter(Cultist entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Cultist entity) {

                entity.setRegion(attackAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayer();
                if (counter < 1 && attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 3) {
                    long id = entity.cultistPush.play();
                    entity.cultistPush.setVolume(id, 0.05f);
                    entity.cultistPush.setLooping(id, false);
                    counter++;
                }
                time += Gdx.graphics.getDeltaTime();
                if (time >= 0.8f) {
                    entity.getFSM().changeState(IDLE);
                    time = 0;
                    entity.alert = false;
                }

                if (attackAnimation.getKeyFrameIndex(entity.getStateTimer()) < 2 || attackAnimation.getKeyFrameIndex(entity.getStateTimer()) >= 5) {
                    if (entity.isHit()) {
                        entity.getFSM().changeState(HIT);
                    }
                }

                if (attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 4 && entity.isInRange()) {
                    entity.punchBody();
                }

            }

            @Override
            public void exit(Cultist entity) {
                counter = 0;
            }

            @Override
            public boolean onMessage(Cultist entity, Telegram telegram) {
                return false;
            }
        }, HIT() {
            float time = 0;
            private int counter = 0;

            @Override
            public void enter(Cultist entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Cultist entity) {
                entity.setRegion(hitAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.turnToPlayer();
                if (counter < 1) {
                    long id = entity.cultistScream.play();
                    entity.cultistScream.setVolume(id, 0.1f);
                    entity.cultistScream.setLooping(id, false);
                    counter++;
                }
                if ((time += Gdx.graphics.getDeltaTime()) >= 0.6f) {
                    entity.getFSM().changeState(IDLE);
                    if (entity.getHealth() <= 0) {
                        entity.getFSM().changeState(DEATH);
                    }
                    entity.setHit(false);
                    time = 0;
                }

            }

            @Override
            public void exit(Cultist entity) {
                entity.damageHealth();
                counter = 0;
            }

            @Override
            public boolean onMessage(Cultist entity, Telegram telegram) {
                return false;
            }
        }, DEATH() {
            @Override
            public void enter(Cultist entity) {
                long id = entity.cultistDie.play();
                entity.cultistDie.setVolume(id, 0.2f);
                entity.cultistDie.setLooping(id, false);
            }

            @Override
            public void update(Cultist entity) {
                entity.setRegion(deathAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.setHealth(0);
                if (deathAnimation.isAnimationFinished(entity.getStateTimer())) {
                    entity.setSetToDestroy(true);
                }

            }

            @Override
            public void exit(Cultist entity) {
            }

            @Override
            public boolean onMessage(Cultist entity, Telegram telegram) {
                return false;
            }
        }
    }
}
