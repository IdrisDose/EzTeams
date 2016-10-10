package net.idrisdev.mc.ezteams.commands.executors;

import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Idris on 7/10/2016.
 */
public class TeamLeaveCommand extends ETUtils implements CommandCallable {
    private final Optional<Text> desc = Optional.of(Text.of("Abandons your team... Forever!"));
    private final Optional<Text> help = Optional.of(Text.of("Abandons your team... Forever!"));

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        source.sendMessage(Text.of("You have abandoned your team, in result lost your honor and points!"));
        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return Collections.emptyList();
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission(TEAMS_LEAVE);
    }

    @Override
    public Optional<? extends Text> getShortDescription(CommandSource source) {
        return desc;
    }

    @Override
    public Optional<? extends Text> getHelp(CommandSource source) {
        return help;
    }

    @Override
    public Text getUsage(CommandSource source) {
        return null;
    }
}
