package com.github.steveice10.mc.protocol.packet.ingame.server.title;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerClearTitlesPacket implements Packet {
    private boolean resetTimes;

    @Override
    public void read(NetInput in) throws IOException {
        this.resetTimes = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.resetTimes);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
