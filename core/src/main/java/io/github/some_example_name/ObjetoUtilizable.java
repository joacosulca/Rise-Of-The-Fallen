package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public abstract class ObjetoUtilizable extends Objeto{

    private Float x;
    private Float y;
    protected int cadencia;
    protected final int cadenciaInicial;
    protected Float danio;
    protected final Float danioInicial;
    private Luchador luchador;

    public ObjetoUtilizable(Luchador luchador, String nombre, Calidad calidad, Sprite sprite) {
        this.luchador = luchador;
        cadenciaInicial = 0;
        danioInicial = 0F;
    }

    @Override
    public void paint(SpriteBatch batch) {

    }
}
