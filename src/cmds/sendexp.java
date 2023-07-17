package cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import GeBlock.Events;
import obj.PlayerInfo;
import utilsGeB.GepUtil;

public class sendexp implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p=(Player) sender;
			PlayerInfo pi=Events.plist.get(p.getName());
			if(args.length>=2){
				if(GepUtil.isNumeric(args[1])){
					int send=GepUtil.intFromString(args[1]);
					if(send<=0){
						p.sendMessage(ChatColor.LIGHT_PURPLE+"Да-да, мы не учли этого, конечно же. Сейчас ты конечно же украдёшь у "+args[0]+" опыт, агась.");
						return true;
					}
					Player tp=Bukkit.getPlayer(args[0]);
					if(tp==null){
						p.sendMessage(ChatColor.BLUE+"Вы пытаетесь отдать свой опыт призраку, которого нет в сети. Правильно ли указан ник?");
						return true;
					}
					PlayerInfo tpi=Events.plist.get(tp.getName());
					if(pi.getExp()<send){
						p.sendMessage(ChatColor.GOLD+"У вас не так много опыта, уж поверьте. Всё-же код, читающий вашу внутреннюю информацию будет правее вас.");
						return true;
					}
					tpi.addExp(send);
					pi.addExp(-send);
					p.sendMessage(ChatColor.AQUA+"Опыт был выслан.");
					tp.sendMessage(ChatColor.AQUA+"Вы получили "+send+" опыта от "+ChatColor.GOLD+p.getName());
				}else{
					p.sendMessage(ChatColor.GOLD+"Укажите целое число опыта во втором аргументе.");
				}
			}
			else{
				p.sendMessage(ChatColor.GOLD+"/sendexp <Ник игрока> <Количество>");
			}
		}
		return true;
	}
	
}
