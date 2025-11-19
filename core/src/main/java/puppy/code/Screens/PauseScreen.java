package puppy.code.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.FlappyGameMenu;
import puppy.code.Screens.UIBase.BaseUIScreen;

/**
 * Pantalla de pausa del juego. Permite continuar o volver al menú principal.
 * Hereda de BaseUIScreen → aplica el patrón Template Method (GM2.2),
 * reutilizando el flujo común de pantallas de interfaz.
 */
public class PauseScreen extends BaseUIScreen {

    // --- Atributos específicos ---
    private final FlappyGameMenu game;
    private final Screen previousGame; // referencia a GameScreen para reanudar
    private Texture bg;
    
 // Campos de layout UI
    private float titleX, titleY;
    private float instructionsX, instructionsY;
    private float titleScale, instructionsScale;
    
    //Constructor: recibe el juego principal y la pantalla anterior.
     
    public PauseScreen(FlappyGameMenu game, Screen previousGame) {
        super(); // dimensiones lógicas de esta pantalla
        this.game = game;
        this.previousGame = previousGame;
    }

    //Carga recursos específicos de la pantalla de pausa 
    @Override
    protected void loadResources() {
        bg = game.getAssets().getGameOverScreen();
    }

    /**
     * Manejo de entrada:
     *  - ESPACIO o CLICK: volver al juego anterior.
     *  - ESC: ir al menú principal.
     */
    @Override
    protected void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.setScreen(previousGame); // reanuda la partida
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game)); // vuelve al menú
        }
    }

    //Dibuja el contenido 
    @Override
    protected void renderContent(float dt) {
        
        // Fondo
        if (bg != null) {
            batch.setColor(1, 1, 1, 1);
            batch.draw(bg, 0, 0, worldWidth, worldHeight);
        }

        // Título
        ui.drawCenteredScaled(batch, "Pausa",
            titleX, titleY, titleScale);

        // Texto de instrucciones
        ui.drawCenteredScaled(batch,
            "ESPACIO: continuar   |   ESC: menu",
            instructionsX, instructionsY, instructionsScale);
    }


    // Libera recursos específicos
    @Override
    protected void unloadResources() {
        if (bg != null) bg.dispose();
    }

	@Override
	protected void setupUI() {

	    // --- Genera las medidas ---
	    titleX = worldWidth / 2f - 5;
	    titleY = worldHeight / 2f + 10f;

	    instructionsX = worldWidth / 2f;
	    instructionsY = worldHeight / 2f - 40f;

	    // --- Escalas ---
	    titleScale = 2.5f;
	    instructionsScale = 1.2f;
	}

}
