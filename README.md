<p align="center">
  <img src="icon.png" alt="AdvancedAFK" width="128">
</p>

# AdvancedAFK

[![Modrinth](https://img.shields.io/badge/Modrinth-Download-green?logo=modrinth)](https://modrinth.com/plugin/advancedafk)
[![bStats](https://img.shields.io/bstats/servers/28448)](https://bstats.org/plugin/bukkit/AdvancedAFK/28448)

**Advanced AFK plugin for Paper servers** with god mode protection, auto-kick, multi-language support, and fully configurable triggers.

## ✨ Features

- **🛡️ God Mode Protection** - AFK players take no damage and cannot be pushed
- **⏰ Auto-AFK & Auto-Kick** - Automatic AFK detection and optional kick for long AFK
- **🌍 Multi-Language Support** - Built-in English & Turkish + 12 community translations
- **⚙️ Configurable Triggers** - Choose what actions exit AFK mode
- **📊 PlaceholderAPI Support** - Display AFK status anywhere
- **📈 bStats Analytics** - Anonymous usage statistics
- **🏷️ Tab List Prefix** - Shows [AFK] without breaking other plugins

## 📋 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/afk [reason]` | Toggle AFK mode | `advancedafk.use` |
| `/afkreload` | Reload configuration | `advancedafk.reload` |

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `advancedafk.use` | Use /afk command | Everyone |
| `advancedafk.reload` | Use /afkreload command | OP |
| `advancedafk.bypass.cooldown` | Bypass command cooldown | OP |
| `advancedafk.bypass.kick` | Bypass auto-kick | OP |

## 📊 PlaceholderAPI

If you have [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) installed, you can use these placeholders:

| Placeholder | Description |
|-------------|-------------|
| `%advancedafk_status%` | Returns "AFK" if AFK, empty otherwise |
| `%advancedafk_status_formatted%` | Returns "[AFK]" if AFK, empty otherwise |
| `%advancedafk_time%` | Formatted AFK duration (e.g., "5m 30s") |
| `%advancedafk_time_minutes%` | AFK duration in minutes |
| `%advancedafk_time_seconds%` | AFK duration in seconds |
| `%advancedafk_reason%` | AFK reason if set |
| `%advancedafk_is_afk%` | "true" or "false" |

## ⚙️ Configuration

### Auto-AFK & Auto-Kick

```yaml
auto-afk:
  enabled: true
  timeout-minutes: 5      # Minutes of inactivity before auto-AFK

auto-kick:
  enabled: false          # Disabled by default
  timeout-minutes: 30     # Minutes of AFK before kick
  min-players-online: 0   # Only kick when server has this many players
```

### Exit Triggers
Control what actions should remove AFK status:

```yaml
triggers:
  mouse-movement: true   # Looking around
  sneak: true            # Pressing shift
  interact: true         # Right/left click
  chat: true             # Sending chat message
  command: true          # Running any command (except /afk)
  inventory: true        # Opening inventory
```

### Protection Settings
Configure god mode features:

```yaml
protection:
  enabled: true            # Master toggle
  prevent-damage: true     # Cancel all damage
  prevent-knockback: true  # Cancel knockback
  lock-position: true      # Prevent being pushed
```

### Multi-Language
Change the language in config.yml:

```yaml
language: en_US  # or tr_TR, or your custom language code
```

Create custom translations by copying `lang/en_US.yml` to `lang/YOUR_LANG.yml`.

**Available translations:** German, French, Spanish, Portuguese, Russian, Chinese, Japanese, Korean, Italian, Dutch, Polish, Ukrainian

## 📥 Installation

1. Download the latest JAR from [Modrinth](https://modrinth.com/plugin/advancedafk)
2. Place in your server's `plugins/` folder
3. Restart the server
4. Configure in `plugins/AdvancedAFK/config.yml`

## 📜 License

MIT License - see [LICENSE](LICENSE) file for details.

## 🐛 Issues & Suggestions

Found a bug or have a suggestion? Open an issue on [GitHub](https://github.com/murqin/AdvancedAFK/issues).
