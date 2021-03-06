/*******************************************************************************
 * Copyright (C) 2019 Tamás Tóth and Márton Kissik (https://www.iceyleagons.net/)
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

import org.bukkit.Bukkit;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Reflections {
    private static final Gson gson = new Gson();

    private static final String craftBukkitString;
    private static final String netMinecraftServerString;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        craftBukkitString = "org.bukkit.craftbukkit." + version + ".";
        netMinecraftServerString = "net.minecraft.server." + version + ".";
    }

    public static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Class<?> getNormalCBClass(String name) {
        return getNormalClass(craftBukkitString + name);
    }

    public static Class<?> getNormalNMSClass(String name) {
        return getNormalClass(netMinecraftServerString + name);
    }

    public static Class<?> getNormalClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field[] getFields(final Class<?> cls) {
        final Field[] fields = cls.getDeclaredFields();
        for (final Field f : fields)
            f.setAccessible(true);

        return fields;
    }

    public static Field getField(final Class<?> clazz, final Class<?> dataType, final int index) {
        int i = 0;
        Field finalField = null;
        List<Field> fieldList = Arrays.stream(getFields(clazz)).parallel()
                .filter(f -> f.getType().equals(dataType))
                .collect(Collectors.toList());
        for (Field field : fieldList)
            if (i++ == index) {
                finalField = field;
                break;
            }
        return finalField;
    }
}
