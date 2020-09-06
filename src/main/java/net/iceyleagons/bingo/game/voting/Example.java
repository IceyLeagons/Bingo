package net.iceyleagons.bingo.game.voting;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author TOTHTOMI
 */
public class Example {

    public void example() {
        Vote vote = new Vote();

        VoteMenu voteMenu = new VoteMenu(Material.ACACIA_FENCE,"Test", Collections.singletonList("Asd"));
        voteMenu.addVoteOption(Material.YELLOW_TERRACOTTA,"Day Time",Collections.singletonList("Day time"),5,0);
        voteMenu.addVoteOption(Material.ORANGE_TERRACOTTA,"Sunset",Collections.singletonList("Sunset"),6,1);

        VoteMenu voteMenu2 = new VoteMenu(Material.ACACIA_FENCE,"Test2", Collections.singletonList("Asd2"));
        voteMenu2.addVoteOption(Material.YELLOW_TERRACOTTA,"Day Time2",Collections.singletonList("Day time"),5,0);
        voteMenu2.addVoteOption(Material.ORANGE_TERRACOTTA,"Sunset2",Collections.singletonList("Sunset"),6,1);

        vote.addMenu(voteMenu,0,5);
        vote.addMenu(voteMenu2,1,6);


        //Count votes
        vote.closeAll(); //Closing the invenory for all players
        vote.setClosed(true); //Closing the voting -> cannot open it anymore

        int voteMenu1Winner = vote.getVoteMenu(0).getIdOfWinningOption(); //if -1 the use default option
        int voteMenu2Winner = vote.getVoteMenu(1).getIdOfWinningOption(); //if -1 use the default option
        if (voteMenu1Winner == 0) System.out.println("Day time"); else if (voteMenu1Winner == 1) System.out.println("Sunset");
        if (voteMenu2Winner == 0) System.out.println("Day time2"); else if (voteMenu2Winner == 1) System.out.println("Sunset2");

    }

}
