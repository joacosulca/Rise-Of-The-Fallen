package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Objeto {
    private String nombre;
    private Calidad calidad;
    private Sprite sprite;

    public Objeto() {
    }

    public Objeto(Luchador luchador) {
    }

    public abstract void paint(SpriteBatch batch);
}
