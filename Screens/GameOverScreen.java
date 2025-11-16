package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Clase GameOverScreen
 * Muestra la pantalla de derrota del jugador y el puntaje obtenido.
 * Hereda de BaseUIScreen → aplica el patrón Template Method (GM2.2),
 * definiendo solo la lógica específica de carga, dibujo e input.
 */
public class GameOverScreen extends BaseUIScreen {

    // --- Atributos específicos ---
    private final FlappyGameMenu game;
    private final int currentScore;

    private Texture bg;
    private Texture birdTex;

    private int highScore;
    private Rectangle playBounds;

    // Dimensiones del mundo (compatibles con GameScreen)
    private static final float WORLD_WIDTH = 288f;
    private static final float WORLD_HEIGHT = GameScreen.worldHeight;

    /**
     * Constructor: recibe la referencia del juego principal y el puntaje actual.
     * También gestiona el guardado del high score con Preferences.
     */
    public GameOverScreen(FlappyGameMenu game, int currentScore) {
        super(WORLD_WIDTH, WORLD_HEIGHT); //--constructor de BaseUIScreen
        this.game = game;
        this.currentScore = currentScore;

        // Manejo de High Score persistente
        Preferences prefs = Gdx.app.getPreferences("flappy_prefs");
        highScore = prefs.getInteger("highScore", 0);
        if (currentScore > highScore) {
            highScore = currentScore;
            prefs.putInteger("highScore", highScore);
            prefs.flush();
        }
    }

    /** Carga de recursos gráficos específicos de GameOverScreen */
    @Override
    protected void loadResources() {
        bg = new Texture(Gdx.files.internal("flappy/gameover_bg.png"));
        birdTex = new Texture(Gdx.files.internal("flappy/bird0.png"));

        // Configuración del botón PLAY
        float buttonWidth = 110;
        float buttonHeight = 40;
        float panelHeight = 140;

        float playX = WORLD_WIDTH / 2f - buttonWidth / 2f;
        float playY = WORLD_HEIGHT / 2f - panelHeight / 2f - buttonHeight - 10;

        playBounds = new Rectangle(playX, playY, buttonWidth, buttonHeight);
    }

    /**
     * Maneja el input del jugador.
     * Click o ESPACIO → crear nueva GameScreen (reinicio limpio).
     */
    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if (playBounds.contains(touch.x, touch.y) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                game.setScreen(new GameScreen(game));
            }
        }
    }

    /** Dibuja el contenido específico de la pantalla GAME OVER */
    @Override
    protected void drawContent() {
        // Fondo
        batch.setColor(1, 1, 1, 1);
        if (bg != null) {
            batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }

        // Panel central
        float panelWidth = WORLD_WIDTH - 40;
        float panelHeight = 140;
        float panelX = (WORLD_WIDTH - panelWidth) / 2f;
        float panelY = WORLD_HEIGHT / 2f - panelHeight / 2f;

        ui.drawPanel(batch, panelX, panelY, panelWidth, panelHeight, 0.55f);
        ui.drawCenteredScaled(batch, "GAME OVER",
            WORLD_WIDTH / 2f,
            panelY + panelHeight - 15,
            1.1f);

        // SCORE
        ui.drawText(batch, "SCORE", panelX + 20, panelY + panelHeight - 40, 0.8f);
        ui.drawText(batch, String.valueOf(currentScore),
            panelX + panelWidth - 35, panelY + panelHeight - 40, 0.8f);

        // HIGH SCORE
        ui.drawText(batch, "HIGH SCORE", panelX + 20, panelY + panelHeight - 70, 0.8f);
        ui.drawText(batch, String.valueOf(highScore),
            panelX + panelWidth - 35, panelY + panelHeight - 70, 0.8f);

        // Pájaro decorativo
        if (birdTex != null) {
            batch.draw(birdTex, panelX + 15, panelY + 15, 34, 24);
        }

        // Botón PLAY
        ui.drawPanel(batch, playBounds.x, playBounds.y,
            playBounds.width, playBounds.height, 0.65f);
        ui.drawCenteredScaled(batch, "PLAY",
            playBounds.x + playBounds.width / 2f,
            playBounds.y + playBounds.height / 2f + 4,
            0.9f);
    }

    /** Libera recursos específicos de esta pantalla */
    @Override
    protected void unloadResources() {
        if (bg != null) bg.dispose();
        if (birdTex != null) birdTex.dispose();
    }
}
