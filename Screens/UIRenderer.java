package puppy.code.Screens;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

/**
 * Clase UIRenderer
 * Clase auxiliar encargada de dibujar los elementos de interfaz (textos, paneles, títulos, etc.).
 *
 * Esta clase promueve la reutilización y la coherencia visual entre pantallas
 * → contribuye a la modularidad (GM1.6) y apoya la estructura común del Template Method (GM2.2),
 * ya que todas las pantallas heredan de BaseUIScreen y utilizan sus métodos de dibujo.
 */
public class UIRenderer {

    // --- Atributos privados ---
    private final Texture white1x1; // Textura base 1x1 blanca para paneles y fondos translúcidos
    private final BitmapFont font;  // Fuente común para toda la interfaz

    /** Constructor: genera una textura blanca y una fuente estándar */
    public UIRenderer() {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        white1x1 = new Texture(pm);
        pm.dispose();
        font = new BitmapFont(); // fuente por defecto de LibGDX
    }

    // --- Métodos de dibujo reutilizables ---

    /** Dibuja un panel rectangular translúcido de color negro (usado en GameOver, EndScreen, etc.) */
    public void drawPanel(SpriteBatch batch, float x, float y, float w, float h, float alpha) {
        Color prev = batch.getColor();
        batch.setColor(0f, 0f, 0f, alpha);
        batch.draw(white1x1, x, y, w, h);
        batch.setColor(prev);
    }

    /** Dibuja texto centrado en una posición (sin escala) */
    public void drawCentered(SpriteBatch batch, String text, float cx, float cy) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, layout, cx - layout.width / 2f, cy + layout.height / 2f);
    }

    /** Dibuja texto centrado con una escala específica (más grande o pequeño) */
    public void drawCenteredScaled(SpriteBatch batch, String text, float cx, float cy, float scale) {
        float prevScale = font.getData().scaleX;
        font.getData().setScale(scale);
        drawCentered(batch, text, cx, cy);
        font.getData().setScale(prevScale);
    }

    /** Dibuja texto simple en coordenadas (no centrado) */
    public void draw(SpriteBatch batch, String text, float x, float y) {
        font.draw(batch, text, x, y);
    }

    /** Dibuja texto con una escala personalizada */
    public void drawScaled(SpriteBatch batch, String text, float x, float y, float scale) {
        float prevScale = font.getData().scaleX;
        font.getData().setScale(scale);
        font.draw(batch, text, x, y);
        font.getData().setScale(prevScale);
    }

    /** Dibuja texto de interfaz (equivalente a drawScaled, pero con nombre semántico claro) */
    public void drawText(SpriteBatch batch, String text, float x, float y, float scale) {
        drawScaled(batch, text, x, y, scale);
    }

    /** Libera los recursos (textura y fuente) */
    public void dispose() {
        white1x1.dispose();
        font.dispose();
    }
}
