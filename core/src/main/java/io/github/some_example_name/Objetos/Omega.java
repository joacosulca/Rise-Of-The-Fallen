package io.github.some_example_name.Objetos;

import io.github.some_example_name.Calidad;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.ObjetoPasivo;

public class Omega extends ObjetoPasivo {
    public Omega(Float x, Float y) {
        super(x, y);
        setSprite(atlas.createSprite("omega"));
        setCalidad(Calidad.Raro);
        setNombre("Omega");
        danioExtra = 20f;
        vidaExtra = 0F;
        cadenciaExtra = 5;
        velocidadExtra = -5f;
    }

    @Override
    public void efecto(Luchador luchador) {
        luchador.setVidas(luchador.getVidas()+5F);
        luchador.setANCHO(luchador.getANCHO()+10);
        luchador.setANCHO(luchador.getALTO()+10);
    }
}
