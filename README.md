# KillEffects

**KillEffects** is a cosmetic plugin for Minecraft servers (Paper 1.21+). It allows players to trigger visual and sound effects upon killing opponents.

## Features

*   **Action-Chain Engine:** Create complex effects with specific timing (e.g., sound at 0ms, particles at 200ms).
*   **Structural Effects:** Uses **Display Entities** (ItemDisplay/BlockDisplay) to spawn temporary graves, crowns, or magical structures without world griefing.
*   **Performance Optimized:**
    *   **Per-player Particles:** Only sent to nearby players who have effects enabled.
    *   **Fake Lightning:** Visual-only lightning that doesn't damage players or cause screen-shake for everyone.
    *   **Batch Database Saving:** Asynchronous MySQL/SQLite saving to handle massive PvP events.
    *   **Low-End PC Toggle:** Players can use `/ke toggle` to hide effects they don't want to see.
*   **Integrations:**
    *   **PlaceholderAPI:** Full support for kills, streaks, and selected effects.
*   **Modern UI:** Clean, GUI with MiniMessage (Hex color) support.

## Commands

*   `/killeffects gui` (or `/ke`) - Open the effect selection menu.
*   `/killeffects toggle` - Enable/Disable seeing effects for yourself.
*   `/killeffects reload` - Reload configurations (Admin only).

## Configuration Example

```yaml
effects:
  soul_burst:
    name: "Soul Burst"
    actions:
      0:
        sound: "ENTITY_GENERIC_EXPLODE"
        volume: 0.5
        pitch: 1.5
        particle: "SOUL"
        count: 20
        offsetX: 0.3
        offsetY: 0.3
        offsetZ: 0.3
        extra: 0.1
      200:
        particle: "SOUL_FIRE_FLAME"
        count: 30
        offsetX: 0.5
        offsetY: 0.5
        offsetZ: 0.5
        extra: 0.05
```

## Requirements

*   Java 21
*   Paper 1.21+
*   ProtocolLib (Required)
*   PlaceholderAPI (Optional)

