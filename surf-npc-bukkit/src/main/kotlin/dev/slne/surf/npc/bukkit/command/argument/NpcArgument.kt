package dev.slne.surf.npc.bukkit.command.argument

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

class NpcArgument(nodeName: String) : CustomArgument<Npc, String>(StringArgument(nodeName), { info ->
    if(info.input.toIntOrNull() != null) {
        npcController.getNpc(info.input.toInt()) ?: throw CustomArgumentException.fromAdventureComponent (
            buildText {
                appendPrefix()
                error("Der Npc mit der ID '${info.input}' wurde nicht gefunden.")
            })
    } else {
        npcController.getNpc(info.input) ?: throw CustomArgumentException.fromAdventureComponent (
            buildText {
                appendPrefix()
                error("Der Npc '${info.input}' wurde nicht gefunden.")
            })
    }
}) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection {
            npcController.getNpcs().map { it.uniqueName }
        })
    }
}

inline fun CommandAPICommand.npcArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(NpcArgument(nodeName).setOptional(optional).apply(block))