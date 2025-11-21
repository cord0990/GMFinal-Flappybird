package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Clase MainMenuScreen
 *
 * Representa la pantalla inicial del juego, donde se muestran instrucciones
 * básicas y se permite iniciar una nueva partida.
 *
 * Hereda de BaseUIScreen, aprovechando la estructura común definida mediante
 * el Template Method (GM2.2) para el ciclo "cargar → actualizar → dibujar".
 * Esto asegura uniformidad visual y de comportamiento entre todas las pantallas UI.
 */
public class MainMenuScreen extends BaseUIScreen {

    // --- Recursos y atributos específicos del menú (encapsulamiento GM1.6) ---
    private final FlappyGameMenu game;
    private Texture bg, pipeTex, birdTex;


    // Textos y parámetros visuales del UI
    private String titleText, instruction1, instruction2, instruction3;
    private float titleScale, titleY;
    private float instruction1Y, instruction2Y, instruction3Y;

    /**
     * Constructor de la pantalla de menú.
     * @param game referencia a la instancia principal del juego.
     */
    public MainMenuScreen(FlappyGameMenu game) {
    	super(); // define dimensiones base desde BaseUIScreen (Template Method GM2.2)
        this.game = game;
    }

    /**
     * Carga los recursos gráficos exclusivos del menú de inicio.
     * Este metodo se llama automáticamente como parte del Template Method.
     */
    @Override
    protected void loadResources() {
        bg = game.getAssets().getStartScreen();
        pipeTex = game.getAssets().getPipeMain();
        birdTex = game.getAssets().getBirdFrames()[1];
    }

    /**
     * Maneja la entrada del usuario:
     *  - Presionar SPACE o hacer clic inicia una nueva partida.
     */
    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game)); // nueva partida limpia
        }
    }

    /**
     * Dibuja todos los elementos visibles del menú.
     * Este metodo forma parte de render() en el Template Method (GM2.2).
     */
    @Override
    protected void renderContent(float dt) {

        // Fondo principal
        if (bg != null) {
            batch.setColor(1, 1, 1, 1);
            batch.draw(bg, 0, 0, worldWidth + 10, worldHeight);
        }

        // Título principal
        ui.drawCenteredScaled(
            batch,
            titleText,
            worldWidth / 2f,
            titleY,
            titleScale
        );

        // Elemento decorativo (pájaro)
        if (birdTex != null) {
            batch.setColor(1, 1, 1, 1f);
            batch.draw(birdTex, 68f, worldHeight / 2f + 135f, 120f, 40f);
        }

        // Mini tutorial visual (pájaro saltando + obstáculo)
        batch.setColor(1, 1, 1, 1f);

        if (birdTex != null) {
            batch.draw(birdTex, 120f, worldHeight / 2f - 90f, 100f, 34f);
        }

        if (pipeTex != null) {
        	batch.draw(pipeTex, worldWidth - 230f, worldHeight / 2f - 100 ,
        	           pipeTex.getWidth(), pipeTex.getHeight());
        }

        // Reset color por seguridad (siempre recomendable en LibGDX)
        batch.setColor(Color.WHITE);

        // Textos explicativos
        ui.drawCenteredScaled( batch,instruction1,180f,instruction1Y,1.4f);
        ui.drawCenteredScaled(batch,instruction2,worldWidth - 195f,instruction2Y,1.4f);
        ui.drawCenteredScaled(batch,instruction3,worldWidth / 2f + 10,instruction3Y,1.4f);
    }


    //Libera recursos específicos, no es necesario.
    @Override
    protected void unloadResources() {
    }

    /**
     * Configura el contenido textual del menú y sus posiciones.
     * Ejecutado una vez al cargar la pantalla.
     */
    @Override
    protected void setupUI() {

        // Título principal del menú
        titleText = "¡Como Jugar!";
        titleScale = 2.3f;
        titleY = worldHeight - 255f;

        // Instrucciones del usuario
        instruction1 = "Presiona espacio para saltar";
        instruction2 = "Esquiva los obstaculos para sobrevivir";
        instruction3 = "Presiona ESPACIO o CLICK para empezar";

        // Posiciones relativas para mantener coherencia visual
        instruction1Y = worldHeight / 2f - 110f;
        instruction2Y = worldHeight / 2f - 111f;
        instruction3Y = 60f;
    }

}
