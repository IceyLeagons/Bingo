package net.iceyleagons.bingo.listeners;

import net.iceyleagons.bingo.packet.PacketHandler;
import net.iceyleagons.bingo.packet.PacketListener;
import net.iceyleagons.bingo.packet.PacketReceiveEvent;

public class PacketListeners implements PacketListener {

    @PacketHandler
    public PacketReceiveEvent handlePacketSend(PacketReceiveEvent event) {
        /*try {
            if (event.getPacketClass() == PacketUtils.packetPlayOutEntityMetadata) {
                int entityId = (int) event.getPacket().readField("a");

                if (entityId < 0) {
                    event.getPacket().writeOverField("a", -entityId);
                    return event;
                }

                // Get the sender by the entity id
                final Player sender = event.getReceiver().getWorld().getPlayers().stream()
                        .parallel().filter(p -> p.getEntityId() == entityId)
                        .findAny().orElse(null);
                // If we didn't find the sender in that world OR if the sender isn't glowing, then skip.
                if (sender == null) return event;
                if (!PacketUtils.GLOW.glowPlayer.containsKey(sender)) return event;
                if (!PacketUtils.GLOW.glowPlayer.get(sender).getKey().contains(event.getReceiver())) return event;

                Object newPacket = PacketUtils.GLOW.getPacket(sender);
                event.setPacket(WrappedPacket.of(newPacket));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }*/

        return event;
    }

}
