package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Clase EndScreen
 * Pantalla final del juego que muestra el mensaje de victoria.
 * Hereda de BaseUIScreen → Template Method (GM2.2).
 */
public class EndScreen extends BaseUIScreen {

    // Atributos específicos
    private final FlappyGameMenu game;
    private final int score;
    private Texture bg, ground, pipeTex, birdTex;

    public EndScreen(FlappyGameMenu game, int score) {
        super(480, 800);
        this.game = game;
        this.score = score;
    }

    @Override
    protected void loadResources() {
        bg = new Texture("flappy/background.png");
        ground = new Texture("flappy/ground.png");
        pipeTex = new Texture("flappy/pipe.png");
        birdTex = new Texture("flappy/bird2.png");
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    protected void drawContent() {
        // Fondo
        batch.draw(bg, 0, 0, worldWidth, worldHeight);

        // Panel y textos
        ui.drawPanel(batch, 40, worldHeight / 2f - 100, worldWidth - 80, 200, 0.45f);
        ui.drawCenteredScaled(batch, "¡Victoria!", worldWidth / 2f, worldHeight / 2f + 40, 1.4f);
        ui.drawCenteredScaled(batch,
            "Puntaje: " + score + "   |   ESPACIO para volver",
            worldWidth / 2f, worldHeight / 2f - 20, 1.0f);

        // Pájaro decorativo
        batch.draw(birdTex, worldWidth / 2f - 24, worldHeight / 2f + 60, 48, 34);
    }

    @Override
    protected void unloadResources() {
        bg.dispose();
        ground.dispose();
        pipeTex.dispose();
        birdTex.dispose();
    }
}
