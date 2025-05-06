package io.github.some_example_name.Objetos;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Calidad;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.ObjetoPasivo;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Corona extends ObjetoPasivo {
    public Corona(Float x, Float y) {
        super(x, y);
        setSprite(atlas.createSprite("corona"));
        setCalidad(Calidad.Raro);
        setNombre("Corona Del Rey Minos");
        danioExtra = 6.25F;
        vidaExtra = 10F;
        cadenciaExtra = 5;
        velocidadExtra = 5F;
    }

    @Override
    public void efecto(Luchador luchador) {

    }

    /*public void efecto(){
        getLuchador().setDanioExtra(danioExtra);
        getLuchador().setCadenciaExtra(cadenciaExtra);

        // TODO  menor daño de enemigos hacia vos O ..
    }*/
}
