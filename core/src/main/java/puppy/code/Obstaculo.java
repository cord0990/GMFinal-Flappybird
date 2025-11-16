package puppy.code;

import com.badlogic.gdx.math.Rectangle;
import puppy.code.Colisiones.Colision;
import puppy.code.Colisiones.Enemigo;
import puppy.code.Colisiones.Tubo;
import puppy.code.Screens.GameScreen;
import com.badlogic.gdx.audio.Sound;



/**
 * Clase Obstaculo
 * Administra todos los objetos que generan colisiones (tubos, enemigos, etc.).
 * Utiliza polimorfismo a través de la interfaz Colision → GM1.5 / GM1.6.
 * Aplica el patrón Strategy (GM2.3), ya que cada objeto tiene su propio comportamiento.
 */
public class Obstaculo {

    // --- Atributo privado (encapsulamiento GM1.6) ---
    private Colision[] colisiones; // Colección de objetos con comportamiento distinto

    /**
     * Constructor: inicializa tubos y enemigo con sus texturas desde Asset.
     * @param ast instancia de Asset (Singleton)
     */
    public Obstaculo(Asset ast) {
        this.colisiones = new Colision[] {
            // Tubos fijos
            new Tubo(ast.getTuboTex(), 350, GameScreen.worldHeight),
            new Tubo(ast.getTuboTex(), 550, GameScreen.worldHeight),
            // Enemigo volador (comportamiento independiente)
            new Enemigo(ast.getEnemyFrames(), ast.getTuboTex().getWidth(), 750, GameScreen.worldHeight)
        };
    }

    /** Devuelve todos los objetos con colisiones activas */
    public Colision[] getColisiones() {
        return this.colisiones;
    }

    /**
     * Método que actualiza, verifica colisiones y administra puntaje.
     * Este método combina estrategias de movimiento y detección (GM2.3).
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
                p.reposicionar(max + 200);
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
                    
                }
            }
        }
    }
}
