package greenstory.game.objects.harmlesobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import greenstory.game.GreenStory;
import greenstory.game.objects.Explosion;
import greenstory.game.objects.collectibles.Arrows;
import greenstory.game.objects.collectibles.Cans;
import greenstory.game.objects.collectibles.Collectibles;
import greenstory.game.objects.collectibles.Crap;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

import java.util.Random;

public class Barrel extends Sprite implements Harmles {
    private TextureRegion barrel;
    private Body body;
    private PlayScreen playScreen;
    private World world;
    private boolean setToDestroy = false;
    private boolean destroyed = false;
    private Explosion explosion;
    private Collectibles collectibles;
    private boolean readyToCreateCollectible = false;
    private boolean readyToUpdateCollectible = false;
    private Random random = new Random();
    private int style = 0;
    private float timer = 0;
    private Sound explosionSound;
    private int counter = 0;


    public Barrel(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        style = random.nextInt(4) + 1;
        barrel = new TextureRegion(StageLoadingScreen.manager.<Texture>get("barrel" + style + ".png"));
        setRegion(barrel);
        setBounds(0, 0, 32 / GreenStory.PPM, 32 / GreenStory.PPM);
        setPosition(x, y);
        setOrigin(getWidth() * 0.5f, getHeight() * 0.5f);
        createBarrel();
        explosion = new Explosion(playScreen, 0, 0);
        explosionSound = StageLoadingScreen.manager.get("sounds/explosion.wav");

    }

    public void update(float delta) {
        setRegion(barrel);
        setPosition((body.getPosition().x - 0.28f) - ((getWidth() / 2) / GreenStory.PPM), ((body.getPosition().y - 0.35f) - ((getHeight() / 2)) / GreenStory.PPM));
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        if (isSetToDestroy() && !isDestroyed()) {
            world.destroyBody(body);
            setDestroyed(true);
        }
        if (destroyed) {
            explosion.setPosition(body.getPosition().x - ((getWidth() / 2) / GreenStory.PPM) - 0.31f, (body.getPosition().y - ((getHeight() / 2)) / GreenStory.PPM) - 0.3f);
            if (!explosion.isReadyToDrawCollectible()) {
                explosion.update(Gdx.graphics.getDeltaTime());
                if (counter < 1) {
                    long id = explosionSound.play();
                    explosionSound.setLooping(id, false);
                    explosionSound.setVolume(id, 0.03f);
                    counter++;
                }
                readyToCreateCollectible = true;
                readyToUpdateCollectible = true;
            }
        }

        //this block creates condition for creating collectible
        if (readyToCreateCollectible) {
            if (MathUtils.randomBoolean(0.5f)) {
                collectibles = new Crap(playScreen, getX(), getY());
            } else if (MathUtils.randomBoolean(0.8f)) {
                collectibles = new Cans(playScreen, getX(), getY() + 0.2f);
            } else {
                collectibles = new Arrows(playScreen, getX(), getY() + 0.2f);
            }
            readyToCreateCollectible = false;
        }
        //
        //this block waits for some time to ends explosion
        if (collectibles != null && collectibles.isReadyToBeCollected() && readyToUpdateCollectible) {
            if ((timer += delta) >= 2)
                collectibles.update(delta);
        }

    }


    public boolean isSetToDestroy() {
        return setToDestroy;
    }


    public void setSetToDestroy(boolean setToDestroy) {
        this.setToDestroy = setToDestroy;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void createBarrel() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = false;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());
        body = world.createBody(bodyDef);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.19f, 0.25f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10;
        fixtureDef.restitution = 0.2f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = GreenStory.BARREL_BIT;
        fixtureDef.filter.maskBits = GreenStory.ATTACK_BIT |
                GreenStory.WALL_BIT | GreenStory.GROUND_BIT;

        fixtureDef.shape = polygonShape;

        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);

        polygonShape.dispose();
    }


    public void dispose() {
        barrel.getTexture().dispose();
        if (explosion != null)
            explosion.dispose();
        if (collectibles != null)
            collectibles.dispose();
    }


    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        } else if (!explosion.isReadyToDrawCollectible()) {
            explosion.draw(batch);
        } else if (collectibles != null && collectibles.isReadyToBeCollected()) {
            collectibles.draw(batch);
        }
    }

    public Body getBody() {
        return this.body;
    }
}
