# FlappyBird

Controla a un pájaro que debe volar entre tubos sin chocar con ellos.  
El objetivo es superar el mayor número de obstáculos posible para alcanzar la puntuación más alta.  
Cada vez que el jugador presiona la **barra espaciadora** o hace **clic con el mouse**, el pájaro salta hacia arriba; la gravedad lo hace descender constantemente, por lo que deberás mantener el ritmo para no caer ni golpear los tubos.  
¡Pon a prueba tus reflejos y mira hasta dónde puedes llegar!

---

## Instrucciones de instalación

Para instalar este juego, primero descarga el archivo `.zip` del repositorio.  
Después de haberlo descargado, descomprímelo y abre la carpeta del proyecto en tu IDE de preferencia (**IntelliJ IDEA** o **Eclipse**).

Una vez dentro del IDE, asegúrate de tener configurado el **JDK 11 o superior**, y ejecuta la clase principal siguiendo esta ruta:

lwjgl3/src/main/java/puppy/code/lwjgl3/Lwjgl3Launcher.java

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
