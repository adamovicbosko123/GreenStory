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
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.enemies.states.CultistStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Cultist extends Enemy implements Telegraph, Disposable {
    private static float WIDTH = 32 / GreenStory.PPM;
    private static float HEIGHT = 32 / GreenStory.PPM;
    private DefaultStateMachine<Cultist, CultistStatesWrapper.CultistStates> fsm;
    private CultistStatesWrapper cultistStatesWrapper;
    private Rectangle rightAttackArea, leftAttackArea;
    public Sound cultistScream, cultistDie,cultistPush;

    public Cultist(PlayScreen playScreen, float x, float y) {
        super(playScreen);
        this.playScreen = playScreen;
        setInitialPosition(x, y);
        createEnemy();
        setBounds(0, 0, 32 / GreenStory.PPM, 38 / GreenStory.PPM);
        cultistStatesWrapper = new CultistStatesWrapper();
        fsm = new DefaultStateMachine<Cultist, CultistStatesWrapper.CultistStates>(this, CultistStatesWrapper.CultistStates.IDLE);
        setMaxHealth(10);
        setHealth(getMaxHealth());
        setAttackRange(1.2f);
        setChasingSpeed(0.8f);
        setHealthBar(this, 12f);
        leftAttackArea = new Rectangle();
        rightAttackArea = new Rectangle();
        cultistScream = StageLoadingScreen.manager.get("sounds/cultistScream.wav");
        cultistDie = StageLoadingScreen.manager.get("sounds/cultistDie.wav");
        cultistPush = StageLoadingScreen.manager.get("sounds/cultistPush.wav");

    }


    public DefaultStateMachine<Cultist, CultistStatesWrapper.CultistStates> getFSM() {
        return fsm;
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y / getHeight() / 2);
        fsm.update();

        if (!isFlipX())
            setHealthBarPosition(-0.1f, 0.4f);
        else
            setHealthBarPosition(-0.2f, 0.4f);
        healthBarUpdate(delta);
        setLeftAttackArea();
        setRightAttackArea();
        if (getFSM().isInState(CultistStatesWrapper.CultistStates.ATTACK)) {
            setScaleX(1.5f);
            setScaleY(1.2f);
            if (isFlipX()) {
                setPosX((body.getPosition().x - WIDTH * 0.5f) + 0.4f);
                setPosY((body.getPosition().y - HEIGHT * 0.5f) + 0.05f);
            } else {
                setPosX((body.getPosition().x - WIDTH * 0.5f) - 0.4f);
                setPosY((body.getPosition().y - HEIGHT * 0.5f) + 0.05f);
            }
        } else {
            setScaleX(1f);
            setScaleY(1.1f);
            setPosX(body.getPosition().x - WIDTH * 0.5f);
            setPosY((body.getPosition().y - HEIGHT * 0.5f) + 0.03f);
        }

        if (getFSM().isInState(CultistStatesWrapper.CultistStates.DEATH)) {
            if (!isFlipX()) {
                enemyBleeding(delta, -0.2f, -0.95f);
            } else {
                enemyBleeding(delta, 0, 0);
            }
        }

    }

    public void draw(Batch batch) {

        batch.draw(this, getPosX(), getPosY(), WIDTH * 0.5f, HEIGHT * 0.5f
                , WIDTH, HEIGHT, getScaleX(), getScaleY(), body.getAngle() * MathUtils.radiansToDegrees);
        super.draw(batch);
        if (((xDistanceFromPlayer() < 2 && xDistanceFromPlayer() > -2) && !getFSM().isInState(CultistStatesWrapper.CultistStates.ATTACK))) {
            if (!isDestroyed())
                getHealthBar().draw(batch, 1);
        }

    }//end of draw complication


    @Override
    public void createEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getInitialPosition().x, getInitialPosition().y);
        bodyDef.fixedRotation = false;
        FixtureDef fixtureDef;
        MassData massData = new MassData();
        massData.mass = 120f;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.setMassData(massData);

        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = GreenStory.ENEMY_BIT;
        fixtureDef.filter.maskBits = GreenStory.PLAYER_BIT | GreenStory.GROUND_BIT |
                GreenStory.ATTACK_BIT
                | GreenStory.DEATH_BIT | GreenStory.WALL_BIT;
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

    public Rectangle getRightAttackArea() {
        return rightAttackArea;
    }

    public void setRightAttackArea() {
        rightAttackArea.set(body.getPosition().x, body.getPosition().y - 0.3f, 0.8f, 0.6f);
    }

    public Rectangle getLeftAttackArea() {
        return this.leftAttackArea;
    }

    public void setLeftAttackArea() {
        leftAttackArea.set(body.getPosition().x - 0.9f, body.getPosition().y - 0.3f, 0.9f, 0.6f);

    }


    @Override
    public boolean handleMessage(Telegram msg) {
        return fsm.handleMessage(msg);
    }

    public void dispose() {
    }
}
