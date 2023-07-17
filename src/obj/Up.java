package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import GeBlock.Events;
import utilsGeB.GepUtil;
import utilsGeB.textUtil;

public class Up {
	/*Хочу получить:
	 * 0. Беспл. (респа каждые 5 сек.)
	 * 1. 10 exp + 16 cobble (респа каждые 4,25 сек.)
	 * 2. 50 exp + 64 cobble (респа каждые 3,5 сек.)
	 * 3. 250 exp + 256 cobble (респа каждые 2,75 сек.)
	 * 4. 1250 exp + 1024 cobble (респа каждые 2 сек.)
	 * 5. 6000 exp + 4096 cobble (респа каждые 1,25 сек.)
	 * 6. 24000 exp + 16384 cobble (респа каждые 0,5 сек.)
	 */
	int exp;
	int expcoef;
	HashMap<String,Integer> needs;
	int needsCoef;
	public Up(int Exp, int ExpC, HashMap<String,Integer> Needs, int NC){
		exp=Exp;
		expcoef=ExpC;
		needs=Needs;
		if(needs==null)needs=new HashMap<>();
		needsCoef=NC;
	}
	public boolean canUp(Player p, int lvl, boolean mes){
		PlayerInfo pi=Events.plist.get(p.getName());
		if(pi.exp>=exp(lvl)){
			for(String st:needs.keySet()){
				if(!GepUtil.haveItem(p, Material.getMaterial(st), needs.get(st)*lvl*needsCoef)){
					if(mes)p.sendMessage(ChatColor.GOLD+"Не хватает "+st+".");
					return false;
				}
			}
			return true;
		}else{
			if(mes)p.sendMessage(ChatColor.GOLD+"Не хватает "+(exp(lvl)-pi.exp)+" exp.");
		}
		return false;
	}
	public void up(Player p, int lvl){
		PlayerInfo pi=Events.plist.get(p.getName());
		pi.addExp(-exp*lvl*expcoef);
		for(String st:needs.keySet()){
			GepUtil.sellItems(p, Material.getMaterial(st), null, (int) (needs.get(st)*Math.pow(needsCoef,lvl)));
		}
	}
	public int exp(int lvl){
		return (int) (exp*Math.pow(expcoef,lvl));
	}
	public HashMap<String,Integer> needs(int lvl){
		HashMap<String,Integer> ret = new HashMap<>();
		for(String st:needs.keySet()){
			ret.put(st, (int) (needs.get(st)*Math.pow(needsCoef,lvl)));
		}
		return ret;
	}
	public List<String> lore(Player p, int lvl){
		PlayerInfo pi=Events.plist.get(p.getName());
		List<String> ret=new ArrayList<>();
		ret.add(ChatColor.AQUA+"Опыт: "+GepUtil.boolCol(pi.getExp()>=exp(lvl))+exp(lvl)+ChatColor.BLUE+" exp");
		HashMap<String,Integer> needs = needs(lvl);
		for(String st:needs.keySet()){
			ret.add(ChatColor.GRAY+textUtil.ruMaterial(st, true)+ChatColor.WHITE+" x"+GepUtil.boolCol(GepUtil.haveItem(p, Material.getMaterial(st), needs.get(st)))+needs.get(st));
		}
		return ret;
	}
}
