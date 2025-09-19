package dev.slne.surf.npc.bukkit.command.argument

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.npc.api.npc.NpcPose

class NpcPoseArgument(nodeName: String) :
    CustomArgument<NpcPose, String>(StringArgument(nodeName), { info ->
        NpcPose.valueOf(info.input.uppercase())
    }) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection {
            NpcPose.entries.map { it.name.lowercase() }
        })
    }
}

inline fun CommandAPICommand.npcPoseArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(NpcPoseArgument(nodeName).setOptional(optional).apply(block))