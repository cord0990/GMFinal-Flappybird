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
 * Aplica principios OO: encapsulamiento y responsabilidad única → GM1.6.
 * Además, puede actuar como contexto para estrategias de movimiento → GM2.3.
 */
public class Character {

    // --- Atributos privados (GM1.6) ---
    private Texture[] frames;       // Sprites del personaje (animación)
    private float animTimer = 0f;   // Control del tiempo de animación
    private int frameIndex = 0;     // Índice del frame actual
    private Rectangle bounds = new Rectangle(); // Área de colisión
    private boolean alive = true;   // Estado del jugador
    private Sound birdFlap;

    // Vectores públicos (pos, vel, size) — usados por el motor físico y otras clases
    public Vector2 pos = new Vector2(0, 0);
    public Vector2 vel = new Vector2(0, 0);
    public Vector2 size = new Vector2(32, 32);

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

    /** Realiza el salto del personaje */
    public void flap() {
        vel.y = 260;
        birdFlap.play();
    }

    /**
     * Aplica gravedad y actualiza animación (física del personaje)
     * @param dt tiempo transcurrido
     * @param gravity constante de gravedad vertical
     */
    public void presionAtmosferica(float dt, float gravity) {
        vel.y += gravity * dt;              // aceleración hacia abajo
        pos.add(vel.x * dt, vel.y * dt);    // movimiento físico

        animTimer += dt;
        if (animTimer > 0.12f) {            // controla frecuencia de aleteo
            animTimer = 0f;
            frameIndex = (frameIndex + 1) % frames.length;
        }

        bounds.setPosition(pos.x, pos.y);   // actualiza la hitbox
    }

    /**
     * Movimiento principal controlado por el jugador
     * Aplica la estrategia de control actual → GM2.3 (Strategy Pattern)
     */
    public void movimiento(float dt, float gravity) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            this.flap();
        }
        this.presionAtmosferica(dt, gravity);
    }

    /** Devuelve la hitbox actual para verificar colisiones */
    public Rectangle getBounds() {
        return bounds;
    }
    
    public void fueraDePantalla(GameScreen screen, FlappyGameMenu game) {
    	if (screen.getBird().pos.y <= 96 || screen.getBird().pos.y + 24 >= GameScreen.getWorldheight()) {
            screen.setGameOver(true);
            if (screen.getScore() > game.getHigherScore()) game.setHigherScore(screen.getScore());}
        }

    /** Indica si el personaje sigue con vida */
    public boolean isAlive() {
        return alive;
    }

    /** Dibuja el personaje en pantalla con el frame animado actual */
    public void draw(SpriteBatch batch) {
        batch.draw(frames[frameIndex], pos.x, pos.y);
    }

    /** Reinicia la posición y velocidad del personaje */
    public void reset(float y) {
        pos.set(80, y);
        vel.set(0, 0);
    }
}
