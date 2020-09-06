package net.iceyleagons.bingo.packet;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.reflect.FieldUtils;

@Data
@RequiredArgsConstructor(staticName = "of")
public class WrappedPacket {
    @NonNull
    private Object packet;

    @SneakyThrows
    public void writeOverField(String nameOfField, Object value) {
        FieldUtils.writeDeclaredField(packet, nameOfField, value, true);
    }

    @SneakyThrows
    public Object readField(String field) {
        return FieldUtils.readDeclaredField(packet, field);
    }

}
