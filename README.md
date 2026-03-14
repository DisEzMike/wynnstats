# Wynnstats

A client-side Fabric mod for Minecraft 1.21.11 that exports [Wynncraft](https://wynncraft.com) player stats, ability trees, and gear data into a JSON file.

> **⚠️ BETA VERSION** - This is an early beta release (v0.1.0). Expect bugs and breaking changes. Please report issues on [GitHub](https://github.com/disezmike/wynnstats).

## Features

- Save equipped gear information (hotbar, armor, accessories)
- Automatic gear data lookup from Wynncraft API
- Output data in standardized JSON format
- Local item cache for offline gear lookups

## Planned Features

- Ability tree export
- Advanced stat export

## Requirements

- **Minecraft**: 1.21.11
- **Fabric Loader**: 0.18.4 or newer
- **Java**: 21 or newer
- **Fabric API**: Latest version for 1.21.11

## Commands

- `/wynnstats` - Exports current player gear to `wynnstats_output.json` in the Minecraft directory.

## Known Limitations (Beta)

- Data format may change in future releases
- Some advanced gear attributes may not be captured

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

*Last Updated: March 2026 - Beta v0.1.0* 