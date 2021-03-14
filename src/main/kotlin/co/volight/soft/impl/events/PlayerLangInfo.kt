package co.volight.soft.impl.events

import co.volight.soft.Soft
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

internal object PlayerLangInfo {
    val playerLanguage = mutableMapOf<UUID, String>()

    fun get(id: UUID): String? = playerLanguage[id]
    fun get(player: ServerPlayerEntity): String {
        val r = get(player.uuid)
        if (r == null) {
            //Soft.LOGGER.warn("${Soft.logName} Can't find the language information of player ${player.entityName} ")
            return "en_us"
        }
        return r
    }

    private fun gcTick(server: MinecraftServer) {
        val manager = server.playerManager
        playerLanguage.entries.removeIf {
            manager.getPlayer(it.key) == null
        }
    }

    private fun regGcTick() {
        val gcTickTime = 600000
        var lastTime = System.currentTimeMillis()
        ServerTickEvents.END_SERVER_TICK.register(ServerTickEvents.EndTick {
            val now = System.currentTimeMillis()
            if (gcTickTime + lastTime < System.currentTimeMillis()) return@EndTick
            lastTime = now
            gcTick(it)
        })
        Soft.LOGGER.info("${Soft.logName} PlayerLangInfo gcTick registered")
    }

    fun regEvent() {
        regGcTick()
    }
}
