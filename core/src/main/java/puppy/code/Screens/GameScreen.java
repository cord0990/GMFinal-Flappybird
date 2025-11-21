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
 * Controla la lógica principal del juego (movimiento, colisiones, puntaje, etc.).
 * Usa Asset como Singleton (GM2.1) y DifficultyStrategy como patrón Strategy (GM2.3).
 */
public class GameScreen implements Screen {

    // --- Constantes y atributos privados ---
    public static final float worldHeight = 600f;
    public static final float worldWidth = 288f;

    private final FlappyGameMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private Character bird;
    private Obstaculo obstaculos;
    private Music bgMusic;

    private final float gravity = -600f;

    private int score = 0;
    private boolean gameOver = false;
    private boolean initialized = false;

    private Asset assets;

    // *** patron Strategy --- Estrategia de dificultad (GM2.3) ***
    private DifficultyStrategy difficulty;

    /** Constructor: recibe la referencia al juego principal */
    public GameScreen(final FlappyGameMenu game) {
        this.game = game;

        // Dificultad inicial → Normal
        this.difficulty = new DynamicDifficulty();
    }

    /** Inicializa recursos y entidades de la partida */
    @Override
    public void show() {
        if (initialized) return; // evita reinicializar si LibGDX llama show() otra vez

        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);

        // Uso del Singleton para recursos compartidos (GM2.1)
        assets = Asset.getInstancia();

        bgMusic = assets.getBackgroundMusic();

        // Entidades principales
        bird = new Character(20, 350, assets.getBirdFrames(), assets.getBirdFlap());

        // Obstáculos creados usando la estrategia de dificultad
        obstaculos = new Obstaculo(assets, difficulty);

        score = 0;
        gameOver = false;
        initialized = true;
    }

    /** Bucle principal de render: actualiza lógica y dibuja */
    @Override
    public void render(float delta) {
        update(delta);

        if (game.getScreen() != this) return;

        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(assets.getBackground(), 0, 0,worldWidth,worldHeight);

        bgMusic.play();
        for (Colision p : obstaculos.getColisiones()) {
            p.draw(batch, worldHeight);
        }

        bird.draw(batch);
        batch.draw(assets.getGround(), 0, 0);
        font.draw(batch, "Puntaje: " + score, 10, worldHeight - 10);
        batch.end();
    }

    /** Actualiza el estado del juego **/
    private void update(float dt) {
        // --- Pausa ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            bgMusic.stop();
            this.pause();
        }

        // --- Si ya está en Game Over ---
        if (gameOver) {
            if (score > game.getHigherScore()) {
                game.setHigherScore(score);
            }
            game.setScreen(new GameOverScreen(game, score));
            bgMusic.stop();
            return;
        }

        // --- Movimiento del jugador ---
        bird.movimiento(dt, gravity);

        obstaculos.actualizarColision(dt,game,this);

        // *** Cambio automático de dificultad (GM2.3) ***
        actualizarDificultad();

        bird.fueraDePantalla(this, game);
    }

    /**
     * Cambia la estrategia de dificultad según el puntaje.
     */
    private void actualizarDificultad() {

        // DynamicDifficulty ahora NO guarda score, recibe score por parámetro
        // y Obstaculo tiene un nuevo setDifficulty(newDifficulty, score)
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

    /** Libera solo recursos locales; Asset se libera en FlappyGameMenu */
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
