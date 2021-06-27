package greenstory.game.utilities;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import greenstory.game.GreenStory;
import greenstory.game.screens.AbstractScreen;

public class GroundCreator {
    public Body solidBody;
    private World world;
    private AbstractScreen screen;
    private TiledMap map;

    public GroundCreator(AbstractScreen screen) {
        this.screen = screen;
        this.world = this.screen.getWorld();
        this.map = this.screen.getMap();

        MapObjects objects = map.getLayers().get("ground").getObjects();
        Array<RectangleMapObject> mapObjects = objects.getByType(RectangleMapObject.class);

        BodyDef solidBodyDef = new BodyDef();
        solidBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 2;
        fixtureDef.density = 50;
        fixtureDef.restitution = 0;

        fixtureDef.filter.categoryBits = GreenStory.GROUND_BIT;
        fixtureDef.filter.maskBits = GreenStory.ENEMY_BIT | GreenStory.PLAYER_BIT | GreenStory.PLAYER_FEET_BIT | GreenStory.BARREL_BIT |
                GreenStory.WIFE_BIT | GreenStory.LEFT_GRAB_BIT | GreenStory.RIGHT_GRAB_BIT;
        PolygonShape polygonShape = new PolygonShape();

        for (RectangleMapObject rectangleMapObject : mapObjects) {
            Rectangle rectangle = rectangleMapObject.getRectangle();
            solidBodyDef.position.set((rectangle.x + rectangle.getWidth() / 2) / GreenStory.PPM, (rectangle.y + rectangle.getHeight() / 2) / GreenStory.PPM);
            solidBody = world.createBody(solidBodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2 / GreenStory.PPM, rectangle.getHeight() / 2 / GreenStory.PPM);

            fixtureDef.shape = polygonShape;
            solidBody.createFixture(fixtureDef).setUserData(this);
            solidBody.setUserData(this);
        }


    }

    public Body getGroundBody() {
        return solidBody;
    }


}
