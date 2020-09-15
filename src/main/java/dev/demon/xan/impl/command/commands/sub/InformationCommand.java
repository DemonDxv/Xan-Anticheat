package dev.demon.xan.impl.command.commands.sub;

import dev.demon.xan.Xan;
import dev.demon.xan.api.check.Check;
import dev.demon.xan.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InformationCommand {

    public void execute(String[] args, String s, CommandSender commandSender) {
        try {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                commandSender.sendMessage(ChatColor.GRAY + "Known information for " + ChatColor.RED + target.getName());

                User targetUser = Xan.getInstance().getUserManager().getUser(target.getUniqueId());

                if (targetUser != null) {

                    int max = 6;

                    commandSender.sendMessage(ChatColor.GRAY + "Ping: " + ChatColor.GREEN + targetUser.getLagProcessor().getLastTransaction());
                    commandSender.sendMessage(ChatColor.GRAY + "Violations: " + ChatColor.GREEN + targetUser.getViolation());
                    commandSender.sendMessage(ChatColor.GRAY + "Lagging: " + (targetUser.getLagProcessor().isLagging() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
                    commandSender.sendMessage(ChatColor.GRAY + "Recent Checks flagged: " + ChatColor.GREEN + max+"/"+ targetUser.getFlaggedChecks().size());

                    HashMap<Check, Integer> tmp = new HashMap<>();
                    AtomicInteger total = new AtomicInteger();

                    targetUser.getFlaggedChecks().forEach((c, i) -> {
                        if (total.get() <= max) {
                            tmp.put(c, i);
                        }
                        total.getAndIncrement();
                    });

                    tmp.forEach((c, v) -> commandSender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + c.getName() + "("+c.getType()+")" + ChatColor.RED + " x"+v));
                }
            } else commandSender.sendMessage(ChatColor.RED + "Target is not online.");
        } catch (Exception ingored) {
            commandSender.sendMessage(ChatColor.RED + "Please supply a players name!");
        }
    }
}
