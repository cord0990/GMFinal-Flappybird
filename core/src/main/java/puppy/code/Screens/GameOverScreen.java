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
    
    //Vaariables labels
    
 // --- Variables UI (definidas en setupUI) ---
    private float panelWidth;
    private float panelHeight;
    private float panelX;
    private float panelY;

    private String titleText;
    private float titleY;
    private float titleScale;

    private float scoreLabelY;
    private float highScoreLabelY;

    private float birdX;
    private float birdY;
    private float birdW = 60;
    private float birdH = 30;


    // Dimensiones del mundo (compatibles con GameScreen)
    /**
     * Constructor: recibe la referencia del juego principal y el puntaje actual.
     * También gestiona el guardado del high score con Preferences.
     */
    public GameOverScreen(FlappyGameMenu game, int currentScore) {
    	super(); //--constructor de BaseUIScreen
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
        bg = game.getAssets().getGameOverScreen();
        birdTex = game.getAssets().getBirdFrames()[0];
    }


    /**
     * Maneja el input del jugador.
     * Click o ESPACIO → crear nueva GameScreen (reinicio limpio).
     */
    @Override
    protected void update(float dt) {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if (playBounds.contains(touch.x, touch.y) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                game.setScreen(new GameScreen(game));
            }
        }
    }

    //renderiza el contenido
    @Override
    protected void renderContent(float dt) {

        // -------- FONDO --------
        batch.setColor(1, 1, 1, 1);
        if (bg != null) {
            batch.draw(bg, 0, 0, worldWidth, worldHeight);
        }

        // -------- PANEL CENTRAL --------
        ui.drawPanel(batch, panelX, panelY, panelWidth, panelHeight, 0.55f);

        // -------- TÍTULO --------
        ui.drawCenteredScaled(
            batch,
            titleText,
            worldWidth / 2f - 10f,   // tu offset original se mantiene
            titleY,
            titleScale
        );

        // -------- SCORE --------
        ui.drawText(batch, "SCORE", panelX + 30, scoreLabelY, 1.1f);
        ui.drawText(batch, String.valueOf(currentScore),
            panelX + panelWidth - 90, scoreLabelY, 1.1f);

        // -------- HIGH SCORE --------
        ui.drawText(batch, "HIGH SCORE", panelX + 30, highScoreLabelY, 1.1f);
        ui.drawText(batch, String.valueOf(highScore),
            panelX + panelWidth - 90, highScoreLabelY, 1.1f);

        // -------- PÁJARO DECORATIVO --------
        if (birdTex != null) {
            batch.draw(birdTex, birdX, birdY, birdW, birdH);
        }

        // -------- BOTÓN PLAY --------
        ui.drawPanel(batch,
            playBounds.x, playBounds.y,
            playBounds.width, playBounds.height,
            0.65f
        );

        ui.drawCenteredScaled(
            batch,
            "PLAY",
            playBounds.x + playBounds.width / 2f -2,
            playBounds.y + playBounds.height / 2f +1,
            1.2f
        );
    }


    //Libera recursos específicos de esta pantalla 
    @Override
    protected void unloadResources() {
        // NO HACER NADA
       
    }

    //Asigna valores para el UI
    @Override
    protected void setupUI() {

        // --- Panel central ---
        panelWidth = worldWidth - 80;
        panelHeight = 120;
        panelX = (worldWidth - panelWidth) / 2f;
        panelY = worldHeight / 2f - panelHeight / 2f;

        // --- Título ---
        titleText = "GAME OVER";
        titleScale = 1.7f;
        titleY = panelY + panelHeight - 18;

        // --- Posiciones de textos SCORE & HIGH SCORE ---
        scoreLabelY = panelY + panelHeight - 40;
        highScoreLabelY = panelY + panelHeight - 60;

        // --- Pájaro decorativo ---
        birdX = panelX + 20;
        birdY = panelY + 10;

        // --- Botón PLAY (AQUÍ VA) ---
        float buttonWidth = 175;
        float buttonHeight = 30;

        float playX = (worldWidth - buttonWidth) / 2f;
        float playY = panelY - buttonHeight - 20;

        playBounds = new Rectangle(playX, playY, buttonWidth, buttonHeight);
    }


}
