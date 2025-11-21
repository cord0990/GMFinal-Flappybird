package puppy.code.Colisiones;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import puppy.code.DifficultyStrategy;

/**
 * Interfaz Colision
 * Define el comportamiento estándar para cualquier elemento del juego
 * que interactúa mediante colisiones (Tubo, Enemigo, etc.).
 * → Evidencia del polimorfismo aplicado (GM1.5) y base para el patron Strategy (GM2.3).
 */
public interface Colision {

    /** Actualiza la posición o estado del objeto según el tiempo transcurrido (delta time) */
    void update(float delta);

    /** Determina si existe colisión con el área (hitbox) del jugador */
    boolean colisiona(Rectangle boundsJugador);

    /** Indica si el objeto ya salió de la pantalla (por la izquierda) */
    boolean fueraDePantalla();

    /** Reposiciona el objeto, por ejemplo, para reciclarlo más a la derecha */
    void reposicionar(float nuevoX);

    /** Devuelve la coordenada X del objeto */
    float getX();

    /** Devuelve el ancho útil del objeto (para cálculo de espacio o colisión) */
    float getAncho();

    /** Dibuja el objeto en pantalla usando un SpriteBatch de LibGDX */
    void draw(SpriteBatch batch, float worldheight);

    /** Devuelve la velocidad horizontal del objeto */
    float getVelocidad();

    /** Devuelve las hitboxes asociadas (superior/inferior o principal) */
    Rectangle[] getBounds();

    /** Permite cambiar la velocidad en tiempo de ejecución (usado por DifficultyStrategy GM2.3) */
    void setVelocidad(float nuevaVelocidad);

    /**
     * Aplica la estrategia de dificultad al objeto usando el puntaje actual.
     * Permite que cada implementación decida su propio ajuste de dificultad.
     * → Evita instanceof y mantiene el polimorfismo limpio para el patron Strategy(GM2.3).
     */
    void aplicarEstrategia(DifficultyStrategy strategy, int score);
}
