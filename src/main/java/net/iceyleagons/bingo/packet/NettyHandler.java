package net.iceyleagons.bingo.packet;

import io.netty.channel.*;
import net.iceyleagons.bingo.Main;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NettyHandler {
    public static Map<Player, String> ids = new HashMap<>();

    public static void handleInjection(final Player player, final Object channel) {
        final ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                PacketEvent event = Main.eventsHandler.callEvent(new PacketReceiveEvent(player, msg));
                if (event.isCancelled())
                    return;

                super.channelRead(ctx, event.getPacket().getPacket());
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                PacketEvent event = Main.eventsHandler.callEvent(new PacketSendEvent(player, msg));
                if (event.isCancelled())
                    return;

                super.write(ctx, event.getPacket().getPacket(), promise);
            }
        };

        final ChannelPipeline channelPipeline = ((Channel) channel).pipeline();
        if (!ids.containsKey(player)) {
            String id = RandomStringUtils.randomAlphabetic(5);
            while (ids.containsValue(id))
                id = RandomStringUtils.randomAlphabetic(5);

            ids.put(player, RandomStringUtils.randomAlphabetic(5));
        }

        channelPipeline.addBefore("packet_handler", ids.get(player), channelDuplexHandler);
    }

    public static void handleUninjection(final Player player, final Object channel) {
        ((Channel) channel).pipeline().remove(ids.get(player));
    }
}
