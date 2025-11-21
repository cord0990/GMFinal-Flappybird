package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import puppy.code.Asset;
import puppy.code.Character;
import puppy.code.FlappyGameMenu;
import puppy.code.Obstaculo;
import puppy.code.Colisiones.Colision;

import puppy.code.DifficultyStrategy;
import puppy.code.DynamicDifficulty;

/**
 * Clase GameScreen
 *
 * Pantalla principal del juego donde ocurre toda la lógica de gameplay:
 * movimiento del jugador, desplazamiento de obstáculos, colisiones,
 * actualización de puntaje y transición a pantallas especiales.
 *
 * - Usa Asset como Singleton para recursos globales (GM2.1).
 * - Aplica el patrón Strategy (GM2.3) mediante DifficultyStrategy para ajustar
 *   dinámicamente la dificultad según el puntaje del jugador.
 * - Mantiene responsabilidades bien separadas → evidencia de OO y encapsulamiento (GM1.6).
 */
public class GameScreen implements Screen {

    /**--- Constantes y atributos privados ---*/

    // --- Dimensiones lógicas de la pantalla ---
    public static final float worldHeight = 600f;
    public static final float worldWidth = 288f;

    // --- Dependencias principales del ciclo de juego ---
    private final FlappyGameMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    // --- Entidades del gameplay ---
    private Character bird;
    private Obstaculo obstaculos;
    private Music bgMusic;

    // --- Constantes de física ---
    private final float gravity = -600f;

    // --- Estado del juego ---
    private int score = 0;
    private boolean gameOver = false;
    private boolean initialized = false;

    private Asset assets;

    // --- Patrón Strategy (GM2.3):
    // Define la estrategia de dificultad utilizada durante la partida.
    private DifficultyStrategy difficulty;

    /**
     * Constructor: recibe la referencia al juego principal y asigna la
     * estrategia inicial de dificultad (DynamicDifficulty).
     */
    public GameScreen(final FlappyGameMenu game) {
        this.game = game;

        // Dificultad inicial → Normal
        this.difficulty = new DynamicDifficulty();
    }

    /**
     * Inicializa los recursos y entidades. LibGDX puede llamar este metodo
     * múltiples veces, por eso se controla mediante "initialized".
     */
    @Override
    public void show() {
        if (initialized) return; // evita reinicializar si LibGDX llama show() otra vez

        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);

        // Acceso global a recursos mediante Singleton (GM2.1)
        assets = Asset.getInstancia();
        bgMusic = assets.getBackgroundMusic();

        // Instancia del jugador y los obstáculos iniciales
        bird = new Character(20, 350, assets.getBirdFrames(), assets.getBirdFlap());
        obstaculos = new Obstaculo(assets, difficulty, score); //ahora obstaculo recibe tambien score inicial -sugerido por ayudante-

        score = 0;
        gameOver = false;
        initialized = true;
    }

    /**Ciclo principal: delega actualización de lógica y luego dibuja.*/
    @Override
    public void render(float delta) {
        update(delta);

        // Seguridad: si la pantalla cambió durante update, no dibujar más
        if (game.getScreen() != this) return;

        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Fondo general
        batch.draw(assets.getBackground(), 0, 0,worldWidth,worldHeight);

        bgMusic.play();

        // Dibujo polimórfico de los obstáculos
        for (Colision p : obstaculos.getColisiones()) {
            p.draw(batch, worldHeight);
        }

        // Jugador y suelo
        bird.draw(batch);
        batch.draw(assets.getGround(), 0, 0);

        // Puntaje actual
        font.draw(batch, "Puntaje: " + score, 10, worldHeight - 10);

        batch.end();
    }

    /**
     * Actualiza física, colisiones y estado del juego.
     * Orquesta la lógica central del gameplay.
     */
    private void update(float dt) {
        // --- Pausa ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            bgMusic.stop();
            this.pause();
        }

        // --- Game Over ---
        if (gameOver) {
            if (score > game.getHigherScore()) {
                game.setHigherScore(score);
            }
            game.setScreen(new GameOverScreen(game, score));
            bgMusic.stop();
            return;
        }

        // --- Movimiento y física ---
        bird.movimiento(dt, gravity);

        // --- Colisiones y reposicionamientos ---
        obstaculos.actualizarColision(dt,game,this);

        // --- Ajuste dinámico de la dificultad (Strategy GM2.3) ---
        actualizarDificultad();

        // --- Verificación de límites de pantalla ---
        bird.fueraDePantalla(this, game);
    }

    /**
     * Aplica la estrategia actual de dificultad enviando el puntaje a Obstaculo.
     * DynamicDifficulty calcula valores dinámicos a partir del score.
     */
    private void actualizarDificultad() {
        obstaculos.setDifficulty(difficulty, score);
    }

    // --- Métodos del ciclo de vida de pantalla ---
    @Override public void resize(int width, int height) { }
    @Override public void pause() {
        game.setScreen(new PauseScreen(game, this));
        return;
    }
    @Override public void resume() { }
    @Override public void hide() { }

    /**
     * Libera los recursos locales de la pantalla.
     * Los recursos globales se liberan a nivel de FlappyGameMenu.
     */
    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (font != null) {
            font.dispose();
            font = null;
        }
        initialized = false;
    }

    // --- Getters utilizados por otras clases (Obstaculo, etc.) ---
    public Character getBird() { return bird; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public boolean getGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public static float getWorldheight() {
        return worldHeight;
    }

    public Asset getAssets() {
        return assets;
    }

    public void setAssets(Asset assets) {
        this.assets = assets;
    }

    public Music getBgMusic() {
        return bgMusic;
    }

    public void setBgMusic(Music bgMusic) {
        this.bgMusic = bgMusic;
    }
}
