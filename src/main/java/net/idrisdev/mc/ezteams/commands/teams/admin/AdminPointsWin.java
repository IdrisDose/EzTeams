package net.idrisdev.mc.ezteams.commands.teams.admin;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Idris on 11/03/2017.
 */
public class AdminPointsWin {

    private static EzTeams plugin = EzTeams.get();

    public AdminPointsWin() {
    }

    public static CommandSpec buildAdminPointsWin() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN)
                .description(Utils.getCmdDescription("Used when a player wins a place."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("target"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("place")))
                )
                .executor((src, args) -> {
                    String target = args.<Player>getOne("target").isPresent()?args.<Player>getOne("target").get().getName():"none";
                    String place = args.<String>getOne("place").get().toLowerCase();

                    if(place.equals("def")||place.equals("default")){
                        place = "gen";
                    }
                    int points;

                    switch(place){
                        case "first":
                            points = plugin.core.getConfigManager().getConfig().pointsCfg.getFirstPlaceWin();
                            break;
                        case "second":
                            points = plugin.core.getConfigManager().getConfig().pointsCfg.getSecondPlaceWin();
                            break;
                        case "third":
                            points = plugin.core.getConfigManager().getConfig().pointsCfg.getThirdPlaceWin();
                            break;
                        case "gen":
                            points = plugin.core.getConfigManager().getConfig().pointsCfg.getGeneralPoints();
                        default:
                            points = 0;
                    }
                    return addPoints(src,target,points);
                })
                .build();
    }

    private static CommandResult addPoints(CommandSource src, String name, int points){

        if(points<0){
            Utils.sendSrcErrorMessage(src,"You cannot use negative numbers.");
            return CommandResult.success();
        }else  if(name.equals("none")){
            Utils.sendSrcErrorMessage(src,"You must specify a target");
            return CommandResult.success();
        }

        Member member = Utils.findMember(name);
        member.addMemberPoints(points);
        return CommandResult.success();
    }
}
