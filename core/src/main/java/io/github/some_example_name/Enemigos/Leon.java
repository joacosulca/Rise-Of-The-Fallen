package io.github.some_example_name.Enemigos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.Enemigo;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.mapas.Piso;

import java.util.ArrayList;
import java.util.List;


public class Leon extends Enemigo {
    private Sprite sprite;
    private TextureAtlas atlas;
    private final Float velocidad = 2F;
    private Float velocidadX;
    private Float velocidadY;
    private Boolean daniado;
    private Float disX;
    private Float disY;
    private Boolean isPlacaje;
    private Float disXAux;
    private Float disYAux;
    private int countPlacaje = 150, countPlacajeAux = countPlacaje;
    private int countDaniado = 20, countDaniadoAux = countDaniado;
    private int cooldownPlacaje = 60,  cooldownPlacajeAux = cooldownPlacaje;

    public Leon(Float x, Float y, Luchador luchador, Piso piso) {
        super(x, y, luchador, piso);
        atlas = new TextureAtlas("Enemigos/Leon.atlas");
        inicializarFrames();
        ANCHO = 120F;
        ALTO = 90F;
        velocidadX = 0F;
        velocidadY = 0F;
        vida=45F;
        daniado = false;
        isPlacaje = false;
    }

    @Override
    public void paint(SpriteBatch batch) {
        if(getVivo()) {
            if (daniado) {
                batch.setColor(Color.valueOf("ff4a4a"));
            }
            if(velocidadX>=0F){
                batch.draw(frameActual, getX(), getY(), ANCHO, ALTO);
            } else {
                batch.draw(frameActual, getX()+ANCHO, getY(), -ANCHO, ALTO);
            }
            batch.setColor(Color.WHITE);

            moverse();
        }
    }

    public void moverse(){
        if(!daniado && !isPlacaje) {
            float dirX = getLuchador().getX() - getX();
            float dirY = getLuchador().getY() - getY();

            float magnitud = (float) Math.sqrt(dirX * dirX + dirY * dirY);

            dirX /= magnitud;
            dirY /= magnitud;

            velocidadX = dirX * velocidad;
            velocidadY = dirY * velocidad;

            disX = (getX()>getLuchador().getX()) ? getX()-getLuchador().getX() : getLuchador().getX()-getX();
            disY = (getY()>getLuchador().getY()) ? getY()-getLuchador().getY() : getLuchador().getY()-getY();
        }
        colisionar();
        esperarPlacaje();
        placaje();

        setX(getX() + velocidadX);
        setY(getY() + velocidadY);

        countCambioAux--;
        if (countCambioAux <= 0) {
            countCambioAux = countCambio;
            if(countPlacajeAux >= countPlacaje/3) {
                if (getVelocidadX() >= 0f) {
                    frameIndex = (frameIndex + 1) % frames.size();  // Avanza al siguiente frame y hace loop
                } else if (getVelocidadX() < 0f) {
                    frameIndex = (frameIndex - 1 + frames.size()) % frames.size();  // Retrocede al frame anterior
                }
                frameActual = frames.get(frameIndex);  // Actualiza el frame actual
            }
        }
    }

    private int ultLadoAux;
    public void colisionar(){
        if(getBounds().overlaps(getLuchador().getBounds()) && getLuchador().getVivo() && !getLuchador().getDaniado()){
            getLuchador().daniar();
        }

        if(getBounds().overlaps(getLuchador().getEspada().getBounds()) && getLuchador().getEspada().isZarpazo() && getLuchador().getVivo()){
            ultLadoAux = getLuchador().getUltimoLado();
            vida -= getLuchador().getEspada().getDanio();

            daniado=true;
            isPlacaje = false;
            countPlacajeAux = countPlacaje;
            cooldownPlacajeAux = cooldownPlacaje;

            if(ultLadoAux==0||ultLadoAux==1){
                velocidadX = (ultLadoAux==1) ? -2f : 2f;
                velocidadY = 0F;
            } else if(ultLadoAux==2||ultLadoAux==3){
                velocidadX = 0F;
                velocidadY = (ultLadoAux==3) ? -2f : 2f;
            }
        }
        estarDaniado();
        if(vida<=0){
            setVivo(false);
        }
    }

    public void placaje(){
        if(!isPlacaje && disX <= 300F && disY<= 300F && cooldownPlacajeAux<=0){
            isPlacaje= true;
            disXAux = disX;
            disYAux = disY;
        }

        if(isPlacaje){
            estarHaciendoPlacaje();
            velocidadX = (getLuchador().getX() >= getX()) ? disXAux / 50 : -(disXAux / 40);
            velocidadY = (getLuchador().getY() >= getY()) ? disYAux / 50 : -(disYAux / 40);
        }
    }


    public void estarDaniado(){
        if(daniado){
            countDaniadoAux--;
            if(countDaniadoAux<=0){
                countDaniadoAux = countDaniado;
                daniado = false;
            }
        }
    }

    public void estarHaciendoPlacaje(){
        if(isPlacaje){
            countPlacajeAux--;
            if(countPlacajeAux<=0){
                isPlacaje = false;
                countPlacajeAux = countPlacaje;
                cooldownPlacajeAux = cooldownPlacaje;
            }
        }
    }

    public void esperarPlacaje(){
        if(!isPlacaje){
            cooldownPlacajeAux--;
        }
    }

    public void inicializarFrames(){
        frames.add(atlas.findRegion("Leon1"));
        frames.add(atlas.findRegion("Leon2"));
        frames.add(atlas.findRegion("Leon3"));
        frames.add(atlas.findRegion("Leon4"));
        frames.add(atlas.findRegion("Leon5"));
        frameActual = frames.get(0);
    }
}
