package dev.slne.surf.npc.bukkit.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

val miniMessage get() = MiniMessage.miniMessage()
fun emptyComponent() = Component.empty()