# 🎮 MiniGames Plugin

A lightweight minigames plugin.  
This plugin is designed for lobbies, allowing players to play with others when they're bored.

---

## 🚀 Features

- Plug-and-play.
- Includes minigames.
- Inventory interfaces via Inventory Framework.
- Advanced command structure through CommandAPI.
- Paper-compatible.
- LuckPerms support.

---

## 📜 Commands

| Command                       | Description                       |
|-------------------------------|-----------------------------------|
| `/minigames`                  | Opens the main menu.              |
| `/minigames <game> <player>`  | Sends a game request to a player. |
| `/minigames accept <player>`  | Accepts a game request.           |
| `/minigames decline <player>` | Declines a game request.          |
| `/minigames help`             | Shows all available commands.     |

---

## 🍀 LuckPerms

| Permission               | Description                              |
|--------------------------|------------------------------------------|
| `minigamesv4.minigames`  | Grants access to all minigame commands.  |
| `minigamesv4.admin`      | Grants access to admin-related commands. |
| `minigamesv4.autotoggle` | Automatically toggles invite settings.   |
| `minigamesv4.*`          | Grants access to all commands.           |

---

## 🕹️ MiniGames

| Game                | Description                                                                        |
|---------------------|------------------------------------------------------------------------------------|
| TicTacToe           | Play in a clean, intuitive GUI with a built-in randomizer to decide who starts.    |
| Rock Paper Scissors | Play against a friend – just type your move. The game listens. No commands needed. |

## 🧩 Dependencies

This plugin uses the following APIs:

- [Inventory Framework](https://github.com/stefvanschie/IF) – tested with **v0.10.19**
- [CommandAPI](https://github.com/CommandAPI/CommandAPI) – tested with **v10.0.0**
- [PaperMC](https://papermc.io/) – tested with **Paper 1.21.4**
- Java **21+** required.

---

## 📦 Plugin Metadata

This plugin uses the [`plugin-yml.paper`](https://docs.eldoria.de/pluginyml/paper/) Gradle-DSL to automatically generate
the `paper-plugin.yml` at build time.

Example configuration in `build.gradle.kts`:

```kotlin
paper {
    main = "de.mikeyllp.miniGamesV4.MiniGamesV4"
    apiVersion = "1.21"
    serverDependencies {
        register("CommandAPI") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}
```

This method avoids YAML files in the repo and ensures type-safe, automatic generation during each build.

---

## ⚠️ Development Status Notes

> This version (`v0.2.1-beta`) is an **Beta release**.  
> Features are unstable and will likely change before the final release.

---

## 🛠️ Installation

✨Coming soon✨

---

## 🧰 Build (for developers)

Build the plugin as a Shadow FatJar with:

```bash
./gradlew shadowJar
```

The resulting file will be located at `build/libs/MiniGamesV4-0.2.1-beta-all.jar`.

---

## 🪪 License

### 📦 Own Plugin

This project is licensed under the **GNU General Public License v3.0**.  
See the [`LICENSE`](LICENSE) file for full license details.

### 📚 Third-party Licenses

This plugin uses third-party libraries:

- **CommandAPI** © JorelAli – MIT License  
  Lizenz: [`THIRD_PARTY_LICENSES/commandapi.txt`](./THIRD_PARTY_LICENSES/commandapi.txt)  
  Quelle: https://github.com/CommandAPI/CommandAPI

- **Inventory Framework** © stefvanschie – The Unlicense  
  Lizenz: [`THIRD_PARTY_LICENSES/inventoryframework.txt`](./THIRD_PARTY_LICENSES/inventoryframework.txt)  
  Quelle: https://github.com/stefvanschie/IF

---

## 🤝 Contributors

Thanks for testing the plugin and giving feedback! 🙌

- [Alex_mhr](https://github.com/Alex1010222)
- [Danilo888TV](https://github.com/Danilo888TV)
- [HiorCraft](https://github.com/HiorCraft)
- [KamiIIe]()
- [Laluck98]()
- [PEKK29]()
- [Pingius2031]()
- [RicTheCraft]()
- [TheBjoRedCraft](https://github.com/TheBjoRedCraft)
- [Timonso](https://github.com/Timonso-1)

---

## 📬 Feedback & Contributing

Feature requests or bugs? Feel free to create an issue or open a pull request.

---

© 2025 MikeyLLP – All rights reserved.

---

NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.
