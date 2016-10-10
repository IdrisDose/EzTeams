package net.idrisdev.mc.ezteams.commands;

import net.idrisdev.mc.ezteams.data.DAOH2;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Idris on 10/10/2016.
 */
public class DebugDBUCmd extends ETUtils implements CommandCallable {
    private final Optional<Text> desc = Optional.of(Text.of("Debugs the db and can execute updating queries!"));
    private final Optional<Text> help = Optional.of(Text.of("enter raw update delete drop sql"));
    private final Text usage = Text.of("<query>");

    @Override
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        int i = DAOH2.executeUQuery(arguments);

        sendSrcPlainMessage(src,"RESULT: "+i);
        return CommandResult.success();
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
