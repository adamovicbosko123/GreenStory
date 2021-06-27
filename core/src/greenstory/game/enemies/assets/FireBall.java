package greenstory.game.enemies.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import greenstory.game.GreenStory;
import greenstory.game.enemies.FireCultist;


public class FireBall extends Sprite {
    private Animation<TextureRegion> travelAnimation;
    private float stateTimer = 0;
    private float positionX, positionY;
    private static final float WIDTH = 32 / GreenStory.PPM;
    private static final float HEIGHT = 32 / GreenStory.PPM;
    private boolean draw = true;
    private FireCultist fireCultist;
    private boolean flipped = false;
    private Vector2 initialPosition;
    private Vector2 velocity = new Vector2();
    private float speed = 0;
    private float distanceTravelled = 0;
    private Rectangle fireBallRectangle;

    public FireBall(FireCultist fireCultist) {
        this.fireCultist = fireCultist;
        Array<TextureRegion> array = new Array<>();
        for (int i = 0; i < 4; i++) {
            array.add(new TextureRegion(new Texture("cultist/firecultist/fireball/fireball" + i + ".png")));
        }
        travelAnimation = new Animation<TextureRegion>(0.1f, array, Animation.PlayMode.LOOP);
        array.clear();

        for (int i = 0; i < 5; i++) {
            array.add(new TextureRegion(new Texture("cultist/firecultist/impact/impact" + i + ".png")));

        }
        // impactAnimation = new Animation<TextureRegion>(0.15f, array, Animation.PlayMode.NORMAL);
        array.clear();

        setRegion(new TextureRegion(new Texture("cultist/firecultist/fireball/fireball0.png")));
        setBounds(0, 0, 32f / GreenStory.PPM, 32f / GreenStory.PPM);
        if (fireCultist.isFlipX()) {
            flip(true, false);
            positionX = fireCultist.getBody().getPosition().x;
            positionY = fireCultist.getBody().getPosition().y - 0.2f;
            flipped = true;

        } else {
            flip(false, false);
            flipped = false;
            positionX = fireCultist.getBody().getPosition().x - 0.5f;
            positionY = fireCultist.getBody().getPosition().y - 0.2f;
        }
        initialPosition = new Vector2(positionX, positionY);
        speed = 4f;
        fireBallRectangle = this.getBoundingRectangle();


    }

    public void resetStateTimer() {
        stateTimer = 0;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public void update(float delta) {
        stateTimer += delta % 5;
        setRegion(travelAnimation.getKeyFrame(stateTimer, true));
        distanceTravelled = initialPosition.x - positionX;
        velocity.add(speed *= delta, 0);
        if (flipped) {
            flip(true, false);
            setPosition(positionX += velocity.x, positionY);
            if (distanceTravelled < -5) draw = false;
            fireBallRectangle.set(positionX + 0.2f, positionY + 0.25f, getWidth() - 0.5f, getHeight() - 0.5f);
        }
        if (!flipped) {
            setPosition(positionX += -velocity.x, positionY);
            if (distanceTravelled > 5) draw = false;
            fireBallRectangle.set(positionX + 0.23f, positionY + 0.25f, getWidth() - 0.5f, getHeight() - 0.5f);
        }


        setBounds(0, 0, 32f / GreenStory.PPM, 32f / GreenStory.PPM);

        if (draw) {
            scaleX = 1.1f;
            scaleY = 1.2f;
        }

    }


    private float scaleX = 0;
    private float scaleY = 0;

    public void draw(Batch batch) {
        if (draw) {
            batch.draw(this, positionX, positionY, WIDTH * 0.5f, HEIGHT * 0.5f
                    , WIDTH, HEIGHT, scaleX, scaleY, 0);
        }

    }

    public Rectangle getFireBallRectangle() {
        return this.fireBallRectangle;
    }

}