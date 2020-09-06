package net.iceyleagons.bingo.packet;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Data
public class PacketReceiveEvent extends PacketEvent {

    @NonNull
    private Player receiver;
    private WrappedPacket packet;
    private boolean cancelled = false;

    public PacketReceiveEvent(Player receiver, Object packet) {
        this.packet = WrappedPacket.of(packet);
        this.receiver = receiver;
    }

    public Class<?> getPacketClass() {
        return packet.getClass();
    }

    public String getPacketName() {
        return getPacketClass().getSimpleName();
    }

}
