<p align="center">
  <img src="icon.png" alt="AdvancedAFK logo" width="128" style="border-radius: 24px;" />
</p>

# AdvancedAFK 🛡️

> **The ultimate, feature-rich AFK protection and automation plugin engineered specifically for Paper Minecraft servers.**

[![Modrinth](https://img.shields.io/badge/Modrinth-Download-green?style=flat-square&logo=modrinth&logoColor=white)](https://modrinth.com/plugin/advancedafk)
[![bStats](https://img.shields.io/bstats/servers/28448?style=flat-square)](https://bstats.org/plugin/bukkit/AdvancedAFK/28448)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)
[![Platform: Paper](https://img.shields.io/badge/Platform-Paper%20%2F%20Spigot-blue?style=flat-square)](#)

AdvancedAFK provides highly configurable, performant, and secure idle player management. Safeguard your server's economies, performance, and player bases with customizable triggers, god mode protection, automatic kicks, and native integration.

---

## ✨ Features

- **🛡️ God Mode Protection:** Fully shields AFK players from damage, mob target acquisition, drowning, and environmental hazards.
- **⏰ Smart Auto-AFK & Kick:** Configurable inactivity timer with optional auto-kick triggers when the server reaches designated player counts.
- **🌍 Multi-Language Native:** Seamless localization supporting English and Turkish with machine-translation fallbacks for 12 other languages.
- **⚙️ Granular Exit Triggers:** Complete administrative control over what physical player actions exit the AFK status.
- **🏷️ Interactive Tab List:** Displays a visible `[AFK]` tag prefix natively in the server tab list without conflicting with existing prefix managers.
- **📊 PlaceholderAPI Support:** Exposes dynamic user states, timers, and reasons to other plugins.
- **📈 bStats Integration:** Safe, anonymous usage diagnostics reporting server metrics.

---

## 📋 Commands & Keybinds

| Command | Description | Permission | Default |
|---------|-------------|------------|---------|
| `/afk [reason]` | Toggle AFK status with an optional custom message. | `advancedafk.use` | Everyone |
| `/afkreload` | Instantly reload configuration keys and translation properties. | `advancedafk.reload` | OP |

---

## 🔑 Permissions

- **`advancedafk.use`:** Grants permission to manual toggles using `/afk`. *(Default: True)*
- **`advancedafk.reload`:** Permits reload commands via `/afkreload`. *(Default: OP)*
- **`advancedafk.bypass.cooldown`:** Bypasses cooldown timers set on `/afk` execution. *(Default: OP)*
- **`advancedafk.bypass.kick`:** Prevents idle players from being disconnected by the auto-kick manager. *(Default: OP)*

---

## 📊 PlaceholderAPI Placeholders

If your server runs [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/), you can query these keys:

| Placeholder | Resolved Output |
|-------------|-----------------|
| `%advancedafk_status%` | Returns `"AFK"` if user is idle, otherwise empty. |
| `%advancedafk_status_formatted%` | Returns `"[AFK]"` if user is idle, otherwise empty. |
| `%advancedafk_time%` | Human-readable duration string (e.g., `5m 30s`). |
| `%advancedafk_time_minutes%` | Total idle time rounded in minutes. |
| `%advancedafk_time_seconds%` | Total idle time rounded in seconds. |
| `%advancedafk_reason%` | Custom idle reason if provided, otherwise empty. |
| `%advancedafk_is_afk%` | Returns `"true"` or `"false"`. |

---

## ⚙️ Configuration (`config.yml`)

### Inactivity Management
```yaml
auto-afk:
  enabled: true
  timeout-minutes: 5      # Inactivity time before setting AFK state

auto-kick:
  enabled: false          # Safe toggle, disabled by default
  timeout-minutes: 30     # AFK time before disconnection
  min-players-online: 0   # Only kick if online player count is above this threshold
```

### Inactivity Exit Triggers
Toggle which standard client-side packets should break AFK states:
```yaml
triggers:
  mouse-movement: true   # Looking around/rotating head
  sneak: true            # Toggling sneak (Shift)
  interact: true         # Block clicks or entity interaction
  chat: true             # Sending a chat packet
  command: true          # Running commands (excluding /afk)
  inventory: true        # Interacting with GUI inventories
```

### Safety & God Mode Controls
```yaml
protection:
  enabled: true            # Master toggle for AFK protection
  prevent-damage: true     # Negates damage packets completely
  prevent-knockback: true  # Blocks physical knockbacks
  lock-position: true      # Prevents being pushed by water, pistons, or players
```

---

## 🌍 Language Customization

To apply a language configuration, update the property inside `config.yml`:
```yaml
language: en_US  # e.g., tr_TR for Turkish
```
Create custom translation bundles by duplicating `lang/en_US.yml` to `lang/YOUR_LOCALE.yml`. 
*Supported machine-translated locales:* German, French, Spanish, Portuguese, Russian, Chinese, Japanese, Korean, Italian, Dutch, Polish, Ukrainian.

---

## 📥 Quick Start Installation

1. Grab the latest plugin `.jar` build from [Modrinth](https://modrinth.com/plugin/advancedafk).
2. Move the package file into your server's `plugins/` directory.
3. Restart your server instance to generate configurations.
4. Customize `plugins/AdvancedAFK/config.yml` and run `/afkreload`.

---

## 📄 License

Licensed under the terms of the MIT License. See [LICENSE](LICENSE) for more details.
