package net.idrisdev.mc.ezteams.commands.teams.member;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Idris on 11/03/2017.
 */
public class MemberPointsCommand {

    private static EzTeams plugin = EzTeams.get();

    public MemberPointsCommand() {
    }

    public static CommandSpec buildMemberPointsCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_BASE)
                .description(Utils.getCmdDescription("Returns how many points the player has."))
                .arguments()
                .executor((src, args) -> {
                    if(!(src instanceof Player)) {
                        Utils.sendSrcErrorMessage(src,"Only players can have points, silly goose!");
                        return CommandResult.empty();
                    }
                    Member memb = Utils.findMember(src.getName());
                    int points = memb!=null?memb.getPoints():0;
                    Utils.sendPrettyMessage(src,"Your points are: "+points);

                    return CommandResult.success();
                })
                .build();
    }
}
