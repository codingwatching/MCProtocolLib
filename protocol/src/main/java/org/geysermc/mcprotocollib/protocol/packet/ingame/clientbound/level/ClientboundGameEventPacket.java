package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.DemoMessageValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.ElderGuardianEffectValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.EnterCreditsValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEvent;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEventValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.LimitedCraftingValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.RainStrengthValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.RespawnScreenValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.ThunderStrengthValue;

@Data
@With
@AllArgsConstructor
public class ClientboundGameEventPacket implements MinecraftPacket {
    private final @NonNull GameEvent notification;
    private final GameEventValue value;

    public ClientboundGameEventPacket(ByteBuf in) {
        this.notification = GameEvent.from(in.readUnsignedByte());
        float value = in.readFloat();
        // TODO: Handle this in MinecraftTypes
        if (this.notification == GameEvent.GUARDIAN_ELDER_EFFECT) {
            this.value = new ElderGuardianEffectValue(value);
        } else if (this.notification == GameEvent.CHANGE_GAME_MODE) {
            this.value = GameMode.byId((int) value);
        } else if (this.notification == GameEvent.DEMO_EVENT) {
            this.value = DemoMessageValue.from((int) value);
        } else if (this.notification == GameEvent.WIN_GAME) {
            this.value = EnterCreditsValue.from((int) value);
        } else if (this.notification == GameEvent.IMMEDIATE_RESPAWN) {
            this.value = RespawnScreenValue.from((int) value);
        } else if (this.notification == GameEvent.LIMITED_CRAFTING) {
            this.value = LimitedCraftingValue.from((int) value);
        } else if (this.notification == GameEvent.RAIN_LEVEL_CHANGE) {
            this.value = new RainStrengthValue(value);
        } else if (this.notification == GameEvent.THUNDER_LEVEL_CHANGE) {
            this.value = new ThunderStrengthValue(value);
        } else {
            this.value = null;
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeByte(this.notification.ordinal());
        float value = 0;
        // TODO: Handle this in MinecraftTypes
        if (this.value instanceof DemoMessageValue) {
            value = ((DemoMessageValue) this.value).getId();
        } else if (this.value instanceof Enum<?>) {
            value = ((Enum<?>) this.value).ordinal();
        } else if (this.value instanceof RainStrengthValue) {
            value = ((RainStrengthValue) this.value).getStrength();
        } else if (this.value instanceof ThunderStrengthValue) {
            value = ((ThunderStrengthValue) this.value).getStrength();
        }

        out.writeFloat(value);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
