package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.CommonComponents
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.clickRunsCommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import net.kyori.adventure.text.format.TextDecoration

class NpcListCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_LIST)
        integerArgument("page", optional = true)
        playerExecutor { player, args ->
            val npcs = npcController.getNpcs()
            val page = args.getOrDefaultUnchecked("page", 1)

            if (npcs.isEmpty()) {
                player.sendText {
                    appendPrefix()
                    error("Es sind keine Npcs vorhanden.")
                }
                return@playerExecutor
            }

            val pagination = Pagination<Npc> {
                title {
                    info("Npc Informationen".toSmallCaps())
                    decorate(TextDecoration.BOLD)
                }
                rowRenderer { npc, index ->
                    listOf(
                        buildText {
                            append(CommonComponents.EM_DASH)
                            appendSpace()
                            variableValue(npc.uniqueName)
                            appendSpace()
                            info("(${npc.id})")
                            clickRunsCommand("/npc info ${npc.uniqueName}")
                        }
                    )
                }
            }

            player.sendText {
                append(pagination.renderComponent(npcs, page))
            }
        }
    }
}