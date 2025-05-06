package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.mapas.Piso;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Individuo {

    protected Sprite sprite;
    protected TextureAtlas atlas;
    protected Float ANCHO;
    protected Float ALTO;
    protected Boolean vivo;
    private Float x;
    private Float y;
    private Float velocidadX;
    private Float velocidadY;
    private Piso piso;
    protected List<TextureRegion> frames;
    protected int frameIndex;
    protected TextureRegion frameActual;
    protected final int countCambio = 8;
    protected int countCambioAux = countCambio;

    public Individuo(Float x, Float y, Piso piso){
        this.x = x;
        this.y = y;
        this.velocidadX=0f;
        this.velocidadY=0f;
        this.piso = piso;
        vivo = true;
        frames = new ArrayList<>();
    }

    public abstract void paint(SpriteBatch batch);

    public abstract void moverse();

    public Rectangle getBounds(){
        return new Rectangle(x, y, ANCHO, ALTO/2);
    }

    public Rectangle getBoundsGrande(){
        return new Rectangle(x, y, ANCHO+ANCHO/4, ALTO);
    }
}
