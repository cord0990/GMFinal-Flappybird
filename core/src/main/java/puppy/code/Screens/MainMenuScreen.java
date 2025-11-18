package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Pantalla inicial del juego: muestra instrucciones y permite iniciar la partida.
 * Hereda de BaseUIScreen → aplica el patrón Template Method (GM2.2),
 * reutilizando el flujo común de pantallas de UI.
 */
public class MainMenuScreen extends BaseUIScreen {

    // --- Atributos específicos (GM1.6) ---
    private final FlappyGameMenu game;
    private Texture bg, pipeTex, birdTex;
    
    
    // --- Variables a determinar ---
    private String titleText, instruction1, instruction2, instruction3;
    private float titleScale, titleY;
    private float instruction1Y, instruction2Y, instruction3Y;

    //Constructor: recibe la referencia del juego principal.
    //Define dimensiones lógicas de la pantalla mediante BaseUIScreen.
     
    public MainMenuScreen(FlappyGameMenu game) {
    	super(); //dimensiones por defecto en BASEUISCREEN
        this.game = game;
    }

    //Carga de recursos gráficos específicos del menú principal 
    @Override
    protected void loadResources() {
        bg = game.getAssets().getStartScreen();
        pipeTex = game.getAssets().getTuboTex();
        birdTex = game.getAssets().getBirdFrames()[1];
    }

    //inicia una nueva partida 
    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game)); // nueva partida limpia
        }
    }

    //Muestra el contenido en pantalla
    @Override
    protected void renderContent(float dt) {

        // --- FONDO ---
        if (bg != null) {
            batch.setColor(1, 1, 1, 1);
            batch.draw(bg, 0, 0, worldWidth + 10, worldHeight);
        }

        // --- TÍTULO ---
        ui.drawCenteredScaled(
            batch,
            titleText,
            worldWidth / 2f,
            titleY,
            titleScale
        );

        // --- PÁJARO DECORATIVO ---
        if (birdTex != null) {
            batch.setColor(1, 1, 1, 1f);
            batch.draw(birdTex, 68f, worldHeight / 2f + 135f, 120f, 40f);
        }

        // --- TUTORIAL VISUAL ---
        batch.setColor(1, 1, 1, 1f);

        if (birdTex != null) {
            batch.draw(birdTex, 120f, worldHeight / 2f - 80f, 70f, 30f);
        }

        if (pipeTex != null) {
            batch.draw(pipeTex, worldWidth - 160f, worldHeight / 2f - 240f, 60f, 240f);
        }

        // Reset color por seguridad (siempre recomendable en LibGDX)
        batch.setColor(Color.WHITE);

        // Aplica las variables a partir de que se necesita
        ui.drawCenteredScaled( batch,instruction1,180f,instruction1Y,1.4f
        );

        ui.drawCenteredScaled(batch,instruction2,worldWidth - 195f,instruction2Y,1.4f
        );

        ui.drawCenteredScaled(batch,instruction3,worldWidth / 2f,instruction3Y,1.4f
        );
    }


    //Libera recursos específicos, no es necesario.
    @Override
    protected void unloadResources() {
    }

    @Override
    protected void setupUI() {
        // Variables lógicas para el texto del título
    	
        // Titulo y sus valores
        titleText = "¡Como Jugar!";
        titleScale = 2f;
        titleY = worldHeight - 250f;

        // Texto en UI
        instruction1 = "Presiona espacio para saltar";
        instruction2 = "Esquiva los tubos para sobrevivir";
        instruction3 = "Presiona ESPACIO o CLICK para empezar";

        // Posiciones “lógicas”
        instruction1Y = worldHeight / 2f - 110f;
        instruction2Y = worldHeight / 2f - 111f;
        instruction3Y = 60f;
    }

}
