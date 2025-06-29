# 🔷 Java 3D Wireframe Renderer

A custom 3D wireframe renderer built from scratch in Java featuring:

- 📦 OBJ model loading (triangles + quads)
- 🧠 Hidden-line removal using plane-based midpoint depth comparison
- 🌀 Orbiting camera that always looks at the model
- 📐 Perspective projection with adjustable focal length
- 🔲 Model auto-centering and scaling to fit the frame

---

## 📸 Screenshot

*(Insert image here after rendering your model)*

---

## 📂 File Structure

```
📦 Renderer/
 ┣ 📜 Display.java              ← main loop and ticking setup
 ┣ 📜 VectorMath.java        ← vector, matrix, and plane utilities
 ┣ 📜 ObjModelLoader.java    ← parses .obj files into vertex/face/edge lists
 ┣ 📜 Render3D.java          ← contains the logic of converting 3d coordinates to 2D and many other testing patterns.
 ┣ 📜 Renderer.java              ← pixel level writing logic
 ┣ 📜 Game.java              ← Unused but can be used for ticking and game logics //more support later (if possible)
📜 male.obj            ← place any .obj model here
┗ 📜 README.md
```

---

## 🚀 How to Run

### ✅ Requirements
- Java 8 or higher
- `.obj` file placed in `/assets`

### ✅ Build and Run
```bash
javac *.java
java Main
```

Or use any Java IDE like IntelliJ or Eclipse to run `Display.java`.

---

## 📁 Supported `.obj` Format

- Supports only `v` and `f` lines
- Faces with 4 vertices (quads) are auto-triangulated
- Model is centered and uniformly scaled to fit the screen

---

## 🎮 Features

| Feature              | Description                                         |
|----------------------|-----------------------------------------------------|
| OBJ loading          | Parses `.obj` files with `v` and `f` lines          |
| Camera orbit         | Camera moves in a circle around the origin          |
| Look-at origin       | Camera always faces the model                       |
| Hidden-line removal  | Midpoint Z-depth test against triangle planes       |
| Perspective projection | Configurable focal length                         |
| Center + Scale model | Automatically fits model in frame                   |
| Real-time rendering  | ~30 FPS depending on model complexity               |

---

## 🛠️ Controls

| Action        | How                      |
|---------------|---------------------------|
| Load model    | Place it outside the renderer folder in .obj format and copy the relative path to Screen.java in the Objmodelloader|
| Camera orbit  | Auto-rotates per frame     |
| Visibility    | Hidden lines auto skipped  |

---

## 📌 How Visibility Works

- Each edge's midpoint is projected to 2D.
- Z-depth is calculated against **all triangle planes**.
- Line is drawn **only if** the closest surface is one of the edge's native faces.

---

## 🧠 Tips

- Use a model under 5k faces for smooth performance
- You can export `.obj` models from Blender, Sketchfab, etc.
- FOV & camera distance can be adjusted via `focalLength` and `radius`

---

## 🤖 Credits

Built from scratch in Java with ❤️  
Developed by Swarit Srivastava

---

## 📋 TODO

- [ ] Z-buffer-based triangle rasterizer
- [ ] Per-triangle shading
- [ ] Mouse-controlled camera
- [ ] Screenshot exporter
- [ ] Animation support
