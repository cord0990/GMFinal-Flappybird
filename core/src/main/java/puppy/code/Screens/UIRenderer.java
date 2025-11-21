package puppy.code.Screens;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

/**
 * Clase UIRenderer
 * Clase auxiliar responsable de dibujar elementos de interfaz reutilizables:
 * paneles, textos y títulos para todas las pantallas del juego.
 *
 * Promueve la coherencia visual y reduce duplicación de código, fortaleciendo
 * la modularidad (GM1.6). Además, funciona como un componente de apoyo dentro
 * de la estructura común de pantallas basada en Template Method (GM2.2),
 * ya que todas las pantallas utilizan sus métodos de dibujo estándar.
 */
public class UIRenderer {

    // --- Atributos privados ---
    private final Texture white1x1; // Textura mínima 1x1 utilizada para generar paneles rectangulares de cualquier tamaño
    private final BitmapFont font;  // Fuente tipográfica utilizada por todas las pantallas del juego

    public UIRenderer() {
        // Genera una textura blanca 1x1 para construir paneles con transparencia
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        white1x1 = new Texture(pm);
        pm.dispose();

        font = new BitmapFont();
        // Asegura una visualización más suave del texto
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    // --- Métodos reutilizables de dibujo (interfaz común para las distintas pantallas) ---

    /** Dibuja un panel rectangular semitransparente (útil para menús y pantallas de fin de juego). */
    public void drawPanel(SpriteBatch batch, float x, float y, float w, float h, float alpha) {
        Color prev = batch.getColor();
        batch.setColor(0f, 0f, 0f, alpha);
        batch.draw(white1x1, x, y, w, h);
        batch.setColor(prev);
    }

    /** Dibuja texto centrado horizontal y verticalmente respecto al punto dado. */
    public void drawCentered(SpriteBatch batch, String text, float cx, float cy) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, layout, cx - layout.width / 2f, cy + layout.height / 2f);
    }

    /** Versión escalada del metodo anterior para títulos o textos destacados. */
    public void drawCenteredScaled(SpriteBatch batch, String text, float cx, float cy, float scale) {
        float prevScale = font.getData().scaleX;
        font.getData().setScale(scale);
        drawCentered(batch, text, cx, cy);
        font.getData().setScale(prevScale);
    }

    /** Dibuja texto sin centrado, usado para mensajes informativos o etiquetas fijas. */
    public void draw(SpriteBatch batch, String text, float x, float y) {
        font.draw(batch, text, x, y);
    }

    /** Dibuja texto con un factor de escalado personalizado. */
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

    /** Libera los recursos gráficos asociados a la interfaz. */
    public void dispose() {
        white1x1.dispose();
        font.dispose();
    }
}
