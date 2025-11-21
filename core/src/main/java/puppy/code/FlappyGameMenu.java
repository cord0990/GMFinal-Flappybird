package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.Screens.MainMenuScreen;

/**
 * Clase FlappyGameMenu
 * Punto de entrada del juego. Administra el ciclo de vida general, las pantallas
 * y los recursos compartidos. Extiende Game de LibGDX, permitiendo gestionar
 * múltiples pantallas de forma centralizada.
 *
 * Usa el Singleton Asset para cargar recursos globales (GM2.1) y aplica
 * encapsulamiento mediante atributos privados y getters (GM1.6).
 */
public class FlappyGameMenu extends Game {

    // --- Constantes y atributos privados (GM1.6) ---
    public static final float GROUND_LEVEL = 96f; // Altura del suelo en el mundo
    private SpriteBatch batch; // Batch global de renderizado
    private BitmapFont font; // Fuente por defecto para textos
    private int higherScore; // Mejor puntaje histórico del jugador
    private Asset assets; // Acceso centralizado a recursos (Singleton)

    /**
     * Metodo principal de inicialización.
     * Configura los recursos compartidos, activa el Singleton Asset
     * y establece la pantalla inicial del juego.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Fuente por defecto de LibGDX
        assets = Asset.getInstancia(); // Uso del Singleton (GM2.1)
        this.setScreen(new MainMenuScreen(this)); // Pantalla inicial del juego
    }

    /**
     * Ciclo de renderizado global.
     * LibGDX delega el render a la pantalla activa.
     */
    @Override
    public void render() {
        super.render(); // Llama al render de la pantalla activa
    }

    /**
     * Libera recursos globales al cerrar la aplicación.
     * Se asegura de que todos los recursos administrados por Asset también sean liberados.
     */
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
