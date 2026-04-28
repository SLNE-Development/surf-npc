package dev.slne.surf.npc.paper.command.sub

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.paper.command.argument.rotationTypeArgument
import dev.slne.surf.npc.paper.controller.npcController
import dev.slne.surf.npc.paper.plugin
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.npc.paper.util.skinDataFromName
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.EntityType

fun CommandAPICommand.npcCreateCommand() = subcommand("create") {
    withPermission(PermissionRegistry.COMMAND_NPC_CREATE)
    textArgument("name")
    stringArgument("uniqueName")
    rotationTypeArgument("rotationType")
    entityTypeArgument("type")
    stringArgument("optionalSkin", optional = true)
    playerExecutor { player, args ->
        val name: String by args
        val uniqueName: String by args
        val rotationType: NpcRotationType by args
        val type: EntityType by args
        val optionalSkin: String? by args
        val location = player.location

        if (!isValidName(name)) {
            player.sendText {
                appendErrorPrefix()
                error("Der Npc Name ist ungültig.")
            }
            return@playerExecutor
        }

        if (npcController.npcs.any { it.uniqueName == name }) {
            player.sendText {
                appendErrorPrefix()
                error("Ein Npc mit diesem Namen existiert bereits.")
            }
            return@playerExecutor
        }

        player.sendText {
            appendInfoPrefix()
            info("Der Npc wird erstellt. Dies kann einen Moment dauern...")
        }

        val parsedName = MiniMessage.miniMessage().deserialize(name)

        plugin.launch {
            val skinData = optionalSkin?.let { skinDataFromName(it) } ?: NpcSkin.empty()
            npcController.createNpc(
                displayName = parsedName,
                uniqueName = uniqueName,
                location = location,
                skin = skinData,
                rotationType = rotationType,
                persistent = true,
                type = type
            )

            player.sendText {
                appendSuccessPrefix()
                success("Der Npc wurde erfolgreich erstellt.")
            }
        }
    }
}

private fun isValidName(name: String): Boolean {
    return name.isNotBlank() && (name.length > 1 || !name[0].isDigit())
}