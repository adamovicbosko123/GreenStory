package greenstory.game.objects.harmlesobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Elevator extends Sprite implements Harmles {
    private TextureRegion platform;
    private Animation<TextureRegion> elevatorAnimation;
    private TextureRegion elevatorTile;
    private TextureRegion elevatorTile1;
    private float stateTimer = 0;
    private PlayScreen playScreen;
  ///  private ShapeRenderer renderer;
    private Vector2 position;
    private World world;
    private Body body;
    private Player player;
    PrismaticJoint prismaticJoint;
    float time = 0;

    public Elevator(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        position = new Vector2(x, y);
        world = playScreen.getWorld();
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(64, 416);

        elevatorTile = tiles[1][1];
        elevatorTile.setRegionWidth(64);
        elevatorTile.setRegionHeight(96);

        elevatorTile1 = tiles[1][7];
        elevatorTile1.setRegionWidth(64);
        elevatorTile1.setRegionHeight(96);

        Array<TextureRegion> elevatorFrames = new Array<>();
        elevatorFrames.add(elevatorTile);
        elevatorFrames.add(elevatorTile1);

        elevatorAnimation = new Animation<TextureRegion>(0.2f, elevatorFrames);


        setBounds(0, 0, 64 / GreenStory.PPM, 128 / GreenStory.PPM);

        setRegion(elevatorTile);

        createElevator();
        setPosition(x, y);
       // renderer = new ShapeRenderer();
    }

    public void update(float delta) {
        // setScale(-0.1f);
        stateTimer += delta;
        setRegion(elevatorAnimation.getKeyFrame(stateTimer, true));
        setPosition(body.getPosition().x - getWidth() * 0.5f, body.getPosition().y - 0.3f);
        if (player != null) {
            setRegion(elevatorTile1);

        } else {
            setRegion(elevatorTile);
        }
        if (body.getPosition().y >= 18.24f) {
            if ((time += delta) >= 1.5f) {
                prismaticJoint.setMotorSpeed(-6.4f);
                time = 0;
            }
        } else if ((body.getPosition().y <= 2.24f) || body.getLinearVelocity().y == 0) {
            if ((time += delta) >= 2) {
                prismaticJoint.setMotorSpeed(1.4f);
                time = 0;
            }
        }

    }


    public void createElevator() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x + 0.6f, position.y + 0.5f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setGravityScale(0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0;
        fixtureDef.friction = 1;
        fixtureDef.filter.maskBits = GreenStory.GROUND_BIT;
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.65f, 0.25f);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);

        fixtureDef = new FixtureDef();
        fixtureDef.friction = 5;
        fixtureDef.density = 10;
        fixtureDef.restitution = 0;
        fixtureDef.filter.categoryBits = GreenStory.ELEVATOR_GROUND_BIT;
        fixtureDef.filter.maskBits = GreenStory.PLAYER_FEET_BIT ;
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.65f, 0.01f, new Vector2(0, 0.4f), 0);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);

        Body anchor;
        bodyDef = new BodyDef();
        bodyDef.position.set(position.x + 0.6f, position.y + 20.5f);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        anchor = world.createBody(bodyDef);
        fixtureDef = new FixtureDef();
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.6f, 0.25f);
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.maskBits = ~GreenStory.PLAYER_BIT;
        anchor.createFixture(fixtureDef);

        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.bodyA = anchor;
        prismaticJointDef.bodyB = body;
        prismaticJointDef.collideConnected = true;
        prismaticJointDef.localAxisA.set(0, 1f);
        prismaticJointDef.localAnchorA.set(0, -20.2f);
        prismaticJointDef.localAnchorB.set(0, 0f);
        prismaticJointDef.enableLimit = true;
        prismaticJointDef.upperTranslation = 16f;
        prismaticJointDef.lowerTranslation = 0f;
        prismaticJointDef.enableMotor = true;
        prismaticJointDef.maxMotorForce = 500.2f;
        prismaticJointDef.motorSpeed = 1.4f;

        prismaticJoint = (PrismaticJoint) world.createJoint(prismaticJointDef);

        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = GreenStory.ELEVATOR_SENSOR_BIT;
        fixtureDef.filter.maskBits = GreenStory.PLAYER_BIT;
        fixtureDef.isSensor = true;
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(1.5f, 1.5f, new Vector2(0, 1.2f), 0f);
        fixtureDef.shape = polygonShape;

        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);

        polygonShape.dispose();


    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void dispose() {
        platform.getTexture().dispose();
        elevatorTile.getTexture().dispose();
        elevatorTile1.getTexture().dispose();
    }
}




