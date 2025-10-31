package puppy.code.Screens.UIBase;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.Asset;

public class BackgroundRenderer {

    private final Asset assets;
    private final float worldWidth;
    private final float worldHeight;

    public BackgroundRenderer(Asset assets, float worldWidth, float worldHeight) {
        this.assets = assets;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void render(SpriteBatch batch) {
        // asegurar color blanco (alpha 1) antes de dibujar texturas
        batch.setColor(1f, 1f, 1f, 1f);

        Texture bg = assets.getBackground();
        Texture ground = assets.getGround();

        if (bg != null) {
            batch.draw(bg, 0, 0, worldWidth, worldHeight);
        }
        if (ground != null) {
            float groundHeight = ground.getHeight();
            batch.draw(ground, 0, 0, worldWidth, groundHeight);
        }
    }
}
