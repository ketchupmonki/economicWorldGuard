# economicWorldGuard
## Description
The economicWorldGuard allows players to protect their own land on a Minecraft server by purchasing the land on a chunk by chunk basis using in game currency.

It depends on both the [Vault](https://github.com/MilkBowl/Vault/) and [WorldGuard](https://github.com/sk89q/worldguard) plugins.

## Command Information
Command | Permission | Description
--------|------------|------------
/buychunk | economicworldguard.user | Purchase the chunk you're currently standing in. (Price set in config file.)
/sellchunk | economicworldguard.user | Sell the chunk you're currently standing in. (Price set in config file.)
/sharechunk [player] | economicworldguard.user | If you're standing in a chunk you own use this command to allow [player] to build in it.
/restrictchunk [player] | economicworldguard.user | If you're standing in a chunk you own use this command to stop [player] to build in it. (Only needed with players you previously shared a chunk with.)
/givechunk [player] | economicworldguard.user | Give the chunk you're standing in to [player].
/evictchunk | economicworldguard.admin | Admin command to clear any economicWorldGuard chunk currently stood in.

## Config
Field | Default | Description
--------|------------|------------
chunkBuyPrice | 250 | The price that players can buy a chunk for.
chunkSellPrice | 125 | The price that players can sell a chunk for.
debug | false | Print information about command usage in the server logs.
ignoreRegions | | Allows server admins to create a list of existing regions that players can create their own regions inside of. Regions should be listed one line at a time, following a '-' at the start of the line. See [YML specs](http://www.yaml.org/spec/1.2/spec.html#id2802432) for an example.

## Information for Server Administrators
When a player chunk is created the plugin will automatically set 'economicWorldGuard' as its parent region within WorldGuard. This is to allow you to set flags on a region named 'economicWorldGuard' that will affect all regions made within economicWorldGuard.

You will have to create the region named 'economicWorldGuard' yourself to keep this control. Player regions do not need to be located within the 'economicWorldGuard' region so don't worry about your region covering a large area, it need not span more than one block.

Please report any issues or feature requests regarding the plugin [here on Github](/issues), remembering to set the appropriate labels.

## Developers
If you have suggestions, or would like to contribute something of your own to this plugin you are very welcome to do so. You can reach me on the [economicWorldGuard-dev Discord channel](https://discord.gg/e2de8Sn).

#### Building
If you would like to make a build of this plugin yourself make sure you reference craftbukkit-#.jar, Vault.jar, worldedit*.jar and worldguard-#.jar as libraries.
