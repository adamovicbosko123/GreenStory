package greenstory.game.objects.enemyobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.enemies.*;
import greenstory.game.enemies.states.*;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

import java.util.Random;

public class Grinder extends Sprite implements Menace {
    private TextureRegion platformRegion;
    private TextureRegion grinderRegion;
    private TextureRegion grinderRegion1;
    private Animation<TextureRegion> grinderAnimation;
    private float stateTimer;
    private Vector2 position;
    private PlayScreen playScreen;
    private World world;
    private RevoluteJoint revoluteJoint;
    private Body grinderBody;
    private Body grinderAnchorBody;
    private Random random;
    private float speed;

    public Grinder(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        platformRegion = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] textureRegions = platformRegion.split(128, 128);
        grinderRegion = textureRegions[8][5];
        grinderRegion1 = textureRegions[8][6];
        Array<TextureRegion> frames;
        frames = new Array<>();
        frames.add(grinderRegion);
        frames.add(grinderRegion1);
        grinderAnimation = new Animation<TextureRegion>(0.01f, frames);
        grinderAnimation.setPlayMode(Animation.PlayMode.LOOP);
        position = new Vector2(x, y);
        createGrinder();
        setRegion(grinderRegion);
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(8, 3);
        random = new Random();
        // speed = random.nextInt(5);

    }


    public void update(float delta) {
        stateTimer += delta % 14;
        setRegion(grinderAnimation.getKeyFrame(stateTimer, true));
        if (revoluteJoint.getJointAngle() <= revoluteJoint.getLowerLimit()) {
            revoluteJoint.setMotorSpeed(1);
        }
        if (revoluteJoint.getJointAngle() >= revoluteJoint.getUpperLimit()) {
            revoluteJoint.setMotorSpeed(-1);
        }
        setPosition(grinderBody.getPosition().x - getWidth() / 2, grinderBody.getPosition().y - getHeight() / 2);
    }


    public void createGrinder() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);
        grinderAnchorBody = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.2f, 0.2f);
        fixtureDef.shape = polygonShape;
        grinderAnchorBody.createFixture(fixtureDef).setUserData(this);
        grinderAnchorBody.setUserData(this);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        grinderBody = world.createBody(bodyDef);
        fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = GreenStory.DEATH_BIT;
        fixtureDef.filter.maskBits = GreenStory.PLAYER_BIT | GreenStory.ENEMY_BIT;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(16f / GreenStory.PPM);
        fixtureDef.shape = circleShape;
        grinderBody.createFixture(fixtureDef).setUserData(this);
        grinderBody.setUserData(this);

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = grinderAnchorBody;
        revoluteJointDef.bodyB = grinderBody;
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.localAnchorA.set(0, -0f);
        revoluteJointDef.localAnchorB.set(0, 1f);
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.maxMotorTorque = 1000f;
        revoluteJointDef.motorSpeed = -1;
        revoluteJointDef.upperAngle = 90f * MathUtils.degreesToRadians;
        revoluteJointDef.lowerAngle = -90f * MathUtils.degreesToRadians;
        revoluteJoint = (RevoluteJoint) world.createJoint(revoluteJointDef);
        revoluteJoint.setUserData(this);
        circleShape.dispose();

        polygonShape.dispose();
    }

    public void grinderTouchedObject(Object object) {
        if (object instanceof Cultist) {
            Cultist cultist = (Cultist) object;
            if (!(cultist.getFSM().isInState(CultistStatesWrapper.CultistStates.DEATH))) {
                cultist.getFSM().changeState(CultistStatesWrapper.CultistStates.HIT);
                cultist.setHealth(0);
            }
        } else if (object instanceof Assassin) {
            Assassin assassin = (Assassin) object;
            if (!(assassin.getFSM().isInState(AssassinStatesWrapper.AssassinStates.DEATH))) {
                assassin.getFSM().changeState(AssassinStatesWrapper.AssassinStates.HIT);
                assassin.setHealth(0);
            }
        } else if (object instanceof BigCultist) {
            BigCultist bigCultist = (BigCultist) object;
            if (!(bigCultist.getFSM().isInState(BigCultistStatesWrapper.BigCultistStates.DEATH))) {
                bigCultist.getFSM().changeState(BigCultistStatesWrapper.BigCultistStates.HIT);
                bigCultist.setHealth(0);
            }
        } else if (object instanceof FireCultist) {
            FireCultist fireCultist = (FireCultist) object;
            if (!(fireCultist.getFSM().isInState(FireCultistStatesWrapper.FireCultistStates.DEATH))) {
                fireCultist.getFSM().changeState(FireCultistStatesWrapper.FireCultistStates.HIT);
                fireCultist.setHealth(0);
            }
        } else if (object instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) object;
            if (!(skeleton.getFSM().isInState(SkeletonStatesWrapper.SkeletonStates.DEATH))) {
                skeleton.getFSM().changeState(SkeletonStatesWrapper.SkeletonStates.HIT);
                skeleton.setHealth(0);
            }
        }
    }

    public void grinderTouchedPLayer(Object object) {
        if (object instanceof Player) {
            Player player = (Player) object;
            if ((!player.isOnElevator() &&  !(player.getFSM().isInState(PlayerStatesWrapper.PlayerStates.DEATH))) && player.getFSM().getPreviousState() != PlayerStatesWrapper.PlayerStates.DEATH) {
                player.getFSM().changeState(PlayerStatesWrapper.PlayerStates.HURT);
                player.setHealth(0);
            }
        }
    }


    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void dispose() {
        platformRegion.getTexture().dispose();
        grinderRegion.getTexture().dispose();
        grinderRegion1.getTexture().dispose();
    }

}
