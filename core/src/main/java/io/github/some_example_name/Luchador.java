
package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.mapas.Piso;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

@Getter
@Setter
public class Luchador extends Individuo{
    Sprite sprite = getSprite();
    private int ultimoLado = 0;

    private TextureAtlas atlas;
    private Array<TextureRegion> framesX;  // Array de frames o regiones del atlas
    private Array<TextureRegion> frameArriba;  // Array de frames o regiones del atlas
    private Array<TextureRegion> frameAbajo;  // Array de frames o regiones del atlas
    private List<ObjetoPasivo> objetos;
    private int frameIndex;  // Índice del frame actual
    private TextureRegion frameActual;  // El frame actual
    private Boolean choquePuerta;
    private Boolean daniado;
    private Float vidas;
    private Float vidasMax;
    private Boolean vivo;
    private Espada espada;
    private Piso piso;
    boolean arr=true, aba=true, izq=true, der=true;
    static Float ANCHO= 60F;
    static Float ALTO= 100F;
    private Texture spriteRect;

    int countCambio = 6, countCambioAux = countCambio;
    int countDaniado = 50, countDaniadoAux = countDaniado;

    private final Float velocidadInicial = 2.5F;
    private final Float velocidadSprintInicial = 5F;
    private Float vidaInicial = 25F;
    private Float velocidad = 2.5f, velocidadAux = velocidad;
    private Float velocidadSprint = 5f;

    private String nombre;
    private int tiempo;
    private float delta;
    private float elapsedTime = 0f;

    public Luchador(Float x, Float y, Piso piso) {
        super(x, y, piso);
        choquePuerta = false;
        this.atlas = new TextureAtlas("caballero/lucifer.atlas");
        this.daniado=false;
        this.vivo=true;
        this.vidas = 25F;
        vidasMax = 15F;
        this.piso = piso;
        objetos = new ArrayList<>();

        setANCHO(ANCHO);
        setALTO(ALTO);

        setVelocidadX(0f);
        setVelocidadY(0f);
        setX(x);
        setY(y);

        this.espada = new Espada(this, "Espada",Calidad.Normal, atlas.createSprite("assets/espada.atlas"));

        inicializarFrameX(atlas);
        inicializarFrameArriba(atlas);
        inicializarFrameAbajo(atlas);

        ultimoLado = 3;
        spriteRect = new Texture("assets/white.png");
    }


    public void resetearPosicion(){
        frameActual = frameAbajo.get(0);
        setX(50f);
        setY(50f);
    }

    @Override
    public void paint(SpriteBatch batch) {

        //spriteRect = new Texture("assets/white.png");
        //batch.draw(spriteRect,getX(), getY(), ANCHO+ANCHO/4, ALTO);

        if(vivo) {

            delta = Gdx.graphics.getDeltaTime();
            elapsedTime += delta;


            cambiarFrame();
            if (ultimoLado != 3) {
                espada.paint(batch);
            }

            if (getVelocidadY() > 0) {
                if(!daniado||countDaniadoAux%2==0) {
                    batch.draw(frameActual, getX(), getY(), getANCHO(), getALTO());
                }
                ultimoLado = 2;
            } else if (getVelocidadY() < 0) {
                if(!daniado||countDaniadoAux%2==0) {
                    batch.draw(frameActual, getX(), getY(), getANCHO(), getALTO());
                }
                ultimoLado = 3;
            } else if (getVelocidadX() > 0) {
                if(!daniado||countDaniadoAux%2==0) {
                    batch.draw(frameActual, getX(), getY(), getANCHO(), getALTO());
                }
                ultimoLado = 0;
            } else if (getVelocidadX() < 0) {
                if(!daniado||countDaniadoAux%2==0) {
                    batch.draw(frameActual, getX() + getANCHO(), getY(), -getANCHO(), getALTO());
                }
                ultimoLado = 1;
            } else {
                if (ultimoLado == 0) {
                    if(!daniado||countDaniadoAux%2==0) {
                        batch.draw(frameActual, getX(), getY(), getANCHO(), getALTO());
                    }
                } else if (ultimoLado == 2) {
                    frameActual = frameArriba.get(0);  // Quieto mirando hacia arriba ("caballeroF1")

                    if(!daniado||countDaniadoAux%2==0) {
                        batch.draw(frameActual, getX(), getY(), getANCHO(), getALTO());
                    }
                } else if (ultimoLado == 3) {
                    frameActual = frameAbajo.get(0);  // Quieto mirando hacia abajo ("caballeroB1")

                    if(!daniado||countDaniadoAux%2==0) {
                        batch.draw(frameActual, getX(), getY(), getANCHO(), getALTO());
                    }
                } else {
                    if(!daniado||countDaniadoAux%2==0) {
                        batch.draw(frameActual, getX() + getANCHO(), getY(), -getANCHO(), getALTO());
                    }
                }
            }
            if (ultimoLado == 3) {
                espada.paint(batch);
            }

            moverse();
            chocarse();

            if(vidas<=0){
                vivo=false;
            } else if(vidas>=vidasMax){
                vidas = vidasMax;
            }
        }

    }

    @Override
    public void moverse() {

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            espada.zarpazo();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
            velocidad = velocidadSprint;
        } else {
            velocidad = velocidadAux;
        }

        if((Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D)) && der){
            if(getX()<=Escenario.ANCHO-getANCHO()-Habitacion.TAMANIO_PARED) {
                setVelocidadX(velocidad);
                setX(getX() + getVelocidadX());
            }
        }
        if((Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A))&&izq){
            if(getX()>0+Habitacion.TAMANIO_PARED){
                setVelocidadX(-velocidad);
                setX(getX()+getVelocidadX());
            }
        }
        if((Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.W))&&arr){
            if(getY()<=Escenario.ALTO-getALTO()-Habitacion.TAMANIO_PARED/2){
                setVelocidadY(velocidad);
                setY(getY()+getVelocidadY());
            }
        }
        if((Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.S))&&aba){
            if(getY()>0+Habitacion.TAMANIO_PARED){
                setVelocidadY(-velocidad);
                setY(getY()+getVelocidadY());
            }
        }

        if(!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            setVelocidadX(0f);
            setVelocidadY(0f);
            frameActual=framesX.get(0);
        }

        if(!(Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.S)||
            Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.W))){
            setVelocidadY(0f);
        }

        if(daniado){
            countDaniadoAux--;
            if(countDaniadoAux<=0){
                daniado=false;
                countDaniadoAux=countDaniado;
            }
        }

        countCambioAux--;
    }

    public void chocarse(){

        if(!arr||!aba||!izq||!der){
            aba=true;
            arr=true;
            izq=true;
            der=true;
        }

        for(Bloque bloque : piso.buscarHab().getBloques()){

            Rectangle reactArr = new Rectangle(getX(),getY()+6, getANCHO(), getALTO()/2);
            Rectangle reactAba = new Rectangle(getX(),getY()-6, getANCHO(), getALTO()/2);
            Rectangle reactIzq = new Rectangle(getX()-6,getY(), getANCHO(), getALTO()/2);
            Rectangle reactDer = new Rectangle(getX()+6,getY(), getANCHO(), getALTO()/2);

            if(reactArr.overlaps(bloque.getBounds())){
                arr=false;
            }
            if(reactAba.overlaps(bloque.getBounds())){
                aba=false;
            }
            if(reactIzq.overlaps(bloque.getBounds())){
                izq=false;
            }
            if(reactDer.overlaps(bloque.getBounds())){
                der=false;
            }
        }
    }

    public void cambiarFrame() {

        if(countCambioAux<=0&&getVelocidadY()!=0){
            countCambioAux=countCambio;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)||Gdx.input.isKeyPressed(Input.Keys.W)) {
                frameIndex = (frameIndex + 1) % frameArriba.size;
                frameActual = frameArriba.get(frameIndex);
                return;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.S)) {
                frameIndex = (frameIndex + 1) % frameArriba.size; ;
                frameActual = frameAbajo.get(frameIndex);
                return;
            }
        }

        if(countCambioAux<=0&&getVelocidadX()!=0) {
            countCambioAux=countCambio;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D)) {
                frameIndex = (frameIndex + 1) % framesX.size;
            }
            frameActual = framesX.get(frameIndex);  // Actualiza el frame actual
        }
    }

    public void inicializarFrameX(TextureAtlas atlas){
        framesX = new Array<>();
        framesX.add(atlas.findRegion("quieto_frame1"));
        framesX.add(atlas.findRegion("quieto_frame2"));
        framesX.add(atlas.findRegion("quieto_frame3"));
        framesX.add(atlas.findRegion("quieto_frame4"));
        framesX.add(atlas.findRegion("quieto_frame5"));
        framesX.add(atlas.findRegion("quieto_frame6"));

        frameIndex = 0;  // Inicializar en la primera región
        frameActual = framesX.get(frameIndex);  // Obtener la primera región
    }

    public void inicializarFrameArriba(TextureAtlas atlas){
        frameArriba = new Array<>();
        frameArriba.add(atlas.findRegion("atras_frame1"));
        frameArriba.add(atlas.findRegion("atras_frame2"));
        frameArriba.add(atlas.findRegion("atras_frame3"));
        frameArriba.add(atlas.findRegion("atras_frame4"));
        frameArriba.add(atlas.findRegion("atras_frame6"));
        frameArriba.add(atlas.findRegion("atras_frame7"));

        frameIndex = 0;  // Inicializar en la primera región
        frameActual = frameArriba.get(frameIndex);  // Obtener la primera región
    }

    public void inicializarFrameAbajo(TextureAtlas atlas){
        frameAbajo = new Array<>();
        frameAbajo.add(atlas.findRegion("delante_frame1"));
        frameAbajo.add(atlas.findRegion("delante_frame2"));
        frameAbajo.add(atlas.findRegion("delante_frame3"));
        frameAbajo.add(atlas.findRegion("delante_frame4"));
        frameAbajo.add(atlas.findRegion("delante_frame5"));
        frameAbajo.add(atlas.findRegion("delante_frame6"));
        frameAbajo.add(atlas.findRegion("delante_frame7"));
        frameAbajo.add(atlas.findRegion("delante_frame8"));

        frameIndex = 0;  // Inicializar en la primera región
        frameActual = frameAbajo.get(frameIndex);  // Obtener la primera región
    }

    public void daniar(){
        if(!daniado){
            daniado=true;
            vidas--;
        }
    }

    public void actualizarStats(){
        System.out.println("danio: " + espada.getDanio() + " cadencia: " + espada.getCadencia() + " vidaMax: " + vidasMax + " velocidad: " + velocidad + " velodicadSprint: " + velocidadSprint);
        int cadenciaExtraTotal = 0;
        Float danioExtraTotal = 0F;
        Float vidaExtraTotal = 0F;
        Float velocidadExtraTotal = 0F;

        for(ObjetoPasivo objeto : objetos) {
            cadenciaExtraTotal += objeto.getCadenciaExtra();
            danioExtraTotal += objeto.getDanioExtra();
            vidaExtraTotal += objeto.getVidaExtra();
            velocidadExtraTotal += objeto.getVelocidadExtra();
        }

        vidasMax = vidaInicial + vidaExtraTotal;
        velocidad =  velocidadInicial + velocidadExtraTotal;
        velocidadAux = velocidad;
        velocidadSprint = velocidadInicial*2 + velocidadExtraTotal;
        espada.setDanio(espada.getDanioInicial()+danioExtraTotal);
        espada.setCadencia(espada.getCadenciaInicial() - cadenciaExtraTotal);
        espada.setCadenciaAux(espada.getCadencia());

        System.out.println("danio: " + espada.getDanio() + " cadencia: " + espada.getCadencia() + " vidaMax: " + vidasMax + " velocidad: " + velocidad + " velodicadSprint: " + velocidadSprint);
    }

    public Rectangle getBoundsPuertas(){
        return new Rectangle(getX()-getANCHO()/2,getY(),ANCHO*2,ALTO);
    }

}
