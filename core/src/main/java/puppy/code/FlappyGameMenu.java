package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.Screens.MainMenuScreen;

/**
 * Clase FlappyGameMenu
 * Clase principal del juego (punto de entrada).
 * Extiende Game de LibGDX → administra pantallas, recursos y flujo general del juego.
 * Aplica encapsulamiento (GM1.6) y usa Singleton de Asset (GM2.1).
 */
public class FlappyGameMenu extends Game {

    // --- Constantes y atributos privados (GM1.6) ---
    public static final float GROUND_LEVEL = 96f;
    private SpriteBatch batch;
    private BitmapFont font;
    private int higherScore;
    private Asset assets; // Acceso centralizado a los recursos del juego

    /** Método principal: inicializa el juego y su primera pantalla */
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Fuente por defecto de LibGDX
        assets = Asset.getInstancia(); // Uso del Singleton (GM2.1)
        this.setScreen(new MainMenuScreen(this)); // Pantalla inicial
    }

    /** Ciclo de renderizado principal (mantiene el flujo del juego) */
    @Override
    public void render() {
        super.render(); // Llama al render de la pantalla activa
    }

    /** Libera los recursos globales al cerrar la aplicación */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        assets.dispose();
    }

    // --- Métodos de acceso (encapsulamiento GM1.6) ---
    public SpriteBatch getBatch() { return batch; }
    public BitmapFont getFont() { return font; }
    public int getHigherScore() { return higherScore; }
    public void setHigherScore(int higherScore) { this.higherScore = higherScore; }
    public Asset getAssets() { return assets; }
}
