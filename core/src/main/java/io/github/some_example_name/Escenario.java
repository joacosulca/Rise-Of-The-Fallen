
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.mapas.Piso;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.github.some_example_name.mapas.Piso.posX_Luchador;
import static io.github.some_example_name.mapas.Piso.posY_Luchador;
import static java.lang.Math.round;

@Getter
@Setter
/** {@link ApplicationListener} implementation shared by all platforms. */
public class
Escenario extends ApplicationAdapter {
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Luchador luchador;
    private OrthographicCamera camera;
    public static final Float ANCHO = 1400f;
    public static final Float ALTO = 900f;
    private int niveles = 3;
    private int nivelActual = 0;
    private BitmapFont font;
    private BitmapFont fontTitulo;
    private List<Habitacion> habitaciones;
    private Texture spriteRect;
    private Piso piso;
    private int countReset = 60, countResetAux = countReset;
    private boolean ganar = false;
    private Sprite fondo;
    private TextureAtlas fondos;
    private Menu menu;
    private int AuxGanar = 0;
    //private Music musicaFondo = Gdx.audio.newMusic(FileHandle.tempFile("assets/audio/cancionVideojuego.mp3"));

    @Override
    public void create() {
        menu = new Menu(this);
        batch = new SpriteBatch();
        atlas = new TextureAtlas("caballero.atlas");

        fondos = new TextureAtlas("escenarios.atlas");
        fondo = fondos.createSprite("infierno");
        fondo.setBounds(0, 0, ANCHO, ALTO);

        font = new BitmapFont(); // Usa la fuente predeterminada
        font.setColor(Color.WHITE);
        fontTitulo = new BitmapFont(); // Usa la fuente predeterminada
        font.setColor(Color.WHITE);
        fontTitulo.getData().setScale(6f);
        fontTitulo.setColor(Color.OLIVE);
        //spriteRect = new Texture("assets/white.png");
        luchador = new Luchador(ANCHO/2-Luchador.ANCHO/2, ALTO/2-Luchador.ALTO/2, null);
        piso = new Piso(nivelActual, luchador,this);
        cargarNivel();
        luchador.setPiso(piso);
        habitaciones = piso.getHabitaciones();
    }

    public void darOrden(){
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        nivelActual = 0;
        luchador = new Luchador(ANCHO/2-Luchador.ANCHO/2, ALTO/2-Luchador.ALTO/2, null);
        piso = new Piso(nivelActual, luchador,this);
        cargarNivel();
        luchador.setPiso(piso);
        habitaciones = piso.getHabitaciones();
        luchador.setNombre(menu.getNombreLuchador());
        agregarLineaARanking("Jugador: " + luchador.getNombre() + " Tiempo: " + luchador.getElapsedTime());
        //musicaFondo.play();
    }

    @Override
    public void render() {
        // Limpiar la pantalla antes de dibujar
        if(!menu.getRanking()) {
            ScreenUtils.clear(0.7f, 0.2f, 0.2f, 0.5f);
        } else {
            ScreenUtils.clear(0.1f, 0.1f, 0f, 0.5f);
        }
        batch.begin();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !menu.isActivo()){
            menu.setSeguro(true);
        } else if(menu.isSeguro()){
            if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){ // Changed to isKeyPressed
                menu.setSeguro(false);
                menu.setActivo(true);
//                musicaFondo.dispose();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { // Changed to isKeyPressed
                menu.setActivo(false);
                menu.setSeguro(false);
            }
        }

        if(!menu.isActivo() && !menu.isSeguro()) {
            batch.draw(fondo, 0, 0, ANCHO, ALTO);

            //System.out.println("seguro: " + menu.isSeguro() + " activo: " + menu.isActivo());

            piso.paint(batch);
            if (!ganar) {
                luchador.paint(batch);
                int AuxGanar = 0;
            } else {
                //fontTitulo.draw(batch, "¡Felicidades, te has ", ANCHO / 6, ALTO / 2 + ALTO / 6);
                //fontTitulo.draw(batch, " convertido en dios!", ANCHO / 6, ALTO / 2);
                if(AuxGanar==0){
                    agregarLineaARanking("Jugador: " + luchador.getNombre() + " Tiempo: " + luchador.getElapsedTime());
                    AuxGanar = 1;
                }
            }

            //drawRectangle(batch,luchador.getBoundsGrande(), spriteRect);

            font.draw(batch, " Nivel: " + nivelActual, 20, 20);
            font.draw(batch, " Vidas: " + luchador.getVidas(), 80, ALTO - 20);

            //System.out.println("posx: " + posX_Luchador + " posy: " + posY_Luchador);

            piso.paintMapa(batch);

            font.draw(batch, "Tiempo: " + redondearFloat(luchador.getElapsedTime(), 1), ANCHO / 2 + ANCHO / 3, ALTO - ALTO / 3);

            if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
                int aux=0;
                font.draw(batch, "Nombre: " + luchador.getNombre(), ANCHO / 2 + ANCHO / 3, ALTO - ALTO / 3 - ALTO / 40);
                font.draw(batch, "Vida Máxima: " + luchador.getVidasMax(), ANCHO / 2 + ANCHO / 3, ALTO - ALTO / 3 - ALTO / 40*2);
                font.draw(batch, "Daño: " + luchador.getEspada().getDanio(), ANCHO / 2 + ANCHO / 3, ALTO - ALTO / 3 - ALTO / 40 * 3);
                font.draw(batch, "Cadencia: " + luchador.getEspada().getCadencia(), ANCHO / 2 + ANCHO / 3, ALTO - ALTO / 3 - ALTO / 40 * 4);
                font.draw(batch, "Daño: " + luchador.getEspada().getDanio(), ANCHO / 2 + ANCHO / 3, ALTO - ALTO / 3 - ALTO / 40 * 5);
                font.draw(batch, "Velocidad: " + luchador.getVelocidad(), ANCHO / 2 + ANCHO / 3, ALTO - ALTO / 3 - ALTO / 40 * 6);

                batch.setColor(new Color(1, 1, 1, 0.5f));
                for(ObjetoPasivo obj : getLuchador().getObjetos()){
                    aux++;
                    batch.draw(obj.getSprite(), 50f, ALTO - ALTO / 3 - ALTO / 40 * aux, obj.getANCHO(), obj.getALTO());
                }
                batch.setColor(Color.WHITE);
            }
            // isJuegoPasado(batch);

        }
        menu.paint(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void pasarNivel() {
        cargarNivel();
        piso = new Piso(nivelActual, luchador,this);
        luchador.setPiso(piso);
        habitaciones = piso.getHabitaciones();
    }

    public void agregarLineaARanking(String nuevaLinea) {
        String rutaArchivo = "assets/Ranking.txt";
        try {
            FileWriter writer = new FileWriter(rutaArchivo, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(nuevaLinea);
            bufferedWriter.newLine();

            bufferedWriter.close();
            writer.close();
            //System.out.println("Línea agregada correctamente: " + nuevaLinea);
        } catch (IOException e) {
            //System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
/*
    public void isJuegoPasado(SpriteBatch batch){
        if(!luchador.getVivo()){
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Lobster.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 50; // font size
            BitmapFont lobster = generator.generateFont(parameter);
            generator.dispose();
            lobster.setColor(com.badlogic.gdx.graphics.Color.RED);
            lobster.draw(batch, "Perdiste", ANCHO/2-lobster.getRegion().getRegionWidth()/2,350);
        }
    }*/

    public void cargarNivel() {
        nivelActual++;
        if(nivelActual>3){
            nivelActual--;
            ganar = true;
            luchador.setVivo(false);
        }
        switch (nivelActual){
            case 2:
                fondo = fondos.createSprite("tierra");
                break;
            case 3:
                fondo = fondos.createSprite("cielo");
                break;
        }
    }

    public static void drawRectangle(SpriteBatch batch, Rectangle rect, Texture sprite) {
        batch.draw(sprite, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public static float redondearFloat(float numero, int decimales) {
        float factor = (float) Math.pow(10, decimales);
        return Math.round(numero * factor) / factor;
    }



}

