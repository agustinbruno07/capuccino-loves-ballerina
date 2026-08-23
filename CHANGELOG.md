# Changelog

Todos los cambios importantes del proyecto serán registrados en este archivo.

## [1.2.0] - 2026-08-25 (en curso)
### Agregado
- Nivel 0 diseñado en Tiled Map Editor: tileset de ladrillos, fondo de mazmorra, capa de objetos (spawns, palanca, puerta, salida) y colisiones por tile con bounding boxes.
- Nuevo paquete `mapa` con las clases `Nivel` (modelo de datos del nivel) y `GestorColisiones` (sólidos construidos desde los tiles, lógica pura reutilizable por el futuro servidor).
- `PantallaJuego` ahora carga y dibuja el mapa de Tiled (TmxMapLoader + OrthogonalTiledMapRenderer).
### Cambiado
- `ANCHO_MUNDO` a 1408 para que el mundo calce exacto con el mapa (22 tiles x 64 px).
- `PantallaJuego` queda como cliente flaco: delega el mapa en `mapa/Nivel`; el renderizador lo crea el cliente.


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
