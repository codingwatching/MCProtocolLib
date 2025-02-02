package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.map.MapData;
import org.geysermc.mcprotocollib.protocol.data.game.level.map.MapIcon;
import org.geysermc.mcprotocollib.protocol.data.game.level.map.MapIconType;

@Data
@With
@AllArgsConstructor
public class ClientboundMapItemDataPacket implements MinecraftPacket {
    private final int mapId;
    private final byte scale;
    private final boolean locked;
    private final @NonNull MapIcon[] icons;

    private final MapData data;

    public ClientboundMapItemDataPacket(int mapId, byte scale, boolean locked, @NonNull MapIcon[] icons) {
        this(mapId, scale, locked, icons, null);
    }

    public ClientboundMapItemDataPacket(ByteBuf in) {
        this.mapId = MinecraftTypes.readVarInt(in);
        this.scale = in.readByte();
        this.locked = in.readBoolean();
        boolean hasIcons = in.readBoolean();
        this.icons = new MapIcon[hasIcons ? MinecraftTypes.readVarInt(in) : 0];
        if (hasIcons) {
            for (int index = 0; index < this.icons.length; index++) {
                int type = MinecraftTypes.readVarInt(in);
                int x = in.readByte();
                int z = in.readByte();
                int rotation = in.readByte();
                Component displayName = MinecraftTypes.readNullable(in, MinecraftTypes::readComponent);

                this.icons[index] = new MapIcon(x, z, MapIconType.from(type), rotation, displayName);
            }
        }

        int columns = in.readUnsignedByte();
        if (columns > 0) {
            int rows = in.readUnsignedByte();
            int x = in.readUnsignedByte();
            int y = in.readUnsignedByte();
            byte[] data = MinecraftTypes.readByteArray(in);

            this.data = new MapData(columns, rows, x, y, data);
        } else {
            this.data = null;
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.mapId);
        out.writeByte(this.scale);
        out.writeBoolean(this.locked);
        if (this.icons.length != 0) {
            out.writeBoolean(true);
            MinecraftTypes.writeVarInt(out, this.icons.length);
            for (MapIcon icon : this.icons) {
                int type = icon.getIconType().ordinal();
                MinecraftTypes.writeVarInt(out, type);
                out.writeByte(icon.getCenterX());
                out.writeByte(icon.getCenterZ());
                out.writeByte(icon.getIconRotation());
                MinecraftTypes.writeNullable(out, icon.getDisplayName(), MinecraftTypes::writeComponent);
            }
        } else {
            out.writeBoolean(false);
        }

        if (this.data != null && this.data.getColumns() != 0) {
            out.writeByte(this.data.getColumns());
            out.writeByte(this.data.getRows());
            out.writeByte(this.data.getX());
            out.writeByte(this.data.getY());
            MinecraftTypes.writeVarInt(out, this.data.getData().length);
            out.writeBytes(this.data.getData());
        } else {
            out.writeByte(0);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
