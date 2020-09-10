package net.iceyleagons.bingo.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * @author TOTHTOMI
 */
@Data
public class DatabaseParams {

    private String url,username,password;

    public DatabaseParams(@NonNull String url, @NonNull String username, @NonNull String password) {
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

}
