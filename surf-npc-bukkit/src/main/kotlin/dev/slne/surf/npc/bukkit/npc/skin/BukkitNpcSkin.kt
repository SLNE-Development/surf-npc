package dev.slne.surf.npc.bukkit.npc.skin

import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.npc.skin.NpcSkinPart
import it.unimi.dsi.fastutil.objects.ObjectSet

data class BukkitNpcSkin(
    override val ownerName: String,
    override val value: String,
    override val signature: String,
    override val parts: ObjectSet<NpcSkinPart>
) : NpcSkin