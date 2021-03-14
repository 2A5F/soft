package co.volight.soft.api.msg

import net.minecraft.entity.Entity
import net.minecraft.network.MessageType
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Util
import java.util.*

fun Entity.sendSysMsg(message: Text) {
    this.sendSystemMessage(message, Util.NIL_UUID)
}
fun Entity.sendSysMsg(message: Text, sender: UUID?) {
    this.sendSystemMessage(message, sender)
}

fun Text.sendTo(e: Entity) {
    e.sendSysMsg(this)
}
fun Text.sendTo(e: Entity, sender: UUID) {
    e.sendSystemMessage(this, sender)
}
fun Text.sendTo(e: ServerPlayerEntity, type: MessageType) {
    e.sendMessage(this, type, Util.NIL_UUID)
}
fun Text.sendTo(e: ServerPlayerEntity, type: MessageType, sender: UUID) {
    e.sendMessage(this, type, sender)
}
