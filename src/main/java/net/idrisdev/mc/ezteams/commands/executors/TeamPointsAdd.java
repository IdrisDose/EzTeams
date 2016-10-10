package net.idrisdev.mc.ezteams.commands.executors;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.data.DataStorage;
import net.idrisdev.mc.ezteams.data.TeamData;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * Created by Idris on 7/10/2016.
 */
public class TeamPointsAdd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("team").get().toLowerCase();
        int points = args.<Integer>getOne("points").get();
        EzTeams.teams.stream().filter(t -> t.getNAME().toLowerCase().equals(name) ).forEach(t -> t.setPOINTS(t.getPOINTS()+points));
        ETUtils.game.getCommandManager().process(src.getCommandSource().get(), "teams points view "+name);
        ETUtils.game.getCommandManager().process(ETUtils.getConsoleSrc(),"broadcast "+name+" just got "+points+"points! ~ Congratz!");
        return CommandResult.success();
    }
}
