package greenstory.game.enemies.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import greenstory.game.enemies.*;
import greenstory.game.enemies.assets.FireBall;
import greenstory.game.enemies.states.*;
import greenstory.game.enemies.helpers.*;

public class FireCultistStatesWrapper {
    private static Animation<TextureRegion> walkAnimation;
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> hitAnimation;
    private static Animation<TextureRegion> attackAnimation;
    private static Animation<TextureRegion> deathAnimation;
    private static Animation<TextureRegion> jumpAnimation;
    private static Animation<TextureRegion> fallAnimation;
    private FireCultistHelper fireCultistHelper;

    public FireCultistStatesWrapper() {
        fireCultistHelper = FireCultistHelper.getInstance();
        walkAnimation = fireCultistHelper.get("walk");
        idleAnimation = fireCultistHelper.get("idle");
        hitAnimation = fireCultistHelper.get("hit");
        attackAnimation = fireCultistHelper.get("attack");
        deathAnimation = fireCultistHelper.get("death");
        jumpAnimation = fireCultistHelper.get("jump");
        fallAnimation = fireCultistHelper.get("fall");
    }


    public enum FireCultistStates implements State<FireCultist> {
        IDLE() {
            @Override
            public void enter(FireCultist entity) {

            }

            @Override
            public void update(FireCultist entity) {
                entity.setRegion(idleAnimation.getKeyFrame(entity.getStateTimer(), true));


                if (entity.alert) {
                    entity.getFSM().changeState(CHASE);
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }


            }

            @Override
            public void exit(FireCultist entity) {

            }

            @Override
            public boolean onMessage(FireCultist entity, Telegram telegram) {
                return false;
            }
        },

        CHASE() {
            float time = 0;
            float attackTime = 0;

            @Override
            public void enter(FireCultist entity) {
                time = 0;
                attackTime = 0;
            }

            @Override
            public void update(FireCultist entity) {
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
                if (entity.alert && ((attackTime += Gdx.graphics.getDeltaTime()) > .4f)) {
                    entity.getFSM().changeState(ATTACK);
                    attackTime = 0;
                }
            }

            @Override
            public void exit(FireCultist entity) {

            }

            @Override
            public boolean onMessage(FireCultist entity, Telegram telegram) {

                return false;
            }
        },
        ATTACK() {
            float time = 0;
            int count = 0;
            FireBall fireBall;
            private int counter = 0;

            @Override
            public void enter(FireCultist entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(FireCultist entity) {

                entity.setRegion(attackAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayer();

                if (counter < 1 && attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 5) {
                    long id = entity.fireCultistFire.play();
                    entity.fireCultistFire.setVolume(id, 0.05f);
                    entity.fireCultistFire.setLooping(id, false);
                    counter++;
                }

                time += Gdx.graphics.getDeltaTime();
                if (time >= .7f) {
                    entity.getFSM().changeState(IDLE);
                    time = 0;
                    entity.alert = false;
                }
                if (attackAnimation.getKeyFrameIndex(entity.getStateTimer()) < 3 || attackAnimation.getKeyFrameIndex(entity.getStateTimer()) >= 6) {
                    if (entity.isHit()) {
                        entity.getFSM().changeState(HIT);
                    }
                }

                if (attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 5) {
                    count++;
                    if (count == 1) {
                        entity.fireBalls.add(fireBall = new FireBall(entity));
                    }

                }
            }

            @Override
            public void exit(FireCultist entity) {
                count = 0;
                counter = 0;

                if (entity.fireBalls.size >= 10) {
                    for (int i = 0; i < entity.getFireBalls().size; i++) {
                        entity.fireBalls.removeIndex(i);
                    }
                }


            }

            @Override
            public boolean onMessage(FireCultist entity, Telegram telegram) {
                return false;
            }
        }, HIT() {
            private float time = 0;
            private int counter = 0;

            @Override
            public void enter(FireCultist entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(FireCultist entity) {
                entity.setRegion(hitAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.turnToPlayer();
                entity.fireBalls.clear();
                if (counter < 1) {
                    long id = entity.fireCultistScream.play();
                    entity.fireCultistScream.setVolume(id, 0.1f);
                    entity.fireCultistScream.setLooping(id, false);
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
            public void exit(FireCultist entity) {
                counter = 0;
                entity.damageHealth();
            }

            @Override
            public boolean onMessage(FireCultist entity, Telegram telegram) {
                return false;
            }
        }, DEATH() {
            @Override
            public void enter(FireCultist entity) {
                entity.resetStateTimer();
                long id = entity.fireCultistDie.play();
                entity.fireCultistDie.setVolume(id, 0.2f);
                entity.fireCultistDie.setLooping(id, false);
            }

            @Override
            public void update(FireCultist entity) {
                entity.setRegion(deathAnimation.getKeyFrame(entity.getStateTimer(), false));

                entity.fireBalls.clear();

                entity.setHealth(0);
                if (deathAnimation.isAnimationFinished(entity.getStateTimer())) {
                    entity.setSetToDestroy(true);
                }

            }

            @Override
            public void exit(FireCultist entity) {

            }

            @Override
            public boolean onMessage(FireCultist entity, Telegram telegram) {
                return false;
            }
        }
    }
}
