package co.volight.soft.mixin;

import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientSettingsC2SPacket.class)
public interface ClientSettingsC2SPacketWithLanguage {
    @Accessor("language")
    String getLanguage();
}
