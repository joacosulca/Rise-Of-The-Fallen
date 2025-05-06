package io.github.some_example_name.Enemigos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.Bloque;
import io.github.some_example_name.Enemigo;
import io.github.some_example_name.Individuo;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.mapas.Piso;

import java.util.ArrayList;
import java.util.List;

public class Paloma extends Enemigo {

    private TextureAtlas atlas;
    private TextureRegion frameActual;
    public static final Float ANCHO = 60F;
    public static final Float ALTO = 80F;
    private Float vida;
    private Float velocidadX;
    private Float velocidadY;
    private Luchador luchador;
    private List<TextureRegion> frames = new ArrayList<>();
    private int countCambio=8, countCambioAux=countCambio;
    private int countDaniado=15, countDaniadoAux=countDaniado;
    private int frameIndex=0;
    private Boolean daniado;
    private int ultimoLadoAux;
    private Piso piso;
    private Float velocidad;

    public Paloma(Float x, Float y, Luchador luchador, Piso piso){
        super(x, y, luchador, piso);
        atlas = new TextureAtlas("Enemigos/paloma.atlas");
        setX(x);
        setY(y);
        setANCHO(60F);
        setALTO(60F);
        vida=120F;
        this.luchador = luchador;
        inicializarFrames();
        velocidadX=0F;
        velocidadY=0F;
        velocidad = 1.5F;
        daniado = false;
        frameActual = frames.get(0);
        setVivo(true);
        this.piso = piso;
        //setAtlas(new TextureAtlas("Enemigos/Demonio.atlas"));
        //setSprite(getAtlas().createSprite("Demonio"));
    }

    @Override
    public void paint(SpriteBatch batch) {
        if(getVivo()) {
            if (velocidadX > 0f) {
                if(daniado){
                    batch.setColor(Color.valueOf("ff4a4a"));
                }
                batch.draw(frameActual, getX(), getY(), getANCHO(), getALTO());
                batch.setColor(Color.WHITE);
            } else {
                if(daniado){
                    batch.setColor(Color.valueOf("ff4a4a"));
                }
                batch.draw(frameActual, getX() + getANCHO(), getY(), -getANCHO(), getALTO());
                batch.setColor(Color.WHITE);
            }

            moverse();
            colisionar();
        }
    }

    @Override
    public void moverse() {
        if (vida <= 0) {
            setVivo(false);
        }
        setX(getX() + velocidadX);
        setY(getY() + velocidadY);
        if(getVivo()) {

            if (daniado) {
                countDaniadoAux--;
                if(ultimoLadoAux==0||ultimoLadoAux==1){
                    velocidadX = (ultimoLadoAux==1) ? -2f : 2f;
                    velocidadY = 0F;
                } else if(ultimoLadoAux==2||ultimoLadoAux==3){
                    velocidadX = 0F;
                    velocidadY = (ultimoLadoAux==3) ? -2f : 2f;
                }
                if (countDaniadoAux <= 0) {
                    countDaniadoAux = countDaniado;
                    daniado = false;
                }
            } else if (!daniado) {
                float dirX = getLuchador().getX() - getX();
                float dirY = getLuchador().getY() - getY();

                float magnitud = (float) Math.sqrt(dirX * dirX + dirY * dirY);

                dirX /= magnitud;
                dirY /= magnitud;

                velocidadX = dirX * velocidad;
                velocidadY = dirY * velocidad;

                countCambioAux--;
                if (countCambioAux <= 0) {
                    countCambioAux = countCambio;
                    if (getVelocidadX() >= 0f) {
                        frameIndex = (frameIndex + 1) % frames.size();  // Avanza al siguiente frame y hace loop
                    } else if (getVelocidadX() < 0f) {
                        frameIndex = (frameIndex - 1 + frames.size()) % frames.size();  // Retrocede al frame anterior
                    }
                    frameActual = frames.get(frameIndex);  // Actualiza el frame actual
                }
                ultimoLadoAux=luchador.getUltimoLado();
            }

        }
    }

    public void inicializarFrames(){
        frames.add(atlas.findRegion("paloma1"));
        frames.add(atlas.findRegion("paloma2"));
    }

    public void colisionar(){
        if(!daniado&&getVivo()) {
            Rectangle reactEspada = luchador.getEspada().getBounds();
            if (reactEspada.overlaps(getBounds())&&luchador.getEspada().isZarpazo()) {
                daniar();
                vida -= luchador.getEspada().getDanio();
            }
        }
        if(getVivo()&&luchador.getVivo()){
            Rectangle reactLuchador = luchador.getBounds();
            if (reactLuchador.overlaps(getBounds())) {
                luchador.daniar();
            }
        }
    }

    public void daniar(){
        daniado=true;
    }

}
