package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Clase MainMenuScreen
 * Pantalla inicial del juego: muestra instrucciones y permite iniciar la partida.
 * Hereda de BaseUIScreen → aplica el patrón Template Method (GM2.2),
 * reutilizando el flujo común de pantallas de UI.
 */
public class MainMenuScreen extends BaseUIScreen {

    // --- Atributos específicos (GM1.6) ---
    private final FlappyGameMenu game;
    private Texture bg, pipeTex, birdTex;

    /**
     * Constructor: recibe la referencia del juego principal.
     * Define dimensiones lógicas de la pantalla mediante BaseUIScreen.
     */
    public MainMenuScreen(FlappyGameMenu game) {
        super(480f, 800f); // dimensiones para esta pantalla
        this.game = game;
    }

    /** Carga de recursos gráficos específicos del menú principal */
    @Override
    protected void loadResources() {
        bg = new Texture(Gdx.files.internal("flappy/Pantalla_inicio.png"));
        pipeTex = new Texture(Gdx.files.internal("flappy/pipe.png"));
        birdTex = new Texture(Gdx.files.internal("flappy/bird0.png"));
    }

    /** Manejo de entrada: inicia una nueva partida */
    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game)); // nueva partida limpia
        }
    }

    /** Dibuja el contenido específico del menú principal */
    @Override
    protected void drawContent() {
        // Fondo
        if (bg != null) {
            batch.setColor(1, 1, 1, 1);
            batch.draw(bg, 0, 0, worldWidth, worldHeight);
        }

        // Título
        ui.drawCenteredScaled(batch, "¡Como Jugar!", worldWidth / 2f, worldHeight - 300f, 1.8f);

        // Pájaro decorativo
        if (birdTex != null) {
            batch.setColor(1, 1, 1, 0.95f);
            batch.draw(birdTex, 200f, worldHeight / 2f + 160f, 80f, 80f);
        }

        // Tutorial visual
        batch.setColor(1, 1, 1, 1f);
        if (birdTex != null) {
            batch.draw(birdTex, 80f, worldHeight / 2f - 90f, 48f, 34f);
        }
        if (pipeTex != null) {
            batch.draw(pipeTex, worldWidth - 160f, worldHeight / 2f - 240f, 72f, 240f);
        }
        batch.setColor(Color.WHITE);

        // Textos de instrucciones
        ui.drawCenteredScaled(batch, "Presiona espacio para saltar",
            160f, worldHeight / 2f - 110f, 1.1f);
        ui.drawCenteredScaled(batch, "Esquiva los tubos para sobrevivir",
            worldWidth - 120f, worldHeight / 2f - 140f, 1.1f);

        ui.drawCenteredScaled(batch,
            "Presiona ESPACIO o CLICK para empezar",
            worldWidth / 2f, 60f, 1.3f);
    }

    /** Libera recursos específicos del menú principal */
    @Override
    protected void unloadResources() {
        if (bg != null) bg.dispose();
        if (pipeTex != null) pipeTex.dispose();
        if (birdTex != null) birdTex.dispose();
    }
}
