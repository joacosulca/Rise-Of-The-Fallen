package io.github.some_example_name.mapas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.*;
import io.github.some_example_name.habMapa.ETipoHab;
import io.github.some_example_name.habMapa.HabMapa;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Piso {
    private Random rand;
    private Mapa mapa;
    private char[][] distribucionNiveles;
    private Integer numHabs;
    private final int dimX = 11;
    private final int dimY = 7;
    private List<Habitacion> habitaciones;
    private List<HabMapa> habsMapa;
    private int nivelActual;
    private static Luchador luchador;
    public static int posY_Luchador;
    public static int posX_Luchador;
    private int countPassHab=100, countPassHabAux=countPassHab;
    private Escenario escenario;

    public Piso(int nivelActual, Luchador luchador, Escenario escenario) {
        this.luchador = luchador;
        this.nivelActual = nivelActual;
        this.escenario = escenario;

        posY_Luchador = 3;
        posX_Luchador = 5;

        mapa = new Mapa();
        rand = new Random();
        habsMapa = new ArrayList<>();
        habitaciones = new ArrayList<>();
        inicializadDN();
        numHabs = rand.nextInt(4) + 15; // 15
        do{
            int numPasHab = rand.nextInt(5) + 2;
            for(int y=0; y<dimY; y++){
                for(int x=0; x<dimX; x++){
                    int dirRand = rand.nextInt(4) + 1;
                    numPasHab--;
                    if(distribucionNiveles[y][x] == 'x' || distribucionNiveles[y][x] == 'h'){
                        if(numPasHab<0) {
                            if (x > 0 && dirRand == 1) {
                                if(distribucionNiveles[y][x - 1] != 'x') {
                                    distribucionNiveles[y][x - 1] = 'h';
                                }
                            } else if (x < dimX - 1 && dirRand == 2) {
                                if(distribucionNiveles[y][x + 1] != 'x') {
                                    distribucionNiveles[y][x + 1] = 'h';
                                }
                            } else if (y > 0 && dirRand == 3) {
                                if(distribucionNiveles[y - 1][x] != 'x') {
                                    distribucionNiveles[y - 1][x] = 'h';
                                }
                            } else if (y < dimY - 1 && dirRand == 4) {
                                if(distribucionNiveles[y + 1][x] != 'x') {
                                    distribucionNiveles[y + 1][x] = 'h';
                                }
                            }
                            numPasHab=rand.nextInt(5) + 2;
                            numHabs--;
                        }
                    }
                }
            }
        }while(numHabs > 0);

        crearHabitaciones();
        crearMapa();


        buscarHab().getEnemigos().clear();
        buscarHab().getBloques().clear();

        crearAdyacentes();
        crearSalaReyYBoss();

        //for(Habitacion hab : habitaciones){
        //   System.out.println("hab: x: " + hab.getPosX() + " hab: y: " + hab.getPosY());
        //}
    }


    public void crearHabitaciones(){
        for (int y = 0; y < getDimY(); y++) {
            for (int x = 0; x < getDimX(); x++) {
                if(distribucionNiveles[y][x]=='x' || distribucionNiveles[y][x]=='h') {
                    List<Puerta> puertas = new ArrayList<>();
                    habitaciones.add(new Habitacion(this, new Mapa(), luchador, nivelActual, y, x, puertas));
                }
            }
        }
        crearPuertas();
    }

    public void crearPuertas(){
        for (Habitacion hab : habitaciones) {
            List<Puerta> puertas = new ArrayList<>();
            int x = hab.getPosX();
            int y = hab.getPosY();

            if(y > 0 && (distribucionNiveles[y - 1][x] == 'x' || distribucionNiveles[y - 1][x] == 'h')){
                puertas.add(new Puerta(luchador, EPosPuerta.ARRIBA, hab, nivelActual));
            }
            if(y < getDimY() - 1 && (distribucionNiveles[y + 1][x] == 'x' || distribucionNiveles[y + 1][x] == 'h')){
                puertas.add(new Puerta(luchador, EPosPuerta.ABAJO, hab, nivelActual));
            }
            if(x > 0 && (distribucionNiveles[y][x - 1] == 'x' || distribucionNiveles[y][x - 1] == 'h')){
                puertas.add(new Puerta(luchador, EPosPuerta.IZQUIERDA, hab, nivelActual));
            }
            if(x < getDimX() - 1 && (distribucionNiveles[y][x + 1] == 'x' || distribucionNiveles[y][x + 1] == 'h')){
                puertas.add(new Puerta(luchador, EPosPuerta.DERECHA, hab, nivelActual));
            }
            hab.setPuertas(puertas);
        }
    }

    public void paint(SpriteBatch batch){
        buscarHab().paint(batch);
        if(countPassHabAux>=0){
            countPassHabAux--;
        }
        //System.out.println("c: " + countPassHabAux);
    }

    boolean aux = true;

    public Habitacion buscarHab(){
        for(Habitacion hab : habitaciones){
            if(hab.getPosX() == posX_Luchador && hab.getPosY()== posY_Luchador){
                if(pasarPiso() && aux){ // Verificar si es la última habitación
                    hab.getPuertas().add(new Puerta(luchador, hab, escenario));
                    aux=false;
                }
                return hab;
            }
        }
        return null;
    }

    public boolean pasarPiso() {
        boolean pasado = true;
        for (Habitacion hab : habitaciones) {
            if(!hab.nivelPasado()){
                pasado = false;
            }
            //System.out.println("hab: " + hab.nivelPasado());
        }
        //System.out.println("pasado: " + pasado);
        return pasado;
    }

    public void paintMapa(SpriteBatch batch){
        cambiarVisitada();
        for(HabMapa hab : habsMapa){
            if(Gdx.input.isKeyPressed(Input.Keys.TAB)){
                batch.setColor(new Color(1, 1, 1, 0.8f)); // Color blanco con 50% de opacidad}
                hab.paint(batch);
            } else {
                if(hab.getTipoHab()==ETipoHab.VISITADA || hab.getTipoHab()==ETipoHab.NO_VISITADA || hab.getTipoHab()==ETipoHab.ACTUAL){
                    batch.setColor(new Color(1, 1, 1, 0.6f)); // Color blanco con 50% de opacidad}
                    hab.paint(batch);
                }
            }
        }
        batch.setColor(new Color(1, 1, 1, 1f)); // Color blanco con 50% de opacidad}
    }

    public void cambiarVisitada(){
        for(HabMapa hab : habsMapa){
            if (hab.getPosX() == posX_Luchador && hab.getPosY()==posY_Luchador) {
                hab.setTipoHab(ETipoHab.ACTUAL);
            }
        }
        for(HabMapa hab : habsMapa){
            if ((hab.getPosX() != posX_Luchador || hab.getPosY() != posY_Luchador) && hab.getTipoHab()==ETipoHab.ACTUAL) {
                hab.setTipoHab(ETipoHab.VISITADA);
            }
        }
    }

    public void crearMapa(){
        Float inicioX = getDimX() * Bloque.ANCHO + Bloque.ANCHO * 11;
        Float inicioY = getDimY() * Bloque.ALTO * 2;

        for(int y=0; y<getDimY(); y++){
            for(int x=0; x<getDimX(); x++){
                ETipoHab tipoHab;
                if(distribucionNiveles[y][x] == 'x'){
                    tipoHab = ETipoHab.ACTUAL;
                } else if(distribucionNiveles[y][x] == 'h'){
                    tipoHab = ETipoHab.NO_VISITADA;
                } else {
                    tipoHab = ETipoHab.BLOQUEADA;
                }
                int yInvertida = (getDimY() - 1 - y); // Invertir la coordenada y  // odio esto de libgdx...
                habsMapa.add(new HabMapa(20*x + inicioX, 20*yInvertida + inicioY, tipoHab, x, y));
            }
        }

        /*
        StringBuilder sb = new StringBuilder();
        if(!Gdx.input.isKeyPressed(Input.Keys.TAB)){
            for (int i = 0; i < getDimY(); i++) {
                for (int j = 0; j < getDimX(); j++) {
                    if(i == posY_Luchador && j == posX_Luchador){
                        sb.append("a").append("  ");
                    } else if(getDistribucionNiveles()[i][j]=='x' || getDistribucionNiveles()[i][j]=='h'){
                        sb.append(getDistribucionNiveles()[i][j]).append("  ");
                    } else {
                        sb.append("  ");
                    }
                }
                sb.append("\n");
            }
        } else {
            for (int i = 0; i < getDimY(); i++) {
                for (int j = 0; j < getDimX(); j++) {
                    if(i == posY_Luchador && j == posX_Luchador){
                        sb.append("a").append("  ");
          } else {
                        sb.append(getDistribucionNiveles()[i][j]).append("  ");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();

        */
    }

    public void crearAdyacentes(){
        for(Habitacion hab : habitaciones){
            for(Habitacion hab_ady : habitaciones){

                if(hab.getPosX() == hab_ady.getPosX()+1 && hab.getPosY() == hab_ady.getPosY()){
                    hab.getHabAdys().add(hab_ady);
                }

                if(hab.getPosX() == hab_ady.getPosX()-1 && hab.getPosY() == hab_ady.getPosY()){
                    hab.getHabAdys().add(hab_ady);
                }

                if(hab.getPosY() == hab_ady.getPosY()+1 && hab.getPosX() == hab_ady.getPosX()){
                    hab.getHabAdys().add(hab_ady);
                }

                if(hab.getPosY() == hab_ady.getPosY()-1 && hab.getPosX() == hab_ady.getPosX()){
                    hab.getHabAdys().add(hab_ady);
                }

            }
        }
    }

    public void crearSalaReyYBoss() {
        int numHabsRey = 2;
        int numR = rand.nextInt(10) + 1;
        do {
            for(Habitacion hab : habitaciones) {
                if(hab.getHabAdys().size()<=2 && numR>0 && hab.getPosX() != 5 && hab.getPosY()!=3){
                    numR--;
                } else if(numR<=0) {
                    hab.setTipoHabitacion(ETipoHabitacion.PREMIO);
                    hab.getPedestal().spawn();
                    numR = rand.nextInt(10) + 1;
                    numHabsRey--;
                }
            }
        }while(numHabsRey >= 0);
    }


                //for (Habitacion hab : habitaciones) {
        //    System.out.println(hab.getHabAdys().size() + " " + hab.getTipoHabitacion());
        //}


    public void inicializadDN() {
        distribucionNiveles = new char[][]{
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', 'x', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}
        };
    }

    public static void luchadorArriba(){
        luchador.setY(Habitacion.BORDE_PARED/2 + Habitacion.BORDE_PARED/5 + luchador.getALTO());
        luchador.setX((Mapa.DIM_X * Bloque.ANCHO) + Bloque.ANCHO * 1.7f);
        posY_Luchador--;
    }

    public static void luchadorAbajo(){
        luchador.setY((Mapa.DIM_X * Habitacion.CASILLA) + Bloque.ALTO*4 + Habitacion.BORDE_PARED - luchador.getALTO()*1.5f);
        luchador.setX((Mapa.DIM_X * Bloque.ANCHO) + Bloque.ANCHO * 1.7f);
        posY_Luchador++;
    }

    public static void luchadorDerecha(){
        luchador.setX(0F + Habitacion.BORDE_PARED + luchador.getANCHO());
        luchador.setY((Mapa.DIM_X * Habitacion.CASILLA) / 2 + Habitacion.CASILLA*2.5F);
        posX_Luchador++;
    }

    public static void luchadorIzquierda(){
        luchador.setX((Mapa.DIM_X * Habitacion.CASILLA) * 2.125f - luchador.getANCHO());
        luchador.setY((Mapa.DIM_X * Habitacion.CASILLA) / 2 + Habitacion.CASILLA*2.5F);
        posX_Luchador--;
    }

    public Boolean getCouldown(){
        if(countPassHabAux<=0){
            return true;
        }
        return false;
    }

    public void resetCouldown(){
        countPassHabAux = countPassHab;
    }

    public void miniResetCouldown(){
        countPassHabAux = countPassHab/4;
    }


}

    /*
    for (int i = 0; i < createPiso.getDimY(); i++) {  // Bucle para las filas
            for (int j = 0; j < createPiso.getDimX(); j++) {  // Bucle para las columnas
                System.out.print(createPiso.getDistribucionNiveles()[i][j] + " ");  // Acceder al elemento en la fila i y columna j
            }
            System.out.println();  // Salto de línea después de cada fila
        }
     */

