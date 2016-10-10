package net.idrisdev.mc.ezteams.commands;

import net.idrisdev.mc.ezteams.commands.executors.*;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * Created by nzdos_000 on 6/10/2016.
 */
public class TeamCommands extends ETUtils {
    public CommandSpec teams;
    public CommandSpec teamJoin;
    public CommandSpec teamsList;
    public CommandSpec teamPoints;
    public TeamLeaveCommand teamLeave = new TeamLeaveCommand();
    public TeamCreditsCommand teamCredits = new TeamCreditsCommand();

    public TeamCommands(){

    }

    public void buildCommands() {

        CommandSpec teamMemberList = CommandSpec.builder()
                .permission(TEAMS_MEMBER_LIST)
                .description(Text.of("Lists members of a specified team."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))))
                .executor(new TeamMembersList())
                .build();

        CommandSpec teamMemberAdd = CommandSpec.builder()
                .permission(TEAMS_MEMBER_ADD)
                .description(Text.of("Add a player to a team"))
                .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))))
                .executor(new TeamMemberAdd())
                .build();

        CommandSpec teamMemberRemove = CommandSpec.builder()
                .permission(TEAMS_MEMBER_REMOVE)
                .description(Text.of("Remove a team member from specified team."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team")))
                )

                .executor(new TeamMemberRemove())
                .build();


        CommandSpec teamMember = CommandSpec.builder()
                .permission(TEAMS_MEMBER_BASE)
                .description(Text.of("Manage teams member"))
                .child(teamMemberAdd, "add","a")
                .child(teamMemberRemove, "remove", "r")
                .child(teamMemberList, "list","r")
                .build();

        /**
         * TeamData Points Management Commands
         * Add - Adds points to a TeamData
         * Remove - Remove points from a team;
         */
        CommandSpec teamPointsAdd = CommandSpec.builder()
                .permission(TEAMS_POINTS_ADD)
                .description(Text.of("Add TeamData Points"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("points")))
                )
                .executor(new TeamPointsAdd())
                .build();

        CommandSpec teamPointsRemove = CommandSpec.builder()
                .permission(TEAMS_POINTS_REMOVE)
                .description(Text.of("Remove TeamData Points"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("points")))
                )
                .executor(new TeamPointsRemove())
                .build();
        CommandSpec teamPointsView = CommandSpec.builder()
                .permission(TEAMS_POINTS_VIEW)
                .description(Text.of("View Your TeamData Points"))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))))
                .executor(new TeamPointsView())
                .build();

        teamPoints = CommandSpec.builder()
                .permission(TEAMS_POINTS_BASE)
                .description(Text.of("Manage TeamData Points"))
                .child(teamPointsView, "view", "v")
                .child(teamPointsAdd, "add","a")
                .child(teamPointsRemove,"remove","r")
                .build();

        /**
         * Main TeamData Command
         */

        teamsList = CommandSpec.builder()
                .permission(TEAMS_LIST)
                .description(Text.of("Lists all the available teams"))
                .arguments()
                .executor(new TeamsList())
                .build();

        teamJoin = CommandSpec.builder()
                .permission(TEAMS_JOIN)
                .description(Text.of("Joins a Specified TeamData"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("team")))
                )
                .executor(new TeamJoin())
                .build();


        teams = CommandSpec.builder()
                .permission(TEAMS_BASE)
                .description(Text.of("Main TeamData command."))
                .child(teamMember, "member", "m")
                .child(teamPoints, "points", "p")
                .child(teamJoin, "join", "j")
                .child(teamLeave, "leave", "l")
                .executor(new TeamsExecutor())
                .build();
    }
}
