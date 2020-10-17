package net.iceyleagons.bingo.utils.voting;

import net.iceyleagons.bingo.commands.CommandManager;
import org.bukkit.inventory.Inventory;

/**
 * @author TOTHTOMI
 */
public class VotingFactory {

    //Customizable, Multi pages


    public VotingFactory() {
        Voting voting = new Voting("asd",54,null);
        VotingMenu votingMenu = new VotingMenu("asd",54,null);
        votingMenu.addVoteOption(null,0, "voting1");


        voting.addSubMenu(null,0,votingMenu);
    }

}
