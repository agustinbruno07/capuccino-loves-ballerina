package com.capuccinolovesballerina.game;

import com.badlogic.gdx.Game;
import com.capuccinolovesballerina.game.pantallas.PantallaMenu;

public class CapuccinoLovesBallerinaGame extends Game {

    @Override
    public void create() {
        setScreen(new PantallaMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
