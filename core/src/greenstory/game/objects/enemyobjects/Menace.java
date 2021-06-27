package greenstory.game.objects.enemyobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public interface Menace extends Disposable {
    void update(float delta);

    void draw(Batch batch);

    void dispose();
}
