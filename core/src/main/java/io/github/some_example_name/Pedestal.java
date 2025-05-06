package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.Objetos.*;

import java.util.Random;

public class Pedestal {
    private Float x;
    private Float y;
    private final Float ANCHO = Bloque.ANCHO;
    private final Float ALTO = Bloque.ALTO;
    private Texture sprite;
    private ObjetoPasivo objetoPasivo;
    private Boolean existente;
    private Random rand = new Random();
    private int numRand;
    private Luchador luchador;
    private Boolean objetoTomado;
    private int countDesaparecer = 200, countDesaparecerAux = countDesaparecer;

    public Pedestal(){}

    public Pedestal(Float x, Float y, Luchador luchador){
        this.luchador = luchador;
        this.x = x;
        this.y = y;
        existente = false;
        objetoTomado = false;
        sprite = new Texture("pedestal.png");
        numRand = rand.nextInt(10) + 1;
        switch(numRand){
            case 1:
                objetoPasivo = new Perla(x, y);
                break;
            case 2:
                objetoPasivo = new Corona(x, y);
                break;
            case 3:
                objetoPasivo = new Frasco(x, y);
                break;
            case 4:
                objetoPasivo = new Piedra(x, y);
                break;
            case 5, 8:
                objetoPasivo = new Copa(x, y);
                break;
            case 6:
                objetoPasivo = new Cruz(x, y);
                break;
            case 7:
                objetoPasivo = new Omega(x, y);
                break;
            default:
                objetoPasivo = new Perla(x, y);
                break;
        }
    }

    public void paint(SpriteBatch batch){
        if(existente){
            batch.draw(sprite, x, y, ANCHO, ALTO);
            if(!objetoTomado) {
                batch.draw(objetoPasivo.getSprite(), objetoPasivo.getX(), objetoPasivo.getY() + 30F);
            }
            chocar();
        }
    }

    public void chocar(){
        if(existente){
            if(getBounds().overlaps(luchador.getBoundsGrande()) && !objetoTomado) {
                luchador.getObjetos().add(objetoPasivo);
                objetoTomado = true;
                luchador.actualizarStats();
                objetoPasivo.efecto(luchador);
                countDesaparecerAux--;
            }
        }
        if(countDesaparecerAux<=countDesaparecer-1){
            countDesaparecerAux--;
        }
        if(countDesaparecerAux<=0){
            existente=false;
        }
    }

    public void spawn(){
        existente = true;
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, ANCHO, ALTO);
    }
}
