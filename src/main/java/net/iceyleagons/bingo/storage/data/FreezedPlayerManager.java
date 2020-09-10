package net.iceyleagons.bingo.storage.data;

import lombok.SneakyThrows;
import net.iceyleagons.bingo.game.BingoPlayer;
import net.iceyleagons.bingo.storage.HibernateManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

/**
 * @author TOTHTOMI
 */
public class FreezedPlayerManager {

    public static BingoPlayer fromFreezedPlayer(FreezedPlayer freezedPlayer) {
        Player player = Bukkit.getPlayer(freezedPlayer.getUuid());
        //TODO
        return new BingoPlayer(player);
    }

    public static Integer generateFromBingoPlayer(BingoPlayer bingoPlayer) {
        if (!HibernateManager.isEnabled()) return null;

        Session session = Objects.requireNonNull(HibernateManager.getSessionFactory()).openSession();
        Integer id = null;
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            FreezedPlayer freezedPlayer = new FreezedPlayer();
            freezedPlayer.setUuid(bingoPlayer.getPlayer().getUniqueId());
            id = (Integer) session.save(freezedPlayer);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            hibernateException.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public static FreezedPlayer getFreezedPlayer(Integer id) {
        if (!HibernateManager.isEnabled()) return null;

        Session session = Objects.requireNonNull(HibernateManager.getSessionFactory()).openSession();
        FreezedPlayer player = null;
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            player = (FreezedPlayer)session.get(FreezedPlayer.class,id);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            hibernateException.printStackTrace();
        } finally {
            session.close();
        }
        return player;
    }

    public static List<FreezedPlayer> loadPlayers() {
        if (!HibernateManager.isEnabled()) return Collections.emptyList();

        List<FreezedPlayer> players = new ArrayList<>();

        Session session = Objects.requireNonNull(HibernateManager.getSessionFactory()).openSession();
        Integer id = null;
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            for (Object o : session.createQuery("FROM Players").list()) {
                FreezedPlayer freezedPlayer = (FreezedPlayer) o;
                players.add(freezedPlayer);
            }
        } catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
        } finally {
            session.close();
        }
        return players;
    }

    public static void updateFreezedPlayer(Integer id, FreezedPlayer freezedPlayerTo) {
        if (!HibernateManager.isEnabled())  return;

        Session session = Objects.requireNonNull(HibernateManager.getSessionFactory()).openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            FreezedPlayer freezedPlayer = (FreezedPlayer)session.get(FreezedPlayer.class,id);

            freezedPlayer.setUuid(freezedPlayerTo.getUuid());
            freezedPlayer.setCheckedItems(freezedPlayerTo.getCheckedItems());
            freezedPlayer.setKills(freezedPlayerTo.getKills());
            freezedPlayer.setLosses(freezedPlayerTo.getLosses());
            freezedPlayer.setWins(freezedPlayerTo.getWins());

            session.update(freezedPlayer);
            transaction.commit();

        } catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
            hibernateException.printStackTrace();
        } finally {
            session.close();
        }

    }

    public static void deleteFreezedPlayer(Integer id) {
        if (!HibernateManager.isEnabled()) return;

        Session session = Objects.requireNonNull(HibernateManager.getSessionFactory()).openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            FreezedPlayer freezedPlayer = (FreezedPlayer) session.get(FreezedPlayer.class,id);
            session.delete(freezedPlayer);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            if (transaction!=null) transaction.rollback();
        } finally {
            session.close();
        }
    }

}
