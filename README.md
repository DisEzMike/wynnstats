# Wynnstats

A client-side Fabric mod for Minecraft 1.21.11 that exports equipped [Wynncraft](https://wynncraft.com) gear and parsed item stats into a JSON file.

## Features

- Export equipped build slots (main hand, armor, accessories)
- Parse lore into structured item stats (base stats, identifications, major IDs, tier)
- Save export output as pretty-printed JSON at `exports/wynnstats_export.json`

## Planned Features

- Ability tree export
- Advanced stat export

## Requirements

- **Minecraft**: 1.21.11
- **Fabric Loader**: 0.18.4 or newer
- **Java**: 21 or newer
- **Fabric API**: Latest version for 1.21.11

## Commands

- `/wynnstats` - Exports current player build data to `exports/wynnstats_export.json` in the Minecraft directory.

## Development

### Building

```bash
./gradlew build
```

### Running Development Client

```bash
./gradlew runClient
```

## Contributing

Found a bug? Have a feature request? Please open an issue on [GitHub](https://github.com/disezmike/wynnstats).

## License

This project is licensed under CC0-1.0 - See LICENSE file for details.

---

*Last Updated: April 2026 - v0.2.1* 