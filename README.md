# Directory Lookup Plugin for NearVanilla

![Near Vanilla Logo](https://github.com/bran-hardy/DirectoryLookupPlugin/blob/master/src/main/resources/icon.png)

The Directory Lookup Plugin is a custom plugin designed for the NearVanilla Minecraft server. It connects directly to the server’s Notion database, giving players up-to-date information on shops and their inventories.

### How to Use:
Type `/shop [item]` in-game to get a list of shops selling the specified item along with their location at spawn. This plugin makes it easy for players to find the resources they need quickly without taking them away from the minecraft server.

---

### Todo

- Store a list of shops in a cache for a set amount of time to avoid repetative query calls when the databaase most likely hasn't changed.
	- Possibly check the "last_edited_time" to see if anything has changed, and query the database when necessary.

### Future Development

- **Additions**
	- Nether Tunnel Lookup
	- Public Farm Lookup
	- Adding/Editing Shop info In-Game
