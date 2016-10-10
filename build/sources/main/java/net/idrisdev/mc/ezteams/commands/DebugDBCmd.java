package net.idrisdev.mc.ezteams.commands;

import net.idrisdev.mc.ezteams.data.DAOH2;
import net.idrisdev.mc.ezteams.data.DataStorage;
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
public class DebugDBCmd extends ETUtils implements CommandCallable {
    private final Optional<Text> desc = Optional.of(Text.of("Debugs the db and can execute non updating queries!"));
    private final Optional<Text> help = Optional.of(Text.of("enter raw sql or reload to reload the system, syncteams or sync players"));
    private final Text usage = Text.of("<query>");

    @Override
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        if(arguments.toLowerCase().equals("reload"))
            new DataStorage().reloadPlz();
        else if(arguments.toLowerCase().equals("syncteams"))
            new DataStorage().syncTeamsDB();
        else if(arguments.toLowerCase().equals("syncplayers"))
            new DataStorage().syncPlayersDB();
        else{
            ResultSet result = DAOH2.executeQuery(arguments);
            if (result != null)
                try {
                    while (result.next())
                        sendSrcPlainMessage(src, "RESULT: " + result.getRow());

                    DAOH2.con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            else
                sendSrcErrorMessage(src, "NIL");
        }

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
