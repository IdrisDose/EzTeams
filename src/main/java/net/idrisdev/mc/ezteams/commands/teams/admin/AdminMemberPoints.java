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

/**
 * Created by Idris on 22/01/2017.
 */
public class AdminMemberPoints {

    private static EzTeams plugin = EzTeams.get();

    public AdminMemberPoints() {
    }

    public static CommandSpec buildAdminMemberPoints() {
        return CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN_MEMBER_POINS)
                .description(Utils.getCmdDescription("Add, Set or Remove a Players points."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("action"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("target"))),
                        GenericArguments.optional(GenericArguments.integer(Text.of("points")))
                )
                .executor((src, args) -> {
                    String action = args.<String>getOne("action").get();
                    String target = args.<Player>getOne("target").isPresent()?args.<Player>getOne("target").get().getName():"none";
                    int points = args.<Integer>getOne("points").isPresent()?args.<Integer>getOne("points").get():0;

                    action = action.toLowerCase();

                    switch(action){
                        case"?":
                            return help(src);
                        case"help":
                            return help(src);
                        case"add":
                            return addPoints(src,target,points);
                        case"remove":
                            return removePoints(src,target,points);
                        case"set":
                            return setPoints(src,target,points);
                        default:
                            Utils.sendSrcErrorMessage(src,"Invalid action!");
                            return help(src);
                    }
                })
                .build();
    }

    private static CommandResult help(CommandSource src){
        Utils.sendPrettyMessage(src,"---Your Available options are---");
        Utils.sendPrettyMessage(src,"Add - Add points to a specific player");
        Utils.sendPrettyMessage(src,"Remove - Remove points from a specific player");
        Utils.sendPrettyMessage(src,"Set - Set a players team points");
        return CommandResult.success();

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
    private static CommandResult removePoints(CommandSource src, String name, int points){

        if(points<0){
            Utils.sendSrcErrorMessage(src,"You cannot use negative numbers.");
            return CommandResult.success();
        }else  if(name.equals("none")){
            Utils.sendSrcErrorMessage(src,"You must specify a target");
            return CommandResult.success();
        }

        Member member = Utils.findMember(name);
        member.removeMemberPoints(points);
        return CommandResult.success();
    }
    private static CommandResult setPoints(CommandSource src, String name, int points){
        if(!src.hasPermission(Permissions.TEAMS_SUDO_POINTS_SET)){
            Utils.sendSrcErrorMessage(src,"No permission");
            return CommandResult.success();
        }


        if(points<0){
            Utils.sendSrcErrorMessage(src,"You cannot use negative numbers.");
            return CommandResult.success();
        }else  if(name.equals("none")){
            Utils.sendSrcErrorMessage(src,"You must specify a target");
            return CommandResult.success();
        }

        Member member = Utils.findMember(name);
        member.setMemberPoints(points);
        return CommandResult.success();
    }
}
