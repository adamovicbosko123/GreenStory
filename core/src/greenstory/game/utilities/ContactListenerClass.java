package greenstory.game.utilities;

import com.badlogic.gdx.physics.box2d.*;
import greenstory.game.GreenStory;
import greenstory.game.enemies.Enemy;
import greenstory.game.enemies.*;
import greenstory.game.enemies.assets.FireBall;
import greenstory.game.enemies.states.*;
import greenstory.game.enemies.states.SkeletonStatesWrapper;
import greenstory.game.objects.enemyobjects.Grinder;
import greenstory.game.objects.enemyobjects.PoisonDrop;
import greenstory.game.objects.enemyobjects.Spikes;
import greenstory.game.objects.harmlesobjects.Barrel;
import greenstory.game.objects.harmlesobjects.Elevator;
import greenstory.game.player.Player;
import greenstory.game.player.Wife;
import greenstory.game.player.assets.Bow;
import greenstory.game.screens.AbstractScreen;

public class ContactListenerClass implements ContactListener {
    //private PlayScreen playScreen;

    public ContactListenerClass(AbstractScreen screen) {
        // this.playScreen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();
        int contactColider = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        switch (contactColider) {
            case GreenStory.ATTACK_BIT | GreenStory.BARREL_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ATTACK_BIT) {
                    ((Player) fixtureA.getUserData()).setBodyToPunch(((Barrel) fixtureB.getUserData()).getBody());
                } else {
                    ((Player) fixtureB.getUserData()).setBodyToPunch(((Barrel) fixtureA.getUserData()).getBody());

                }
                break;
            case GreenStory.ATTACK_BIT | GreenStory.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ATTACK_BIT) {
                    ((Player) fixtureA.getUserData()).setBodyToPunch(((Enemy) fixtureB.getUserData()).getBody());
                } else {
                    ((Player) fixtureB.getUserData()).setBodyToPunch(((Enemy) fixtureA.getUserData()).getBody());

                }
                break;


            case GreenStory.PLAYER_SENSOR | GreenStory.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ENEMY_BIT && !(fixtureA.getUserData() instanceof FireBall)) {
                    ((Player) fixtureB.getUserData()).addEnemy((Enemy) fixtureA.getUserData());
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.ENEMY_BIT && !(fixtureB.getUserData() instanceof FireBall)) {
                    ((Player) fixtureA.getUserData()).addEnemy((Enemy) fixtureB.getUserData());
                }
                break;


            case GreenStory.PLAYER_BIT | GreenStory.ELEVATOR_SENSOR_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ELEVATOR_SENSOR_BIT) {
                    ((Elevator) fixtureA.getUserData()).setPlayer((Player) fixtureB.getUserData());
                } else {
                    ((Elevator) fixtureB.getUserData()).setPlayer((Player) fixtureA.getUserData());
                }
                break;
            case GreenStory.PLAYER_FEET_BIT | GreenStory.ELEVATOR_GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT) {
                    ((Player) fixtureA.getUserData()).setOnElevator(true);
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT) {
                    ((Player) fixtureB.getUserData()).setOnElevator(true);
                }
                break;

            case GreenStory.GROUND_BIT | GreenStory.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ENEMY_BIT) {
                    if (fixtureA.getUserData() instanceof PoisonDrop)
                        ((PoisonDrop) fixtureA.getUserData()).setContactWithGround(true);
                } else {
                    if (fixtureB.getUserData() instanceof PoisonDrop)
                        ((PoisonDrop) fixtureB.getUserData()).setContactWithGround(true);
                }
                break;
            case GreenStory.DEATH_BIT | GreenStory.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.DEATH_BIT) {
                    if (fixtureA.getUserData() instanceof Spikes)
                        ((Spikes) fixtureA.getUserData()).spikesTouchedObject(fixtureB.getUserData());
                    if (fixtureA.getUserData() instanceof Grinder)
                        ((Grinder) fixtureA.getUserData()).grinderTouchedObject(fixtureB.getUserData());
                    if (fixtureA.getUserData() instanceof Bow) {
                        if (!(fixtureB.getUserData() instanceof FireBall)) {
                            ((Enemy) fixtureB.getUserData()).setHit(true);
                            ((Enemy) fixtureB.getUserData()).setHealth(0);
                            ((Bow) fixtureA.getUserData()).setReadyToDestroy(true);
                        } else if (fixtureB.getUserData() instanceof FireBall) {
                            // ((FireBall) fixtureB.getUserData()).setReadyToDestroy(true);
                        }
                    }
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.DEATH_BIT) {
                    if (fixtureB.getUserData() instanceof Spikes)
                        ((Spikes) fixtureB.getUserData()).spikesTouchedObject(fixtureA.getUserData());
                    if (fixtureB.getUserData() instanceof Grinder)
                        ((Grinder) fixtureB.getUserData()).grinderTouchedObject(fixtureA.getUserData());
                    if (fixtureB.getUserData() instanceof Bow) {
                        if (!(fixtureA.getUserData() instanceof FireBall)) {
                            ((Enemy) fixtureA.getUserData()).setHit(true);
                            ((Enemy) fixtureA.getUserData()).setHealth(0);
                            ((Bow) fixtureB.getUserData()).setReadyToDestroy(true);
                        } else if (fixtureA.getUserData() instanceof FireBall) {
                            // ((FireBall) fixtureA.getUserData()).setReadyToDestroy(true);
                        }
                    }
                }
                break;
            case GreenStory.DEATH_BIT | GreenStory.PLAYER_BIT:
            case GreenStory.DEATH_BIT | GreenStory.PLAYER_FEET_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.DEATH_BIT) {
                    if (fixtureA.getUserData() instanceof Spikes)
                        ((Spikes) fixtureA.getUserData()).spikesTouchedPLayer(fixtureB.getUserData());
                    if (fixtureA.getUserData() instanceof Grinder)
                        ((Grinder) fixtureA.getUserData()).grinderTouchedPLayer(fixtureB.getUserData());
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.DEATH_BIT) {
                    if (fixtureB.getUserData() instanceof Spikes)
                        ((Spikes) fixtureB.getUserData()).spikesTouchedPLayer(fixtureA.getUserData());
                    if (fixtureB.getUserData() instanceof Grinder)
                        ((Grinder) fixtureB.getUserData()).grinderTouchedPLayer(fixtureA.getUserData());
                }
                break;


            case GreenStory.WALL_BIT | GreenStory.PLAYER_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).setSlidingDownTheWall(true);
                } else {
                    ((Player) fixtureB.getUserData()).setSlidingDownTheWall(true);
                }
                break;
            //this long code block makes sure that player cannot ride on the enemy
            case GreenStory.PLAYER_BIT | GreenStory.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ENEMY_BIT) {

                    if (fixtureA.getUserData() instanceof Skeleton) {
                        Skeleton skeleton = (Skeleton) fixtureA.getUserData();
                        if (!skeleton.getFSM().isInState(SkeletonStatesWrapper.SkeletonStates.DEATH)) {
                            skeleton.getFSM().changeState(SkeletonStatesWrapper.SkeletonStates.ATTACK);
                            if (fixtureB.getUserData() instanceof Player) {
                                Player player = (Player) fixtureB.getUserData();
                                player.setHit(true);
                            }
                        }

                    }
                    if (fixtureA.getUserData() instanceof FireCultist) {
                        FireCultist fireCultist = (FireCultist) fixtureA.getUserData();
                        if (!fireCultist.getFSM().isInState(FireCultistStatesWrapper.FireCultistStates.DEATH)) {
                            fireCultist.getFSM().changeState(FireCultistStatesWrapper.FireCultistStates.ATTACK);
                            if (fixtureB.getUserData() instanceof Player) {
                                Player player = (Player) fixtureB.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureA.getUserData() instanceof Cultist) {
                        Cultist cultist = (Cultist) fixtureA.getUserData();
                        if (!cultist.getFSM().isInState(CultistStatesWrapper.CultistStates.DEATH)) {
                            cultist.getFSM().changeState(CultistStatesWrapper.CultistStates.ATTACK);
                            if (fixtureB.getUserData() instanceof Player) {
                                Player player = (Player) fixtureB.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureA.getUserData() instanceof BigCultist) {
                        BigCultist bigCultist = (BigCultist) fixtureA.getUserData();
                        if (!bigCultist.getFSM().isInState(BigCultistStatesWrapper.BigCultistStates.DEATH)) {
                            bigCultist.getFSM().changeState(BigCultistStatesWrapper.BigCultistStates.ATTACK);
                            if (fixtureB.getUserData() instanceof Player) {
                                Player player = (Player) fixtureB.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureA.getUserData() instanceof Assassin) {
                        Assassin assassin = (Assassin) fixtureA.getUserData();
                        if (!assassin.getFSM().isInState(AssassinStatesWrapper.AssassinStates.DEATH)) {
                            assassin.getFSM().changeState(AssassinStatesWrapper.AssassinStates.ATTACK);
                            if (fixtureB.getUserData() instanceof Player) {
                                Player player = (Player) fixtureB.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureA.getUserData() instanceof FireBall) {
                        // ((FireBall) fixtureA.getUserData()).setReadyToDestroy(true);
                        if (fixtureB.getUserData() instanceof Player) {
                            Player player = (Player) fixtureB.getUserData();
                            player.setHit(true);
                        }
                    }
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.ENEMY_BIT) {
                    if (fixtureB.getUserData() instanceof Skeleton) {
                        Skeleton skeleton = (Skeleton) fixtureB.getUserData();
                        if (!skeleton.getFSM().isInState(SkeletonStatesWrapper.SkeletonStates.DEATH)) {
                            skeleton.getFSM().changeState(SkeletonStatesWrapper.SkeletonStates.ATTACK);
                            if (fixtureA.getUserData() instanceof Player) {
                                Player player = (Player) fixtureA.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureB.getUserData() instanceof FireCultist) {
                        FireCultist fireCultist = (FireCultist) fixtureB.getUserData();
                        if (!fireCultist.getFSM().isInState(FireCultistStatesWrapper.FireCultistStates.DEATH)) {
                            fireCultist.getFSM().changeState(FireCultistStatesWrapper.FireCultistStates.ATTACK);
                            if (fixtureA.getUserData() instanceof Player) {
                                Player player = (Player) fixtureA.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureB.getUserData() instanceof Cultist) {
                        Cultist fireCultist = (Cultist) fixtureB.getUserData();
                        if (!fireCultist.getFSM().isInState(CultistStatesWrapper.CultistStates.DEATH)) {
                            fireCultist.getFSM().changeState(CultistStatesWrapper.CultistStates.ATTACK);
                            if (fixtureA.getUserData() instanceof Player) {
                                Player player = (Player) fixtureA.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureB.getUserData() instanceof BigCultist) {
                        BigCultist fireCultist = (BigCultist) fixtureB.getUserData();
                        if (!fireCultist.getFSM().isInState(BigCultistStatesWrapper.BigCultistStates.DEATH)) {
                            fireCultist.getFSM().changeState(BigCultistStatesWrapper.BigCultistStates.ATTACK);
                            if (fixtureA.getUserData() instanceof Player) {
                                Player player = (Player) fixtureA.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureB.getUserData() instanceof Assassin) {
                        Assassin assassin = (Assassin) fixtureB.getUserData();
                        if (!assassin.getFSM().isInState(AssassinStatesWrapper.AssassinStates.DEATH)) {
                            assassin.getFSM().changeState(AssassinStatesWrapper.AssassinStates.ATTACK);
                            if (fixtureA.getUserData() instanceof Player) {
                                Player player = (Player) fixtureA.getUserData();
                                player.setHit(true);
                            }
                        }
                    }
                    if (fixtureB.getUserData() instanceof FireBall) {
                        //((FireBall) fixtureB.getUserData()).setReadyToDestroy(true);
                        if (fixtureA.getUserData() instanceof Player) {
                            Player player = (Player) fixtureA.getUserData();
                            player.setHit(true);
                        }
                    }
                }
                break;

            case GreenStory.ENEMY_BIT | GreenStory.WALL_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ENEMY_BIT) {
                    if (fixtureA.getUserData() instanceof FireBall) {
                        //((FireBall) fixtureA.getUserData()).setReadyToDestroy(true);
                    }
                } else {
                    // if (fixtureB.getUserData() instanceof FireBall)
                    //  ((FireBall) fixtureB.getUserData()).setReadyToDestroy(true);
                }
                break;


            case GreenStory.DEATH_BIT | GreenStory.WALL_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.DEATH_BIT) {
                    if (fixtureA.getUserData() instanceof Bow)
                        ((Bow) fixtureA.getUserData()).setReadyToDestroy(true);
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.DEATH_BIT) {
                    if (fixtureB.getUserData() instanceof Bow)
                        ((Bow) fixtureB.getUserData()).setReadyToDestroy(true);

                }
            case GreenStory.RIGHT_GRAB_BIT | GreenStory.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.RIGHT_GRAB_BIT) {
                    ((Player) fixtureA.getUserData()).setRightHandGrabbed(true);
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.RIGHT_GRAB_BIT) {
                    ((Player) fixtureB.getUserData()).setRightHandGrabbed(true);
                }
                break;

            case GreenStory.LEFT_GRAB_BIT | GreenStory.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.LEFT_GRAB_BIT) {
                    ((Player) fixtureA.getUserData()).setLeftHandGrabbed(true);
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.LEFT_GRAB_BIT) {
                    ((Player) fixtureB.getUserData()).setLeftHandGrabbed(true);
                }
                break;

            case GreenStory.PLAYER_BIT | GreenStory.WIFE_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.PLAYER_BIT) {
                    ((Player) (fixtureA.getUserData())).setMetWithTheWife(true);
                    ((Wife) (fixtureB.getUserData())).setMetWithThePlayer(true);
                } else {
                    ((Player) (fixtureB.getUserData())).setMetWithTheWife(true);
                    ((Wife) (fixtureA.getUserData())).setMetWithThePlayer(true);

                }
                break;
            case GreenStory.PLAYER_FEET_BIT | GreenStory.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT && (fixtureA.getUserData() instanceof Player)) {
                    ((Player) (fixtureA.getUserData())).setReadyToJump(true);
                } else if(fixtureB.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT && (fixtureB.getUserData() instanceof Player)) {
                    ((Player) (fixtureB.getUserData())).setReadyToJump(true);
                }
                break;

        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        int contactColider = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        switch (contactColider) {

            case GreenStory.PLAYER_SENSOR | GreenStory.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ENEMY_BIT && !(fixtureA.getUserData() instanceof FireBall)) {
                    ((Player) fixtureB.getUserData()).removeEnemy((Enemy) fixtureA.getUserData());
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.ENEMY_BIT && !(fixtureB.getUserData() instanceof FireBall)) {
                    ((Player) fixtureA.getUserData()).removeEnemy((Enemy) fixtureB.getUserData());

                }
                break;
            case GreenStory.PLAYER_BIT | GreenStory.ELEVATOR_SENSOR_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ELEVATOR_SENSOR_BIT) {
                    ((Elevator) fixtureA.getUserData()).setPlayer(null);
                } else {
                    ((Elevator) fixtureB.getUserData()).setPlayer(null);
                }
                break;
            case GreenStory.PLAYER_FEET_BIT | GreenStory.ELEVATOR_GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT) {
                    ((Player) fixtureA.getUserData()).setOnElevator(false);
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT) {
                    ((Player) fixtureB.getUserData()).setOnElevator(false);
                }
                break;
            case GreenStory.WALL_BIT | GreenStory.PLAYER_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).setSlidingDownTheWall(false);
                } else {
                    ((Player) fixtureB.getUserData()).setSlidingDownTheWall(false);
                }
                break;


            case GreenStory.ATTACK_BIT | GreenStory.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ATTACK_BIT) {
                    ((Player) fixtureA.getUserData()).setBodyToPunch(null);
                } else {
                    ((Player) fixtureB.getUserData()).setBodyToPunch(null);

                }
                break;
            case GreenStory.ATTACK_BIT | GreenStory.BARREL_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.ATTACK_BIT) {
                    ((Player) fixtureA.getUserData()).setBodyToPunch(null);
                } else {
                    ((Player) fixtureB.getUserData()).setBodyToPunch(null);

                }
                break;

            case GreenStory.RIGHT_GRAB_BIT | GreenStory.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.RIGHT_GRAB_BIT) {
                    ((Player) fixtureA.getUserData()).setRightHandGrabbed(false);
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.RIGHT_GRAB_BIT) {
                    ((Player) fixtureB.getUserData()).setRightHandGrabbed(false);
                }
                break;

            case GreenStory.LEFT_GRAB_BIT | GreenStory.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.LEFT_GRAB_BIT) {
                    ((Player) fixtureA.getUserData()).setLeftHandGrabbed(false);
                } else if (fixtureB.getFilterData().categoryBits == GreenStory.LEFT_GRAB_BIT) {
                    ((Player) fixtureB.getUserData()).setLeftHandGrabbed(false);
                }
                break;
            case GreenStory.PLAYER_FEET_BIT | GreenStory.GROUND_BIT:
                if (fixtureA.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT && (fixtureA.getUserData() instanceof Player)) {
                    ((Player) (fixtureA.getUserData())).setReadyToJump(false);
                } else if(fixtureB.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT && (fixtureB.getUserData() instanceof Player)) {
                    ((Player) (fixtureB.getUserData())).setReadyToJump(false);
                }
                break;

        }

    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
