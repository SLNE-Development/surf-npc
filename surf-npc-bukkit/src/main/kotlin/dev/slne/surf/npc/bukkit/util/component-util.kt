package dev.slne.surf.npc.bukkit.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

val miniMessage get() = MiniMessage.miniMessage()

fun Component.toPlain(): String {
    return PlainTextComponentSerializer.plainText().serialize(this)
}