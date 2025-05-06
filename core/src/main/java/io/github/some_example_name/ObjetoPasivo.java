package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ObjetoPasivo extends Objeto{
    protected final TextureAtlas atlas = new TextureAtlas("Objetos.atlas");
    private final Float ALTO = 50F;
    private final Float ANCHO = 50F;
    private Float x;
    private Float y;
    protected int cadenciaExtra;
    protected Float danioExtra;
    protected Float vidaExtra;
    protected Float velocidadExtra;

    public ObjetoPasivo(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(SpriteBatch batch) {
        batch.draw(getSprite(), x, y, ANCHO, ALTO);
    }

    public abstract void efecto(Luchador luchador);


}
