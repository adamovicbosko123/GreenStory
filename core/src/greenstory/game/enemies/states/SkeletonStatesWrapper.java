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

public class SkeletonStatesWrapper {
    private SkeletonHelper skeletonHelper;
    private static Animation<TextureRegion> skeletonIdleAnimation;
    private static Animation<TextureRegion> skeletonWalkAnimation;
    private static Animation<TextureRegion> skeletonReactAnimation;
    private static Animation<TextureRegion> skeletonAttackAnimation;
    private static Animation<TextureRegion> skeletonHitAnimation;
    private static Animation<TextureRegion> skeletonDeathAnimation;

    public SkeletonStatesWrapper() {
        skeletonHelper = SkeletonHelper.getInstance();
        skeletonWalkAnimation = skeletonHelper.get("walk");
        skeletonIdleAnimation = skeletonHelper.get("idle");
        skeletonHitAnimation = skeletonHelper.get("hit");
        skeletonAttackAnimation = skeletonHelper.get("attack");
        skeletonDeathAnimation = skeletonHelper.get("death");
        skeletonReactAnimation = skeletonHelper.get("react");
    }


    public enum SkeletonStates implements State<Skeleton> {
        IDLE() {
            float time = 0;

            @Override
            public void enter(Skeleton entity) {
                time = 0;
            }

            @Override
            public void update(Skeleton entity) {
                entity.setRegion(skeletonIdleAnimation.getKeyFrame(entity.getStateTimer(), true));

                if (entity.alert) {
                    entity.getFSM().changeState(REACT);
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }


            }

            @Override
            public void exit(Skeleton entity) {

            }

            @Override
            public boolean onMessage(Skeleton entity, Telegram telegram) {
                return false;
            }
        },

        CHASE() {
            float time = 0;

            @Override
            public void enter(Skeleton entity) {
                time = 0;
            }


            @Override
            public void update(Skeleton entity) {
                entity.setRegion(skeletonWalkAnimation.getKeyFrame(entity.getStateTimer(), true));

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
            public void exit(Skeleton entity) {

            }

            @Override
            public boolean onMessage(Skeleton entity, Telegram telegram) {

                return false;
            }
        },
        ATTACK() {
            float time = 0;
            private int counter = 0;
            private int counter1 = 0;

            @Override
            public void enter(Skeleton entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Skeleton entity) {

                entity.setRegion(skeletonAttackAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayer();
                if (counter < 1 && skeletonAttackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 7) {
                    long id = entity.skeletonSwing.play();
                    entity.skeletonSwing.setVolume(id, 0.1f);
                    entity.skeletonSwing.setLooping(id, false);
                    counter++;
                }
                if (counter1 < 1 && skeletonAttackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 10) {
                    long id = entity.skeletonPunch.play();
                    entity.skeletonPunch.setVolume(id, 0.1f);
                    entity.skeletonPunch.setLooping(id, false);
                    counter1++;
                }

                time += Gdx.graphics.getDeltaTime();
                if (time >= .45f) {
                    entity.getFSM().changeState(IDLE);
                    time = 0;
                    entity.alert = false;
                }
                if (skeletonAttackAnimation.getKeyFrameIndex(entity.getStateTimer()) < 4 || skeletonAttackAnimation.getKeyFrameIndex(entity.getStateTimer()) > 13) {
                    if (entity.isHit()) {
                        entity.getFSM().changeState(HIT);
                    }
                }


                if (skeletonAttackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 8 && entity.isInRange()) {
                    entity.punchBody();
                }

            }

            @Override
            public void exit(Skeleton entity) {
                counter = 0;
                counter1 = 0;
            }

            @Override
            public boolean onMessage(Skeleton entity, Telegram telegram) {
                return false;
            }
        }, HIT() {
            private float time = 0;
            private int counter = 0;

            @Override
            public void enter(Skeleton entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Skeleton entity) {
                entity.setRegion(skeletonHitAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.turnToPlayer();
                if (counter < 1) {
                    long id = entity.skeletonScream.play();
                    entity.skeletonScream.setVolume(id, 0.1f);
                    entity.skeletonScream.setLooping(id, false);
                    counter++;
                }


                if ((time += Gdx.graphics.getDeltaTime()) >= 0.6f) {
                    entity.getFSM().changeState(IDLE);
                    entity.setHit(false);

                    if (entity.getHealth() <= 0) {
                        entity.getFSM().changeState(DEATH);
                    }
                    time = 0;
                }

            }

            @Override
            public void exit(Skeleton entity) {
                counter = 0;
                entity.damageHealth();
            }

            @Override
            public boolean onMessage(Skeleton entity, Telegram telegram) {
                return false;
            }
        }, DEATH() {
            @Override
            public void enter(Skeleton entity) {
                entity.resetStateTimer();
                long id = entity.skeletonDie.play();
                entity.skeletonDie.setVolume(id, 0.2f);
                entity.skeletonDie.setLooping(id, false);
            }

            @Override
            public void update(Skeleton entity) {
                entity.setRegion(skeletonDeathAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.setHealth(0);
                if (skeletonDeathAnimation.isAnimationFinished(entity.getStateTimer())) {
                    entity.setSetToDestroy(true);
                }
            }

            @Override
            public void exit(Skeleton entity) {

            }

            @Override
            public boolean onMessage(Skeleton entity, Telegram telegram) {
                return false;
            }
        }, REACT() {
            float time = 0;

            @Override
            public void enter(Skeleton entity) {

            }

            @Override
            public void update(Skeleton entity) {
                entity.setRegion(skeletonReactAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.turnToPlayer();

                time += Gdx.graphics.getDeltaTime();
                if (time >= 0.6f) {
                    entity.getFSM().changeState(CHASE);
                    time = 0;
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }
            }

            @Override
            public void exit(Skeleton entity) {

            }

            @Override
            public boolean onMessage(Skeleton entity, Telegram telegram) {
                return false;
            }
        }
    }


}
