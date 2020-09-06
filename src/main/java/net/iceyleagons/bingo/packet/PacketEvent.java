package net.iceyleagons.bingo.packet;

public abstract class PacketEvent {
    public abstract WrappedPacket getPacket();

    public abstract void setPacket(WrappedPacket packet);

    public abstract boolean isCancelled();

    public abstract void setCancelled(boolean cancelled);
}
