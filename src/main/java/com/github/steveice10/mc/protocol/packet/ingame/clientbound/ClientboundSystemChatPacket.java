package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSystemChatPacket implements MinecraftPacket {
	private final Component content;
	private final MessageType type;

	public ClientboundSystemChatPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
		this.content = helper.readComponent(in);
		this.type = helper.readMessageType(in);
	}

	@Override
	public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
		helper.writeString(out, DefaultComponentSerializer.get().serialize(this.content));
		helper.writeMessageType(out, this.type);
	}
}