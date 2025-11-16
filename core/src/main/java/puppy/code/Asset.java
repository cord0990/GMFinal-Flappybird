package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase Asset
 * Encargada de cargar y administrar todos los recursos gráficos del juego (texturas).
 * Aplica el patrón Singleton → evidencia GM2.1.
 * Utiliza encapsulamiento (atributos privados + getters/setters) → evidencia GM1.6.
 */
public class Asset {

    // --- Instancia estática única (Singleton GM2.1) ---
    private static Asset instancia;

    // --- Atributos privados ---
    private Texture background;
    private Music backgroundMusic;
    private Texture ground;
    private Texture tuboTex;
    private Texture[] birdFrames;
    private Texture[] enemyFrames;
    private Sound birdHurt;
    private Sound birdFlap;

    /**
     * Constructor privado → evita creación de múltiples instancias.
     * Carga los recursos gráficos desde la carpeta "flappy/".
     */
    private Asset() {
        background = new Texture("flappy/background.png");
        setBackgroundMusic(Gdx.audio.newMusic(Gdx.files.internal("flappy_Sounds/gameplay_music.mp3")));

        
        ground = new Texture("flappy/ground.png");
        tuboTex = new Texture("flappy/pipe.png");

        birdFrames = new Texture[]{
            new Texture("flappy/bird0.png"),
            new Texture("flappy/bird1.png"),
            new Texture("flappy/bird2.png")
        };
        birdHurt = Gdx.audio.newSound(Gdx.files.internal("flappy_Sounds/hurt_music.mp3"));
        setBirdFlap(Gdx.audio.newSound(Gdx.files.internal("flappy_Sounds/bird_jump.mp3")));

        

        enemyFrames = new Texture[]{
            new Texture(Gdx.files.internal("flappy/enemy0.png")),
            new Texture(Gdx.files.internal("flappy/enemy1.png")),
            new Texture(Gdx.files.internal("flappy/enemy2.png"))
        };
    }

    /**
     * Devuelve la instancia única de la clase (patrón Singleton GM2.1)
     */
    public static Asset getInstancia() {
        if (instancia == null) {
            instancia = new Asset();
        }
        return instancia;
    }

    // --- Métodos de acceso (encapsulamiento GM1.6) ---
    public Texture getBackground() { return background; }
    public Texture getGround() { return ground; }
    public Texture getTuboTex() { return tuboTex; }
    public Texture[] getBirdFrames() { return birdFrames; }
    public Texture[] getEnemyFrames() { return enemyFrames; }

    /** Libera los recursos cargados al finalizar el juego */
    public void dispose() {
        background.dispose();
        ground.dispose();
        tuboTex.dispose();
        for (Texture t : birdFrames) t.dispose();
        for (Texture t : enemyFrames) t.dispose();
        birdHurt.dispose();
    }

	public Sound getBirdHurt() {
		return birdHurt;
	}

	public void setBirdHurt(Sound birdHurt) {
		this.birdHurt = birdHurt;
	}

	public Music getBackgroundMusic() {
		return backgroundMusic;
	}

	public void setBackgroundMusic(Music backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}

	public Sound getBirdFlap() {
		return birdFlap;
	}

	public void setBirdFlap(Sound birdFlap) {
		this.birdFlap = birdFlap;
	}
}
