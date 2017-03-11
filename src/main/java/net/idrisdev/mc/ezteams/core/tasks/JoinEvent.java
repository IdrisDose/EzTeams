package net.idrisdev.mc.ezteams.core.tasks;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.data.DAO;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Idris on 11/03/2017.
 */
public class JoinEvent {
    public JoinEvent(){

    }

    public static void runJoinEvent(Player player, EzTeams plugin, boolean DEBUG) {
        String name = player.getName();
        String uuid = player.getUniqueId().toString();
        if(DAO.playerExists(player)) {
            Member temp = Utils.findPastMember(player.getUniqueId().toString());

            if (temp == null) {
                plugin.getLogger().info("New Player joining!");
                temp = new Member(uuid, name);
                plugin.getAllPlayers().add(temp);

                plugin.getLogger().info("Player added to all member list.");
            }

            if (DEBUG) {
                plugin.getLogger().info("------------");
                plugin.getLogger().info("Player connecting with: ");
                plugin.getLogger().info(temp.toString());
                plugin.getLogger().info("------------");
            }


            plugin.getOnlineMembers().add(temp);
        }
    }
}
