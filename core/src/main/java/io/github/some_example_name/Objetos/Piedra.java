package io.github.some_example_name.Objetos;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.Calidad;
import io.github.some_example_name.Luchador;
import io.github.some_example_name.ObjetoPasivo;
import lombok.Getter;

@Getter
public class Piedra extends ObjetoPasivo {
    private Float danioExtra = 0F;
    private Float vidaExtra = 20F;
    private int cadenciaExtra = 0;
    private Float velocidadExtra = 0F;
    public Piedra(Float x, Float y) {
        super(x, y);
        setSprite(atlas.createSprite("cristal"));
        setCalidad(Calidad.Normal);
        setNombre("Piedrita");
    }

    @Override
    public void efecto(Luchador luchador) {
        luchador.setVidas(luchador.getVidasMax());
    }
}
