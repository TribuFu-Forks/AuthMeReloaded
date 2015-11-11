package fr.xephi.authme.command.executable.authme;

import org.bukkit.command.CommandSender;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.command.CommandParts;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;

public class SetEmailCommand extends ExecutableCommand {

    /**
     * Execute the command.
     *
     * @param sender           The command sender.
     * @param commandReference The command reference.
     * @param commandArguments The command arguments.
     *
     * @return True if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean executeCommand(CommandSender sender, CommandParts commandReference, CommandParts commandArguments) {
        // AuthMe plugin instance
        AuthMe plugin = AuthMe.getInstance();

        // Messages instance
        Messages m = Messages.getInstance();

        // Get the player name and email address
        String playerName = commandArguments.get(0);
        String playerEmail = commandArguments.get(1);

        // Validate the email address
        if (!Settings.isEmailCorrect(playerEmail)) {
            m.send(sender, "email_invalid");
            return true;
        }

        // Validate the user
        PlayerAuth auth = plugin.database.getAuth(playerName.toLowerCase());
        if (auth == null) {
            m.send(sender, "unknown_user");
            return true;
        }

        // Set the email address
        auth.setEmail(playerEmail);
        if (!plugin.database.updateEmail(auth)) {
            m.send(sender, "error");
            return true;
        }

        // Update the player cache
        if (PlayerCache.getInstance().getAuth(playerName.toLowerCase()) != null)
            PlayerCache.getInstance().updatePlayer(auth);

        // Show a status message
        m.send(sender, "email_changed");
        return true;
    }
}