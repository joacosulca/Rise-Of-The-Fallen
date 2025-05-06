package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

import static io.github.some_example_name.ETipoBloque.*;

@Getter
@Setter
public class Bloque {
    private Float x;
    private Float y;
    public static final Float ANCHO = 50F;
    public static final Float ALTO = 50F;
    private ETipoBloque tipoBloque;
    private TextureAtlas atlas;
    private Sprite sprite;

    public Bloque(){

    }

    public Bloque(Float x, Float y, ETipoBloque tipoBloque){
        this.x = x;
        this.y = y;
        this.tipoBloque = tipoBloque;
        atlas = new TextureAtlas(Gdx.files.internal("assets/Bloques.atlas"));

        if(tipoBloque==CIELO) {
            sprite = atlas.createSprite("Bloque_cielo");
        } else if(tipoBloque==TIERRA){
            sprite = atlas.createSprite("Bloque_tierra");
        } else if(tipoBloque==INFIERNO){
            sprite = atlas.createSprite("Bloque_infierno");
        }
        sprite.setBounds(x, y, ANCHO, ALTO);
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,ANCHO,ALTO);
    }
}
