package co.volight.soft.api.permission

import co.volight.soft.api.service.Service
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity

interface Permission {
    fun has(player: PlayerEntity, permission: String): Boolean
    fun has(entity: Entity, permission: String): Boolean
    fun has(block: BlockEntity, permission: String): Boolean
    fun has(target: PermisObject, permission: String) = target.hasPermission(permission)

    companion object: Permission {
        private val impl = Service.service<Permission>()

        override fun has(player: PlayerEntity, permission: String) = impl.get().has(player, permission)
        override fun has(entity: Entity, permission: String) = impl.get().has(entity, permission)
        override fun has(block: BlockEntity, permission: String) = impl.get().has(block, permission)
        override fun has(target: PermisObject, permission: String) = impl.get().has(target, permission)
    }
}

fun PlayerEntity.hasPermission(permission: String) = Permission.has(this, permission)
fun Entity.hasPermission(permission: String) = Permission.has(this, permission)
fun BlockEntity.hasPermission(permission: String) = Permission.has(this, permission)

interface PermisObject {
    fun hasPermission(permission: String): Boolean
}

interface PermisContext {

}

interface PermisVolume {

}
