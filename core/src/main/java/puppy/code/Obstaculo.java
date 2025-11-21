package puppy.code;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;
import puppy.code.Colisiones.Colision;
import puppy.code.Screens.GameScreen;
import puppy.code.FlappyGameMenu; // ← IMPORT NECESARIO

/**
 * Clase Obstaculo
 * Administra todos los objetos que generan colisiones (tubos, enemigos, etc.).
 * Utiliza polimorfismo a través de la interfaz Colision → GM1.5 / GM1.6.
 * Aplica el patrón Strategy (GM2.3), ya que cada objeto tiene su propio comportamiento.
 */
public class Obstaculo {

    // --- Atributo privado (encapsulamiento GM1.6) ---
    private Colision[] colisiones; // Colección de objetos con comportamiento distinto

    // Strategy de dificultad (GM2.3)
    private DifficultyStrategy difficulty;

    /**
     * Constructor: inicializa tubos y enemigo con sus texturas desde Asset.
     * @param ast instancia de Asset (Singleton)
     * @param difficulty estrategia de dificultad (Strategy GM2.3)
     */
    public Obstaculo(Asset ast, DifficultyStrategy difficulty) {
        this.difficulty = difficulty;

        // Al inicio asumimos score = 0
        int initialScore = 0;

        this.colisiones = new Colision[] {
            new puppy.code.Colisiones.Tubo(ast.getTuboTex(), 350, GameScreen.worldHeight, difficulty.getPipeSpeed(initialScore)),
            new puppy.code.Colisiones.Tubo(ast.getTuboTex(), 550, GameScreen.worldHeight, difficulty.getPipeSpeed(initialScore)),
            new puppy.code.Colisiones.Enemigo(ast.getEnemyFrames(), ast.getTuboTex().getWidth(),
                750, GameScreen.worldHeight, difficulty.getEnemySpeed(initialScore))
        };
    }

    /** Devuelve todos los objetos con colisiones activas */
    public Colision[] getColisiones() {
        return this.colisiones;
    }

    /**
     * Permite cambiar la dificultad en tiempo real.
     * Se usa cuando GameScreen actualiza el puntaje.
     */
    public void setDifficulty(DifficultyStrategy newDifficulty, int score) {
        this.difficulty = newDifficulty;

        // Polimorfismo puro — cada objeto aplica la estrategia según su tipo
        for (Colision c : colisiones) {
            c.aplicarEstrategia(newDifficulty, score);
        }
    }

    /**
     * Metodo que actualiza, verifica colisiones y administra puntaje.
     * Este metodo combina estrategias de movimiento y detección (GM2.3).
     */
    public void actualizarColision(float dt, FlappyGameMenu game, GameScreen screen) {
        Sound hurt = (Sound) screen.getAssets().getBirdHurt();
        boolean colisiona = false;

        for (Colision p : this.getColisiones()) {
            p.update(dt); // Cada tipo ejecuta su propia estrategia

            // --- Reposicionamiento cuando sale de pantalla ---
            if (p.fueraDePantalla()) {
                float max = 0;
                for (Colision other : this.getColisiones())
                    if (other.getX() > max) max = other.getX();

                // distance basada en la estrategia
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

                if (centroAntes >= birdX && centroAhora < birdX) {
                    screen.setScore(screen.getScore() + 1);

                    if (screen.getScore() > game.getHigherScore()) {
                        game.setHigherScore(screen.getScore());
                    }

                    // Aplicamos estrategia al subir puntaje (redundante pero válido)
                    p.aplicarEstrategia(difficulty, screen.getScore());
                }
            }
        }
    }
}
