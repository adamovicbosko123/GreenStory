package greenstory.game.enemies.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import greenstory.game.enemies.BigCultist;
import greenstory.game.enemies.helpers.BigCultistHelper;

public class BigCultistStatesWrapper {
    private static Animation<TextureRegion> walkAnimation;
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> hitAnimation;
    private static Animation<TextureRegion> attackAnimation;
    private static Animation<TextureRegion> deathAnimation;
    private BigCultistHelper bigCultistHelper;

    public BigCultistStatesWrapper() {
        bigCultistHelper = BigCultistHelper.getInstance();
        walkAnimation = bigCultistHelper.get("walk");
        idleAnimation = bigCultistHelper.get("idle");
        hitAnimation = bigCultistHelper.get("hit");
        attackAnimation = bigCultistHelper.get("attack");
        deathAnimation = bigCultistHelper.get("death");
    }


    public enum BigCultistStates implements State<BigCultist> {
        IDLE() {
            @Override
            public void enter(BigCultist entity) {

            }

            @Override
            public void update(BigCultist entity) {
                entity.setRegion(idleAnimation.getKeyFrame(entity.getStateTimer(), true));
                if (entity.alert) {
                    entity.getFSM().changeState(CHASE);
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }


            }

            @Override
            public void exit(BigCultist entity) {

            }

            @Override
            public boolean onMessage(BigCultist entity, Telegram telegram) {
                return false;
            }
        },


        CHASE() {
            float time = 0;

            @Override
            public void enter(BigCultist entity) {
                time = 0;
            }

            @Override
            public void update(BigCultist entity) {
                entity.setRegion(walkAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayerForChase();
                if ((time += Gdx.graphics.getDeltaTime()) > 2.4) {
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
                if (entity.alert && MathUtils.randomBoolean(0.08f) && entity.xDistanceFromPlayer() < 1.5 && entity.xDistanceFromPlayer() > -1.5) {
                    entity.getFSM().changeState(ATTACK);
                }
            }

            @Override
            public void exit(BigCultist entity) {

            }

            @Override
            public boolean onMessage(BigCultist entity, Telegram telegram) {

                return false;
            }
        },
        ATTACK() {
            float time = 0;
            private int counter = 0;
            private int counter1 = 0;
            private int counter2 = 0;

            @Override
            public void enter(BigCultist entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(BigCultist entity) {

                entity.setRegion(attackAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayer();
                if (counter < 1 && attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 4) {
                    long id = entity.bigCultistSwing.play();
                    entity.bigCultistSwing.setVolume(id, 0.1f);
                    entity.bigCultistSwing.setLooping(id, false);
                    counter++;
                }
                if (counter1 < 1 && attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 12) {
                    long id = entity.bigCultistSwing.play();
                    entity.bigCultistSwing.setVolume(id, 0.1f);
                    entity.bigCultistSwing.setLooping(id, false);
                    counter1++;
                }
                if (counter2 < 1 && attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 14) {
                    long id = entity.bigCultistPunch.play();
                    entity.bigCultistPunch.setVolume(id, 0.1f);
                    entity.bigCultistPunch.setLooping(id, false);
                    counter2++;
                }
                time += Gdx.graphics.getDeltaTime();
                if (time >= 2f) {
                    entity.getFSM().changeState(IDLE);
                    time = 0;
                    entity.alert = false;
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }


                if (((attackAnimation.getKeyFrameIndex(entity.getStateTimer()) >= 4 && attackAnimation.getKeyFrameIndex(entity.getStateTimer()) <= 6) && entity.isInRange()) || ((attackAnimation.getKeyFrameIndex(entity.getStateTimer()) >= 12 && attackAnimation.getKeyFrameIndex(entity.getStateTimer()) <= 15) && entity.isInRange())) {
                    entity.punchBody();
                }

            }

            @Override
            public void exit(BigCultist entity) {
                counter = 0;
                counter1 = 0;
                counter2 = 0;
            }

            @Override
            public boolean onMessage(BigCultist entity, Telegram telegram) {
                return false;
            }
        }, HIT() {
            private float time = 0;
            private int counter = 0;

            @Override
            public void enter(BigCultist entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(BigCultist entity) {
                entity.setRegion(hitAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.turnToPlayer();
                if (counter < 1) {
                    long id = entity.bigCultistScream.play();
                    entity.bigCultistScream.setVolume(id, 0.1f);
                    entity.bigCultistScream.setLooping(id, false);
                    counter++;
                }
                if ((time += Gdx.graphics.getDeltaTime()) >= 0.3f) {
                    entity.getFSM().changeState(IDLE);
                    if (entity.getHealth() <= 0) {
                        entity.getFSM().changeState(DEATH);
                    }
                    entity.setHit(false);
                    time = 0;
                }

            }

            @Override
            public void exit(BigCultist entity) {
                counter = 0;
                entity.damageHealth();
            }

            @Override
            public boolean onMessage(BigCultist entity, Telegram telegram) {
                return false;
            }
        }, DEATH() {
            @Override
            public void enter(BigCultist entity) {
                long id = entity.bigCultistDie.play();
                entity.bigCultistDie.setVolume(id, 0.3f);
                entity.bigCultistDie.setLooping(id, false);
            }

            @Override
            public void update(BigCultist entity) {
                entity.setRegion(deathAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.setHealth(0);
                if (deathAnimation.isAnimationFinished(entity.getStateTimer())) {
                    entity.setSetToDestroy(true);
                }

            }

            @Override
            public void exit(BigCultist entity) {

            }

            @Override
            public boolean onMessage(BigCultist entity, Telegram telegram) {
                return false;
            }
        }
    }
}
