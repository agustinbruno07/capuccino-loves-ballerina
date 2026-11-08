package com.capuccinolovesballerina.game.Utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.capuccinolovesballerina.game.entidades.BallerinaCapuccina;
import com.capuccinolovesballerina.game.entidades.CapuccinoAssassino;
import com.capuccinolovesballerina.game.entidades.Player;
import com.capuccinolovesballerina.game.graficos.AnimacionesCappuccino;
import com.capuccinolovesballerina.game.graficos.Estado;
import com.capuccinolovesballerina.game.mapa.Nivel;
import com.capuccinolovesballerina.game.objetos.BloqueEspuma;
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
    private final BallerinaCapuccina ballerina;
    private final AnimacionesCappuccino animaciones;
    private static final float ALCANCE_PALANCA = 12f;

    public ControladorPartida(
        Nivel nivel,
        ElementosNivel elementos,
        MenuPausa menuPausa,
        MenuGameOver menuGameOver,
        MenuVictoria menuVictoria,
        HUD hud,
        ControladorEntradaJugador entrada,
        CapuccinoAssassino cappuccino,
        BallerinaCapuccina ballerina,
        AnimacionesCappuccino animaciones
    ) {
        this.nivel = nivel;
        this.elementos = elementos;
        this.menuPausa = menuPausa;
        this.menuGameOver = menuGameOver;
        this.menuVictoria = menuVictoria;
        this.hud = hud;
        this.entrada = entrada;
        this.cappuccino = cappuccino;
        this.ballerina = ballerina;
        this.animaciones = animaciones;
    }

    public void actualizar(float delta) {

        if (entrada.isEscapePresionado()) {
            if (menuPausa.estaVisible()) {
                menuPausa.ocultar();
            } else if (!menuGameOver.estaVisible() && !menuVictoria.estaVisible()) {
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

        ballerina.actualizarHabilidad(delta);

        if (entrada.isHabilidadPresionadaBallerina()) {
            ballerina.crearEspuma();
        }


        Array<Rectangle> solidos = new Array<>(nivel.getColisiones().getSolidos());

        for (ObjetoCortable cortable : elementos.getCortables()) {
            if (cortable.bloquea()) {
                solidos.add(cortable.getZona());
            }
        }

        BloqueEspuma espuma = ballerina.getEspuma();
        if (espuma != null) {
            solidos.add(espuma.getZona());
        }

        cappuccino.actualizar(
            delta,
            entrada.isIzquierda(),           // A
            entrada.isDerecha(),             // D
            entrada.isSaltoPresionado(),     // W / Espacio
            solidos
        );

        ballerina.actualizar(
            delta,
            entrada.isIzquierdaBallerina(),      // ←
            entrada.isDerechaBallerina(),        // →
            entrada.isSaltoPresionadoBallerina(),// ↑
            solidos
        );


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


        if (estaMuerto(cappuccino) || estaMuerto(ballerina)) {
            mostrarGameOver();
            return;
        }


        comprobarVictoria();

        if (menuVictoria.estaVisible()) {
            return;
        }
        actualizarPalancas();
        entrada.limpiarEventos();
    }

    private void actualizarPalancas() {
        Palanca palCap = elementos.getPalancaCappuccino();
        Palanca palBal = elementos.getPalancaBallerina();

        if (entrada.isInteractuarPresionado()
            && estaCerca(cappuccino, palCap)) {
            intentarAlternar(palCap);
        }

        if (entrada.isInteractuarPresionadoBallerina()
            && estaCerca(ballerina, palBal)) {
            intentarAlternar(palBal);
        }
    }
    private boolean estaCerca(Player personaje, Palanca palanca) {
        if (palanca == null) return false;
        Rectangle c = personaje.getCaja();
        Rectangle alcance = new Rectangle(
            c.x - ALCANCE_PALANCA, c.y - ALCANCE_PALANCA,
            c.width + 2 * ALCANCE_PALANCA, c.height + 2 * ALCANCE_PALANCA);
        return alcance.overlaps(palanca.getZona());
    }
    private void intentarAlternar(Palanca palanca) {
        Puerta puerta = elementos.getPuerta();

        // No dejar cerrar la puerta con alguien parado adentro
        boolean seCerraria = palanca.estaActivada() && puerta != null && puerta.estaAbierta();
        if (seCerraria && alguienEnLaPuerta()) return;

        palanca.alternar();
        actualizarPuerta();
    }


    private void actualizarPuerta() {
        Puerta puerta = elementos.getPuerta();
        Palanca palCap = elementos.getPalancaCappuccino();
        Palanca palBal = elementos.getPalancaBallerina();
        if (puerta == null || (palCap == null && palBal == null)) return;

        // si un nivel solo tiene una palanca, la que falta no bloquea
        boolean capOk = palCap == null || palCap.estaActivada();
        boolean balOk = palBal == null || palBal.estaActivada();
        puerta.setAbierta(capOk && balOk);
    }
    private boolean alguienEnLaPuerta() {
        Puerta puerta = elementos.getPuerta();
        return puerta != null
            && (cappuccino.getCaja().overlaps(puerta.getZona())
            || ballerina.getCaja().overlaps(puerta.getZona()));
    }


    private void comprobarVictoria() {
        Puerta puerta = elementos.getPuerta();

        if (puerta == null || !puerta.estaAbierta()) {
            return;
        }

        Rectangle zona = puerta.getZona();

        if (cappuccino.getCaja().overlaps(zona) && ballerina.getCaja().overlaps(zona)) {
            entrada.soltarTodo();
            menuVictoria.mostrar();
        }
    }

    private boolean estaMuerto(Player personaje) {
        if (personaje.getY() < -personaje.getAlto()) {
            return true;
        }

        for (Rectangle peligro : elementos.getHitboxPeligros()) {
            if (personaje.getCaja().overlaps(peligro)) {
                return true;
            }
        }

        return false;
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
            if (!cortable.estaCortado() && zonaCorte.overlaps(cortable.getZona())) {
                cortable.setCortado(true);
            }
        }
    }

    public void reiniciar() {
        cappuccino.reaparecer(elementos.getSpawnX(), elementos.getSpawnY());
        ballerina.reaparecer(elementos.getSpawnBallerinaX(), elementos.getSpawnBallerinaY());

        ballerina.reiniciarHabilidad();

        Puerta puerta = elementos.getPuerta();

        if (puerta != null) {
            puerta.setAbierta(false);
        }

        Palanca palCap = elementos.getPalancaCappuccino();
        Palanca palBal = elementos.getPalancaBallerina();
        if (palCap != null && palCap.estaActivada()) palCap.alternar();
        if (palBal != null && palBal.estaActivada()) palBal.alternar();

        for (ObjetoCortable cortable : elementos.getCortables()) {
            cortable.setCortado(false);
        }

        entrada.soltarTodo();
        animaciones.reiniciar();
        hud.reiniciar();
    }
}
