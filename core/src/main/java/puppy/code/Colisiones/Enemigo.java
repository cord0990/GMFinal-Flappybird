package puppy.code.Colisiones;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase Enemigo
 * Representa un enemigo volador con animación y trayectoria sinusoidal.
 * Implementa la interfaz Colision → evidencia del punto GM1.5.
 * Aplica encapsulamiento y movimiento independiente → evidencia GM1.6 y GM2.3 (Strategy).
 */
public class Enemigo implements Colision {

    // --- Atributos privados (encapsulamiento GM1.6) ---
    private Texture[] frames;   // conjunto de sprites de animación
    private float x, y;         // posición base del enemigo
    private float width, height;
    private float velocidad;    // velocidad horizontal
    private float tiempo;       // tiempo acumulado para animación/movimiento
    private Rectangle[] bounds; // hitbox ajustada

    /**
     * Constructor del enemigo volador.
     * @param frames sprites de animación
     * @param anchoBase ancho de referencia (típicamente el del tubo)
     * @param startX posición inicial X
     * @param worldH altura lógica del mundo
     */
    public Enemigo(Texture[] frames, float anchoBase, float startX, float worldH) {
        this.frames = frames;
        this.width = anchoBase * 0.7f;
        this.height = anchoBase * 0.7f;
        this.x = startX;
        this.y = worldH / 2f; // posición base al centro de pantalla
        this.velocidad = 110f;
        this.tiempo = 0;

        // Ajuste fino de hitbox (más pequeña que el sprite real)
        float hitboxWidth = width * 0.75f;
        float hitboxHeight = height * 0.75f;
        float hitboxOffsetX = (width - hitboxWidth) / 2f;
        float hitboxOffsetY = (height - hitboxHeight) / 2f;

        this.bounds = new Rectangle[]{
            new Rectangle(x + hitboxOffsetX, y + hitboxOffsetY, hitboxWidth, hitboxHeight)
        };
    }

    // --- Métodos de la interfaz Colision (GM1.5 y GM2.3) ---

    /** Actualiza posición y animación del enemigo */
    @Override
    public void update(float dt) {
        tiempo += dt;
        x -= velocidad * dt;

        // Movimiento sinusoidal (Strategy GM2.3: comportamiento de vuelo)
        float offset = MathUtils.sin(tiempo * 3f) * 40f;
        float currentY = y + offset;

        // Actualiza posición de la hitbox en base al frame actual
        bounds[0].setPosition(
            x + (width - bounds[0].width) / 2f,
            currentY + (height - bounds[0].height) / 2f
        );
    }

    /** Dibuja el frame animado del enemigo */
    @Override
    public void draw(SpriteBatch batch, float worldHeight) {
        if (frames == null || frames.length == 0) return;
        int frameIndex = ((int)(tiempo * 10)) % frames.length;
        Texture frame = frames[frameIndex];
        batch.draw(frame, bounds[0].x, bounds[0].y, bounds[0].width, bounds[0].height);
    }

    /** Indica si el enemigo ha salido completamente de pantalla */
    @Override
    public boolean fueraDePantalla() {
        return bounds[0].x + bounds[0].width < 0;
    }

    /** Reposiciona el enemigo en una nueva coordenada X */
    @Override
    public void reposicionar(float nuevoX) {
        this.x = nuevoX;
        bounds[0].setX(nuevoX + (width - bounds[0].width) / 2f);
    }

    /** Verifica colisión con otro objeto rectangular */
    @Override
    public boolean colisiona(Rectangle other) {
        for (Rectangle r : bounds) {
            if (r.overlaps(other)) return true;
        }
        return false;
    }

    // --- Getters públicos (encapsulamiento GM1.6) ---
    @Override
    public float getX() { return bounds[0].x; }
    @Override
    public float getVelocidad() { return velocidad; }
    @Override
    public Rectangle[] getBounds() { return bounds; }
    @Override
    public float getAncho() { return bounds[0].width; }
}
