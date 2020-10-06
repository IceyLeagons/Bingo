/*******************************************************************************
 * Copyright (C) 2019 Tóth Tamás and Márton Kissik (https://www.iceyleagons.net/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package net.iceyleagons.bingo.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * PacketUtils contains a few packet instructions. It's not meant to replace
 * anything, rather to extend on them. If you need some help, feel free to
 * contact me on my email @ : gabe@iceyleagons.net Quick demo:
 * {@code PacketUtils.sendPacket(player, PacketUtils.RESPAWN.getRespawn());}
 *
 * @author Gabe
 * @version 2.0
 * @category Packet
 * @since 2019-04-07
 */
@SuppressWarnings(
        {"deprecation"})
public class PacketUtils {
    private static boolean NETTY_7 = false;

    public static Class<?> nettyClass,

    craftPlayer, craftEntity, craftItemStack, craftWorld, entityPlayer, minecraftServer, worldServer,
            playOutPlayerInfo, playerInteractManager, playOutEntityEquipment, itemStack, dataWatcher, dataWatcherItem, item, int2ObjectOpenHashMap,

    chatBaseComponent, packet, playOutChat, playOutHeaderFooter, playOutCooldown, networkManager,
            playOutTitle, playOutWorldBorder, playOutRespawn, playOutAnimation, playInFlying, playerConnection, packetPlayOutEntityMetadata;

    public static Method getWorldHandle, getHandle, sendPacket, getDataWatcher, dataWatcherA, asNmsItemstack, getItem, itemD, itemB, itemA;


    static {
        try {
            try {
                nettyClass = Class.forName("io.netty.channel.Channel");
            } catch (ClassNotFoundException ignored) {
                NETTY_7 = true;
                nettyClass = Class.forName("net.minecraft.util.io.netty.channel.Channel");
            }
            /*
             * Variables
             */
            craftPlayer = Reflections.getNormalCBClass("entity.CraftPlayer");
            craftEntity = Reflections.getNormalCBClass("entity.CraftEntity");
            craftItemStack = Reflections.getNormalCBClass("inventory.CraftItemStack");
            worldServer = Reflections.getNormalNMSClass("WorldServer");
            itemStack = Reflections.getNormalNMSClass("ItemStack");
            item = Reflections.getNormalNMSClass("Item");
            craftWorld = Reflections.getNormalCBClass("CraftWorld");
            chatBaseComponent = Reflections.getNormalNMSClass("ChatBaseComponent");
            entityPlayer = Reflections.getNormalNMSClass("EntityPlayer");
            minecraftServer = Reflections.getNormalNMSClass("MinecraftServer");
            dataWatcher = Reflections.getNormalNMSClass("DataWatcher");
            dataWatcherItem = Reflections.getNormalNMSClass("DataWatcher$Item");
            try {
                int2ObjectOpenHashMap = Class.forName("it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap");
            } catch (ClassNotFoundException ignored) {
                int2ObjectOpenHashMap = Class.forName("org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap");
            }

            /*
             * Packets
             */

            packet = Reflections.getNormalNMSClass("Packet");
            playOutChat = Reflections.getNormalNMSClass("PacketPlayOutChat");
            playOutHeaderFooter = Reflections.getNormalNMSClass("PacketPlayOutPlayerListHeaderFooter");
            playOutCooldown = Reflections.getNormalNMSClass("PacketPlayOutSetCooldown");
            playOutTitle = Reflections.getNormalNMSClass("PacketPlayOutTitle");
            playOutWorldBorder = Reflections.getNormalNMSClass("PacketPlayOutWorldBorder");
            playOutAnimation = Reflections.getNormalNMSClass("PacketPlayOutAnimation");
            playOutRespawn = Reflections.getNormalNMSClass("PacketPlayOutRespawn");
            playerConnection = Reflections.getNormalNMSClass("PlayerConnection");
            playOutPlayerInfo = Reflections.getNormalNMSClass("PacketPlayOutPlayerInfo");
            playerInteractManager = Reflections.getNormalNMSClass("PlayerInteractManager");
            playOutEntityEquipment = Reflections.getNormalNMSClass("PacketPlayOutEntityEquipment");
            playInFlying = Reflections.getNormalNMSClass("PacketPlayInFlying");
            packetPlayOutEntityMetadata = Reflections.getNormalNMSClass("PacketPlayOutEntityMetadata");

            /*
             * Methods
             */
            getItem = itemStack.getDeclaredMethod("getItem");
            asNmsItemstack = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            getWorldHandle = craftWorld.getDeclaredMethod("getHandle");
            getHandle = craftPlayer.getDeclaredMethod("getHandle");
            sendPacket = playerConnection.getDeclaredMethod("sendPacket", packet);
            getDataWatcher = Reflections.getNormalNMSClass("Entity").getDeclaredMethod("getDataWatcher");
            /*dataWatcherA = dataWatcher.getDeclaredMethod("a", Integer.class, Object.class);
            itemD = dataWatcherItem.getDeclaredMethod("d");
            itemB = dataWatcherItem.getDeclaredMethod("b");
            itemA = dataWatcherItem.getDeclaredMethod("a", Object.class);*/
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public static void sendPacket(@NonNull Player p, @NonNull Object packet) {
        try {
            Object cp = craftPlayer.cast(p);
            Object pc = getHandle.invoke(cp);
            sendPacket.invoke(pc, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * CraftPlayer cp = (CraftPlayer) p; PlayerConnection pc =
         * cp.getHandle().playerConnection; pc.sendPacket(packet);
         */
    }

    /**
     * Send the specified packet to the specified players
     *
     * @param players
     * @param packet
     */
    public static void sendPacket(@NonNull List<Player> players, @NonNull Object packet) {
        try {
            for (Player p : players) {
                Object cp = craftPlayer.cast(p);
                Object pc = getHandle.invoke(cp);
                sendPacket.invoke(pc, packet);
                /*
                 * CraftPlayer cp = (CraftPlayer) p; PlayerConnection pc =
                 * cp.getHandle().playerConnection; pc.sendPacket(packet);
                 */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the specified packet to all the online players
     *
     * @param packet
     * @deprecated
     */
    public static void sendPacket(@NonNull Object packet) {
        try {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Object cp = craftPlayer.cast(p);
                Object pc = getHandle.invoke(cp);
                sendPacket.invoke(pc, packet);
                /*
                 * CraftPlayer cp = (CraftPlayer) plr; PlayerConnection pc =
                 * cp.getHandle().playerConnection; pc.sendPacket(packet);
                 */
            }
        } catch (Exception e) {
        }
    }

    public static void sendPacket(@NonNull Player p, @NonNull Object[] packets) {
        try {
            Object cp = craftPlayer.cast(p);
            Object pc = getHandle.invoke(cp);
            for (Object packet : packets)
                sendPacket.invoke(pc, packet);
        } catch (Exception e) {
        }
    }

    /**
     * Send the specified packets to the specified players
     *
     * @param players
     * @param packets
     */
    public static void sendPacket(@NonNull List<Player> players, @NonNull Object[] packets) {
        try {
            for (Player p : players) {
                Object cp = craftPlayer.cast(p);
                Object pc = getHandle.invoke(cp);
                for (Object packet : packets)
                    sendPacket.invoke(pc, packet);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Send the specified packets to all the online players
     *
     * @param packets
     * @deprecated
     */
    public static void sendPacket(@NonNull Object[] packets) {
        try {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Object cp = craftPlayer.cast(p);
                Object pc = getHandle.invoke(cp);
                for (Object packet : packets)
                    sendPacket.invoke(pc, packet);
            }
        } catch (Exception e) {
        }
    }

    @SneakyThrows
    public static void injectPlayer(final Player player) {
        /*if (NETTY_7) {
            throw new UnsupportedOperationException("Please update your java, as I don't plan on making a downgraded version.");
        } else {
            NettyHandler.handleInjection(player, getChannel(player));
        }*/
    }

    @SneakyThrows
    public static void uninjectPlayer(final Player player) {
        /*if (NETTY_7) {
            throw new UnsupportedOperationException("Please update your java, as I don't plan on making a downgraded version.");
        } else {
            NettyHandler.handleUninjection(player, getChannel(player));
        }*/
    }

    private static Object getChannel(final Player player) {
        try {
            return Reflections.getField(networkManager, nettyClass, 0)
                    .get(Reflections.getField(playerConnection, networkManager, 0)
                            .get(getHandle.invoke(craftPlayer.cast(player))));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class TRANSLATIONS {
        public static String get(ItemStack item) {
            try {
                Object iS = asNmsItemstack.invoke(null, item);

                TranslatableComponent translatableComponent = new TranslatableComponent(getItem.invoke(iS).toString());
                return translatableComponent.getTranslate();
            } catch (Exception ignored) {

            }
            return "";
        }
    }

    public static class GLOW {
        public static HashMap<Player, Map.Entry<List<Player>, Boolean>> glowPlayer = new HashMap<>();

        public static void switchGlowing(Player glowingPlayer, boolean glow, Player... sendPacketsTo) {
            List<Player> sendTo = Arrays.asList(sendPacketsTo);
            glowPlayer.put(glowingPlayer, new AbstractMap.SimpleEntry<>(sendTo, glow));
            PacketUtils.sendPacket(sendTo, getPacket(glowingPlayer));
        }

        public static Object getPacket(@NonNull Player player) {
            /*try {
                Object entityPlayer = getHandle.invoke(craftPlayer.cast(player));

                Object toCloneDataWatcher = getDataWatcher.invoke(entityPlayer);
                Object newDataWatcher = dataWatcher.getDeclaredConstructor(entityPlayer.getClass()).newInstance(entityPlayer);

                // The map that stores the DataWatcherItems is private within the DataWatcher Object.
                // We need to use Reflection to access it from Apache Commons and change it.
                Map<Integer, Object> currentMap = (Map<Integer, Object>) FieldUtils.readDeclaredField(toCloneDataWatcher, "d", true);
                Map<Integer, Object> newMap = (Map<Integer, Object>) int2ObjectOpenHashMap.newInstance();

                // We need to clone the DataWatcher.Items because we don't want to point to those values anymore.
                for (Integer integer : currentMap.keySet())
                    newMap.put(integer, itemD.invoke(currentMap.get(integer))); // Puts a copy of the DataWatcher.Item in newMap

                // Get the 0th index for the BitMask value. http://wiki.vg/Entities#Entity
                Object item = newMap.get(0);
                byte initialBitMask = (Byte) itemB.invoke(item); // Gets the initial bitmask/byte value so we don't overwrite anything.
                byte bitMaskIndex = (byte) 6; // The index as specified in wiki.vg/Entities
                if (glowPlayer.get(player).getValue())
                    itemA.invoke(item, (byte) (initialBitMask | 1 << bitMaskIndex));
                else
                    itemA.invoke(item, (byte) (initialBitMask & ~(1 << bitMaskIndex))); // Inverts the specified bit from the index.

                // Set the newDataWatcher's (unlinked) map data
                FieldUtils.writeDeclaredField(newDataWatcher, "d", newMap, true);
                FieldUtils.writeDeclaredField(newDataWatcher, "a", -((int) FieldUtils.readDeclaredField(toCloneDataWatcher, "a", true)), true);

                return getPacket(player.getEntityId(), newDataWatcher, true);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) { // Catch statement necessary for FieldUtils.readDeclaredField()
                e.printStackTrace();
            }
*/
            return null;
        }

        private static Object getPacket(@NonNull int id, @NonNull Object dataWatcherObject, @NonNull boolean b) {
            try {
                Constructor<?> ctor = packetPlayOutEntityMetadata.getConstructor(Integer.class, dataWatcher, Boolean.class);
                return ctor.newInstance(id, dataWatcherObject, b);
            } catch (Exception ignored) {
            }
            return null;
        }
    }

    /**
     * PacketUtils' Title packet class
     *
     * @author Gabe
     * @version 1.0
     * @category Packet
     */
    public static class TITLE {

        public static Object getPacket(@NonNull Object titleAction, @NonNull Object chatBaseComponent) {
            try {
                Constructor<?> ctor = playOutTitle.getConstructor(titleAction.getClass(), chatBaseComponent.getClass());
                return ctor.newInstance(titleAction, chatBaseComponent);
            } catch (Exception e) {
            }
            return null;
        }

        public static Object getPacket(@NonNull int fadeIn, @NonNull int stay, @NonNull int fadeOut) {
            try {
                Constructor<?> ctor = playOutTitle.getConstructor(Integer.class, Integer.class, Integer.class);
                return ctor.newInstance(fadeIn, stay, fadeOut);
            } catch (Exception e) {
            }
            return null;
        }
    }

    /**
     * PacketUtils' Chat packet class
     *
     * @author Gabe
     * @version 1.0
     * @category Packet
     */
    public static class CHAT {

        public static Object getPacket(@NonNull Object chatBaseComponent) {
            try {
                return playOutChat.getConstructor(PacketUtils.chatBaseComponent.getClass())
                        .newInstance(chatBaseComponent);
            } catch (Exception e) {
            }
            return null;
        }
    }

    /**
     * PacketUtils' Latency class
     *
     * @author Gabe
     * @version 1.0
     * @category Information
     */
    public static class LATENCY {
        /**
         * Get the Latency/Ping of the player specified
         *
         * @param player
         * @return the ping of the player
         */
        public static int getLatency(@NonNull Player player) {
            try {
                return getHandle.invoke(player).getClass().getField("ping").getInt(null);
            } catch (Exception e) {
            }
            return 0;
        }
    }

    /**
     * PacketUtils' Header & Footer packet class
     *
     * @author Gabe
     * @version 1.0
     * @category Packet
     */
    public static class PLAYER_HEADER_FOOTER {
        /**
         * Send the player a Tab format packet (Header Footer)
         *
         * @param player
         * @param header
         * @param footer
         */
		/*public static void setPlayerListHeaderFooter(@NonNull Player player, String headerString, String footerString) {
			try {
				Object pLP = getExactPacket();
				pLP.getClass().getField("header").set(null, toJSON.invoke(null, "\"" + headerString + "\""));
				pLP.getClass().getField("footer").set(null, toJSON.invoke(null, "\"" + footerString + "\""));
				PacketUtils.sendPacket(player, pLP);
			} catch (Exception e) {
			}
		}*/

        /**
         * Get HeaderFooter packet exactly as HeaderFooter packet
         *
         * @return new HeaderFooter packet
         * @throws IllegalAccessException
         * @throws InstantiationException
         */
        public static Object getExactPacket() throws InstantiationException, IllegalAccessException {
            return playOutHeaderFooter.newInstance();
        }
    }

    /**
     * PacketUtils' Cooldown packet class
     *
     * @author Gabe
     * @version 1.0
     * @category Packet
     */
    public static class COOLDOWN {

        private static Object getNMSItem(ItemStack item) {
            try {
                return craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(item);
            } catch (Exception e) {
            }
            return null;
        }

        public static Object getCooldown(@NonNull Object item, @NonNull int cooldown) {
            try {
                Constructor<?> ctor = playOutCooldown.getConstructor(item.getClass(), Integer.class);
                return ctor.newInstance(item, cooldown);
            } catch (Exception e) {
            }
            return null;
        }

        public static Object getCooldown(@NonNull ItemStack item, @NonNull int cooldown) {
            try {
                Object nmsItem = getNMSItem(item);
                Constructor<?> ctor = playOutCooldown.getConstructor(item.getClass(), Integer.class);
                return ctor.newInstance(nmsItem, cooldown);
            } catch (Exception e) {
            }
            return null;
        }

        public static Object getCooldown(@NonNull ItemStack item) {
            try {
                Object nmsItem = getNMSItem(item);
                Constructor<?> ctor = playOutCooldown.getConstructor(item.getClass(), Integer.class);
                return ctor.newInstance(nmsItem, 0);
            } catch (Exception e) {
            }
            return null;
        }

        public static Object getCooldown(@NonNull Object item) {
            try {
                Constructor<?> ctor = playOutCooldown.getConstructor(item.getClass(), Integer.class);
                return ctor.newInstance(item, 0);
            } catch (Exception e) {
            }
            return null;
        }
    }

    /**
     * PacketUtils' Respawn packet class
     *
     * @author Gabe
     * @version 1.0
     * @category Packet
     */
    public static class RESPAWN {
        /**
         * Get the packet for respawning player I.e:
         * {@code PacketUtils.sendPacket(player, PacketUtils.RESPAWN.getRespawn());}
         *
         * @return new Respawn packet
         */
        public static Object getRespawn() {
            try {
                return playOutRespawn.newInstance();
            } catch (Exception e) {
            }
            return null;
        }
    }

    public static class ITEMSTACK {
        public static Object getNMS(ItemStack item) {
            try {
                return craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(item);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class EQUIPMENT {
        public static Object getPacket() {
            try {
                return playOutEntityEquipment.newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        public static Object getPacket(int entityId, int slot, Object item) {
            try {
                return playOutEntityEquipment.getConstructor(int.class, int.class, itemStack).newInstance(entityId,
                        slot, item);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
