package io.github.some_example_name.habMapa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabMapa {
    private final Float ANCHO = 20F;
    private final Float ALTO = 20F;
    private Float x;
    private Float y;
    private ETipoHab tipoHab;
    private TextureAtlas atlas;
    private Sprite sprite;
    private int posX;
    private int posY;


    public HabMapa(Float x, Float y, ETipoHab tipoHab, int posX, int posY){
        this.x = x;
        this.y = y;
        this.tipoHab = tipoHab;
        this.posX = posX;
        this.posY = posY;

        atlas = new TextureAtlas(Gdx.files.internal("assets/habMapa.atlas"));
    }

    public void paint(SpriteBatch batch){
        switch(tipoHab){
            case VISITADA:
                sprite = atlas.createSprite("Visitada");
                break;
            case NO_VISITADA:
                sprite = atlas.createSprite("NoVisitada");
                break;
            case BLOQUEADA:
                sprite = atlas.createSprite("Bloqueada");
                break;
            case ACTUAL:
                sprite = atlas.createSprite("Actual");
                break;
        }
        batch.draw(sprite, x, y, ANCHO, ALTO);
    }
}
