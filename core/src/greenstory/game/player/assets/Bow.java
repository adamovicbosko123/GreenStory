package greenstory.game.player.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;

public class Bow extends Sprite implements Disposable {
    private Animation<TextureRegion> travelAnimation;
    private float stateTimer = 0;
    private float positionX, positionY;
    private static final float WIDTH = 32 / GreenStory.PPM;
    private static final float HEIGHT = 32 / GreenStory.PPM;
    private boolean draw = true;
    private World world;
    private Body body;
    private Player player;
    private boolean readyToDestroy = false;
    private boolean destroyed = false;
    private boolean flipped = false;
    private Vector2 initialPosition;
    private float scaleX = 0;
    private float scaleY = 0;
    private float posX = 0;
    private float posY = 0;
    private float timer = 0;

    public Bow(World world, Player player) {
        this.world = world;
        this.player = player;
        Array<TextureRegion> array = new Array<>();
        for (int i = 0; i < 4; i++) {
            array.add(new TextureRegion(new Texture("cultist/firecultist/fireball/fireball" + i + ".png")));
        }
        travelAnimation = new Animation<TextureRegion>(0.1f, array, Animation.PlayMode.LOOP);
        array.clear();

        for (int i = 0; i < 5; i++) {
            array.add(new TextureRegion(new Texture("cultist/firecultist/impact/impact" + i + ".png")));

        }
        array.clear();
        if (player.isFlipX()) {
            positionX = player.getPlayerBody().getPosition().x - 0.5f;
            positionY = player.getPlayerBody().getPosition().y;
        } else {
            positionX = player.getPlayerBody().getPosition().x + 0.5f;
            positionY = player.getPlayerBody().getPosition().y;

        }
        createArrow();
        setRegion(new TextureRegion(new Texture("cultist/firecultist/fireball/fireball0.png")));
        setBounds(0, 0, 32f / GreenStory.PPM, 32f / GreenStory.PPM);
        if (player.isFlipX()) {
            flip(false, false);
            flipped = false;
        }
        if (!player.isFlipX()) {
            flip(true, false);
            flipped = true;
        }
        initialPosition = new Vector2(player.getPlayerBody().getPosition().x, player.getPlayerBody().getPosition().y);

    }


    public void update(float delta) {
        stateTimer += delta % 5;
        setRegion(travelAnimation.getKeyFrame(stateTimer, true));

        if (flipped) {
            flip(true, false);
            body.setLinearVelocity(5.5f, 0);
            if (body.getPosition().x > initialPosition.x + 5) {
                if (!readyToDestroy)
                    readyToDestroy = true;
            }
        }
        if (!flipped) {
            body.setLinearVelocity(-5.5f, 0);
            if (body.getPosition().x < initialPosition.x - 5) {
                if (!readyToDestroy)
                    readyToDestroy = true;
            }
        }
        setBounds(0, 0, 32f / GreenStory.PPM, 32f / GreenStory.PPM);
        setPosition(body.getPosition().x + 1, body.getPosition().y);

        if (readyToDestroy && !destroyed) {
            world.destroyBody(this.body);
            destroyed = true;
        }
        if (destroyed) {
            draw = false;
            timer += Gdx.graphics.getDeltaTime();
        }
        if (draw) {
            scaleX = 2f;
            scaleY = 0.3f;
            if (isFlipX()) {
                posX = body.getPosition().x - 0.1f;
                posY = body.getPosition().y - 0.3f;
            } else {
                posX = body.getPosition().x - 0.5f;
                posY = body.getPosition().y - 0.3f;
            }
        }

    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setReadyToDestroy(boolean readyToDestroy) {
        this.readyToDestroy = readyToDestroy;
    }

    public boolean isReadyToDestroy() {
        return readyToDestroy;
    }

    public void draw(Batch batch) {
        if (draw) {
            if (timer < 0.2f) {
                batch.draw(this, posX, posY, WIDTH * 0.5f, HEIGHT * 0.5f
                        , WIDTH, HEIGHT, scaleX, scaleY, 0);
            }
        }
    }

    public void createArrow() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(positionX, positionY);
        bodyDef.gravityScale = 0;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = GreenStory.DEATH_BIT;
        fixtureDef.filter.maskBits = GreenStory.ENEMY_BIT | GreenStory.WALL_BIT ;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(4f / GreenStory.PPM);
        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);
        circleShape.dispose();

    }

    public Body getBody() {
        return this.body;
    }

    public void readyToDestroy(boolean readyToDestroy) {
        this.readyToDestroy = readyToDestroy;
    }

    public void dispose() {

    }

}
