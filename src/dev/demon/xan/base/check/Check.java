package dev.demon.xan.base.check;

import dev.demon.xan.Xan;
import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.AnticheatListener;
import dev.demon.xan.base.user.User;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

public abstract class Check implements AnticheatListener {

    public double violation;
    public boolean enabled, experimental, ban;

    private boolean isTestServer = false;

    public Check() {
        enabled = true;
        ban = false;
    }

    public String getName() {
        return CheckManager.getCheckInfo(this).name();
    }

    public String getType() {
        return CheckManager.getCheckInfo(this).type();
    }

    public abstract void onHandle(User user, AnticheatEvent e);

    protected void alert(User user, String... strings) {
        if (user.isBypass()) {
            return;
        }

        StringBuilder dataStr = new StringBuilder();
        for (String s : strings) {
            dataStr.append(s).append((strings.length == 1 ? "" : ", "));
        }
        String alert = ChatColor.RED + "Xan" + ChatColor.RED + " -> " + ChatColor.WHITE + user.getPlayer().getName() + ChatColor.GRAY + " has flagged " + ChatColor.WHITE + getName() + ChatColor.WHITE + " " + ChatColor.WHITE + getType() + ChatColor.RED + " (x" + user.getViolation() + ")";


        TextComponent textComponent = new TextComponent(alert);

        if (dataStr.length() > 0) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + dataStr.toString()).create()));
        }

        Xan.getInstance().getUserManager().getUsers().stream().parallel().filter(staff -> (staff.getPlayer().hasPermission("demon.alerts") && staff.isAlerts())).forEach(staff -> staff.getPlayer().spigot().sendMessage(textComponent));

        user.setViolation(user.getViolation() + 1);
    }
}