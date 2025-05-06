package io.github.some_example_name.Objetos;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Calidad;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.ObjetoPasivo;
import lombok.Getter;

@Getter
public class Frasco extends ObjetoPasivo {
    private Float danioExtra = 0F;
    private Float vidaExtra = 0F;
    private int cadenciaExtra = 8;
    private Float velocidadExtra = 0F;
    public Frasco(Float x, Float y) {
        super(x, y);
        setSprite(atlas.createSprite("pocion"));
        setCalidad(Calidad.Raro);
        setNombre("Frasco Maligno");
    }

    @Override
    public void efecto(Luchador luchador) {

    }
}
