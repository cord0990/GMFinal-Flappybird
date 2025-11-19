package puppy.code.Screens.UIBase;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import puppy.code.Screens.UIRenderer;

/**
 * BaseUIScreen
 * Clase abstracta que define el flujo común de las pantallas de UI.
 * Aplica el patrón Template Method. (GM 2.2)
 *
 * Hooks protegidos que las subclases deben/pueden implementar:
 *   - loadResources(): cargar texturas/fuentes/recursos específicos
 *   - setupUI(): inicializar valores relacionados con layout/UI
 *   - update(delta): lógica de actualización no relacionada con render batched
 *   - renderContent(delta): dibujado concreto (dentro de batch.begin()/end())
 *   - unloadResources(): liberar recursos propios
 *
 * Provee campos protegidos útiles para subclases: batch, camera, ui, worldWidth/worldHeight.
 */
public abstract class BaseUIScreen implements Screen {

    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected UIRenderer ui;
    protected float worldWidth = 800f;   // valores por defecto - Aplica en todas las pantallas
    protected float worldHeight = 480f;

    // ---------- Metodos ----------

    /** Constructor vacío. Subclases pueden añadir constructores con parámetros. */
    public BaseUIScreen() {
    }

    /** show: hook inicial donde preparamos recursos comunes y llamamos a hooks concretos */
    @Override
    public void show() {
        // Inicializa batch y cámara si no están
        if (batch == null) batch = new SpriteBatch();
        if (camera == null) {
            camera = new OrthographicCamera();
            camera.setToOrtho(false, worldWidth, worldHeight);
        }
        if (ui == null) ui = new UIRenderer();

        // Hooks para la subclase
        loadResources(); // cargar texturas/fuentes propias
        setupUI();       // preparar layout, valores, etc.
    }

    /**
     * render — Aplicacion método template:
     * 1) limpiar pantalla
     * 2) actualizar cámara
     * 3) delegar update lógico
     * 4) comenzar batch -> delegar renderContent -> terminar batch (Depende de la pantalla)
     */
    @Override
    public final void render(float delta) {
        // 1) limpiar pantalla
        clearScreen(); // negro por defecto

        // 2) actualizar cámara
        updateCamera();

        // 3) lógica de actualización (input, animaciones, etc.)
        update(delta);

        // 4) dibujado en batch
        batch.begin();
        renderContent(delta);
        batch.end();
    }

    //Solo por si se necesita en otro momento: si la ventana cambia tamaño, actualiza cámara y guarda dimensiones. 
    @Override
    public void resize(int width, int height) {
        // por simplicidad mantenemos worldWidth/worldHeight fijos, pero actualizamos la cámara.
        camera.setToOrtho(false, worldWidth, worldHeight);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
    
    private void clearScreen() {
    	ScreenUtils.clear(0f, 0f, 0f, 1f);
    }

    //Posiciona la camara
    private void updateCamera() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }
 

    //libera recursos comunes y llama al hook para recursos propios 
    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (ui != null) {
            ui.dispose();
            ui = null;
        }
        unloadResources(); // hook para la subclase
    }

    // ---------- Hooks que las subclases deben/pueden implementar ----------

    //Cargar recursos específicos (texturas, sonidos, etc.). Llamado desde show(). */
    protected abstract void loadResources();

    //Preparar UI, posiciones, variables iniciales. Llamado desde show(). */
    protected abstract void setupUI();

    //Actualización por frame (inputs, contadores). Llamado antes de dibujar. */
    protected abstract void update(float delta);

    //Dibujar el contenido particular de la pantalla,Todas las operaciones de dibujo con SpriteBatch deben realizarse aquí.
    protected abstract void renderContent(float delta);

    //Liberar recursos específicos de la pantalla (texturas, etc.). 
    protected void unloadResources() {};
}

