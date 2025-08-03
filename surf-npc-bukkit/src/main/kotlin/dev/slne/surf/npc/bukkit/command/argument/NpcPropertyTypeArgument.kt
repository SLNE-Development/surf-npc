package dev.slne.surf.npc.bukkit.command.argument

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

class NpcPropertyTypeArgument(nodeName: String) : CustomArgument<NpcPropertyType, String>(StringArgument(nodeName), { info ->
    propertyTypeRegistry.get(info.input) ?: throw CustomArgumentException.fromAdventureComponent (
        buildText {
            appendPrefix()
            error("Der Npc Property Type '${info.input}' wurde nicht gefunden.")
        })
}) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection {
            propertyTypeRegistry.getIds()
        })
    }
}

inline fun CommandAPICommand.npcPropertyTypeArgument (
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(NpcPropertyTypeArgument(nodeName).setOptional(optional).apply(block))