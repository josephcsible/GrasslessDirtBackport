# GrasslessDirtBackport

## Basics

### What does this mod do?
Minecraft 1.8 replaced grassless dirt with coarse dirt and made several
changes to it. This mod backports most of those changes. Specifically, here's
what it does:
- It makes grassless dirt show up as "Grassless Dirt" instead of just "Dirt"
- It adds a crafting recipe for grassless dirt (2 dirt and 2 gravel)
- It causes grassless dirt blocks to drop as themself instead of as regular dirt
- It causes hoeing grassless dirt to turn it into regular dirt
- It adds grassless dirt to the creative inventory
- It causes pick block on a grassless dirt block to pick grassless dirt

Note that not all changes were backported. In particular, it was not renamed to
coarse dirt, and the texture was not changed.

### How do I use this mod?
You need Minecraft Forge installed first. Once that's done, just drop
grasslessdirtbackport-*version*.jar in your Minecraft instance's mods/
directory.

## Development

### How do I compile this mod from source?
You need a JDK installed first. Start a command prompt or terminal in the
directory you downloaded the source to. If you're on Windows, type
`gradlew.bat build`. Otherwise, type `./gradlew build`. Once it's done, the mod
will be saved to build/libs/grasslessdirtbackport-*version*.jar.

### When I try to run this mod from my IDE, it doesn't load!
Copy the dummy.jar file into the IDE instance's mods/ directory.

### How can I contribute to this mod's development?
Send pull requests. Note that by doing so, you agree to release your
contributions under this mod's license.

## Licensing/Permissions

### What license is this released under?
It's released under the GPL v2 or later.

### Can I use this in my modpack?
Yes, even if you monetize it with adf.ly or something, and you don't need to
ask me for my permission first.
