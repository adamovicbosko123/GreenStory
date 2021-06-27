package greenstory.game.enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import greenstory.game.GreenStory;
import greenstory.game.enemies.states.SkeletonStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Skeleton extends Enemy implements Telegraph {
    private static float WIDTH = 32 / GreenStory.PPM;
    private static float HEIGHT = 32 / GreenStory.PPM;
    private DefaultStateMachine<Skeleton, SkeletonStatesWrapper.SkeletonStates> fsm;
    private SkeletonStatesWrapper skeletonStatesWrapper;
    private Rectangle rightAttackArea, leftAttackArea;
    public Sound skeletonScream, skeletonDie,skeletonSwing,skeletonPunch;


    public Skeleton(PlayScreen playScreen, float x, float y) {
        super(playScreen);
        setInitialPosition(x, y);
        createEnemy();
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        skeletonStatesWrapper = new SkeletonStatesWrapper();
        fsm = new DefaultStateMachine<Skeleton, SkeletonStatesWrapper.SkeletonStates>(this, SkeletonStatesWrapper.SkeletonStates.IDLE);
        setAttackRange(1.2f);
        setMaxHealth(10);
        setHealth(getMaxHealth());
        setChasingSpeed(0.7f);
        setHealthBar(this, 12f);
        rightAttackArea = new Rectangle();
        leftAttackArea = new Rectangle();
        setInitialPosition(body.getPosition().x, body.getPosition().y);
        skeletonScream = StageLoadingScreen.manager.get("sounds/skeletonScream.wav");
        skeletonDie = StageLoadingScreen.manager.get("sounds/skeletonDie.wav");
        skeletonSwing = StageLoadingScreen.manager.get("sounds/swish.wav");
        skeletonPunch = StageLoadingScreen.manager.get("sounds/skeletonSwing.wav");


    }



    public DefaultStateMachine<Skeleton, SkeletonStatesWrapper.SkeletonStates> getFSM() {
        return fsm;
    }


    public void update(float delta) {
        super.update(delta);
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y / getHeight() / 2);
        fsm.update();
        if (isFlipX())
            setHealthBarPosition(-0.3f, 0.4f);
        else
            setHealthBarPosition(0, 0.4f);

        healthBarUpdate(delta);
        setLeftAttackArea();
        setRightAttackArea();

        if (getFSM().isInState(SkeletonStatesWrapper.SkeletonStates.ATTACK)) {
            setScaleX(1.5f);
            setScaleY(1.4f);
            if (isFlipX()) {
                setPosX((body.getPosition().x - WIDTH * 0.5f) - 0.3f);
                setPosY((body.getPosition().y - HEIGHT * 0.5f) + 0.18f);
            } else {
                setPosX((body.getPosition().x - WIDTH * 0.5f) + 0.3f);
                setPosY((body.getPosition().y - HEIGHT * 0.5f) + 0.16f);
            }
        } else {
            setScaleX(1f);
            setScaleY(1.1f);
            if (isFlipX()) {
                setPosX((body.getPosition().x - WIDTH * 0.5f) - 0.2f);
            } else {
                setPosX((body.getPosition().x - WIDTH * 0.5f) + 0.2f);
            }
            setPosY((body.getPosition().y - HEIGHT * 0.5f) + 0.05f);
        }


        if (getFSM().isInState(SkeletonStatesWrapper.SkeletonStates.DEATH)) {
            if (!isFlipX()) {
                enemyBleeding(delta, -0.2f, -0.95f);
            } else {
                enemyBleeding(delta, 0, 0);
            }
        }
    }

    public Rectangle getRightAttackArea() {
        return rightAttackArea;
    }

    public void setRightAttackArea() {
        rightAttackArea.set(body.getPosition().x, body.getPosition().y - 0.3f, 0.7f, 0.8f);
    }

    public Rectangle getLeftAttackArea() {
        return this.leftAttackArea;
    }

    public void setLeftAttackArea() {
        leftAttackArea.set(body.getPosition().x - 0.7f, body.getPosition().y - 0.3f, 0.7f, 0.8f);

    }


    public void draw(Batch batch) {
        batch.draw(this, getPosX(), getPosY(), WIDTH * 0.5f, HEIGHT * 0.5f
                , WIDTH, HEIGHT, getScaleX(), getScaleY(), body.getAngle() * MathUtils.radiansToDegrees);
        super.draw(batch);
        if ((xDistanceFromPlayer() < 2 && xDistanceFromPlayer() > -2) && !getFSM().isInState(SkeletonStatesWrapper.SkeletonStates.ATTACK)) {
            if (!isDestroyed())
                getHealthBar().draw(batch, 1);
        }
    }

    public void createEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getInitialPosition().x, getInitialPosition().y);
        bodyDef.fixedRotation = false;
        FixtureDef fixtureDef;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        MassData massData = new MassData();
        massData.mass = 100;
        body.setMassData(massData);
        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = GreenStory.ENEMY_BIT;
        fixtureDef.filter.maskBits = GreenStory.PLAYER_BIT | GreenStory.GROUND_BIT |
                GreenStory.ATTACK_BIT | GreenStory.GROUND_BIT |
                GreenStory.DEATH_BIT | GreenStory.WALL_BIT;
        PolygonShape polygonShape = new PolygonShape();
        Vector2[] vtcs = new Vector2[6];
        vtcs[0] = new Vector2(-0.0f, -0.29f);
        vtcs[1] = new Vector2(-0.12f, -0.18f);
        vtcs[2] = new Vector2(0.12f, -0.18f);
        vtcs[3] = new Vector2(-0.12f, 0.18f);
        vtcs[4] = new Vector2(0.12f, 0.18f);
        vtcs[5] = new Vector2(0.0f, 0.30f);
        polygonShape.set(vtcs);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);
        polygonShape.dispose();
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        return fsm.handleMessage(msg);
    }

}
