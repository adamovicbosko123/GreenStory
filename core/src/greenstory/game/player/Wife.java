package greenstory.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.screens.AbstractScreen;

public class Wife extends Sprite implements Disposable {
    private Vector2 position;

    private World world;
    public Body wifeBody;
    private float stateTimer;
    private AbstractScreen screen;
    private TextureRegion idle;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private boolean playerHasBeenSeen = false;
    private boolean metWithThePlayer = false;

    public Wife(AbstractScreen screen, World world) {
        this.world = world;
        this.screen = screen;
        idle = new TextureRegion(new Texture("wifeidle/idle0.png"));
        setRegion(idle);
        setBounds(0, 0, 32 / GreenStory.PPM, 44 / GreenStory.PPM);
        position = new Vector2(8, 2.5f);
        setPosition(position.x, position.y);
        defineWife();
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(new Texture("wifeidle/idle" + i + ".png")));
        }
        idleAnimation = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(new Texture("wiferun/run" + i + ".png")));
        }
        runAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
    }

    public void update(float delta) {
        stateTimer += delta % 5;
        if (!playerHasBeenSeen) {
            setRegion(idleAnimation.getKeyFrame(stateTimer, true));
        } else if (playerHasBeenSeen && !metWithThePlayer) {
            setRegion(runAnimation.getKeyFrame(stateTimer, true));
            if (wifeBody.getLinearVelocity().x >= -2)
                wifeBody.applyForceToCenter(-2, 0, true);
        } else {
            flip(true, false);
            setRegion(idleAnimation.getKeyFrame(stateTimer, true));
        }
        setBounds(0, 0, 44 / GreenStory.PPM, 40 / GreenStory.PPM);
        setPosition((wifeBody.getPosition().x - ((getWidth() / 2) / GreenStory.PPM) - 0.6f), ((wifeBody.getPosition().y - ((getHeight() / 2)) / GreenStory.PPM)) - 0.3f);
    }

    public void setMetWithThePlayer(boolean met) {
        this.metWithThePlayer = met;
    }

    public boolean isMetWithThePlayer() {
        return metWithThePlayer;
    }

    public Body getWifeBody() {
        return this.wifeBody;
    }

    public boolean isPlayerHasBeenSeen() {
        return playerHasBeenSeen;
    }

    public void setPlayerHasBeenSeen(boolean seen) {
        this.playerHasBeenSeen = seen;
    }

    private float timer = 0;

    public boolean waitABit() {
        if ((timer += Gdx.graphics.getDeltaTime()) > 1) {
            timer = 0;
            return true;
        }
        return false;
    }

    public void defineWife() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 1f;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());
        wifeBody = world.createBody(bodyDef);
        PolygonShape polygonShape = new PolygonShape();
        Vector2[] vtcs = new Vector2[6];
        vtcs[0] = new Vector2(-0.0f, -0.29f);
        vtcs[1] = new Vector2(-0.08f, -0.18f);
        vtcs[2] = new Vector2(0.08f, -0.18f);
        vtcs[3] = new Vector2(-0.08f, 0.18f);
        vtcs[4] = new Vector2(0.08f, 0.18f);
        vtcs[5] = new Vector2(0.0f, 0.30f);
        polygonShape.set(vtcs);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.restitution = -1;
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = GreenStory.WIFE_BIT;
        fixtureDef.filter.maskBits = GreenStory.GROUND_BIT | GreenStory.PLAYER_BIT;

        fixtureDef.shape = polygonShape;

        wifeBody.createFixture(fixtureDef).setUserData(this);
        wifeBody.setUserData(this);

        //creates bottom
        fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0;
        fixtureDef.density = 5;
        fixtureDef.friction = 0.5f;
        fixtureDef.filter.categoryBits = GreenStory.PLAYER_FEET_BIT;
        fixtureDef.filter.maskBits = GreenStory.GROUND_BIT;
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.04f, 0.12f, new Vector2(0, -0.18f), 0);
        fixtureDef.shape = polygonShape;

        wifeBody.createFixture(fixtureDef).setUserData(this);
        wifeBody.setUserData(this);
        //

    }


    public void dispose() {

    }
}
