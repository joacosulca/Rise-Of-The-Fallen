package io.github.some_example_name.Objetos;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Calidad;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.ObjetoPasivo;
import lombok.Getter;

@Getter
public class Perla extends ObjetoPasivo {
    private Float danioExtra = 10.00F;
    private Float vidaExtra = -10F;
    private int cadenciaExtra = 0;
    private Float velocidadExtra = 0F;

    public Perla(Float x, Float y) {
        super(x, y);
        setSprite(atlas.createSprite("bola"));
        setCalidad(Calidad.Normal);
        setNombre("Lanza");
    }

    public void efecto(){

    }

    @Override
    public void efecto(Luchador luchador) {

    }
}
