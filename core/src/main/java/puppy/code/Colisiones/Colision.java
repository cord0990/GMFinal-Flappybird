package puppy.code.Colisiones;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Interfaz Colision
 * Define el contrato para todos los objetos que participan en el sistema de colisiones del juego.
 * Implementada por: Tubo, Enemigo, (y opcionalmente Jugador)
 * → Evidencia directa del punto GM1.5.
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
}
