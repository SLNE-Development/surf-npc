package dev.slne.surf.npc.bukkit.command.sub.property

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.CommonComponents
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.clickSuggestsCommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import net.kyori.adventure.text.format.TextDecoration

fun CommandAPICommand.npcPropertyListCommand() = subcommand("list") {
    withPermission(PermissionRegistry.COMMAND_NPC_PROPERTY_LIST)
    npcArgument("npc")
    integerArgument("page", optional = true)
    playerExecutor { player, args ->
        val npc: Npc by args
        val properties = npc.properties.values
        val page = args.getOrDefaultUnchecked("page", 1)

        if (properties.isEmpty()) {
            player.sendText {
                appendPrefix()
                error("Der NPC '${npc.uniqueName}' besitzt keine Eigenschaften.")
            }
            return@playerExecutor
        }

        val pagination = Pagination<NpcProperty> {
            title {
                info("Npc Eigenschaften".toSmallCaps())
                decorate(TextDecoration.BOLD)
            }
            rowRenderer { property, _ ->
                listOf(
                    buildText {
                        append(CommonComponents.EM_DASH)
                        appendSpace()
                        variableValue(property.key)
                        appendSpace()
                        info("(${property.type.id})")
                        hoverEvent(buildText {
                            variableValue(property.type.encode(property.value))
                        })
                        clickSuggestsCommand("/npc property remove ${npc.uniqueName} ${property.key}")
                    }
                )
            }
        }

        player.sendText {
            append(pagination.renderComponent(properties, page))
        }
    }
}