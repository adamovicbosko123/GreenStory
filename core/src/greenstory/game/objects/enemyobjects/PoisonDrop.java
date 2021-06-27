package greenstory.game.objects.enemyobjects;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;
import greenstory.game.utilities.GroundCreator;

public class PoisonDrop extends Sprite implements Telegraph, Disposable, Menace {
    private TextureRegion platformRegion;
    private TextureRegion drop;
    private Animation<TextureRegion> fallingDropAnimation;
    private Animation<TextureRegion> falledDropAnimation;
    private TextureRegion fallingDrop;
    private TextureRegion falledDrop;
    private float stateTimer = 0;
    private StateMachine<PoisonDrop, BloodDropsStates> fsm;
    private GroundCreator groundCreator;
    private PlayScreen playScreen;
    private Vector2 initialPosition;
    private Vector2 position;
    private World world;
    private Body upperAnchor, dropBody;
    private PrismaticJoint prismaticJoint;
    private boolean contactWithGround;
    private float time = 0;
    private Rectangle dropRectangle;
    private int count = 0;


    public enum BloodDropsStates implements State<PoisonDrop> {
        FALLING() {
            @Override
            public void enter(PoisonDrop entity) {
            }

            @Override
            public void update(PoisonDrop entity) {
                entity.setRegion(entity.fallingDropAnimation.getKeyFrame(entity.stateTimer, false));
            }

            @Override
            public void exit(PoisonDrop entity) {
            }

            @Override
            public boolean onMessage(PoisonDrop entity, Telegram telegram) {
                return false;
            }
        }, DROP() {
            @Override
            public void enter(PoisonDrop entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(PoisonDrop entity) {
                entity.setRegion(entity.drop);
            }

            @Override
            public void exit(PoisonDrop entity) {

            }

            @Override
            public boolean onMessage(PoisonDrop entity, Telegram telegram) {
                return false;
            }
        }, FALLED() {
            @Override
            public void enter(PoisonDrop entity) {
                entity.resetStateTimer();
            }

            @Override
            public void update(PoisonDrop entity) {

                entity.setRegion(entity.falledDropAnimation.getKeyFrame(entity.stateTimer, false));
            }

            @Override
            public void exit(PoisonDrop entity) {
            }

            @Override
            public boolean onMessage(PoisonDrop entity, Telegram telegram) {
                return false;
            }
        }

    }

    public PoisonDrop(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        Array<TextureRegion> frames = new Array<>();
        platformRegion = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));

        TextureRegion[][] textureRegions = platformRegion.split(64, 64);
        drop = textureRegions[0][16];

        TextureRegion[][] fallingDropRegions = platformRegion.split(64, 96);
        fallingDrop = fallingDropRegions[1][16];
        fallingDrop.setRegionWidth(64);
        fallingDrop.setRegionHeight(64);
        frames.add(fallingDrop);

        fallingDrop = fallingDropRegions[1][17];
        fallingDrop.setRegionWidth(64);
        fallingDrop.setRegionHeight(64);
        frames.add(fallingDrop);

        fallingDrop = fallingDropRegions[1][18];
        fallingDrop.setRegionWidth(64);
        fallingDrop.setRegionHeight(64);
        frames.add(fallingDrop);

        fallingDrop = fallingDropRegions[1][19];
        fallingDrop.setRegionWidth(64);
        fallingDrop.setRegionHeight(64);
        frames.add(fallingDrop);

        fallingDrop = fallingDropRegions[1][20];
        fallingDrop.setRegionWidth(64);
        fallingDrop.setRegionHeight(64);
        frames.add(fallingDrop);
        fallingDropAnimation = new Animation<TextureRegion>(0.4f, frames);
        frames.clear();

        TextureRegion[][] falledDropRegions = platformRegion.split(64, 64);
        falledDrop = falledDropRegions[3][16];
        frames.add(falledDrop);
        falledDrop = falledDropRegions[3][17];
        frames.add(falledDrop);
        falledDrop = falledDropRegions[3][18];
        frames.add(falledDrop);
        falledDropAnimation = new Animation<TextureRegion>(0.3f, frames);
        frames.clear();

        setBounds(0, 0, 64f / GreenStory.PPM, 64f / GreenStory.PPM);
        fsm = new DefaultStateMachine<>(this, BloodDropsStates.FALLING);

        initialPosition = new Vector2(x, y);
        groundCreator = new GroundCreator(playScreen);
        position = new Vector2(x, y);
        createPoisonDrop();
        setRegion(drop);
        dropRectangle = new Rectangle();
    }

    public void update(float delta) {
        fsm.update();
        setColor(Color.GREEN);
        stateTimer += delta % 14;
        if ((int) dropBody.getPosition().y == (int) initialPosition.y) {
            fsm.changeState(BloodDropsStates.FALLING);
            prismaticJoint.enableLimit(true);
            dropBody.setGravityScale(0);
            if ((time += delta) >= fallingDropAnimation.getAnimationDuration()) {
                dropBody.setGravityScale(1);
                prismaticJoint.enableMotor(true);
                prismaticJoint.setMotorSpeed(6);
                time = 0;
            }
        }
        if (contactWithGround) {
            fsm.changeState(BloodDropsStates.FALLED);
            if ((time += delta) >= 1) {
                prismaticJoint.enableMotor(true);
                prismaticJoint.setMotorSpeed(-50);
                time = 0;
                contactWithGround = false;
            }
        }
        if ((int) dropBody.getPosition().y != (int) initialPosition.y && dropBody.getLinearVelocity().y == 0) {
            prismaticJoint.enableLimit(false);
            if ((time += delta) >= 2) {
                prismaticJoint.enableMotor(true);
                prismaticJoint.setMotorSpeed(-3);
                time = 0;
            }
        }
        if (dropBody.getLinearVelocity().y < 0) {
            fsm.changeState(BloodDropsStates.DROP);
        }
        setPosition(dropBody.getPosition().x - getWidth() * 0.5f, dropBody.getPosition().y - getHeight() * 0.50f);
        if (fsm.getCurrentState() == BloodDropsStates.FALLED) {
            setY(getDropBody().getPosition().y - 0.25f);
        }
        dropRectangle.set(getX() + 0.32f, getY(), getWidth() - 0.59f, getHeight() - 0.9f);

    }

    public void setContactWithGround(boolean contact) {
        contactWithGround = contact;
    }



    public void createPoisonDrop() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);
        upperAnchor = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        dropBody = world.createBody(bodyDef);

        fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.25f);
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = GreenStory.ENEMY_BIT;
        fixtureDef.filter.maskBits = GreenStory.GROUND_BIT;
        dropBody.createFixture(fixtureDef).setUserData(this);
        //dropBody.setGravityScale(5f);
        dropBody.setUserData(this);
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.bodyA = upperAnchor;
        prismaticJointDef.bodyB = dropBody;
        prismaticJointDef.collideConnected = false;
        prismaticJointDef.localAxisA.set(0, -2f);
        prismaticJointDef.localAnchorA.set(0, -0f);
        prismaticJointDef.localAnchorB.set(0, 0f);
        prismaticJointDef.enableLimit = true;
        prismaticJointDef.upperTranslation = groundCreator.getGroundBody().getPosition().y;
        prismaticJointDef.lowerTranslation = 0f;
        prismaticJointDef.enableMotor = false;
        prismaticJointDef.maxMotorForce = 500.2f;
        prismaticJointDef.motorSpeed = .4f;


        prismaticJoint = (PrismaticJoint) world.createJoint(prismaticJointDef);
        prismaticJoint.setUserData(this);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

   // private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void draw(Batch batch) {
        if (dropBody.getLinearVelocity().y <= 0) {
            super.draw(batch);
            batch.end();
           // shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
           // shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            // shapeRenderer.rect(dropRectangle.x, dropRectangle.y, dropRectangle.width, dropRectangle.height);
            //shapeRenderer.end();
            batch.begin();
        }
    }

    public Body getDropBody() {
        return dropBody;
    }

    public Rectangle getDropRectangle() {
        if (this.fsm.isInState(BloodDropsStates.FALLING)) {
            return null;
        }
        return this.dropRectangle;
    }

    public void resetStateTimer() {
        stateTimer = 0;
    }

    //next two methods check for collision and make health lower down  by 1 point
    public void checkCollisionWithPlayer(Player player) {
        if (getDropRectangle() != null) {
            if (getDropRectangle().overlaps(player.getPlayerRectangle())) {
                dropTouchedPlayer(player);
            } else {
                count = 0;
            }

        }
    }

    public void dropTouchedPlayer(Player player) {
        if (count < 1 && player.getFSM().getCurrentState() != PlayerStatesWrapper.PlayerStates.DEATH) {
            player.getFSM().changeState(PlayerStatesWrapper.PlayerStates.HURT);
            count++;
        } else if (player.getHealth() <= 0) {
            player.getFSM().changeState(PlayerStatesWrapper.PlayerStates.DEATH);
        }
    }//end

    public void dispose() {
        platformRegion.getTexture().dispose();
        drop.getTexture().dispose();
        falledDrop.getTexture().dispose();
        fallingDrop.getTexture().dispose();
    }
}
