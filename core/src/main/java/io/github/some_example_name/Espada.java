package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Espada extends ObjetoUtilizable{
    private TextureAtlas atlas;

    private Sprite frameActual;
    private List<Sprite> frames = new ArrayList<>();
    private int frameIndex;
    private int countCambio = 2, countCambioAux = countCambio;
    private boolean isZarpazo;
    private Sound sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("assets/audio/sonidoEspada.wav"));

    private int cadencia = 20, cadenciaAux = cadencia;
    private int cadenciaInicial = cadencia;
    private Float danioInicial;

    public Espada(Luchador luchador, String nombre, Calidad calidad, Sprite sprite) {
        super(luchador, nombre, calidad, sprite);
        atlas = new TextureAtlas("assets/espada.atlas");
        setSprite(atlas.createSprite("Espada1"));
        frameActual = atlas.createSprite("Espada1");
        inicializarFrames(atlas);
        this.isZarpazo = false;
        cadenciaInicial = cadencia;
        danio = 12.5f;
        danioInicial = danio;
        setX(getLuchador().getX());
        setY(getLuchador().getY()+50);
    }

    public void paint(SpriteBatch batch){
        moverse();
        if(getLuchador().getUltimoLado()==0) {
            frameActual.setRotation(0F);
            batch.draw(frameActual,getX(),getY(), frameActual.getRegionWidth(), frameActual.getRegionHeight());
        } else if(getLuchador().getUltimoLado()==1){
            frameActual.setRotation(0F);
            batch.draw(frameActual,getX(), getY(), -(frameActual.getRegionWidth()), frameActual.getRegionHeight());
        } else if (getLuchador().getUltimoLado()==2) {
            frameActual.setRotation(90F);
            batch.draw(frameActual,getX(), getY());
        } else if (getLuchador().getUltimoLado()==3) {
            frameActual.setRotation(180F);
            batch.draw(frameActual,getX(), getY());
        }
        espadazo();
    }

    public void moverse(){
        int ult = getLuchador().getUltimoLado();
        if(ult==0||ult==1){
            if(ult==0) {
                setX(getLuchador().getX()+50);
                setY(getLuchador().getY());
            } else if(ult==1)  {
                setX(getLuchador().getX()+20);
                setY(getLuchador().getY());
            }
            if(isZarpazo){
                setY(getY()-frameIndex*3);
            }
        } else {
            if(ult==2){
                setX(getLuchador().getX());
                setY(getLuchador().getY()+getLuchador().getALTO()/2);
            } else if(ult==3){
                setX(getLuchador().getX());
                setY(getLuchador().getY()-getLuchador().getALTO()/2);
            }

            if(isZarpazo){
                setY(getY()-frameIndex*3);
            }
        }
    }

    public void zarpazo(){
        cadenciaAux--;
        if(!isZarpazo&&cadenciaAux<=0) {
            isZarpazo = true;
            sonidoDisparo.play(0.03f);
            cadenciaAux = cadencia;
        }
    }

    public void espadazo(){
        countCambioAux--;
        if(isZarpazo&&countCambioAux<=0){
            if(frames.size()-1!=frameIndex){
                frameIndex++;
            } else {
                frameIndex = 0;
                isZarpazo=false;
            }
            frameActual = frames.get(frameIndex);  // Actualiza el frame actual
            countCambioAux=countCambio;
        }
        if(cadenciaAux>=0){
            cadenciaAux--;
        }
    }

    public void inicializarFrames(TextureAtlas atlas){
        frames.add(atlas.createSprite("Espada1"));
        frames.add(atlas.createSprite("Espada2"));
        frames.add(atlas.createSprite("Espada3"));
        frames.add(atlas.createSprite("Espada4"));
        frames.add(atlas.createSprite("Espada5"));
        frames.add(atlas.createSprite("Espada6"));
    }

    public Rectangle getBounds(){
        if(getLuchador().getUltimoLado()==0||getLuchador().getUltimoLado()==2||getLuchador().getUltimoLado()==3){
            return new Rectangle(getX(),getY(),frameActual.getRegionWidth(),frameActual.getRegionHeight());
        } else {
            return new Rectangle(getX()-getANCHO(), getY(), frameActual.getRegionWidth(), frameActual.getRegionHeight());
        }
    }

    /*
    public void actualizarStats(){
        tempZarpazo = 30 - getLuchador().getCadenciaExtra();
        tempZarpazoAux = tempZarpazo;
        danio = 12.5F + getLuchador().getDanioExtra();
    }*/

    public int getANCHO(){
        return frameActual.getRegionWidth();
    }

    public int getALTO(){
        return frameActual.getRegionHeight();
    }
}
