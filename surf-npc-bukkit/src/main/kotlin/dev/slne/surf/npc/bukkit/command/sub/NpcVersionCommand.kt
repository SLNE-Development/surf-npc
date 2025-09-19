package dev.slne.surf.npc.bukkit.command.sub

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.service.versionService
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcVersionCommand() = subcommand("version") {
    withPermission(PermissionRegistry.COMMAND_NPC_VERSION)
    anyExecutor { executor, _ ->
        plugin.launch {
            executor.sendText {
                appendPrefix()
                if (versionService.isUpToDate()) info("Das Plugin ist up-to-date.") else variableKey(
                    "Es gibt eine neuere Version."
                )

                versionService.currentVersion.let {
                    appendNewline {
                        appendPrefix()
                        variableKey("Aktuelle Version: ")
                        variableValue(it.toString())
                    }
                }

                versionService.latestVersion.let {
                    appendNewline {
                        appendPrefix()
                        variableKey("Neueste Version: ")
                        variableValue(it.toString())
                    }
                }

                appendNewline {
                    appendPrefix()
                    info("Neuste Version herunterladen: ")
                    variableKey("[DOWNLOAD]")
                    clickOpensUrl(
                        versionService.link
                            ?: "http://github.com/SLNE-Development/surf-npc/releases/latest"
                    )
                }
            }
        }
    }
}