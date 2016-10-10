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
 * Created by nzdos_000 on 6/10/2016.
 */
public class TeamCreditsCommand extends ETUtils implements CommandCallable {

    private final Optional<Text> desc = Optional.of(Text.of("Displays the author of the teams command!"));
    private final Optional<Text> help = Optional.of(Text.of("Displays the author of the teams command!"));

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        source.sendMessage(Text.of("Ez-Teams "+getVersion()+" by Idris_"));
        return CommandResult.empty();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return Collections.emptyList();
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
        return null;
    }
}
