package cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class spawn implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p=(Player) sender;
			p.teleport(Bukkit.getWorld("world").getSpawnLocation().clone().add(0.5, 0.1, 0.5));
		}
		return true;
	}
}
