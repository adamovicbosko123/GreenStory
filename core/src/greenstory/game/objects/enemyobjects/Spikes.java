package greenstory.game.objects.enemyobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import greenstory.game.GreenStory;
import greenstory.game.enemies.*;
import greenstory.game.enemies.states.*;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;
import greenstory.game.screens.PlayScreen;

public class Spikes implements Menace {
   // private ShapeRenderer renderer;
    private Rectangle rectangle;
    private World world;
    private Body body;
    private PlayScreen playScreen;
    private Vector2 position;

    public Spikes(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        position = new Vector2(x, y);
        createBody();
    }

    public void update(float delta) {
    }

    public void draw(Batch batch) {
    }

    public void spikesTouchedObject(Object object) {
        if (object instanceof Cultist) {
            Cultist cultist = (Cultist) object;
            cultist.getFSM().changeState(CultistStatesWrapper.CultistStates.HIT);
            cultist.setHealth(0);
        } else if (object instanceof Assassin) {
            Assassin assassin = (Assassin) object;
            assassin.getFSM().changeState(AssassinStatesWrapper.AssassinStates.HIT);
            assassin.setHealth(0);
        } else if (object instanceof BigCultist) {
            BigCultist bigCultist = (BigCultist) object;
            bigCultist.getFSM().changeState(BigCultistStatesWrapper.BigCultistStates.HIT);
            bigCultist.setHealth(0);
        } else if (object instanceof FireCultist) {
            FireCultist fireCultist = (FireCultist) object;
            fireCultist.getFSM().changeState(FireCultistStatesWrapper.FireCultistStates.HIT);
            fireCultist.setHealth(0);
        } else if (object instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) object;
            skeleton.getFSM().changeState(SkeletonStatesWrapper.SkeletonStates.HIT);
            skeleton.setHealth(0);
        }
    }

    public void spikesTouchedPLayer(Object object) {
        if (object instanceof Player) {
            Player player = (Player) object;
            player.getFSM().changeState(PlayerStatesWrapper.PlayerStates.HURT);
            player.setHealth(0);
        }
    }


    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x, position.y);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = GreenStory.DEATH_BIT;
        fixtureDef.filter.maskBits = GreenStory.PLAYER_FEET_BIT | GreenStory.ENEMY_BIT | GreenStory.PLAYER_BIT;
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1f, 0.10f, new Vector2(0, -0.25f), 0);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);
        polygonShape.dispose();
    }
    public void dispose(){}
}
