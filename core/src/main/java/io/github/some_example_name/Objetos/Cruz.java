package io.github.some_example_name.Objetos;

import io.github.some_example_name.Calidad;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.ObjetoPasivo;

public class Cruz extends ObjetoPasivo {
    public Cruz(Float x, Float y) {
        super(x, y);
        setSprite(atlas.createSprite("dios"));
        setCalidad(Calidad.Raro);
        setNombre("Cruz de dios");
        danioExtra = 0f;
        vidaExtra = 10F;
        cadenciaExtra = 5;
        velocidadExtra = 5f;

    }

    @Override
    public void efecto(Luchador luchador) {
        luchador.setVidas(luchador.getVidas()+10F);
        luchador.setANCHO(luchador.getANCHO()-10);
        luchador.setANCHO(luchador.getALTO()-10);
    }
}
