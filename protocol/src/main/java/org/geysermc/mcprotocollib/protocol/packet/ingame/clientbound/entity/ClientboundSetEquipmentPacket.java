package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EquipmentSlot;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Equipment;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEquipmentPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull Equipment[] equipment;

    public ClientboundSetEquipmentPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        boolean hasNextEntry = true;
        List<Equipment> list = new ArrayList<>();
        while (hasNextEntry) {
            byte rawSlot = in.readByte();
            EquipmentSlot slot = EquipmentSlot.fromEnumOrdinal(rawSlot & 127);
            ItemStack item = MinecraftTypes.readOptionalItemStack(in);
            list.add(new Equipment(slot, item));
            hasNextEntry = (rawSlot & 128) == 128;
        }
        this.equipment = list.toArray(new Equipment[0]);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        for (int i = 0; i < this.equipment.length; i++) {
            int rawSlot = this.equipment[i].getSlot().ordinal();
            if (i != equipment.length - 1) {
                rawSlot = rawSlot | 128;
            }
            out.writeByte(rawSlot);
            MinecraftTypes.writeOptionalItemStack(out, this.equipment[i].getItem());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
