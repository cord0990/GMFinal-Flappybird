package puppy.code;

/**
 * Interfaz DifficultyStrategy
 * Define una familia de estrategias de dificultad para el juego,
 * permitiendo variar velocidades y separación de obstáculos
 * sin cambiar el código de las clases que los usan.
 * Es la base del patrón Strategy (GM2.3) aplicado a la dificultad.
 */
public interface DifficultyStrategy {

    /** Retorna la velocidad de los tubos según el puntaje actual, para el patrón Strategy (GM2.3). */
    float getPipeSpeed(int score);

    /** Retorna la velocidad del enemigo volador según el puntaje actual, para el patrón Strategy (GM2.3). */
    float getEnemySpeed(int score);

    /** Retorna la distancia horizontal entre obstáculos según el puntaje actual, para el patrón Strategy (GM2.3). */
    float getObstacleSpacing(int score);
}
