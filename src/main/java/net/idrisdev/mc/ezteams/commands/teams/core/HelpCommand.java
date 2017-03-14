package net.idrisdev.mc.ezteams.commands.teams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandSpec;

/**
 * Created by Idris on 11/03/2017.
 */
public class HelpCommand {

    private static EzTeams plugin = EzTeams.get();

    public HelpCommand() {
    }

    public static CommandSpec buildHelpCommand() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_BASE)
                .description(Utils.getCmdDescription("Help for teams"))
                .arguments()
                .executor((src, args) -> {
                    sendBaseHelp(src);
                    if(src.hasPermission(Permissions.TEAMS_ADMIN))
                        sendAdminHelp(src);

                    if(src.hasPermission(Permissions.TEAMS_SUDO))
                        sendSudoHelp(src);

                    return CommandResult.success();
                })
                .build();
    }

    private static String convertBoolToString(boolean b) {
        return b ?"Yes":"No";
    }

    private static void sendBaseHelp(CommandSource src){
        Utils.sendPrettyMessage(src,"----EzTeams Help----");
        Utils.sendPrettyMessage(src,"-Commands-");
        Utils.sendPrettyMessage(src,"-join : Allows you to join a team.");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_JOIN)));
        Utils.sendPrettyMessage(src," ");

        Utils.sendPrettyMessage(src,"-leave : Allows you to leave a team.");
        Utils.sendPrettyMessage(src, "You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_LEAVE)));
        Utils.sendPrettyMessage(src," ");

        Utils.sendPrettyMessage(src,"-list|standings : List all teams and their points");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_LEAVE)));
        Utils.sendPrettyMessage(src," ");

        Utils.sendPrettyMessage(src,"-points : List your current points");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_POINTS_VIEW)));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-------------------");
    }

    private static void sendAdminHelp(CommandSource src){
        Boolean setTeam = src.hasPermission(Permissions.TEAMS_ADMIN_ADD);
        Boolean removeTeam = src.hasPermission(Permissions.TEAMS_ADMIN_REMOVE);
        Boolean points = src.hasPermission(Permissions.TEAMS_ADMIN_MEMBER_POINS);
        Boolean win = src.hasPermission(Permissions.TEAMS_ADMIN_MEMBER_WIN);

        Utils.sendPrettyMessage(src,"----EzTeams Admin Help----");
        Utils.sendPrettyMessage(src,"-Commands-");
        Utils.sendPrettyMessage(src,"-set|add : Allows you to set a members team");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(setTeam));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-remove : Allows you to remove a player from a team");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(removeTeam));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-points player add|remove : Allows you to modify a members points");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(points));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-win position : Adds a preset amount of points to a player");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(win));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-------------------");
    }

    private static void sendSudoHelp(CommandSource src){
        Utils.sendPrettyMessage(src,"----EzTeams  Help----");
        Utils.sendPrettyMessage(src,"-Commands-");
        Utils.sendPrettyMessage(src,"-create <name> <prefix> : Allows you to create a new team.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-delete <name> : Allows you to delete a team.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-reset all|<name> : Allows you to reset a specific team and it's members or resets all points.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-prefix|setprefix <name> | Allows you to change a teams prefix.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-------------------");
    }
}
