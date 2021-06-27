package greenstory.game.utilities;
//kreira pojedinacne enemy objekte

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import greenstory.game.GreenStory;
import greenstory.game.enemies.Enemy;
import greenstory.game.objects.enemyobjects.Menace;
import greenstory.game.objects.harmlesobjects.*;
import greenstory.game.objects.enemyobjects.*;
import greenstory.game.objects.*;
import greenstory.game.screens.PlayScreen;
import greenstory.game.enemies.*;
import greenstory.game.screens.*;


public class MapBodiesCreator {

    private final Array<Barrel> punchingBoxes;
    private final Array<Elevator> elevators;
    private final Array<Healer> healers;
    private final Array<Ladder> ladders;
    private final Array<Enemy> enemies;
    private final Array<Menace> menaces;
    private final Array<Harmles> harmles;

    public MapBodiesCreator(PlayScreen playScreen) {
        TiledMap map = playScreen.getMap();
        enemies = new Array<>();
        punchingBoxes = new Array<>();
        elevators = new Array<>();
        healers = new Array<>();
        ladders = new Array<>();
        menaces = new Array<>();
        harmles = new Array<>();

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            MapProperties properties = object.getProperties();

            if (properties.get("name").equals("skeleton")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                enemies.add(new Skeleton(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("assassin")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                enemies.add(new Assassin(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("cultist")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                enemies.add(new Cultist(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("bigcultist")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                enemies.add(new BigCultist(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("firecultist")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                enemies.add(new FireCultist(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("barrel")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                harmles.add(new Barrel(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("poisondrop")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                menaces.add(new PoisonDrop(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("fire")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                menaces.add(new Fire(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("elevator")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                harmles.add(new Elevator(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("grinder")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                menaces.add(new Grinder(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("thunder")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                menaces.add(new Thunder(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("lightning")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                menaces.add(new Lightning(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("healer")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                harmles.add(new Healer(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("ladder")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                harmles.add(new Ladder(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM, rectangle.getWidth() / GreenStory.PPM, rectangle.getHeight() / GreenStory.PPM));
            } else if (properties.get("name").equals("smoke")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                menaces.add(new Smoke(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));
            } else if (properties.get("name").equals("spikes")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                menaces.add(new Spikes(playScreen, rectangle.getX() / GreenStory.PPM + ((rectangle.width / 2) / GreenStory.PPM), rectangle.getY() / GreenStory.PPM + ((rectangle.height / 2) / GreenStory.PPM)));
            } else if (properties.get("name").equals("arrow")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                // harmles.add(new Arrows(playScreen, rectangle.getX() / MainGame.PPM, rectangle.getY() / MainGame.PPM));
            } else if (properties.get("name").equals("can")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                //harmles.add(new Cans(playScreen, rectangle.getX() / MainGame.PPM, rectangle.getY() / MainGame.PPM));
            } else if (properties.get("name").equals("door")) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                harmles.add(new Door(playScreen, rectangle.getX() / GreenStory.PPM, rectangle.getY() / GreenStory.PPM));

            }
        }


    }

    public Array<Barrel> getPunchingBoxes() {
        return new Array<>(punchingBoxes);
    }

    public Array<Elevator> getElevators() {
        return new Array<>(elevators);
    }

    public Array<Ladder> getLadders() {
        return new Array<>(ladders);
    }

    public Array<Healer> getHealers() {
        return new Array<>(healers);
    }

    public Array<Enemy> getEnemies() {
        return new Array<>(enemies);
    }

    public Array<Menace> getMenaces() {
        return new Array<>(menaces);
    }

    public Array<Harmles> getHarmles() {
        return new Array<>(harmles);
    }


}
