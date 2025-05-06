package io.github.some_example_name.Objetos;

import io.github.some_example_name.Calidad;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.ObjetoPasivo;

public class Copa extends ObjetoPasivo {
    public Copa(Float x, Float y) {
        super(x, y);
        setSprite(atlas.createSprite("corona"));
        setCalidad(Calidad.Raro);
        setNombre("Corona Del Rey Minos");
        danioExtra = 6.25F;
        vidaExtra = 10F;
        cadenciaExtra = 5;
        velocidadExtra = 0F;
    }

    @Override
    public void efecto(Luchador luchador) {
        luchador.setVidas(luchador.getVidas()+vidaExtra);
    }

    /*public void efecto(){
        getLuchador().setDanioExtra(danioExtra);
        getLuchador().setCadenciaExtra(cadenciaExtra);

        // TODO  menor daño de enemigos hacia vos O ..
    }*/
}
