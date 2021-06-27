package greenstory.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.enemies.Enemy;
import greenstory.game.enemies.assets.Blood;
import greenstory.game.enemies.assets.FireBall;
import greenstory.game.objects.harmlesobjects.Aureol;
import greenstory.game.objects.harmlesobjects.Barrel;
import greenstory.game.player.assets.Bow;
import greenstory.game.screens.AbstractScreen;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.Vulnerable;

import java.util.Random;

public class Player extends Sprite implements Disposable, Telegraph, Vulnerable {
    private DefaultStateMachine<Player, PlayerStatesWrapper.PlayerStates> fsm;
    private Vector2 position;
    private World world;
    public Body playerBody;
    private FixtureDef fixtureDef;
    private float stateTimer;
    private PlayerStatesWrapper playerStatesWrapper;
    public boolean swordHolding;
    private int direction = 0;
    private Body bodyToPunch;
    private Array<Enemy> enemies;
    private AbstractScreen screen;
    private boolean hit;
    private int health = 0;
    private int maxHealth = 0;
    private boolean onElevator = false;
    private Rectangle playerRectangle;
    private Aureol aureol;
    private boolean playerOnLadder = false;
    private boolean slidingDownTheWall = false;
    private boolean drawBlood = false;
    private Blood blood;
    public Array<Bow> arrows;
    private boolean rightHandGrabbed;
    private boolean leftHandGrabbed;
    private float maxStrength = 0;
    private float strength = 0;
    private int arrowCount = 0;
    private int maxArrowCount = 0;

    private int maxEnergyTime = 0;
    private int energyTime = 0;
    Random random = new Random();
    private boolean metWithTheWife = false;
    private float timerForEnergy = 0;
    private boolean startAureol = false;
    private boolean readyToJump = false;
    private float runningForce = 0;
    public Sound gameOverSound, swish, bow, barrelImpact, punch;


    public Player(World world, AbstractScreen screen) {
        this.world = world;
        this.screen = screen;
        TextureRegion idle = new TextureRegion(new Texture("idle.png"));
        setRegion(idle);
        setBounds(0, 0, idle.getRegionWidth(), idle.getRegionHeight());
        position = new Vector2(4, 4);
        setPosition(position.x, position.y);
        definePlayer();
        setMaxHealth(84);
        setHealth(getMaxHealth());
        fsm = new DefaultStateMachine<Player, PlayerStatesWrapper.PlayerStates>(this, PlayerStatesWrapper.PlayerStates.IDLE);
        playerStatesWrapper = new PlayerStatesWrapper();
        enemies = new Array<>();
        playerRectangle = new Rectangle();
        aureol = new Aureol(this.screen, 0, 0);
        blood = new Blood();
        arrows = new Array<>();
        setMaxStrength(100);
        setStrength(getMaxStrength());

        setMaxArrowCount(12);
        setArrowCount(getMaxArrowCount());

        setMaxEnergyTime(10);
        setEnergyTime(getMaxEnergyTime());
        setRunningForce(25);
        swish = StageLoadingScreen.manager.get("sounds/swish.wav");
        barrelImpact = StageLoadingScreen.manager.get("sounds/barrelImpact.wav");
        punch = StageLoadingScreen.manager.get("sounds/punch.wav");
        bow = StageLoadingScreen.manager.get("sounds/bow.wav");
    }

    public Sound getGameOverSound() {
        return gameOverSound;
    }

    public void setRunningForce(float runningForce) {
        this.runningForce = runningForce;
    }

    public float getRunningForce() {
        return this.runningForce;
    }

    public void setReadyToJump(boolean readyToJump) {
        this.readyToJump = readyToJump;
    }

    public boolean isReadyToJump() {
        return readyToJump;
    }

    //this blocks check are player in contact with his wife
    public void setMetWithTheWife(boolean metWithTheWife) {
        this.metWithTheWife = metWithTheWife;
    }

    public boolean isMetWithTheWife() {
        return metWithTheWife;
    }
    //

    //this blocks are working on players state of energy during collected cans
    public boolean isInTimeOfMaxEnergy() {
        return getEnergyTime() > 0;
    }

    public int getMaxEnergyTime() {
        return this.maxEnergyTime;
    }

    public void setMaxEnergyTime(int maxEnergyTime) {
        this.maxEnergyTime = maxEnergyTime;
    }

    public int getEnergyTime() {
        return this.energyTime;
    }

    public void setEnergyTime(int energyTime) {
        this.energyTime = energyTime;
    }
//

    //arrow collecting
    public int getArrowCount() {
        return this.arrowCount;
    }

    public void setArrowCount(int arrowCount) {
        this.arrowCount = arrowCount;
    }


    public int getMaxArrowCount() {
        return this.maxArrowCount;
    }

    public void setMaxArrowCount(int maxArrowCount) {
        this.maxArrowCount = maxArrowCount;
    }

    //


    public float getStrength() {
        return this.strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public float getMaxStrength() {
        return this.maxStrength;
    }

    public void setMaxStrength(float maxStrength) {
        this.maxStrength = maxStrength;
    }

    //behavior during hang with hands grabed on solid object contacts
    public void setLeftHandGrabbed(boolean leftHandGrabbed) {
        this.leftHandGrabbed = leftHandGrabbed;

    }

    public boolean isLeftHandGrabbed() {
        return leftHandGrabbed;
    }

    public void grabLeft() {
        for (Fixture f : playerBody.getFixtureList()) {
            if (f.getFilterData().categoryBits == GreenStory.LEFT_GRAB_BIT) {
                f.setSensor(false);
                f.setDensity(1);
                f.setFriction(1);
            }
        }
    }

    public void ungrabLeft() {
        for (Fixture f : playerBody.getFixtureList()) {
            if (f.getFilterData().categoryBits == GreenStory.LEFT_GRAB_BIT) {
                f.setSensor(true);
                f.setDensity(0);
            }
        }
    }

    public void setRightHandGrabbed(boolean rightHandGrabbed) {
        this.rightHandGrabbed = rightHandGrabbed;

    }

    public boolean isRightHandGrabbed() {
        return rightHandGrabbed;
    }

    public void grabRight() {
        for (Fixture f : playerBody.getFixtureList()) {
            if (f.getFilterData().categoryBits == GreenStory.RIGHT_GRAB_BIT) {
                f.setSensor(false);
                f.setDensity(1);
                f.setFriction(1);
            }
        }
    }


    public void ungrabRight() {
        for (Fixture f : playerBody.getFixtureList()) {
            if (f.getFilterData().categoryBits == GreenStory.RIGHT_GRAB_BIT) {
                f.setSensor(true);
                f.setDensity(0);
            }
        }


    }
//

    //sliding down the wall and climbing ladder contacts
    public void setSlidingDownTheWall(boolean slidingDownTheWall) {
        this.slidingDownTheWall = slidingDownTheWall;
    }

    public boolean isSlidingDownTheWall() {
        return slidingDownTheWall;
    }

    public void setPlayerOnLadder(boolean playerOnLadder) {
        this.playerOnLadder = playerOnLadder;
    }

    public boolean isPlayerOnLadder() {
        return playerOnLadder;
    }
//


    //Vulnerable interface implementation
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

    public void damageHealth() {
        this.health--;
    }
//

    //elevator contacts and behaviors
    public void setOnElevator(boolean onElevator) {
        this.onElevator = onElevator;
    }

    public boolean isOnElevator() {
        return onElevator;
    }

    public void playerBehaviorInElevator() {
        swordAlreadyHolding = false;
        swordHolding = false;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            fsm.changeState(PlayerStatesWrapper.PlayerStates.RUNNING);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            fsm.changeState(PlayerStatesWrapper.PlayerStates.RUNNING);
        } else {
            fsm.changeState(PlayerStatesWrapper.PlayerStates.IDLE);
        }
    }
//


    //here i worked on healer behavior
    public void startHealing() {
        startAureol = true;
        aureol.startAureol();
    }

    public boolean playerOverlapsHealer(Rectangle rectangle) {
        if (this.getPlayerRectangle().overlaps(rectangle)) {
            return true;
        } else {
            return false;
        }
    }
//

    //main player update loop
    public void update(float delta) {
        strength = MathUtils.clamp(strength, 0, maxStrength);
        stateTimer += delta % 5;
        setScale(1f / GreenStory.PPM);
        listenInput();
        setPosition(playerBody.getPosition().x - ((getWidth() / 2) / GreenStory.PPM), (playerBody.getPosition().y - ((getHeight() / 2) - 3f) / GreenStory.PPM));
        fsm.update();
        if (startAureol) {
            aureol.update(delta);
            aureol.setPosition(getX() + 0.1f, getY() - 0.1f);
        }
        //this block is strict connected to player standing in the elevator
        if (this.isOnElevator()) {
            playerBehaviorInElevator();
        }//end
        setPlayerRectangle();

        if (getFSM().isInState(PlayerStatesWrapper.PlayerStates.DEATH)) {
            die(delta);
        }


        if (!arrows.isEmpty()) {
            for (int i = 0; i < arrows.size; i++) {
                if (!arrows.get(i).isDestroyed()) {
                    arrows.get(i).update(delta);
                }
            }

            for (int i = 0; i < arrows.size; i++) {
                if (arrows.get(i).isDestroyed()) {
                    arrows.removeValue(arrows.get(i), true);
                }
            }
        }
        if (!getFSM().isInState(PlayerStatesWrapper.PlayerStates.RIGHT_GRAB)) {
            rightHandGrabbed = false;
            ungrabRight();
        }
        if (!getFSM().isInState(PlayerStatesWrapper.PlayerStates.LEFT_GRAB)) {
            leftHandGrabbed = false;
            ungrabLeft();
        }

        if (!getFSM().isInState(PlayerStatesWrapper.PlayerStates.IDLE) && !isInTimeOfMaxEnergy()) {
            strength -= 0.09f;
        } else {
            strength += 0.25f;

        }
        //strength = stamina / maxStamina;
        if (arrows.size >= 4) {
            arrows.clear();
        }

        if ((timerForEnergy += Gdx.graphics.getDeltaTime()) >= 1) {
            energyTime--;
            if (energyTime <= 0) {
                energyTime = 0;
            }
            timerForEnergy = 0;
        }

        if (getFSM().isInState(PlayerStatesWrapper.PlayerStates.PULL_UP)) {
            for (Fixture fixture : playerBody.getFixtureList()) {
                if (fixture.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT) {
                    fixture.setSensor(true);
                }
            }
        } else {
            for (Fixture fixture : playerBody.getFixtureList()) {
                if (fixture.getFilterData().categoryBits == GreenStory.PLAYER_FEET_BIT) {
                    fixture.setSensor(false);
                }
            }
        }


    }
    //

    //this block is fine tuning the rectangle around player, for better collision
    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }

    public void setPlayerRectangle() {
        if (direction == 0) {
            playerRectangle.set(getX() + 0.3f, getY(), getWidth() / GreenStory.PPM - 0.6f, getHeight() / GreenStory.PPM - 0.1f);
        } else if (direction == 1) {
            playerRectangle.set(getX() + 0.3f, getY(), getWidth() / GreenStory.PPM - 0.6f, getHeight() / GreenStory.PPM - 0.1f);
        } else {
            playerRectangle.set(getX() + 0.27f, getY(), getWidth() / GreenStory.PPM - 0.6f, getHeight() / GreenStory.PPM - 0.1f);

        }//end
    }

    public void die(float delta) {
        drawBlood = true;
        blood.update(delta);
        blood.setPosition(getX() + 0.1f, getY() - 0.63f);
    }


    public void listenInput() {
        if (!(getFSM().isInState(PlayerStatesWrapper.PlayerStates.DEATH)) && !screen.isEndOfStage()) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playerBody.getLinearVelocity().x <= 2 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !leftHandGrabbed && !rightHandGrabbed && !getFSM().isInState(PlayerStatesWrapper.PlayerStates.PULL_UP) && getFSM().getPreviousState() != PlayerStatesWrapper.PlayerStates.PULL_UP) {
                playerBody.applyForceToCenter(getRunningForce(), 0, true);
                direction = 1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && playerBody.getLinearVelocity().x >= -2 && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !leftHandGrabbed && !rightHandGrabbed && !getFSM().isInState(PlayerStatesWrapper.PlayerStates.PULL_UP) && getFSM().getPreviousState() != PlayerStatesWrapper.PlayerStates.PULL_UP) {
                playerBody.applyForceToCenter(-getRunningForce(), 0, true);
                direction = -1;
            } else {
                playerBody.applyForceToCenter(0, 0, false);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isPlayerOnLadder() && isReadyToJump()) {
                playerBody.applyForceToCenter(0, 110, true);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                swordHolding = !swordHolding;
            }
        }
    }

    public boolean swordAlreadyHolding = false;

    public int getDirection() {
        return direction;
    }

    public Vector2 getLinearVelocity() {
        return playerBody.getLinearVelocity();
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void resetStateTimer() {
        stateTimer = 0;
    }

    public DefaultStateMachine<Player, PlayerStatesWrapper.PlayerStates> getFSM() {
        return fsm;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isHit() {
        return hit;
    }

    private void definePlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 1f;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());
        playerBody = world.createBody(bodyDef);


        PolygonShape polygonShape = new PolygonShape();
        Vector2[] vtcs = new Vector2[6];
        vtcs[0] = new Vector2(-0.0f, -0.29f);
        vtcs[1] = new Vector2(-0.15f, -0.18f);
        vtcs[2] = new Vector2(0.15f, -0.18f);
        vtcs[3] = new Vector2(-0.15f, 0.18f);
        vtcs[4] = new Vector2(0.15f, 0.18f);
        vtcs[5] = new Vector2(0.0f, 0.30f);
        polygonShape.set(vtcs);

        fixtureDef = new FixtureDef();
        fixtureDef.density = 1.7f;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = GreenStory.PLAYER_BIT;
        fixtureDef.filter.maskBits = GreenStory.ENEMY_BIT
                | GreenStory.WALL_BIT
                | GreenStory.ELEVATOR_SENSOR_BIT | GreenStory.ELEVATOR_GROUND_BIT |
                GreenStory.DEATH_BIT | GreenStory.WIFE_BIT;

        fixtureDef.shape = polygonShape;

        playerBody.createFixture(fixtureDef).setUserData(this);
        playerBody.setUserData(this);

        //creates bottom
        fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0;
        fixtureDef.density = 5;
        fixtureDef.friction = 0.5f;
        fixtureDef.filter.categoryBits = GreenStory.PLAYER_FEET_BIT;
        fixtureDef.filter.maskBits = GreenStory.ELEVATOR_GROUND_BIT | GreenStory.GROUND_BIT
                | GreenStory.DEATH_BIT | GreenStory.BARREL_BIT;
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.04f, 0.12f, new Vector2(0, -0.18f), 0);
        fixtureDef.shape = polygonShape;

        playerBody.createFixture(fixtureDef).setUserData(this);
        playerBody.setUserData(this);
        //

        polygonShape.setAsBox(1.32f, 0.28f);
        fixtureDef.isSensor = true;
        fixtureDef.density = 0;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = GreenStory.PLAYER_SENSOR;
        fixtureDef.filter.maskBits = GreenStory.ENEMY_BIT
                | GreenStory.DEATH_BIT;
        fixtureDef.shape = polygonShape;

        playerBody.createFixture(fixtureDef).setUserData(this);
        playerBody.setUserData(this);

        FixtureDef sensorFixture = new FixtureDef();
        PolygonShape polygonShape5 = new PolygonShape();
        Vector2[] vertices4 = new Vector2[6];
        vertices4[0] = new Vector2(0.08f, -0.25f);
        vertices4[1] = new Vector2(0.30f, -0.20f);
        vertices4[2] = new Vector2(0.49f, -0.10f);
        vertices4[3] = new Vector2(0.46f, -0.05f);
        vertices4[4] = new Vector2(0.15f, 0.0f);
        vertices4[5] = new Vector2(0.0f, 0.1f);
        polygonShape5.set(vertices4);

        sensorFixture.shape = polygonShape5;
        sensorFixture.isSensor = true;
        sensorFixture.density = 0;
        sensorFixture.friction = 0;
        sensorFixture.filter.categoryBits = GreenStory.ATTACK_BIT;
        sensorFixture.filter.maskBits = GreenStory.ENEMY_BIT | GreenStory.BARREL_BIT;
        playerBody.createFixture(sensorFixture).setUserData(this);
//////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////
        sensorFixture = new FixtureDef();
        PolygonShape polygonShape6 = new PolygonShape();
        Vector2[] vertices5 = new Vector2[6];
        vertices5[0] = new Vector2(-0.08f, -0.25f);
        vertices5[1] = new Vector2(-0.30f, -0.20f);
        vertices5[2] = new Vector2(-0.49f, -0.10f);
        vertices5[3] = new Vector2(-0.46f, -0.05f);
        vertices5[4] = new Vector2(-0.15f, 0.0f);
        vertices5[5] = new Vector2(-0.0f, 0.1f);
        polygonShape6.set(vertices5);

        sensorFixture.shape = polygonShape6;
        sensorFixture.isSensor = true;
        sensorFixture.density = 0;
        sensorFixture.friction = 0;
        sensorFixture.filter.categoryBits = GreenStory.ATTACK_BIT;
        sensorFixture.filter.maskBits = GreenStory.ENEMY_BIT | GreenStory.BARREL_BIT;
        playerBody.createFixture(sensorFixture).setUserData(this);
        //////////////////////////////////////////////////////////////////
        sensorFixture = new FixtureDef();
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.1f, 0.1f, new Vector2(0.25f, 0.4f), 0);
        sensorFixture.shape = polygonShape;
        sensorFixture.isSensor = true;
        sensorFixture.density = 0;
        sensorFixture.friction = 1;
        sensorFixture.restitution = 0;
        sensorFixture.filter.categoryBits = GreenStory.RIGHT_GRAB_BIT;
        sensorFixture.filter.maskBits = GreenStory.GROUND_BIT;
        playerBody.createFixture(sensorFixture).setUserData(this);
        ////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////
        sensorFixture = new FixtureDef();
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.1f, 0.1f, new Vector2(-0.25f, 0.4f), 0);
        sensorFixture.shape = polygonShape;
        sensorFixture.isSensor = true;
        sensorFixture.density = 0;
        sensorFixture.friction = 1;
        sensorFixture.restitution = 0;
        sensorFixture.filter.categoryBits = GreenStory.LEFT_GRAB_BIT;
        sensorFixture.filter.maskBits = GreenStory.GROUND_BIT;
        playerBody.createFixture(sensorFixture).setUserData(this);
        ////////////////////////////////////////////////////////////

        polygonShape.dispose();
        polygonShape5.dispose();
        polygonShape6.dispose();
    }


    public void dispose() {
    }

    public Body getPlayerBody() {
        return this.playerBody;
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        return fsm.handleMessage(msg);
    }

    public void setBodyToPunch(Body body) {
        this.bodyToPunch = body;
    }

    public Body getBodyToPunch() {
        if (bodyToPunch != null && !(bodyToPunch.getUserData() instanceof FireBall))
            return bodyToPunch;
        else
            return null;
    }

    //adds and remove enemy from player sensor
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy) {
        if (enemies.size > 0) {
            for (Enemy e : enemies) {
                enemies.removeValue(enemy, true);
            }
        }

    }

//*********************************

    public boolean distancingHimself(Body body) {
        float bodyX = body.getPosition().x;
        float playerX = playerBody.getPosition().x;
        float difference = playerX - bodyX;
        if (difference < 0 && direction == -1) {
            return true;
        } else if (difference > 0 && direction == 1) {
            return true;
        }
        return false;
    }

    public void punchBody() {
        if (bodyToPunch.getPosition().x > playerBody.getPosition().x) {
            Object o = bodyToPunch.getUserData();
            if (o instanceof Enemy) {
                Enemy e = (Enemy) o;
                if (!swordHolding) {
                    switch (e.getRecievedPunchStrength()) {
                        case 3:
                            e.getBody().applyForce(new Vector2(5000, 0), e.getBody().getWorldCenter(), true);
                            break;
                        case 2:
                            e.getBody().applyForce(new Vector2(4000, 0), e.getBody().getWorldCenter(), true);
                            break;
                        case 1:
                        default:
                            e.getBody().applyForce(new Vector2(3000, 0), e.getBody().getWorldCenter(), true);
                            break;
                    }
                }
                e.setHit(true);
            }
            if (o instanceof Barrel) {
                Barrel barrel = (Barrel) o;

                barrel.getBody().applyForce(new Vector2(125.1f, 0.5f), barrel.getBody().getWorldCenter(), true);
                if (!swordHolding && MathUtils.randomBoolean(0.03f)) {
                    barrel.setSetToDestroy(true);
                } else if (swordHolding && MathUtils.randomBoolean(0.1f)) {
                    barrel.setSetToDestroy(true);
                }
            }
        } else {
            Object o = bodyToPunch.getUserData();
            if (o instanceof Enemy) {
                Enemy e = (Enemy) o;
                if (!swordHolding) {
                    switch (e.getRecievedPunchStrength()) {
                        case 3:
                            e.getBody().applyForce(new Vector2(-5000, 0), e.getBody().getWorldCenter(), true);
                            break;
                        case 2:
                            e.getBody().applyForce(new Vector2(-4000, 0), e.getBody().getWorldCenter(), true);
                            break;
                        case 1:
                        default:
                            e.getBody().applyForce(new Vector2(-3000, 0), e.getBody().getWorldCenter(), true);
                            break;
                    }
                }

                e.setHit(true);
            }
            if (o instanceof Barrel) {
                Barrel barrel = (Barrel) o;

                barrel.getBody().applyForce(new Vector2(-125.1f, 0.5f), barrel.getBody().getWorldCenter(), true);
                if (!swordHolding && MathUtils.randomBoolean(0.03f)) {
                    barrel.setSetToDestroy(true);
                } else if (swordHolding && MathUtils.randomBoolean(0.1f)) {
                    barrel.setSetToDestroy(true);
                }
            }
        }
    }

    public Array<Enemy> getEnemies() {
        return new Array<>(enemies);
    }


    // private ShapeRenderer renderer = new ShapeRenderer();

    public void draw(Batch batch) {
        batch.end();
        batch.setProjectionMatrix(screen.getBatch().getProjectionMatrix());
        batch.begin();
        super.draw(batch);
        if (drawBlood) {
            blood.draw(batch);
        }
        aureol.draw(batch);
        batch.end();
        // renderer.begin(ShapeRenderer.ShapeType.Line);
        // renderer.rect(playerRectangle.x, playerRectangle.y, playerRectangle.width, playerRectangle.height);
        // renderer.end();

        batch.begin();
    }

}
