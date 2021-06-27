package greenstory.game.objects.harmlesobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import greenstory.game.screens.PlayScreen;

public class Ladder extends Sprite implements Harmles {
    private PlayScreen playScreen;
    private Rectangle ladderRectangle;
    // private ShapeRenderer renderer;

    public Ladder(PlayScreen playScreen, float x, float y, float width, float height) {
        this.playScreen = playScreen;
        ladderRectangle = new Rectangle();
        ladderRectangle.set(x, y, width, height);
        // renderer = new ShapeRenderer();
    }

    public void update(float delta) {
    }


    public Rectangle getLadderRectangle() {
        return this.ladderRectangle;
    }

    private ShapeRenderer renderer = new ShapeRenderer();

    public void draw(Batch batch) {
        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        //renderer.rect(ladderRectangle.x, ladderRectangle.y, ladderRectangle.width, ladderRectangle.height);
        renderer.end();
        batch.begin();
    }

    public void dispose() {

    }
}
