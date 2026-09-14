# Beads Web Application

A web front-end for the **Beads** project — a small design tool for planning bead-weaving patterns (e.g. peyote/brick or square stitch beadwork). It lets you set a grid size and stitch type, pick colors, and paint the pattern cell by cell in the browser.

This project was built for the **Web Applikationen** course at **HTWG Konstanz**. It wraps the existing `de.htwg.se.beads` core (model/controller logic, packaged as [`lib/htwg-scala-seed.jar`](lib/htwg-scala-seed.jar), originally built with a Swing/TUI front-end in the accompanying Software Engineering project Beads https://github.com/ma981son/Beads_SS2024) with a browser-based UI built on the [Play Framework](https://www.playframework.com/).

## Features

- Create a new, empty bead template with a chosen size and stitch (**Square** or **Brick**).
- Resize the template (rows/columns) on the fly.
- Pick a color and click individual beads to color them, or fill the whole template at once.
- Live template state kept in sync between server and browser:
  - Initial state and reloads are fetched via AJAX (`GET /json`).
  - Server-side changes (e.g. from another client) are pushed to the browser over a WebSocket (`/websocket`).
- Export/download the current pattern as JSON.
- A simple "About" page describing the project.

## Tech stack

- **Backend:** Scala 2.13 + [Play Framework](https://www.playframework.com/) 2.8 (Twirl templates, Akka Streams/Actors for the WebSocket)
- **Core logic:** `de.htwg.se.beads` — the model/controller from the Beads Software Engineering project, consumed as a prebuilt JAR (`lib/htwg-scala-seed.jar`)
- **Frontend:** Vue 2, jQuery, Bootstrap 4 (loaded via CDN, plus local copies under `public/javascripts`)
- **Build tools:** sbt 1.6, giter8, sbt-less
- **Tests:** ScalaTest / scalatestplus-play

## Project structure

```
app/
  controllers/
    BeadsController.scala   # Routes, template CRUD, JSON export, WebSocket actor
    Color.scala             # java.awt.Color <-> RGB string helper
  filters/
    ExampleFilter.scala     # Sample Play filter (adds a response header)
  views/
    beads.scala.html        # Main design page (grid, stitch/size controls, color picker)
    index.scala.html        # About page
    main.scala.html         # Shared page layout (head, scripts, styles)
  assets/stylesheets/       # LESS/CSS sources compiled by sbt-less

conf/
  routes                    # URL -> controller action mappings
  application.conf          # Play configuration (CORS, CSP, i18n, ...)

public/
  javascripts/beads.js      # Grid state, AJAX calls, bead click handling, WebSocket client
  javascripts/beadsVue.js   # Vue component rendering the bead grid
  stylesheets/beads.css     # Page styling
  images/                   # Decorative pattern images used in the UI

lib/htwg-scala-seed.jar     # Prebuilt Beads model/controller (de.htwg.se.beads.*)
test/controllers/           # ScalaTest specs (functional + browser tests)
```

## Routes

| Method | Path                          | Action                                   |
|--------|-------------------------------|-------------------------------------------|
| GET    | `/`                            | Render the bead design page               |
| GET    | `/new`                         | Create a new empty template               |
| GET    | `/stitch/Square`                | Create a new template with Square stitch  |
| GET    | `/stitch/Brick`                 | Create a new template with Brick stitch   |
| GET    | `/resize/:length/:width`        | Resize the current template               |
| GET    | `/set/:row/:col/:r/:g/:b`       | Set a bead's color (RGB)                  |
| GET    | `/about`                       | About page                                |
| GET    | `/json`                        | Current template as JSON                  |
| GET    | `/websocket`                    | WebSocket endpoint for live updates       |
| GET    | `/assets/*file`                | Static assets                             |

See [`conf/routes`](conf/routes) for the authoritative list.

## Getting started

### Prerequisites

- JDK 8 or 11
- [sbt](https://www.scala-sbt.org/) 1.6+
- Node.js/npm (optional — only needed if you want to manage the `vue`/`jquery` packages in `package.json`; the app itself loads its JS from `public/javascripts` and CDNs)

### Run in development

```bash
sbt run
```

Then open [http://localhost:9000](http://localhost:9000) in your browser. Play will compile the app on first request.

### Run the tests

```bash
sbt test
```

This runs the functional and browser specs under `test/controllers`.

### Build a distributable package

```bash
sbt dist
```

## Notes for the course

- The Beads domain logic (template/stitch/color model, the observable `ControllerInterface`) lives in a separate project and is only referenced here as a compiled JAR — this repository only adds the Play web layer on top of it.
- The WebSocket actor listens to the shared `beadController` and pushes the updated JSON to connected clients whenever the template, its size, or a bead changes (`TemplateChanged`, `TemplateSizeChanged`, `BeadChanged` events).
- CORS and Content-Security-Policy settings for the app are configured in [`conf/application.conf`](conf/application.conf).
