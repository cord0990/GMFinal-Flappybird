package puppy.code.Screens.UIBase;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import puppy.code.Screens.UIRenderer;

/**
 * BaseUIScreen
 * Clase abstracta que define el flujo común de las pantallas de UI.
 * Aplica el patrón Template Method (GM2.2).
 */
public abstract class BaseUIScreen implements Screen {

    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected UIRenderer ui;

    protected final float worldWidth;
    protected final float worldHeight;

    // Constructor usado por las subclases (EndScreen, PauseScreen, etc.)
    protected BaseUIScreen(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    /** Método plantilla: inicialización común */
    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);
        ui = new UIRenderer();
        loadResources();          // paso específico de cada subclase
    }

    /** Método plantilla: ciclo de renderizado común */
    @Override
    public final void render(float delta) {
        update(delta);            // maneja input / lógica específica
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawContent();            // dibujo específico de cada subclase
        batch.end();
    }

    /** Lógica por defecto: solo delega en handleInput() */
    protected void update(float delta) {
        handleInput();
    }

    // Métodos que cada pantalla concreta debe implementar
    protected abstract void loadResources();
    protected abstract void drawContent();
    protected abstract void handleInput();

    /** Permite a cada subclase liberar sus recursos propios */
    protected void unloadResources() {}

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
    }

    /** Libera recursos comunes + propios */
    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (ui != null) ui.dispose();
        unloadResources();
    }
}
