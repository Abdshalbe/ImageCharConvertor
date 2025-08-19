# 🎨 ASCII Art Generator

This project converts images into ASCII art using Java.  
It provides multiple output options (console, HTML) and allows users to configure resolution, rounding strategies, and input methods.

---

## 📌 Features
- 🖼️ Convert images into ASCII art.  
- 🎛️ Adjustable resolution for fine-tuning output quality.  
- 🔤 Customizable character sets for rendering.  
- ↔️ Multiple rounding strategies (`ceil`, `floor`, `absolute`) for brightness mapping.  
- ⌨️ Interactive shell with keyboard commands.  
- 📄 Output options:
  - Console (text-based).
  - HTML (styled ASCII art).

---

## 📂 Project Structure
```
src/
 ├── ascii_art/               # Core ASCII art algorithm & shell
 ├── ascii_output/            # Output options (Console, HTML, Factory)
 ├── commands/                # User command handling
 ├── image/                   # Image loading & processing
 ├── image_char_matching/     # Matching pixels to ASCII chars
 │    └── RoundingStratgie/   # Different rounding strategies
 └── Main.java                # Entry point
```

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/Abdshalbe/ImageCharConvertor.git
cd ImageCharConvertor
```

### 2. Compile the project
```bash
javac -d bin $(find src -name "*.java")
```

### 3. Run the program
```bash
java -cp bin Main
```

---

## 🖥️ Usage
Once running, you’ll enter an interactive shell. Available commands include:
- `chars` → Show current character set.  
- `add <char>` → Add a character.  
- `remove <char>` → Remove a character.  
- `res up / res down` → Change resolution.  
- `output console` → Set output to console.  
- `output html` → Set output to HTML.  
- `render` → Generate ASCII art.  
- `exit` → Quit the program.  
---

## 🛠️ Technologies
- **Java** (OOP principles, modular design).  
- **Factory Pattern** (for outputs and rounding strategies).  
- **Command Pattern** (for shell commands).  

---

## 📖 Future Enhancements
- Add support for colored ASCII art.  
- Support for live camera input.  
- Advanced image pre-processing (contrast, brightness).  
- More export options (PDF, Markdown).  

---

## 👤 Author
Developed by **[Abd Shalbe](https://github.com/Abdshalbe)**  
