package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Clase PauseScreen
 * Pantalla de pausa del juego. Permite continuar o volver al menú principal.
 * Hereda de BaseUIScreen → aplica el patrón Template Method (GM2.2),
 * reutilizando el flujo común de pantallas de interfaz.
 */
public class PauseScreen extends BaseUIScreen {

    // --- Atributos específicos ---
    private final FlappyGameMenu game;
    private final Screen previousGame; // referencia a GameScreen para reanudar
    private Texture bg;

    /**
     * Constructor: recibe el juego principal y la pantalla anterior.
     */
    public PauseScreen(FlappyGameMenu game, Screen previousGame) {
        super(480f, 800f); // dimensiones lógicas de esta pantalla
        this.game = game;
        this.previousGame = previousGame;
    }

    /** Carga recursos específicos de la pantalla de pausa */
    @Override
    protected void loadResources() {
        bg = new Texture(Gdx.files.internal("flappy/gameover_bg.png"));
    }

    /**
     * Manejo de entrada:
     *  - ESPACIO o CLICK: volver al juego anterior.
     *  - ESC: ir al menú principal.
     */
    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.setScreen(previousGame); // reanuda la partida
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game)); // vuelve al menú
        }
    }

    /** Dibuja el contenido específico de la pantalla de pausa */
    @Override
    protected void drawContent() {
        // Fondo
        if (bg != null) {
            batch.setColor(1, 1, 1, 1);
            batch.draw(bg, 0, 0, worldWidth, worldHeight);
        }

        // Textos
        ui.drawCenteredScaled(batch, "Pausa",
            worldWidth / 2f,
            worldHeight / 2f + 50f,
            2.0f);

        ui.drawCenteredScaled(batch,
            "ESPACIO: continuar   |   ESC: menu",
            worldWidth / 2f,
            worldHeight / 2f - 40f,
            1.3f);
    }

    /** Libera recursos específicos de pausa */
    @Override
    protected void unloadResources() {
        if (bg != null) bg.dispose();
    }
}
