package net.iceyleagons.bingo.packet;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class PacketEventsHandler {

    HashMap<PacketListener, List<Method>> listenerArray = new HashMap<>();

    @SneakyThrows
    public PacketEvent callEvent(PacketEvent event) {
        PacketEvent finalPart = event;
        for (Map.Entry<PacketListener, List<Method>> entry : listenerArray.entrySet()) {
            PacketListener listener = entry.getKey();
            List<Method> methods = entry.getValue();
            for (Method method : methods.stream().parallel().filter(method -> method.getParameterTypes()[0] == event.getClass()).collect(Collectors.toList()))
                finalPart = (PacketEvent) method.invoke(listener, finalPart);
        }

        return finalPart;
    }

    public void registerListener(PacketListener listener) {
        listenerArray.put(listener, Arrays.stream(listener.getClass().getMethods()).parallel()
                .filter(method -> method.isAnnotationPresent(PacketHandler.class))
                .filter(method -> method.getReturnType().getSuperclass().equals(PacketEvent.class))
                .collect(Collectors.toList()));
    }

}
