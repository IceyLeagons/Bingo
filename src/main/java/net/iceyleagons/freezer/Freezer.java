package net.iceyleagons.freezer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A freezer is basically a cache, that synchronizes data to a Database.
 * The cache can be read/written and the database won't change, needs synchronization!
 * Every {@link Freezer} is unique to a {@link net.iceyleagons.bingo.storage.DatabaseType}
 *
 * @author TOTHTOMI
 */
public interface Freezer {

    /**
     * @return the type of this {@link Freezer}
     */
    FreezerType getType();

    /**
     * @return the name of the Freezer
     */
    String getName();

    /**
     * @return the version of the Freezer (used in case of format changes)
     */
    Integer getVersion();

    /**
     * @return the time of the last update in millis
     */
    Long getLastUpdate();

    /**
     * Pushes all the data in cache to the database
     *
     * @return A {@link CompletableFuture} with a boolean, if true the task was successful, if false it wasn't.
     */
    CompletableFuture<Boolean> push();

    /**
     * Pulls data from the database to the cache
     *
     * @return A {@link CompletableFuture} with a boolean, if true the task was successful, if false it wasn't.
     */
    CompletableFuture<Boolean> pull();

    /**
     * Updates the cache, firstly push all the data, that has changed and pull the complete data from the database.
     *
     * @return true if the update was successfull false otherwise
     */
    boolean update();

    /**
     * Adds an {@link Object} to the cache
     *
     * @param id the id of the {@link Object}, !must be unique(the same ID can exist in multiple freezers but not in the same)!
     * @param o  the Object to add
     */
    void add(int id, Object o);

    /**
     * Removes an {@link Object} from the cache
     *
     * @param id the id of the {@link Object} to remove.
     */
    void remove(int id);


    /**
     * Returns an Object from the cache.
     *
     * @param id the id of the {@link Object}
     */
    Object get(int id);


}
