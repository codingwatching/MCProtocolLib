package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPingPacket implements MinecraftPacket {
    private final int id;

    public ClientboundPingPacket(ByteBuf in) {
        this.id = in.readInt();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeInt(this.id);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
