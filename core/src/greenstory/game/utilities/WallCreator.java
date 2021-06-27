package greenstory.game.utilities;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import greenstory.game.GreenStory;
import greenstory.game.screens.AbstractScreen;

public class WallCreator {
    public static Body wallBody;
    private World world;
    private AbstractScreen screen;
    private TiledMap map;

    public WallCreator(AbstractScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();

        MapObjects objects = map.getLayers().get("wall").getObjects();
        Array<RectangleMapObject> mapObjects = objects.getByType(RectangleMapObject.class);

        BodyDef solidBodyDef = new BodyDef();
        solidBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = GreenStory.WALL_BIT;
        fixtureDef.filter.maskBits = -1;

        PolygonShape polygonShape = new PolygonShape();

        for (RectangleMapObject rectangleMapObject : mapObjects) {
            Rectangle rectangle = rectangleMapObject.getRectangle();
            solidBodyDef.position.set((rectangle.x + rectangle.getWidth() / 2) / GreenStory.PPM, (rectangle.y + rectangle.getHeight() / 2) / GreenStory.PPM);
            wallBody = world.createBody(solidBodyDef);
            polygonShape.setAsBox(rectangle.getWidth() / 2 / GreenStory.PPM, rectangle.getHeight() / 2 / GreenStory.PPM);

            fixtureDef.shape = polygonShape;
            wallBody.createFixture(fixtureDef).setUserData(this);
            wallBody.setUserData(this);
        }


    }

    public Body getWallBody() {
        return wallBody;
    }


}
