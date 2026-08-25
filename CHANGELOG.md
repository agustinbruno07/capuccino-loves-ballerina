# Changelog

Todos los cambios importantes del proyecto serán registrados en este archivo.

## [1.2.0] - 2026-08-24 (en curso)
### Agregado    
- Corte de Katana de Cappuccino Assassino: con F corta objetos `cuerda` y `barril` de la capa `objetos` (`ObjetoCortable` + `Nivel.buscarObjetos`); mientras no están cortados bloquean el paso como sólidos y al reintentar vuelven a su estado inicial.
- Mecánica cooperativa de palanca y puerta : nuevo paquete `objetos` con `Palanca` y `Puerta`; la palanca se alterna con E estando cerca y abre/cierra la puerta; la puerta cerrada bloquea el paso como sólido adicional y no puede cerrarse con el jugador en el hueco.
- Condición de victoria: con la puerta abierta, al presionar E sobre su hueco aparece `MenuVictoria` ("NIVEL COMPLETADO" con JUGAR DE NUEVO y VOLVER AL MENU).
- Placeholders de salida , puerta  y palanca dibujados con ShapeRenderer.
- Movimiento de Cappuccino Assassino: desplazamiento horizontal (A/D), salto (W o espacio), gravedad y colisiones contra los sólidos del mapa (`Player` + `ControladorEntradaJugador`).
- Spawn del personaje leído desde la capa `objetos` de Tiled (`spawn_cappuccino`).
- Muerte por caída al vacío con pantalla de GAME OVER (`interfaz/game_over.png`) y botones REINTENTAR y VOLVER AL MENU.
- Nivel 0 diseñado en Tiled Map Editor: tileset de ladrillos, fondo de mazmorra, capa de objetos (spawns, palanca, puerta, salida) y colisiones por tile con bounding boxes.
- Nuevo paquete `mapa` con las clases `Nivel` (modelo de datos del nivel) y `GestorColisiones` (sólidos construidos desde los tiles, lógica pura reutilizable por el futuro servidor).
- `PantallaJuego` ahora carga y dibuja el mapa de Tiled (TmxMapLoader + OrthogonalTiledMapRenderer).
- Spritesheet de Cappuccino Assassino (`assets/sprites/cappuccino.png`) con animaciones de idle, caminar, saltar y atacar, armado en grilla de celdas de 128x128 con fondo transparente.
- Nuevo paquete `graficos` con la clase `animacionesCappuccino`: recorte del spritesheet con `TextureRegion.split` y animaciones con `Animation<TextureRegion>` en modo LOOP (idle/caminar) y NORMAL (saltar/atacar), con ataque que no se interrumpe hasta terminar.
- Animaciones integradas en `PantallaJuego`: maquina de estados segun input y fisica (F → atacar, en el aire → saltar, moverse → caminar, quieto → idle), dibujo con `SpriteBatch` y espejado del sprite segun la direccion de movimiento.
### Cambiado
- `reintentar()` ahora también devuelve la puerta y la palanca a su estado inicial, además de reposicionar al personaje.
- `ANCHO_MUNDO` a 1408 para que el mundo calce exacto con el mapa (22 tiles x 64 px).
- `PantallaJuego` queda como cliente flaco: delega el mapa en `mapa/Nivel`; el renderizador lo crea el cliente.
### Corregido
- Teclas quedaban "pegadas" al abrir un menú o reintentar: se agregó `soltarTodo()` a `ControladorEntradaJugador`, que resetea las teclas sostenidas cada vez que el control pasa a un menú.

## [1.1.0] 2026-08-23

### Agregado
- Menú de pausa como overlay (clase MenuPausa) con botones Reanudar, Volver al menú y Salir.
- Alternancia de la pausa con la tecla ESC en la pantalla de juego.
- Cámara fija con FitViewport en la pantalla de juego: el contenido se adapta a distintos tamaños de ventana sin deformarse.
- Entorno de prueba con plataformas en la pantalla de juego.
- Menú principal con viewport propio (FitViewport) y fondo como actor del Stage, que se adapta al redimensionar sin deformarse.
- Paquete Utilidades con tres clases nuevas:
    - Constantes: centraliza la resolución virtual y las rutas de assets.
    - FabricaViewport: estandariza la creación de viewports en todas las pantallas.
    - Recursos: gestor centralizado de skin y texturas con caché (HashMap) para evitar cargas duplicadas.
- Clase BallerinaCapuccina (esqueleto del segundo personaje jugable).

### Cambiado
- PantallaJuego ahora recibe la instancia del juego en el constructor para permitir la navegación entre pantallas.
- PantallaJuego y MenuPausa usan FabricaViewport y Constantes en lugar de valores propios.
- Resolución virtual y ventana de escritorio configuradas en 1366x768 (antes 1280x720).
- projectVersion en gradle.properties actualizado a 1.1.0 para alinearlo con este registro.

### Corregido
- Menú principal: los botones no respondían al maximizar la ventana porque resize() no actualizaba el viewport del Stage.


## [1.0.1] 2026-08-04

### Agregado
- Actualización sobre el README en el apartado de Ejecución y Compilación.
- Se cambio el nombre de la clase Principal a "CapuccinoLovesBallerinaGame".
- Creacion del Menu principal
- Fondo del menu Principal
- Botones Salir, Opciones y Jugar
- Funcionalidades de botones

## [1.0.0] 2026-07-16

### Agregado

- Creación inicial del proyecto con LibGDX.
- Configuración del módulo Core.
- Configuración de la plataforma de escritorio LWJGL3
- Incorporación del archivo .gitignore.
- Creación del repositorio en GitHub.
- Creación del README inicial.
- Creación de la Wiki con la propuesta del proyecto.
