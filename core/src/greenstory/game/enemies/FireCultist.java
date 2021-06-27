package greenstory.game.enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.enemies.assets.FireBall;
import greenstory.game.enemies.states.FireCultistStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class FireCultist extends Enemy implements Telegraph, Disposable {
    private static float WIDTH = 32 / GreenStory.PPM;
    private static float HEIGHT = 32 / GreenStory.PPM;
    private DefaultStateMachine<FireCultist, FireCultistStatesWrapper.FireCultistStates> fsm;
    private FireCultistStatesWrapper fireCultistStatesWrapper;
    public Array<FireBall> fireBalls = new Array<FireBall>();
    public Sound fireCultistScream, fireCultistDie,fireCultistFire;

    public FireCultist(PlayScreen playScreen, float x, float y) {
        super(playScreen);
        this.playScreen = playScreen;
        setInitialPosition(x, y);
        createEnemy();
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        fireCultistStatesWrapper = new FireCultistStatesWrapper();
        fsm = new DefaultStateMachine<FireCultist, FireCultistStatesWrapper.FireCultistStates>(this, FireCultistStatesWrapper.FireCultistStates.IDLE);
        setMaxHealth(10);
        setHealth(getMaxHealth());
        setAttackRange(6f);
        setChasingSpeed(0.8f);
        setHealthBar(this, 12f);
        fireCultistScream = StageLoadingScreen.manager.get("sounds/fireCultistScream.wav");
        fireCultistDie = StageLoadingScreen.manager.get("sounds/fireCultistDie.wav");
        fireCultistFire = StageLoadingScreen.manager.get("sounds/fireCultistFire.wav");

    }


    public DefaultStateMachine<FireCultist, FireCultistStatesWrapper.FireCultistStates> getFSM() {
        return fsm;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y / getHeight() / 2);
        fsm.update();

        if (!isFlipX())
            setHealthBarPosition(-0.1f, 0.48f);
        else
            setHealthBarPosition(-0.2f, 0.48f);
        healthBarUpdate(delta);

        if (!fireBalls.isEmpty()) {
            for (FireBall f : fireBalls) {
                if (f.isDraw()) {
                    f.update(delta);
                    if (f.getFireBallRectangle().overlaps(playScreen.getPlayer().getPlayerRectangle())) {
                        playScreen.getPlayer().setHit(true);
                        f.setDraw(false);
                    }
                }
            }
        }

        setScaleX(1.1f);
        setScaleY(1.2f);
        setPosX(body.getPosition().x - WIDTH * 0.5f);
        setPosY(body.getPosition().y - HEIGHT * 0.5f + 0.05f);


        if (getFSM().isInState(FireCultistStatesWrapper.FireCultistStates.DEATH) ) {
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
        if ((xDistanceFromPlayer() < 2 && xDistanceFromPlayer() > -2) && !getFSM().isInState(FireCultistStatesWrapper.FireCultistStates.ATTACK)) {
            if (!isDestroyed())
                getHealthBar().draw(batch, 1);
        }

    }

    public Array<FireBall> getFireBalls() {
        return new Array<>(fireBalls);
    }


    @Override
    public void createEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getInitialPosition().x, getInitialPosition().y);
        bodyDef.fixedRotation = false;
        FixtureDef fixtureDef;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);
        MassData massData = new MassData();
        massData.mass = 139;
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

    public PlayScreen getPlayScreen() {
        return this.playScreen;
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        return fsm.handleMessage(msg);
    }

    public void dispose() {

    }

}
