package net.iceyleagons.bingo.storage.data;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

/**
 * Player information put into a freezer, basically an object that we use in Hibernate
 *
 * @author TOTHTOMI
 */
@Entity
@Table(name = "PLAYERS")
@Data
public class FreezedPlayer {

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "wins")
    private int wins = 0;

    @Column(name = "losses")
    private int losses = 0;

    @Column(name = "kills")
    private int kills = 0;

    @Column(name = "checked_items")
    private int checkedItems = 0;


    @Override
    public String toString() {
        return "FreezedPlayer(" + this.getUuid() + ", " + this.getWins() + ", " + this.getLosses() + ", " + this.getKills() + ", " + this.getCheckedItems() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof FreezedPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof FreezedPlayer)) return false;
        FreezedPlayer other = (FreezedPlayer) o;
        if (!other.canEqual((Object)this)) return false;
        if (this.getUuid() == null ? other.getUuid() != null : !this.getUuid().equals(other.getUuid())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result*PRIME) + (this.getUuid() == null ? 43 : this.getUuid().hashCode());
        return result;
    }

}
