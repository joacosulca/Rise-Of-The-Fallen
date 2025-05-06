package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.Enemigos.*;
import io.github.some_example_name.mapas.ETipoHabitacion;
import io.github.some_example_name.mapas.Mapa;
import io.github.some_example_name.mapas.Piso;
import lombok.Getter;
import lombok.Setter;

import java.beans.PersistenceDelegate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Habitacion {
    public static final Float TAMANIO_PARED = 150F;
    public static final Float BORDE_PARED = 50F;
    private List<Enemigo> enemigos;
    private List<Bloque> bloques;
    private List<Puerta> puertas;
    private Boolean pasado;
    private Mapa mapa;
    private char[][] estruMapa;
    public static final Float CASILLA = Bloque.ANCHO;
    private int DIM_X = Mapa.DIM_X;
    private int  DIM_Y = Mapa.DIM_Y;
    private int numMapa;
    private Luchador luchador;
    private int posX;
    private int posY;
    private int nivelActual;
    private Piso piso;
    private ETipoHabitacion tipoHabitacion;
    private List<Habitacion> habAdys;
    private Pedestal pedestal;

    public Habitacion(Piso piso, Mapa mapa, Luchador luchador, int nivelActual, int posY, int posX, List<Puerta> puertas) {
        this.piso = piso;
        this.mapa = mapa;
        this.luchador = luchador;
        this.nivelActual = nivelActual;
        this.posY=posY;
        this.posX=posX;
        this.puertas = puertas;
        tipoHabitacion = ETipoHabitacion.MAZMORRA;
        habAdys = new ArrayList<>();
        pedestal = new Pedestal();

        pasado = false;
        enemigos = new ArrayList<>();
        bloques = new ArrayList<>();

        ETipoBloque tipoBloque = null;

        if (piso.getNivelActual() == 1) {
            tipoBloque = ETipoBloque.INFIERNO;
        } else if (piso.getNivelActual() == 2) {
            tipoBloque = ETipoBloque.TIERRA;
        } else if (piso.getNivelActual() == 3) {
            tipoBloque = ETipoBloque.CIELO;
        } else {
            tipoBloque = ETipoBloque.INFIERNO;
        }


        for(int y=0;y<Mapa.DIM_X;y++){
            for(int x=0;x<Mapa.DIM_Y;x++){
                Float posicionX = x*CASILLA+TAMANIO_PARED;
                Float posicionY = (DIM_Y - 1 - y) * CASILLA - TAMANIO_PARED*2 - CASILLA;
                if(mapa.obtenerNivel()[y][x] == 'B'){
                    bloques.add(new Bloque(posicionX, posicionY, tipoBloque));
                } else if(mapa.obtenerNivel()[y][x] == 'E' && piso.getNivelActual() == 0){
                    enemigos.add(new Demonio(posicionX, posicionY, luchador, piso));

                } else if(mapa.obtenerNivel()[y][x] == 'E' && piso.getNivelActual() == 1){
                    enemigos.add(new Cura(posicionX, posicionY, luchador, piso));

                } else if(mapa.obtenerNivel()[y][x] == 'E' && piso.getNivelActual() >= 2){
                    enemigos.add(new Angel(posicionX, posicionY, luchador, piso));
                }

                else if(mapa.obtenerNivel()[y][x] == 'P' && piso.getNivelActual() == 0){
                    enemigos.add(new Leon(posicionX, posicionY, luchador, piso));
                } else if(mapa.obtenerNivel()[y][x] == 'P' && piso.getNivelActual() == 1){
                    enemigos.add(new Hipogrifo(posicionX, posicionY, luchador, piso));
                } else if(mapa.obtenerNivel()[y][x] == 'P' && piso.getNivelActual() >= 2){
                    enemigos.add(new Cura(posicionX, posicionY, luchador, piso));
                }


                else if(mapa.obtenerNivel()[y][x] == 'V' && piso.getNivelActual() == 0) {
                    enemigos.add(new DemonioVolador(posicionX, posicionY,luchador, piso));
                } else if(mapa.obtenerNivel()[y][x] == 'L' && piso.getNivelActual() == 1) {
                    enemigos.add(new Lobo(posicionX, posicionY,luchador, piso));
                } else if(mapa.obtenerNivel()[y][x] == 'V' && piso.getNivelActual() >= 2) {
                    enemigos.add(new Paloma(posicionX, posicionY,luchador, piso));
                } else if(mapa.obtenerNivel()[y][x] == 'L'){
                    enemigos.add(new Cura(posicionX, posicionY, luchador, piso));
                }


                else if(mapa.obtenerNivel()[y][x] == 'I'){
                    pedestal = new Pedestal(posicionX, posicionY, luchador);
                }
            }
        }
    }

    boolean aux = false;
    public boolean nivelPasado(){
        boolean pasado = true;
        for(Enemigo enemigo : enemigos){
            if(enemigo.getVivo()){
                pasado = false;
            }
        }
        if(pasado&&!aux){
            aux=true;
            piso.miniResetCouldown();
        }
        return pasado;
    }

    public void clear(){
        puertas.clear();
        bloques.clear();
        enemigos.clear();
    }

    public void paint(SpriteBatch batch){
        for(Bloque bloque : bloques){
            bloque.draw(batch);
        }
        for(Enemigo enemigo : enemigos){
            enemigo.paint(batch);
        }
        //int x = 0;
        for(Puerta puerta : puertas){
            //x++;
            puerta.paint(batch);
            //System.out.println("puerta: " + puerta.getPosPuerta());
        }
        if(pedestal != null){
            pedestal.paint(batch);
        }
        //System.out.println("x: " + x);
    }
}
