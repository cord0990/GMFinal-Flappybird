package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import puppy.code.Screens.GameScreen;

/**
 * Clase Character
 * Representa al jugador (pájaro) controlado por el usuario.
 * Aplica encapsulamiento y responsabilidad única para manejar
 * animación, física y colisiones del jugador (GM1.6).
 * Su metodo movimiento permite integrar cambios de control
 * mediante estrategias externas para el patrón Strategy (GM2.3).
 */
public class Character {

    // --- Atributos privados (GM1.6) ---
    private Texture[] frames;       // Animación del personaje
    private float animTimer = 0f;   // Control del tiempo de animación
    private int frameIndex = 0;     // Índice del frame actual
    private Rectangle bounds = new Rectangle(); // Hitbox del jugador
    private boolean alive = true;   // Estado del jugador
    private Sound birdFlap;         // Sonido al aletear

    // --- Atributos públicos usados por sistemas externos ---
    public Vector2 pos = new Vector2(0, 0); // Posición en 2D
    public Vector2 vel = new Vector2(0, 0); // Velocidad actual
    public Vector2 size = new Vector2(32, 32); // Tamaño del sprite

    /**
     * Constructor del personaje principal.
     * @param x posición inicial en X
     * @param y posición inicial en Y
     * @param sprite frames de animación (desde Asset)
     * @param sound
     */
    public Character(float x, float y, Texture[] sprite, Sound sound) {
        this.pos.set(x, y);
        this.size.set(32, 32);
        this.frames = sprite;
        this.bounds.set(x, y, size.x, size.y);
        this.birdFlap = sound;
    }

    /** Ejecuta el salto del personaje y reproduce el sonido */
    public void flap() {
        vel.y = 260;
        birdFlap.play();
    }

    /**
     * Actualiza la física del personaje:
     * aplica gravedad, movimiento y calcula el cambio de frames.
     * @param dt tiempo transcurrido
     * @param gravity constante de gravedad vertical
     */
    public void presionAtmosferica(float dt, float gravity) {
        vel.y += gravity * dt;              // Aceleración vertical
        pos.add(vel.x * dt, vel.y * dt);    // Movimiento según velocidad

        // Control de animación (velocidad del aleteo)
        animTimer += dt;
        if (animTimer > 0.12f) {            // controla frecuencia de aleteo
            animTimer = 0f;
            frameIndex = (frameIndex + 1) % frames.length;
        }

        bounds.setPosition(pos.x, pos.y);   // actualiza la hitbox
    }

    /**
     * Movimiento principal del jugador.
     * Puede ser reemplazado por estrategias de control
     * Usado para el patrón Strategy (GM2.3).
     */
    public void movimiento(float dt, float gravity) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            this.flap();
        }
        this.presionAtmosferica(dt, gravity);
    }

    /** Devuelve la hitbox actual para detección de colisiones */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Detecta si el personaje sale de los límites verticales.
     * En caso de hacerlo, activa la pantalla Game Over.
     */
    public void fueraDePantalla(GameScreen screen, FlappyGameMenu game) {
    	if (screen.getBird().pos.y <= 96 || screen.getBird().pos.y + 24 >= GameScreen.getWorldheight()) {
            screen.setGameOver(true);
            if (screen.getScore() > game.getHigherScore()) game.setHigherScore(screen.getScore());}
        }

    /** Indica si el personaje sigue vivo */
    public boolean isAlive() {
        return alive;
    }

    /** Dibuja el sprite del personaje con el frame animado actual */
    public void draw(SpriteBatch batch) {
        batch.draw(frames[frameIndex], pos.x, pos.y);
    }

    /** Restablece la posición y velocidad del jugador */
    public void reset(float y) {
        pos.set(80, y);
        vel.set(0, 0);
    }
}
