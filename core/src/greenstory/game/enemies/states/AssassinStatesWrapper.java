package greenstory.game.enemies.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.enemies.Assassin;
import greenstory.game.enemies.helpers.AssasinHelper;

public class AssassinStatesWrapper implements Disposable {
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> hitAnimation;
    private static Animation<TextureRegion> attackAnimation;
    private static Animation<TextureRegion> deathAnimation;
    private static Animation<TextureRegion> ambushAnimation;
    private static Animation<TextureRegion> blinkRunAnimation;
    private static Animation<TextureRegion> vanishAnimation;
    private AssasinHelper assasinHelper;

    public AssassinStatesWrapper() {
        assasinHelper = AssasinHelper.getInstance();
        idleAnimation = assasinHelper.get("idle");
        hitAnimation = assasinHelper.get("hit");
        attackAnimation = assasinHelper.get("attack");
        deathAnimation = assasinHelper.get("death");
        ambushAnimation = assasinHelper.get("ambush");
        blinkRunAnimation = assasinHelper.get("blinkRun");
        vanishAnimation = assasinHelper.get("vanish");
    }


    public enum AssassinStates implements State<Assassin> {
        IDLE() {
            @Override
            public void enter(Assassin entity) {

            }

            @Override
            public void update(Assassin entity) {
                entity.setRegion(idleAnimation.getKeyFrame(entity.getStateTimer(), true));

                if (entity.alert) {
                    entity.getFSM().changeState(CHASE);
                }

                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }


            }

            @Override
            public void exit(Assassin entity) {

            }

            @Override
            public boolean onMessage(Assassin entity, Telegram telegram) {
                return false;
            }
        },


        CHASE() {
            float time = 0;

            @Override
            public void enter(Assassin entity) {
                time = 0;
            }

            @Override
            public void update(Assassin entity) {
                entity.setRegion(blinkRunAnimation.getKeyFrame(entity.getStateTimer(), true));
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
                if (entity.alert && MathUtils.randomBoolean(0.06f) && entity.isInRange()) {
                    entity.getFSM().changeState(ATTACK);
                }
            }

            @Override
            public void exit(Assassin entity) {

            }

            @Override
            public boolean onMessage(Assassin entity, Telegram telegram) {

                return false;
            }
        },
        ATTACK() {
            float time = 0;

            @Override
            public void enter(Assassin entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Assassin entity) {

                entity.setRegion(attackAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayer();
                time += Gdx.graphics.getDeltaTime();
                if (time >= 1.6f) {
                    entity.getFSM().changeState(IDLE);
                    time = 0;
                    entity.alert = false;
                }

                if (attackAnimation.getKeyFrameIndex(entity.getStateTimer()) == 8 && entity.isInRange()) {
                    entity.punchBody();
                }
                if (entity.isHit()) {
                    entity.getFSM().changeState(HIT);
                }
            }

            @Override
            public void exit(Assassin entity) {
            }

            @Override
            public boolean onMessage(Assassin entity, Telegram telegram) {
                return false;
            }
        }, HIT() {
            private float time = 0;
            private int counter = 0;

            @Override
            public void enter(Assassin entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Assassin entity) {
                entity.setRegion(hitAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.turnToPlayer();
                if (counter < 1) {
                    long id = entity.assassinScream.play();
                    entity.assassinScream.setVolume(id, 0.1f);
                    entity.assassinScream.setLooping(id, false);
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
            public void exit(Assassin entity) {
                counter = 0;
                entity.damageHealth();
            }

            @Override
            public boolean onMessage(Assassin entity, Telegram telegram) {
                return false;
            }
        }, DEATH() {
            @Override
            public void enter(Assassin entity) {
                long id = entity.assassinDie.play();
                entity.assassinDie.setVolume(id, 0.3f);
                entity.assassinDie.setLooping(id, false);
            }

            @Override
            public void update(Assassin entity) {
                entity.setRegion(deathAnimation.getKeyFrame(entity.getStateTimer(), false));
                entity.setHealth(0);
                if (deathAnimation.isAnimationFinished(entity.getStateTimer())) {
                    entity.setSetToDestroy(true);
                }
            }

            @Override
            public void exit(Assassin entity) {

            }

            @Override
            public boolean onMessage(Assassin entity, Telegram telegram) {
                return false;
            }
        }, AMBUSH() {
            float time = 0;

            @Override
            public void enter(Assassin entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Assassin entity) {
                entity.setRegion(ambushAnimation.getKeyFrame(entity.getStateTimer(), true));
                entity.turnToPlayer();

                if (entity.ambush) {
                    if (((time += Gdx.graphics.getDeltaTime()) >= 1.2f)) {
                        entity.getFSM().changeState(ATTACK);

                    }

                }


            }

            @Override
            public void exit(Assassin entity) {

            }

            @Override
            public boolean onMessage(Assassin entity, Telegram telegram) {
                return false;
            }
        }, VANISH() {
            float time = 0;

            @Override
            public void enter(Assassin entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(Assassin entity) {
                entity.setRegion(vanishAnimation.getKeyFrame(entity.getStateTimer(), true));
                if ((time += Gdx.graphics.getDeltaTime()) >= 0.8f) {
                    entity.draw = false;
                    entity.ambush = false;
                    entity.getBody().setActive(true);
                    entity.getBody().setTransform(entity.getInitialPosition().x, entity.getInitialPosition().y, 0);
                    time = 0;
                    entity.getFSM().changeState(AMBUSH);
                }
            }

            @Override
            public void exit(Assassin entity) {

            }

            @Override
            public boolean onMessage(Assassin entity, Telegram telegram) {
                return false;
            }
        }
    }

    public void dispose() {

    }

}
