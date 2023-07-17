package cmds;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import GeBlock.Events;
import obj.PlayerInfo;
import utilsGeB.ItemUtil;

public class menu implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p=(Player) sender;
			PlayerInfo pi=Events.plist.get(p.getName());
			if(pi.timers.containsKey("menu")){
				p.sendMessage(ChatColor.GOLD+"Нечего тут менюшкам инфляцию повышать. Жди хотя бы "+pi.timers.get("menu")+" секунд или попроси у друга.");
				return true;
			}
			pi.timers.put("menu", 60);
			p.getInventory().addItem(ItemUtil.create(Material.EYE_OF_ENDER, 1, 0, ChatColor.DARK_GREEN+"Менюшка", new String[]{
					ChatColor.AQUA+"/menu, чтобы меня получить!"
			}, null, 0));
			p.getInventory().addItem(ItemUtil.create(Material.BEDROCK, 1, 0, ChatColor.AQUA+"GeBlock", new String[]{
					ChatColor.GREEN+"Твоя личная автошахта!",
					ChatColor.AQUA+"Просто поставь ГеБлок!"
			}, null, 0));
		}
		return true;
	}
}
