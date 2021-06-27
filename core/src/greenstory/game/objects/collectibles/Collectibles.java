package greenstory.game.objects.collectibles;

import com.badlogic.gdx.utils.Disposable;
import greenstory.game.objects.harmlesobjects.Harmles;
import greenstory.game.player.Player;

public interface Collectibles extends Harmles, Disposable {
    void updatePlayer(Player player);

    boolean isReadyToBeCollected();

    void dispose();
}
