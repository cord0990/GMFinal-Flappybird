package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Clase PauseScreen
 *
 * Pantalla de pausa mostrada durante una partida activa. Su función es detener
 * temporalmente el flujo del juego y permitir al jugador continuar o regresar
 * al menú principal.
 *
 * Hereda de BaseUIScreen, incorporándose al flujo común de pantallas definido
 * mediante el Template Method (GM2.2). Esto permite mantener una estructura
 * homogénea en el proceso de carga, actualización y renderizado UI.
 */
public class PauseScreen extends BaseUIScreen {

    // --- Atributos específicos ---
    private final FlappyGameMenu game;
    private final Screen previousGame; // Referencia al juego principal y a la pantalla donde se estaba jugando
    private Texture bg;// Recursos gráficos específicos de esta pantalla

    // Coordenadas y escalas de los textos UI
    private float titleX, titleY;
    private float instructionsX, instructionsY;
    private float titleScale, instructionsScale;

    /**
     * Constructor de PauseScreen.
     * @param game          instancia principal del juego
     * @param previousGame  pantalla activa antes de pausar (GameScreen)
     */
    public PauseScreen(FlappyGameMenu game, Screen previousGame) {
        super(); // aplica las dimensiones base (Template Method GM2.2)
        this.game = game;
        this.previousGame = previousGame;
    }

    /** Carga los recursos gráficos exclusivos de esta pantalla. */
    @Override
    protected void loadResources() {
        bg = game.getAssets().getGameOverScreen();
    }

    /**
     * Manejo de entrada del usuario para esta pantalla:
     *  - SPACE o clic: reanudar la partida.
     *  - ESC: volver al menú principal.
     */
    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.setScreen(previousGame); // reanuda la partida
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game)); // vuelve al menú
        }
    }

    /**
     * Renderizado del contenido propio de la pantalla.
     * Se apoya en los métodos utilitarios de UIRenderer, promoviendo
     * la modularidad y coherencia visual (GM1.6).
     */
    @Override
    protected void renderContent(float dt) {

        // Fondo
        if (bg != null) {
            batch.setColor(1, 1, 1, 1);
            batch.draw(bg, 0, 0, worldWidth, worldHeight);
        }

        // Título de la pantalla
        ui.drawCenteredScaled(batch, "Pausa",
            titleX, titleY, titleScale);

        // Instrucciones al usuario
        ui.drawCenteredScaled(batch,
            "ESPACIO: continuar   |   ESC: menu",
            instructionsX, instructionsY, instructionsScale);
    }

    /** Libera los recursos exclusivos de PauseScreen. */
    @Override
    protected void unloadResources() {
        if (bg != null) bg.dispose();
    }

    /**
     * Configuración inicial del layout UI.
     * Define las posiciones y escalas de los textos.
     */
	@Override
	protected void setupUI() {

        // Coordenadas del título
	    titleX = worldWidth / 2f - 5;
	    titleY = worldHeight / 2f + 10f;

        // Coordenadas de las instrucciones
	    instructionsX = worldWidth / 2f;
	    instructionsY = worldHeight / 2f - 40f;

        // Escala de textos
	    titleScale = 2.5f;
	    instructionsScale = 1.2f;
	}
}
