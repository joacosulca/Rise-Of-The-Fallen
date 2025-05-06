package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.mapas.Piso;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

@Getter
@Setter
public class Menu {
    private final float ANCHO = Escenario.ANCHO;
    private final float ALTO = Escenario.ALTO;
    private boolean activo;
    private boolean seguro;
    private Stage stage;
    private ImageButton botonInicio;
    private ImageButton botonRanking;
    private ImageButton botonCerrar;
    private TextureAtlas atlas;
    private Texture texturaSeguro;
    private Texture textureRanking;
    private Texture textureGanar;
    private Texture texturePerder;
    private Sprite spriteRanking;
    private Sprite menuSprite;
    private Escenario escenario;
    private String nombreLuchador;
    private Boolean ranking;
    private Boolean perder;
    private Boolean ganar;
    private BitmapFont font;
    private Music musicaDeFondo = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/musicaMenu.mp3"));

    public Menu(Escenario escenario){
        musicaDeFondo.setVolume(0.2F);
        musicaDeFondo.setLooping(true);
        this.escenario = escenario;
        activo = true;
        seguro = false;
        ranking = false;
        ganar = false;
        perder = false;
        atlas = new TextureAtlas("Menus/MenuBotones.atlas");

        textureGanar = new Texture("Menu/MenuGanar.png");
        texturePerder= new Texture("Menu/MenuPerder.png");

        texturaSeguro = new Texture("Menu/MenuSeguro.png");
        textureRanking = new Texture("Menu/MenuRanking.png");

        spriteRanking = new Sprite(textureRanking);
        spriteRanking.setBounds(ANCHO/2 - (textureRanking.getWidth()* 1.5f)/2, ALTO/2 - (textureRanking.getHeight()* 1.5f)/2, textureRanking.getWidth() * 1.5f, textureRanking.getHeight() * 1.5f);


        font = new BitmapFont(); // Usa la fuente predeterminada
        font.setColor(Color.WHITE);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Texture menu = new Texture("Menu.jpg");
        menuSprite = new Sprite(menu);
        menuSprite.setSize(ANCHO, ALTO);
        menuSprite.setColor(1, 1, 1, 0.6f);

        // Crea el botón de inicio
        botonInicio = new ImageButton(new TextureRegionDrawable(atlas.findRegion("JugarNormal")));
        botonInicio.setPosition(ANCHO / 6 - botonInicio.getWidth() / 2, ALTO / 3 - 10 - ALTO/20);

        botonRanking = new ImageButton(new TextureRegionDrawable(atlas.findRegion("RankingNormal")));
        botonRanking.setPosition(ANCHO / 6 - botonRanking.getWidth() / 2, ALTO / 3 - 20 - ALTO/40*7);

        botonCerrar = new ImageButton(new TextureRegionDrawable(atlas.findRegion("SalirNormal")));
        botonCerrar.setPosition(ANCHO / 6 - botonCerrar.getWidth() / 2, ALTO / 3 - 30 - ALTO/20*6);

        // Agrega listeners a los botones
        addHoverListener(botonInicio, "JugarNormal", "JugarHover");
        botonInicio.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(activo && !ranking) {
                    activo = false;
                    JOptionPane JOptionPane = null;
                    nombreLuchador = "";
                    nombreLuchador = JOptionPane.showInputDialog(null, "Por favor, introduce tu nombre:", "Introduce tu nombre", JOptionPane.QUESTION_MESSAGE);

                    if (nombreLuchador.isEmpty() || nombreLuchador==null) {
                        nombreLuchador = "Incognito";
                    }

                    escenario.darOrden();
                }
            }
        });

        addHoverListener(botonRanking, "RankingNormal", "RankingHover");
        botonRanking.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //mostrarRanking();
                ranking = true;
            }
        });

        addHoverListener(botonCerrar, "SalirNormal", "SalirHover");
        botonCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(activo && !ranking) {
                    Gdx.app.exit(); // Usar Gdx.app.exit() en lugar de System.exit()
                }
            }
        });

        stage.addActor(botonInicio);
        stage.addActor(botonRanking);
        stage.addActor(botonCerrar);
    }

    // Helper method to add hover listeners
    private void addHoverListener(ImageButton button, String normalRegion, String hoverRegion) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion(hoverRegion));
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.getStyle().imageUp = new TextureRegionDrawable(atlas.findRegion(normalRegion));
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });
    }

    public void paint(SpriteBatch batch) {

        if(escenario.isGanar() && !perder){
            ganar = true;
        } else if(ganar) {
            batch.draw(textureGanar, ANCHO / 2 - (textureGanar.getWidth() * 1.5f) / 2, ALTO / 2 - (textureGanar.getHeight() * 1.5f) / 2, textureGanar.getWidth() * 1.5f, textureGanar.getHeight() * 1.5f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                escenario.darOrden();
                ganar = false;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                escenario.darOrden();
                activo = true;
                ganar = false;
            }
        }

        if(escenario.getLuchador().getVivo()==false && !perder){
            perder = true;
        } else if(perder){
            batch.draw(texturePerder, ANCHO / 2 - (texturePerder.getWidth() * 1.5f) /2, ALTO / 2 - (texturePerder.getHeight() * 1.5f) /2, texturePerder.getWidth() * 1.5f, texturePerder.getHeight() * 1.5f);
            if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
                escenario.darOrden();
                perder=false;
            } else if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
                escenario.darOrden();
                activo=true;
                perder=false;
            }
        }




        if(!activo){
            musicaDeFondo.dispose();
        }

        if (activo && !ranking) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw(); // Dibuja todos los actores del stage (botones) DESPUÉS
        }

        if (seguro && !perder) {
            batch.draw(texturaSeguro, ANCHO / 2 - texturaSeguro.getWidth() / 2, ALTO / 2 - texturaSeguro.getHeight() / 2);
        } else if(ranking&&activo) {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            spriteRanking.draw(batch); // esta linea
            mostrarRanking(batch);
            if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
                ranking = false;
            }
        } else if (activo) {
            menuSprite.draw(batch);
            musicaDeFondo.play();
        }
    }

    private void mostrarRanking(SpriteBatch batch) {
        String rutaArchivo = "assets/Ranking.txt";
        List<String> mejoresTiempos = obtenerMejoresTiempos(rutaArchivo, 10);
        int aux = 0;

        for (String linea : mejoresTiempos) {
            aux++;
            font.draw( batch, linea, 500f, 600f-aux*30f);
            System.out.println(linea);
        }
    }

    public static List<String> obtenerMejoresTiempos(String rutaArchivo, int limite) {
        PriorityQueue<String> colaPrioridad = new PriorityQueue<>(Comparator.comparingDouble(Menu::extraerTiempo));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Leer línea por línea
            while ((linea = bufferedReader.readLine()) != null) {
                colaPrioridad.offer(linea);
                // Mantener la cola con un tamaño máximo del límite
                if (colaPrioridad.size() > limite) {
                    colaPrioridad.poll(); // Elimina el mayor (deja los menores)
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        // Convertir la cola a lista y ordenarla
        List<String> resultado = new ArrayList<>(colaPrioridad);
        resultado.sort(Comparator.comparingDouble(Menu::extraerTiempo));
        return resultado;
    }

    // Método auxiliar para extraer el tiempo de una línea
    private static double extraerTiempo(String linea) {
        try {
            // Suponiendo que la línea tiene el formato: "Jugador: Nombre Tiempo: XX.XX"
            String[] partes = linea.split("Tiempo: ");
            return Double.parseDouble(partes[1].trim());
        } catch (Exception e) {
            // Si la línea no tiene el formato esperado, se considera un tiempo muy alto
            return Double.MAX_VALUE;
        }
    }
}
