package puppy.code;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;
import puppy.code.Colisiones.Colision;
import puppy.code.Screens.GameScreen;

/**
 * Clase Obstaculo
 * Administra todos los objetos que generan colisiones (tubos, enemigos, etc.)
 * en la escena de juego. Centraliza la actualización, reposicionamiento y
 * detección de colisiones con el jugador.
 *
 * Utiliza polimorfismo a través de la interfaz Colision (GM1.5 / GM1.6)
 * y coordina los parámetros de dificultad usando DifficultyStrategy
 * para el patrón Strategy (GM2.3).
 */
public class Obstaculo {

    // --- Atributo privado (encapsulamiento GM1.6) ---
    private Colision[] colisiones; // Conjunto de objetos que pueden colisionar con el jugador

    // Estrategia de dificultad aplicada a los obstáculos (para el patrón Strategy (GM2.3))
    private DifficultyStrategy difficulty;

    /**
     * Constructor: inicializa tubos y enemigo con sus texturas desde Asset.
     * Asigna la estrategia de dificultad recibida, que define velocidades
     * iniciales según el puntaje.
     * @param ast instancia de Asset (Singleton)
     * @param difficulty estrategia de dificultad (Strategy GM2.3)
     */
    public Obstaculo(Asset ast, DifficultyStrategy difficulty) {
        this.difficulty = difficulty;

        // Puntaje inicial del jugador
        int initialScore = 0;

        this.colisiones = new Colision[] {
            new puppy.code.Colisiones.Tubo(ast.getTuboTex(), 350, GameScreen.worldHeight, difficulty.getPipeSpeed(initialScore)),
            new puppy.code.Colisiones.Tubo(ast.getTuboTex(), 550, GameScreen.worldHeight, difficulty.getPipeSpeed(initialScore)),
            new puppy.code.Colisiones.Enemigo(ast.getEnemyFrames(), ast.getTuboTex().getWidth(),
                750, GameScreen.worldHeight, difficulty.getEnemySpeed(initialScore))
        };
    }

    /** Devuelve todos los objetos de colisión activos en la escena. */
    public Colision[] getColisiones() {
        return this.colisiones;
    }

    /**
     * Actualiza la estrategia de dificultad asociada a los obstáculos.
     * Se invoca desde GameScreen cuando cambia el puntaje y se requiere
     * ajustar velocidades y espaciado para el patrón Strategy (GM2.3).
     *
     * @param newDifficulty nueva estrategia de dificultad a aplicar
     * @param score puntaje actual del jugador
     */
    public void setDifficulty(DifficultyStrategy newDifficulty, int score) {
        this.difficulty = newDifficulty;

        // Polimorfismo puro: cada Colision sabe cómo aplicar la estrategia
        for (Colision c : colisiones) {
            c.aplicarEstrategia(newDifficulty, score);
        }
    }

    /**
     * Actualiza, reposiciona y verifica colisiones para todos los obstáculos.
     * También administra el puntaje del jugador al superar cada obstáculo.
     * Combina el comportamiento propio de cada Colision con la dificultad
     * definida por DifficultyStrategy para el patrón Strategy (GM2.3).
     */
    public void actualizarColision(float dt, FlappyGameMenu game, GameScreen screen) {
        Sound hurt = (Sound) screen.getAssets().getBirdHurt();
        boolean colisiona = false;

        for (Colision p : this.getColisiones()) {
            // Cada obstáculo actualiza su lógica interna (tubos/enemigo)
            p.update(dt);

            // --- Reposicionamiento cuando sale de pantalla ---
            if (p.fueraDePantalla()) {
                float max = 0;
                for (Colision other : this.getColisiones())
                    if (other.getX() > max) max = other.getX();

                // Distancia entre obstáculos determinada por la estrategia de dificultad
                float spacing = difficulty.getObstacleSpacing(screen.getScore());

                p.reposicionar(max + spacing);
            }

            // --- Verificación de colisión con el jugador ---
            for (Rectangle b : p.getBounds()) {
                if (screen.getBird().getBounds().overlaps(b)) {
                    colisiona = true;
                    break;
                }
            }

            if (colisiona) {
                screen.setGameOver(true);
                hurt.play();

                if (screen.getScore() > game.getHigherScore()) {
                    game.setHigherScore(screen.getScore());
                }
            }

            // --- Sistema de puntaje: cuando el pájaro cruza un obstáculo ---
            if (!screen.getGameOver()) {
                float birdX = screen.getBird().pos.x;
                float centroAhora = p.getX() + p.getAncho() / 2f;
                float centroAntes = centroAhora + p.getVelocidad() * dt;

                // Si el pájaro cruzó el centro del obstáculo entre frames
                if (centroAntes >= birdX && centroAhora < birdX) {
                    screen.setScore(screen.getScore() + 1);

                    if (screen.getScore() > game.getHigherScore()) {
                        game.setHigherScore(screen.getScore());
                    }

                    // Aplicamos la estrategia con el nuevo puntaje (dificultad dinámica)
                    p.aplicarEstrategia(difficulty, screen.getScore());
                }
            }
        }
    }
}
