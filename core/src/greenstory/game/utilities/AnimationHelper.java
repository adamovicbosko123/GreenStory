package greenstory.game.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationHelper {
    private Animation<TextureRegion> animation;
    private Array<TextureRegion> frames;
    private String pathToFile;
    private int numOfFrames;
    private AssetManager manager;

    public AnimationHelper(AssetManager manager, String pathToFile, String file, int numOfFrames, float frameTime, Animation.PlayMode playMode) {
        this.manager = manager;
        frames = new Array<>();
        this.pathToFile = pathToFile;
        this.numOfFrames = numOfFrames;
        for (int i = 0; i < numOfFrames; i++) {
            frames.add(new TextureRegion(manager.<Texture>get(pathToFile + "/" + file + i + ".png")));
        }
        animation = new Animation<>(frameTime, frames);
        animation.setPlayMode(playMode);
        frames.clear();
    }
    public AnimationHelper(AssetManager manager, String pathToFile, int count, float frameTime, Animation.PlayMode mode){
        this.manager = manager;
        frames = new Array<>();
        this.pathToFile = pathToFile;
        Texture texture = new Texture(this.pathToFile);
        TextureRegion textureRegion = new TextureRegion(texture);
        int frameWidth = textureRegion.getRegionWidth() / count;
        int frameHeight = textureRegion.getRegionHeight();
        TextureRegion[][] textureRegions = textureRegion.split(frameWidth,frameHeight);
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < count; j++) {
                frames.add(textureRegions[i][j]);
            }
        }
        animation = new Animation<>(frameTime, frames);
        animation.setPlayMode(mode);
        frames.clear();

    }
    public Animation<TextureRegion> getAnimation() {
        return this.animation;
    }

}
