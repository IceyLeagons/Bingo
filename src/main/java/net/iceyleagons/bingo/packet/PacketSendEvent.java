package net.iceyleagons.bingo.packet;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;

@Data
public class PacketSendEvent extends PacketEvent {

    @NonNull
    private Player sender;
    private WrappedPacket packet;
    private boolean cancelled = false;

    public PacketSendEvent(Player sender, Object packet) {
        this.packet = WrappedPacket.of(packet);
        this.sender = sender;
    }

    public Class<?> getPacketClass() {
        return packet.getClass();
    }

    public String getPacketName() {
        return getPacketClass().getSimpleName();
    }

}
