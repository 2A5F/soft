package co.volight.soft.api.permission

import co.volight.soft.api.service.Service
import net.minecraft.entity.player.PlayerEntity
import java.util.*

interface Permission {
    fun has(player: PlayerEntity, permission: String): Boolean
    fun has(subject: Subject, permission: String) = subject.hasPermission(permission)

    companion object: Permission {
        private val impl = Service.service<Permission>()

        override fun has(player: PlayerEntity, permission: String) = impl.get().has(player, permission)
        override fun has(subject: Subject, permission: String) = impl.get().has(subject, permission)
    }
}

interface Subject {
    fun hasPermission(permission: String): Boolean
}

fun PlayerEntity.hasPermission(permission: String) = Permission.has(this, permission)
