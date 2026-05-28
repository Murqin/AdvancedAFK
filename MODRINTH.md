# AdvancedAFK 🛡️

> **The ultimate AFK plugin for Paper servers** — featuring god mode protection, auto-kick, multi-language support, and fully configurable exit triggers.

[![Modrinth](https://img.shields.io/badge/Modrinth-Download-green?style=flat-square&logo=modrinth&logoColor=white)](https://modrinth.com/plugin/advancedafk)
[![bStats](https://img.shields.io/bstats/servers/28448?style=flat-square)](https://bstats.org/plugin/bukkit/AdvancedAFK/28448)

---

## ✨ Features

### 🛡️ God Mode Protection
AFK players are completely protected:
- **No damage** from any source (mobs, players, environment).
- **No knockback** from attacks or explosions.
- **Position lock** — cannot be pushed by entities, pistons, or water flows.

### ⏰ Auto-AFK & Auto-Kick
- **Auto-AFK:** Automatically set players AFK after inactivity.
- **Auto-Kick:** Kick players who are AFK too long (disabled by default).
- **Smart Kick:** Only kick when server reaches minimum player threshold.

### ⚙️ Configurable Exit Triggers
Choose exactly what actions should remove AFK status:
- Mouse movement (looking around).
- Sneaking (pressing shift).
- Interacting (clicking blocks or entities).
- Chatting.
- Running commands.
- Opening inventory UI.

*Each trigger can be individually enabled or disabled!*

### 🌍 Multi-Language Support
- Built-in **English** and **Turkish** languages.
- **12 machine translations** available on GitHub.
- Easily add your own translations.
- All messages are fully customizable.

### 📊 PlaceholderAPI Integration
Display AFK information anywhere using PlaceholderAPI:
- `%advancedafk_status%` — "AFK" or empty.
- `%advancedafk_time%` — Formatted duration.
- `%advancedafk_reason%` — AFK reason.
- `%advancedafk_is_afk%` — "true" or "false".
- And more!

### 🏷️ Tab List Support
Shows `[AFK]` prefix in the player list **without breaking other plugins' formatting**.

### 📈 bStats Analytics
Anonymous usage statistics to help improve the plugin.

---

## 📋 Commands

| Command | Description |
|---------|-------------|
| `/afk [reason]` | Toggle AFK mode with optional reason. |
| `/afkreload` | Reload configuration and language files. |

---

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `advancedafk.use` | Use /afk command. | Everyone |
| `advancedafk.reload` | Use /afkreload. | OP |
| `advancedafk.bypass.cooldown` | Bypass cooldown. | OP |
| `advancedafk.bypass.kick` | Bypass auto-kick. | OP |

---

## ⚡ Requirements

- **Paper 26.1+** (or compatible fork).
- **Java 25+**.
- **PlaceholderAPI** (optional, for placeholders).

---

## 📥 Quick Start

1. Download and place in your `plugins/` folder.
2. Restart the server.
3. Use `/afk` to toggle AFK mode!

Customize everything in `plugins/AdvancedAFK/config.yml`
