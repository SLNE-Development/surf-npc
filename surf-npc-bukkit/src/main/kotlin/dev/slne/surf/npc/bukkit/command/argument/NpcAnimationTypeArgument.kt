package dev.slne.surf.npc.bukkit.command.argument

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.npc.api.npc.animation.NpcAnimationType
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

class NpcAnimationTypeArgument(nodeName: String) :
    CustomArgument<NpcAnimationType, String>(StringArgument(nodeName), { info ->
        NpcAnimationType.valueOf(info.input.uppercase())
    }) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection {
            NpcAnimationType.entries.map { it.name.lowercase() }
        })
    }
}

inline fun CommandAPICommand.npcAnimationTypeArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(NpcAnimationTypeArgument(nodeName).setOptional(optional).apply(block))