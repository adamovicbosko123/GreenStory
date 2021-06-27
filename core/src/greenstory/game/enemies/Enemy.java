package greenstory.game.enemies;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import greenstory.game.GreenStory;
import greenstory.game.enemies.assets.Blood;
import greenstory.game.objects.HealthBar;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.utilities.Vulnerable;
import greenstory.game.enemies.*;
import greenstory.game.enemies.states.*;
import greenstory.game.enemies.helpers.*;

public abstract class Enemy extends Sprite implements Telegraph, Vulnerable {
    protected Body body;
    protected PlayScreen playScreen;
    protected Vector2 velocity;
    private Vector2 initialPosition;
    protected World world;
    protected Player player;
    private float stateTimer;
    private int health;
    private int maxHealth;
    private float attackRange = 0;
    private int direction = 0;
    public boolean alert;
    private float chasingSpeed;
    private boolean animationPaused = false;
    public float[] aabb = new float[10];
    protected Blood blood;
    private boolean drawBlood = false;
    private HealthBar healthBar;
    private Actor sliderActor;
    private boolean hit = false;
    private boolean setToDestroy = false;
    private float scaleX = 0;
    private float scaleY = 0;
    private float posX = 0;
    private float posY = 0;
    private boolean destroyed = false;
    private int recievedPunchStrength = 0;

    @Override
    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    @Override
    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }


    public Enemy(PlayScreen playScreen) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        velocity = new Vector2(0, 0);
        initialPosition = new Vector2();
        player = playScreen.getPlayer();
        direction = -1;
        blood = new Blood();
    }


    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setSetToDestroy(boolean setToDestroy) {
        this.setToDestroy = setToDestroy;
    }

    public boolean isSetToDestroy() {
        return setToDestroy;
    }

    public void setHealthBar(Vulnerable vulnerable, float scale) {
        healthBar = new HealthBar(vulnerable, scale);
        healthBar.setWidth(healthBar.getWidth());
        healthBar.setHeight(healthBar.getHeight());
        sliderActor = healthBar.getHealthBarSlider();
        sliderActor.setWidth(sliderActor.getWidth());
        sliderActor.setHeight(sliderActor.getHeight());
    }


    //how much health the enemy has lost after punch
    public void setHit(boolean hit) {
        if (player.getStrength() > 66f) {
            recievedPunchStrength = 3;
        } else if (player.getStrength() > 33f && player.getStrength() <= 66f) {
            recievedPunchStrength = 2;
        } else {
            recievedPunchStrength = 1;
        }
        if (player.swordHolding) {
            recievedPunchStrength *= 2;
        }
        this.hit = hit;
        // System.out.println(recievedPunchStrength);
    }

    public int getRecievedPunchStrength() {
        return this.recievedPunchStrength;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHealthBarPosition(float x, float y) {
        healthBar.setPosition(body.getPosition().x + x, body.getPosition().y + y);
    }

    public HealthBar getHealthBar() {
        return this.healthBar;
    }

    public void healthBarUpdate(float delta) {
        healthBar.update(delta);
    }

    public void setDrawBlood(boolean drawBlood) {
        this.drawBlood = drawBlood;
    }

    public boolean isDrawBlood() {
        return drawBlood;
    }

    public void setMaxHealth(int max) {
        maxHealth = max;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    //uses some calculation for variable punching strength
    public void damageHealth() {
        this.health -= recievedPunchStrength;
    }

    //punches player with some force
    public void punchBody() {
        if (player.getPlayerBody().getPosition().x > body.getPosition().x) {
            player.getPlayerBody().applyForceToCenter(new Vector2(20.1f, 0), true);
            player.setHit(true);
        } else {
            player.getPlayerBody().applyForceToCenter(new Vector2(-20.1f, 0), true);
            player.setHit(true);
        }
    }

    //here i should check if it combat with big cultist, skeleton or others
    public boolean isInRange() {
        if (!(this instanceof Skeleton) && !(this instanceof FireCultist)) {
            if (player.getPlayerRectangle().overlaps(getRightAttackArea()) && isFlipX()) {
                return true;
            } else if (player.getPlayerRectangle().overlaps(getLeftAttackArea()) && !isFlipX()) {
                return true;
            }
        } else if (!(this instanceof FireCultist)) {
            if (player.getPlayerRectangle().overlaps(getRightAttackArea()) && !isFlipX()) {
                return true;
            } else if (player.getPlayerRectangle().overlaps(getLeftAttackArea()) && isFlipX()) {
                return true;
            }
        }
        return false;

    }

    //Checks for horizontal distance
    public float xDistanceFromPlayer() {
        Vector2 playerPosition = player.getPlayerBody().getPosition();
        Vector2 enemyPosition = body.getPosition();
        return playerPosition.x - enemyPosition.x;
    }

    public void update(float delta) {

        if (!animationPaused) {
            stateTimer += delta % 15;
        }
        aabb[0] = body.getPosition().x - 3f;
        aabb[1] = body.getPosition().y - 0.2f;

        aabb[2] = body.getPosition().x + 3f;
        aabb[3] = body.getPosition().y - 0.2f;

        aabb[4] = body.getPosition().x + 3f;
        aabb[5] = body.getPosition().y + 0.4f;

        aabb[6] = body.getPosition().x - 3f;
        aabb[7] = body.getPosition().y + 0.4f;

        aabb[8] = body.getPosition().x - 3f;
        aabb[9] = body.getPosition().y - 0.2f;

        world.QueryAABB(areaCallback, aabb[0], aabb[1], aabb[4], aabb[5]);

        if (isSetToDestroy() && !isDestroyed()) {
            world.destroyBody(body);
            setDestroyed(true);
        }
        if (player.isOnElevator() || player.getFSM().getCurrentState() == PlayerStatesWrapper.PlayerStates.LEFT_GRAB
                || player.getFSM().getCurrentState() == PlayerStatesWrapper.PlayerStates.RIGHT_GRAB
                || player.isPlayerOnLadder() || player.getFSM().isInState(PlayerStatesWrapper.PlayerStates.JUMPING)) {
            alert = false;
        }

    }

    //set condition for blood animation and its position
    public void enemyBleeding(float delta, float x, float y) {
            getBlood().setPosition(getBody().getPosition().x + x, getBody().getPosition().y + y);
            getBlood().update(delta);
            setDrawBlood(true);

    }

    public Blood getBlood() {
        return this.blood;
    }

    public abstract void createEnemy();


    public Body getBody() {
        return this.body;
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void resetStateTimer() {
        stateTimer = 0;
    }

    public boolean isAnimationPaused() {
        return animationPaused;
    }

    public void setAnimationPaused(boolean paused) {
        this.animationPaused = paused;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public void setInitialPosition(float x, float y) {
        this.initialPosition.set(x, y);
    }

    public Vector2 getInitialPosition() {
        return initialPosition;
    }


    //called from chase task
    public void turnToPlayerForChase() {
        if (this instanceof Cultist || this instanceof Assassin || this instanceof BigCultist || this instanceof FireCultist) {
            if (xDistanceFromPlayer() > 0) {
                flip(true, false);
                body.setLinearVelocity(chasingSpeed, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(-chasingSpeed, body.getLinearVelocity().y);
            }
        }
        if (this instanceof Skeleton) {
            if (xDistanceFromPlayer() < 0) {
                flip(true, false);
                body.setLinearVelocity(-chasingSpeed, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(chasingSpeed, body.getLinearVelocity().y);
            }

        }
    }//end of method called from chase task


    //keeps an eye on the player
    public void turnToPlayer() {
        if (this instanceof Cultist || this instanceof Assassin || this instanceof BigCultist || this instanceof FireCultist) {
            if (xDistanceFromPlayer() > 0) {
                flip(true, false);
            }
        }
        if (this instanceof Skeleton) {
            if (xDistanceFromPlayer() < 0) {
                flip(true, false);
            }
        }
    }

    // checks if player fixture is on the range
    public QueryCallback areaCallback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (fixture.getFilterData().categoryBits == GreenStory.PLAYER_BIT) {
                alert = true;
            }
            return true;
        }

    };

    //implementation are left for subclasses to make
    public Rectangle getRightAttackArea() {
        return null;
    }

    public void setRightAttackArea() {
    }

    public Rectangle getLeftAttackArea() {
        return null;
    }

    public void setLeftAttackArea() {
    }


    public void setChasingSpeed(float chasingSpeed) {
        this.chasingSpeed = chasingSpeed;
    }
//

    public void draw(Batch batch) {

        if (isDrawBlood()) {
            getBlood().draw(batch);
        }

    }

}
