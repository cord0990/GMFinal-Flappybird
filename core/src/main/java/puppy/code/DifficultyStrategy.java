package puppy.code;

/**
 * Interfaz DifficultyStrategy
 * Define los parámetros de dificultad del juego (velocidades, separación, etc.).
 * → Evidencia directa del patrón Strategy (GM2.3).
 */
public interface DifficultyStrategy {

    /** Velocidad de desplazamiento de los tubos */
    float getPipeSpeed(int score);

    /** Velocidad de desplazamiento del enemigo volador */
    float getEnemySpeed(int score);

    /** Distancia horizontal entre obstáculos cuando se reposicionan */
    float getObstacleSpacing(int score);
}
