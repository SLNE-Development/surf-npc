package dev.slne.surf.npc.bukkit.command.argument

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

class RotationTypeArgument(nodeName: String) : CustomArgument<NpcRotationType, String>(StringArgument(nodeName), { info ->
    NpcRotationType.entries.find { it.name.equals(info.input, ignoreCase = true) } ?: throw CustomArgumentException.fromAdventureComponent (
        buildText {
            appendPrefix()
            error("Der Npc '${info.input}' wurde nicht gefunden.")
        })
}) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection {
            NpcRotationType.entries.map { it.name }
        })
    }
}

inline fun CommandAPICommand.rotationTypeArgument (
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(RotationTypeArgument(nodeName).setOptional(optional).apply(block))