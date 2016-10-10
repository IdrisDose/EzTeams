package net.idrisdev.mc.ezteams.commands.executors;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * Created by Idris on 7/10/2016.
 */
public class TeamPointsView extends ETUtils implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("team").get().toLowerCase();
        if(name.equals("all"))
            if (EzTeams.teams.isEmpty())
                sendSrcErrorMessage(src, "There appear to be no teams! - Notify a server admin!");
            else
                EzTeams.teams.stream().forEach(team -> ETUtils.sendSrcPlainMessage(src, team.toString()));
        else
            EzTeams.teams.stream().filter(f->f.getNAME().toLowerCase().equals(name)).forEach(team -> ETUtils.sendSrcPlainMessage(src, team.toString()));
        return CommandResult.success();
    }
}
