# Capuccino Loves Ballerina

## Integrantes del grupo

- Guillermo Bordoli
- Agustín Bruno
- Santino Fierro

## Descripción del videojuego

Capuccino Loves Ballerina es un videojuego cooperativo de plataformas y puzles en 2D para PC, inspirado en las mecánicas principales de Fireboy and Watergirl.

Cada jugador controlará desde una computadora diferente a Cappuccino Assassino o Ballerina Capuccina. Ambos personajes deberán colaborar para superar obstáculos, activar mecanismos, resolver puzles y escapar de una estructura misteriosa mediante el uso coordinado de sus habilidades especiales.

## Tecnologías principales

- Java JDK 17
- LibGDX 1.14.1
- Gradle
- IntelliJ IDEA
- Git y GitHub
- Plataforma objetivo: escritorio mediante LWJGL3

## Wiki del proyecto

La propuesta formal y detallada del videojuego se encuentra en la Wiki:

[Ver la propuesta del proyecto](https://github.com/agustinbruno07/capuccino-loves-ballerina/wiki)

## Compilación y ejecución

### Requisitos

Para compilar y ejecutar el proyecto es necesario contar con:

- Java JDK 17.
- Git.
- IntelliJ IDEA o cualquier entorno compatible con proyectos Gradle.

### Clonar el repositorio

Abrir una terminal y ejecutar:

```bash
git clone https://github.com/agustinbruno07/capuccino-loves-ballerina.git
```

### Ingresar a la carpeta del proyecto

Una vez clonado el repositorio, ejecutar:

```bash
cd capuccino-loves-ballerina
```

### Ejecutar en Windows

Desde la carpeta raíz del proyecto, ejecutar:

```bash
gradlew.bat lwjgl3:run
```

Si se utiliza Git Bash, también se puede ejecutar:

```bash
./gradlew.bat lwjgl3:run
```

### Ejecutar en Linux o macOS

Desde la carpeta raíz del proyecto, ejecutar:

```bash
./gradlew lwjgl3:run
```

### Ejecutar desde IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar `Open`.
3. Buscar y abrir la carpeta `capuccino-loves-ballerina`.
4. Esperar a que Gradle termine de importar el proyecto y descargar las dependencias.
5. Abrir el módulo `lwjgl3`.
6. Buscar la clase `Lwjgl3Launcher.java`.
7. Ejecutar el método `main` de `Lwjgl3Launcher`.

## Video de demostración (segunda pre-entrega)
[▶ Ver video de demostración](https://drive.google.com/file/d/1RYrjh4mWFUnnSkVwSDYIL4kscPV1n7NP/view?usp=drive_link)

## Estado actual del proyecto (v1.4.0)
Prototipo jugable del nivel 0 en escritorio con:
- Movimiento, salto, gravedad y colisiones contra el mapa de Tiled.
- Animaciones de Cappuccino Assassino (idle/caminar/saltar/atacar) desde spritesheet.
- Mecánica cooperativa de palanca y puerta + corte de katana (F) sobre lianas.
- Zonas de peligro (pinchos) y caída al vacío con GAME OVER y reinicio.
- Condición de victoria al interactuar con la puerta abierta.
- Pantallas: menú principal, opciones, juego, pausa, derrota y victoria.
- HUD con cronómetro de partida.
- Música de fondo por pantalla (menú y juego) con volumen configurable.
- Arte real de objetos (puerta, palanca, pinchos, liana) y niveles diseñados en Tiled.
