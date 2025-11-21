package puppy.code.Colisiones;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import puppy.code.DifficultyStrategy; // ← NUEVO IMPORT

/**
 * Clase Tubo
 * Representa los obstáculos del juego (tubos superior e inferior).
 * Implementa la interfaz Colision → evidencia del punto GM1.5.
 * Aplica encapsulamiento (atributos privados + getters) → evidencia GM1.6.
 */
public class Tubo implements Colision {

    // --- Atributos privados (encapsulamiento GM1.6) ---
    private float xInicio;               // posición X actual del tubo
    private float gapY;                  // centro vertical del hueco de paso
    private float velocidad = 120f;      // velocidad de desplazamiento
    private static final float GAP = 120f; // tamaño del hueco entre tubos
    private Texture textura;             // textura del tubo
    private final Rectangle[] bounds = new Rectangle[2]; // hitboxes: [0]=superior, [1]=inferior
    private Vector2 size = new Vector2(); // tamaño del sprite
    private float worldHeight;           // alto lógico del mundo (para evitar 512 "quemado")

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
     * Constructor del tubo con velocidad parametrizable
     * (útil para DifficultyStrategy en GM2.3).
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

    /** Genera aleatoriamente la posición vertical del hueco entre tubos */
    private void randomizeGap(float worldHeight) {
        float minY = 150;
        float maxY = worldHeight - 150;
        gapY = MathUtils.random(minY, maxY);
    }

    /** Actualiza las posiciones de las hitboxes superior e inferior */
    private void updateRects(float worldHeight) {
        // tubo inferior
        float bottomHeight = gapY - GAP / 2f;
        if (bottomHeight < 0) bottomHeight = 0;
        bounds[1].set(xInicio, 0, size.x, bottomHeight);

        // tubo superior
        float topY = gapY + GAP / 2f;
        float topHitboxY = topY + TOP_LIFT;
        float topHitboxHeight = worldHeight - topHitboxY;
        if (topHitboxHeight < 0) topHitboxHeight = 0;

        bounds[0].set(xInicio, topHitboxY, size.x, topHitboxHeight);
    }

    // --- Métodos de la interfaz Colision (GM1.5) ---

    /** Actualiza la posición del tubo a lo largo del tiempo */
    @Override
    public void update(float dt) {
        xInicio -= velocidad * dt;
        updateRects(worldHeight);
    }

    /** Indica si el tubo ya salió completamente de la pantalla */
    @Override
    public boolean fueraDePantalla() {
        return xInicio + size.x < 0;
    }

    /** Reposiciona el tubo a una nueva coordenada X, generando un nuevo hueco aleatorio */
    @Override
    public void reposicionar(float newX) {
        xInicio = newX;
        randomizeGap(worldHeight);
        updateRects(worldHeight);
    }

    /** Dibuja el par de tubos (superior e inferior) */
    @Override
    public void draw(SpriteBatch batch, float worldHeight) {
        batch.draw(textura, xInicio, 0, size.x, (int) (gapY - GAP / 2f)); // tubo inferior
        batch.draw(textura, xInicio, (int) (gapY + GAP / 2f),
            size.x, (int) (worldHeight - (gapY + GAP / 2f))); // tubo superior
    }

    /** Verifica colisión entre el jugador y las hitboxes del tubo */
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

    /** Permite cambiar la velocidad en tiempo de ejecución (Strategy de dificultad) */
    @Override
    public void setVelocidad(float nuevaVelocidad) {
        this.velocidad = nuevaVelocidad;
    }

    /**
     * Aplica la estrategia de dificultad al tubo usando el puntaje actual.
     * Evita el uso de instanceof en Obstaculo.
     */
    @Override
    public void aplicarEstrategia(DifficultyStrategy strategy, int score) {
        this.velocidad = strategy.getPipeSpeed(score);
    }
}
