package puppy.code;

/**
 * Clase DynamicDifficulty
 * Implementa una estrategia concreta de dificultad dinámica,
 * ajustando la velocidad de tubos, enemigos y el espaciado
 * según el puntaje actual del jugador.
 * Forma parte de las estrategias aplicadas para el patrón Strategy (GM2.3).
 */
public class DynamicDifficulty implements DifficultyStrategy {

    /**
     * Devuelve la velocidad de los tubos según el puntaje actual.
     * Incrementa progresivamente para aumentar la dificultad
     * de manera controlada en el patrón Strategy (GM2.3).
     */
    @Override
    public float getPipeSpeed(int score) {
        if (score < 10) return 120f;
        if (score < 20) return 135f;
        if (score < 30) return 150f;
        return 165f;
    }

    /**
     * Devuelve la velocidad del enemigo volador según el puntaje.
     * Mantiene coherencia con el aumento gradual de dificultad.
     * Parte clave de la estrategia usada por el patrón Strategy (GM2.3).
     */
    @Override
    public float getEnemySpeed(int score) {
        if (score < 10) return 120f;
        if (score < 20) return 135f;
        if (score < 30) return 150f;
        return 165f;
    }

    /**
     * Devuelve el espaciado horizontal entre obstáculos según el puntaje.
     * A menor separación, mayor es la dificultad del recorrido.
     * Parámetro adaptable mediante el patrón Strategy (GM2.3).
     */
    @Override
    public float getObstacleSpacing(int score) {
        if (score < 10) return 200f;
        if (score < 20) return 190f;
        if (score < 30) return 180f;
        return 170f;
    }
}
