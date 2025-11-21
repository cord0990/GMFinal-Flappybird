package puppy.code.Colisiones;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

import puppy.code.DifficultyStrategy;//Integración para el patrón Strategy (GM2.3)

/**
 * Clase Tubo
 * Representa un par de tubos (superior e inferior) que actúan como obstáculos.
 * Implementa la interfaz Colision → evidencia del polimorfismo (GM1.5).
 * Aplica encapsulamiento mediante atributos privados y getters (GM1.6).
 * Ajusta su comportamiento usando DifficultyStrategy para el patrón Strategy (GM2.3).
 */
public class Tubo implements Colision {

    // --- Atributos privados (encapsulamiento GM1.6) ---
    private float xInicio;               // posición X actual del tubo
    private float gapY;                  // centro vertical del hueco de paso
    private float velocidad = 120f;      // velocidad de desplazamiento
    private static final float GAP = 120f; // tamaño del hueco entre tubos
    private Texture textura;             // textura del tubo
    private final Rectangle[] bounds = new Rectangle[2]; // hitboxes: [0]=superior, [1]=inferior
    private Vector2 size = new Vector2(); // Tamaño de la textura
    private float worldHeight;           // // Altura lógica del mundo (para evitar 512 "quemado")

    // Ajuste fino de la hitbox superior
    private static final float TOP_LIFT = 14f;

    /**
     * Constructor del tubo.
     * @param pipeTex textura del tubo
     * @param startX posición inicial en el eje X
     * @param worldHeight alto del mundo del juego
     */
    public Tubo(Texture pipeTex, float startX, float worldHeight) {
        // Constructor original: usa velocidad por defecto (120f)
        this(pipeTex, startX, worldHeight, 120f);
    }

    /**
     * Constructor parametrizado: permite definir la velocidad inicial del tubo.
     * Utilizado por estrategias de dificultad para el patrón Strategy (GM2.3).
     */
    public Tubo(Texture pipeTex, float startX, float worldHeight, float velocidad) {
        this.textura = pipeTex;
        this.xInicio = startX;
        this.size = new Vector2(pipeTex.getWidth(), pipeTex.getHeight());
        this.velocidad = velocidad;
        this.worldHeight = worldHeight;
        bounds[0] = new Rectangle();
        bounds[1] = new Rectangle();
        randomizeGap(worldHeight);
        updateRects(worldHeight);
    }

    // --- Métodos privados auxiliares ---

    /** Calcula aleatoriamente la posición vertical del hueco entre tubos */
    private void randomizeGap(float worldHeight) {
        float minY = 150;
        float maxY = worldHeight - 150;
        gapY = MathUtils.random(minY, maxY);
    }

    /** Actualiza las hitboxes superior e inferior según la posición actual */
    private void updateRects(float worldHeight) {
        // Hitbox del tubo inferior
        float bottomHeight = gapY - GAP / 2f;
        if (bottomHeight < 0) bottomHeight = 0;
        bounds[1].set(xInicio, 0, size.x, bottomHeight);

        // Hitbox del tubo superior
        float topY = gapY + GAP / 2f;
        float topHitboxY = topY + TOP_LIFT;
        float topHitboxHeight = worldHeight - topHitboxY;
        if (topHitboxHeight < 0) topHitboxHeight = 0;

        bounds[0].set(xInicio, topHitboxY, size.x, topHitboxHeight);
    }

    //--- Implementación de la interfaz Colision --- (GM1.5) ---

    /** Actualiza la posición del tubo desplazándolo hacia la izquierda */
    @Override
    public void update(float dt) {
        xInicio -= velocidad * dt;
        updateRects(worldHeight);
    }

    /** Indica si el tubo salió completamente de la pantalla */
    @Override
    public boolean fueraDePantalla() {
        return xInicio + size.x < 0;
    }

    /** Reposiciona el tubo y genera un nuevo hueco aleatorio */
    @Override
    public void reposicionar(float newX) {
        xInicio = newX;
        randomizeGap(worldHeight);
        updateRects(worldHeight);
    }

    /** Dibuja el tubo superior e inferior */
    @Override
    public void draw(SpriteBatch batch, float worldHeight) {
        batch.draw(textura, xInicio, 0, size.x, (int) (gapY - GAP / 2f)); // tubo inferior
        batch.draw(textura, xInicio, (int) (gapY + GAP / 2f),
            size.x, (int) (worldHeight - (gapY + GAP / 2f))); // tubo superior
    }

    /** Retorna true si el jugador colisiona con alguno de los tubos */
    @Override
    public boolean colisiona(Rectangle boundsJugador) {
        return boundsJugador.overlaps(bounds[0]) || boundsJugador.overlaps(bounds[1]);
    }

    // --- Getters públicos (encapsulamiento GM1.6) ---
    @Override
    public float getX() { return xInicio; }

    @Override
    public float getVelocidad() { return velocidad; }

    @Override
    public Rectangle[] getBounds() { return bounds; }

    public Vector2 getSize() { return size; }

    @Override
    public float getAncho() { return size.x; }

    /** Cambia la velocidad del tubo (usado por Strategy) */
    @Override
    public void setVelocidad(float nuevaVelocidad) {
        this.velocidad = nuevaVelocidad;
    }

    /**
     * Ajusta la velocidad del tubo usando la estrategia de dificultad.
     * Parte fundamental para el patrón Strategy (GM2.3).
     */
    @Override
    public void aplicarEstrategia(DifficultyStrategy strategy, int score) {
        this.velocidad = strategy.getPipeSpeed(score);
    }
}
