package com.capuccinolovesballerina.game;

import com.badlogic.gdx.Game;
import com.capuccinolovesballerina.game.pantallas.PantallaMenu;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new PantallaMenu());
    }

    @Override
    public void render() {
        super.render();
    }
}
