package greenstory.game.enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import greenstory.game.GreenStory;
import greenstory.game.enemies.states.BigCultistStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class BigCultist extends Enemy implements Telegraph {
    private static float WIDTH = 94 / GreenStory.PPM;
    private static float HEIGHT = 64 / GreenStory.PPM;
    private DefaultStateMachine<BigCultist, BigCultistStatesWrapper.BigCultistStates> fsm;
    private BigCultistStatesWrapper bigCultistStatesWrapper;
    private Rectangle rightAttackArea;
    private Rectangle leftAttackArea;
    public Sound bigCultistScream, bigCultistDie, bigCultistSwing, bigCultistPunch;


    public BigCultist(PlayScreen playScreen, float x, float y) {
        super(playScreen);
        this.playScreen = playScreen;
        setInitialPosition(x, y);
        createEnemy();
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        bigCultistStatesWrapper = new BigCultistStatesWrapper();
        fsm = new DefaultStateMachine<BigCultist, BigCultistStatesWrapper.BigCultistStates>(this, BigCultistStatesWrapper.BigCultistStates.IDLE);
        setMaxHealth(20);
        setHealth(getMaxHealth());
        setAttackRange(1f);
        setChasingSpeed(1.8f);
        setHealthBar(this, 12f);
        rightAttackArea = new Rectangle();
        leftAttackArea = new Rectangle();
        bigCultistScream = StageLoadingScreen.manager.get("sounds/bigCultistScream.wav");
        bigCultistDie = StageLoadingScreen.manager.get("sounds/bigCultistDie.wav");
        bigCultistSwing = StageLoadingScreen.manager.get("sounds/swish.wav");
        bigCultistPunch = StageLoadingScreen.manager.get("sounds/skeletonSwing.wav");

    }


    public DefaultStateMachine<BigCultist, BigCultistStatesWrapper.BigCultistStates> getFSM() {
        return fsm;
    }


    @Override
    public void update(float delta) {

        super.update(delta);
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y / getHeight() / 2);
        setRightAttackArea();
        setLeftAttackArea();
        fsm.update();
        if (!isFlipX())
            setHealthBarPosition(-0.1f, 0.48f);
        else
            setHealthBarPosition(-0.2f, 0.48f);
        healthBarUpdate(delta);
        setScaleX(1f);
        setScaleY(1f);
        if (isFlipX()) {
            setPosX((body.getPosition().x - WIDTH * 0.5f) + 0.25f);
            setPosY(body.getPosition().y - HEIGHT * 0.5f + 0.25f);
        } else {
            setPosX((body.getPosition().x - WIDTH * 0.5f) - 0.25f);
            setPosY(body.getPosition().y - HEIGHT * 0.5f + 0.25f);
        }

        if (getFSM().isInState(BigCultistStatesWrapper.BigCultistStates.DEATH)) {
            enemyBleeding(delta, 0f, -1f);
        }
    }

    private ShapeRenderer renderer = new ShapeRenderer();

    public void draw(Batch batch) {

        batch.draw(this, getPosX(), getPosY(), WIDTH * 0.5f, HEIGHT * 0.5f
                , WIDTH, HEIGHT, getScaleX(), getScaleY(), body.getAngle() * MathUtils.radiansToDegrees);
        super.draw(batch);
        if ((xDistanceFromPlayer() < 2 && xDistanceFromPlayer() > -2) && !getFSM().isInState(BigCultistStatesWrapper.BigCultistStates.ATTACK)) {
            if (!isDestroyed())
                getHealthBar().draw(batch, 1);
        }

    }//end of draw complication


    @Override
    public void createEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getInitialPosition().x, getInitialPosition().y);
        bodyDef.fixedRotation = true;
        FixtureDef fixtureDef;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        fixtureDef = new FixtureDef();
        fixtureDef.density = 100;
        fixtureDef.friction = 10;
        fixtureDef.restitution = 0;
        MassData massData = new MassData();
        massData.mass = 859;
        fixtureDef.filter.categoryBits = GreenStory.ENEMY_BIT;
        body.setMassData(massData);
        fixtureDef.filter.maskBits = GreenStory.PLAYER_BIT | GreenStory.GROUND_BIT |
                GreenStory.ATTACK_BIT
                | GreenStory.DEATH_BIT | GreenStory.WALL_BIT;
        PolygonShape polygonShape = new PolygonShape();
        Vector2[] vtcs = new Vector2[6];
        vtcs[0] = new Vector2(-0.0f, -0.35f);
        vtcs[1] = new Vector2(-0.15f, -0.20f);
        vtcs[2] = new Vector2(0.15f, -0.20f);
        vtcs[3] = new Vector2(-0.15f, 0.20f);
        vtcs[4] = new Vector2(0.15f, 0.20f);
        vtcs[5] = new Vector2(0.0f, 0.35f);
        polygonShape.set(vtcs);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);
        polygonShape.dispose();

    }

    public Rectangle getRightAttackArea() {
        return rightAttackArea;
    }

    public void setRightAttackArea() {
        rightAttackArea.set(body.getPosition().x, body.getPosition().y - 0.3f, 0.7f, 0.9f);
    }

    public Rectangle getLeftAttackArea() {
        return this.leftAttackArea;
    }

    public void setLeftAttackArea() {
        leftAttackArea.set(body.getPosition().x - 0.7f, body.getPosition().y - 0.3f, 0.7f, 0.9f);

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return fsm.handleMessage(msg);
    }
}
