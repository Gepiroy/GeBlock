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
						p.sendMessage(ChatColor.LIGHT_PURPLE+"��-��, �� �� ���� �����, ������� ��. ������ �� ������� �� ������� � "+args[0]+" ����, �����.");
						return true;
					}
					Player tp=Bukkit.getPlayer(args[0]);
					if(tp==null){
						p.sendMessage(ChatColor.BLUE+"�� ��������� ������ ���� ���� ��������, �������� ��� � ����. ��������� �� ������ ���?");
						return true;
					}
					PlayerInfo tpi=Events.plist.get(tp.getName());
					if(pi.getExp()<send){
						p.sendMessage(ChatColor.GOLD+"� ��� �� ��� ����� �����, �� ��������. ��-�� ���, �������� ���� ���������� ���������� ����� ������ ���.");
						return true;
					}
					tpi.addExp(send);
					pi.addExp(-send);
					p.sendMessage(ChatColor.AQUA+"���� ��� ������.");
					tp.sendMessage(ChatColor.AQUA+"�� �������� "+send+" ����� �� "+ChatColor.GOLD+p.getName());
				}else{
					p.sendMessage(ChatColor.GOLD+"������� ����� ����� ����� �� ������ ���������.");
				}
			}
			else{
				p.sendMessage(ChatColor.GOLD+"/sendexp <��� ������> <����������>");
			}
		}
		return true;
	}
	
}
