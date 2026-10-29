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


        actualizarPalanca();

        if (estaMuerto(cappuccino) || estaMuerto(ballerina)) {
            mostrarGameOver();
            return;
        }


        comprobarVictoria();

        if (menuVictoria.estaVisible()) {
            return; // Ganamos, frenamos todo lo demás
        }

        entrada.limpiarEventos();
    }

    private void actualizarPalanca() {
        Palanca palanca = elementos.getPalanca();
        Puerta puerta = elementos.getPuerta();

        if (palanca == null) {
            return;
        }

        Rectangle cajaCap = cappuccino.getCaja();
        Rectangle cajaBal = ballerina.getCaja();

        Rectangle alcanceCap = new Rectangle(cajaCap.x - 12, cajaCap.y - 12, cajaCap.width + 24, cajaCap.height + 24);
        Rectangle alcanceBal = new Rectangle(cajaBal.x - 12, cajaBal.y - 12, cajaBal.width + 24, cajaBal.height + 24);

        boolean capCercaDePalanca = alcanceCap.overlaps(palanca.getZona());
        boolean balCercaDePalanca = alcanceBal.overlaps(palanca.getZona());

        if (!capCercaDePalanca && !balCercaDePalanca) {
            return;
        }

        boolean capActivo = capCercaDePalanca && entrada.isInteractuarPresionado();      // E
        boolean balActivo = balCercaDePalanca && entrada.isInteractuarPresionadoBallerina(); // Enter

        if (!capActivo && !balActivo) {
            return;
        }

        boolean alguienEnLaPuerta = puerta != null
            && (cajaCap.overlaps(puerta.getZona()) || cajaBal.overlaps(puerta.getZona()));

        boolean vaACerrar = palanca.estaActivada();

        if (!vaACerrar || !alguienEnLaPuerta) {
            palanca.alternar();
            if (puerta != null) {
                puerta.setAbierta(palanca.estaActivada());
            }
        }
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

        for (Rectangle peligro : elementos.getPeligros()) {
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
