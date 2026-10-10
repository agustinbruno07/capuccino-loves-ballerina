
package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.capuccinolovesballerina.game.Utilidades.ControladorEntradaJugador;
import com.capuccinolovesballerina.game.entidades.CapuccinoAssassino;
import com.capuccinolovesballerina.game.graficos.AnimacionesCappuccino;
import com.capuccinolovesballerina.game.graficos.Estado;
import com.capuccinolovesballerina.game.mapa.Nivel;
import com.capuccinolovesballerina.game.objetos.ElementosNivel;
import com.capuccinolovesballerina.game.objetos.ObjetoCortable;
import com.capuccinolovesballerina.game.objetos.Palanca;
import com.capuccinolovesballerina.game.objetos.Puerta;
import com.capuccinolovesballerina.game.pantallas.HUD;
import com.capuccinolovesballerina.game.pantallas.MenuGameOver;
import com.capuccinolovesballerina.game.pantallas.MenuPausa;
import com.capuccinolovesballerina.game.pantallas.MenuVictoria;

public class ControladorPartida {

    private final Nivel nivel;
    private final ElementosNivel elementos;

    private final MenuPausa menuPausa;
    private final MenuGameOver menuGameOver;
    private final MenuVictoria menuVictoria;
    private final HUD hud;

    private final ControladorEntradaJugador entrada;
    private final CapuccinoAssassino cappuccino;
    private final AnimacionesCappuccino animaciones;

    public ControladorPartida(Nivel nivel, ElementosNivel elementos, MenuPausa menuPausa, MenuGameOver menuGameOver, MenuVictoria menuVictoria, HUD hud, ControladorEntradaJugador entrada, CapuccinoAssassino cappuccino, AnimacionesCappuccino animaciones) {
        this.nivel = nivel;
        this.elementos = elementos;
        this.menuPausa = menuPausa;
        this.menuGameOver = menuGameOver;
        this.menuVictoria = menuVictoria;
        this.hud = hud;
        this.entrada = entrada;
        this.cappuccino = cappuccino;
        this.animaciones = animaciones;
    }

    public void actualizar(float delta) {

        if (entrada.isEscapePresionado()) {
            if (menuPausa.estaVisible()) {
                menuPausa.ocultar();
            } else if (!menuGameOver.estaVisible()
                && !menuVictoria.estaVisible()) {

                menuPausa.mostrar();
                entrada.soltarTodo();
            }
        }

        if (menuPausa.estaVisible()) {
            menuPausa.actualizar(delta);
            return;
        }

        if (menuGameOver.estaVisible()) {
            menuGameOver.actualizar(delta);
            return;
        }

        if (menuVictoria.estaVisible()) {
            menuVictoria.actualizar(delta);
            return;
        }

        if (Gdx.input.getInputProcessor() != entrada) {
            Gdx.input.setInputProcessor(entrada);
        }

        hud.actualizar(delta);

        Array<Rectangle> solidos = new Array<>(nivel.getColisiones().getSolidos());

        for (ObjetoCortable cortable : elementos.getCortables()) {
            if (cortable.bloquea()) {
                solidos.add(cortable.getZona());
            }
        }

        cappuccino.actualizar(delta, entrada.isIzquierda(), entrada.isDerecha(), entrada.isSaltoPresionado(), solidos);

        if (entrada.isHabilidadPresionada()) {
            animaciones.setEstado(Estado.ATTACK);
            cortar();
        } else if (!cappuccino.isEnSuelo()) {
            animaciones.setEstado(Estado.JUMP);
        } else if (entrada.isIzquierda() || entrada.isDerecha()) {
            animaciones.setEstado(Estado.WALK);
        } else {
            animaciones.setEstado(Estado.IDLE);
        }

        animaciones.actualizar(delta);
        actualizarPalanca();
        comprobarVictoria();

        if (menuVictoria.estaVisible()) {
            return;
        }

        if (cappuccino.getY() < -cappuccino.getAlto()) {
            mostrarGameOver();
            return;
        }

        for (Rectangle peligro : elementos.getPeligros()) {
            if (cappuccino.getCaja().overlaps(peligro)) {
                mostrarGameOver();
                return;
            }
        }

        entrada.limpiarEventos();
    }

    private void actualizarPalanca() {

        Palanca palanca = elementos.getPalanca();
        Puerta puerta = elementos.getPuerta();

        if (palanca == null || !entrada.isInteractuarPresionado()) {
            return;
        }

        Rectangle caja = cappuccino.getCaja();

        Rectangle alcance = new Rectangle(caja.x - 12, caja.y - 12, caja.width + 24, caja.height + 24);

        if (!alcance.overlaps(palanca.getZona())) {
            return;
        }

        boolean vaACerrar = palanca.estaActivada();
        boolean jugadorEnLaPuerta = puerta != null && caja.overlaps(puerta.getZona());

        if (!vaACerrar || !jugadorEnLaPuerta) {
            palanca.alternar();

            if (puerta != null) {
                puerta.setAbierta(palanca.estaActivada());
            }
        }
    }

    private void comprobarVictoria() {

        Puerta puerta = elementos.getPuerta();

        if (puerta != null && puerta.estaAbierta() && entrada.isInteractuarPresionado() && cappuccino.getCaja().overlaps(puerta.getZona())) {
            entrada.soltarTodo();
            menuVictoria.mostrar();
        }
    }

    private void mostrarGameOver() {
        entrada.soltarTodo();
        menuGameOver.mostrar();
    }

    private void cortar() {

        Rectangle caja = cappuccino.getCaja();
        float alcance = 40f;
        Rectangle zonaCorte;

        if (cappuccino.isMirandoDerecha()) {
            zonaCorte = new Rectangle(caja.x + caja.width, caja.y, alcance, caja.height);
        } else {
            zonaCorte = new Rectangle(caja.x - alcance, caja.y, alcance, caja.height);
        }

        for (ObjetoCortable cortable : elementos.getCortables()) {
            if (!cortable.estaCortado()
                && zonaCorte.overlaps(cortable.getZona())) {

                cortable.setCortado(true);
            }
        }
    }

    public void reiniciar() {

        cappuccino.reaparecer(elementos.getSpawnX(), elementos.getSpawnY());

        Puerta puerta = elementos.getPuerta();
        Palanca palanca = elementos.getPalanca();

        if (puerta != null) {
            puerta.setAbierta(false);
        }

        if (palanca != null && palanca.estaActivada()) {
            palanca.alternar();
        }

        for (ObjetoCortable cortable : elementos.getCortables()) {
            cortable.setCortado(false);
        }

        entrada.soltarTodo();
        animaciones.reiniciar();
        hud.reiniciar();
    }
}
