**A very basic json merger for ActorInfo.product.sbyml files.**

_Requirements:_

* [ninten-file-tool](https://github.com/arbiter34/ninten-file-tool)
* The original unmodified ActorInfo.product.sbyml from your game files
* Two or more BotW mods you intend to merge
* An edited `settings.json` file

_Usage:_

1. Use `ninten-file-tool-with-compress.bat` to decompress the ActorInfo.product.sbyml files from the Mods you wish to merge
2. Edit the `settings.json` file. *Number the mods by priority!* Lower-numbered mods will merge later, overwriting any conflicting values from prior mods (the order in the json doesn't matter, only the actual number matters)
3. Run `merge.bat` or `java -jar actorinfomerge.jar` (or whatever the jar's filename is today)
4. Use `ninten-file-tool-with-compress.bat` on the generated `ActorInfo.product.sbyml.json` file to recompress it into `ActorInfo.product.sbyml` (On my computer this takes forever!)
5. Copy the Mods' files together, and use the merged ActorInfo file. You should know how to use mods if you've gotten this far
