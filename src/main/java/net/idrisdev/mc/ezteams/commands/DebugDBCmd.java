package net.idrisdev.mc.ezteams.commands;

import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;

/**
 * Created by Idris on 10/10/2016.
 */
public class DebugDBCmd extends Utils implements CommandCallable {
    private final Optional<Text> desc = Optional.of(Text.of("Debugs the db and can execute non updating queries!"));
    private final Optional<Text> help = Optional.of(Text.of("enter raw sql or reload to reload the system, syncteams or sync players"));
    private final Text usage = Text.of("<query>");

    @Override
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        return CommandResult.empty();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return true;
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
        return usage;
    }
}
