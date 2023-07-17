package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import GeBlock.main;
import utilsGeB.GepUtil;
import utilsGeB.textUtil;

public class PlayerInfo {
	public HashMap<String,Integer> timers=new HashMap<>();
	public HashMap<String,Integer> fastTimers=new HashMap<>();
	public List<String> bools=new ArrayList<>();
	public HashMap<String,Long> realTimers = new HashMap<>();
    public HashMap<String,Integer> waits = new HashMap<>();
    public ArrayList<Location> gebs=new ArrayList<>();
    public List<String> achievements=new ArrayList<>();
    public List<String> collects=new ArrayList<>();
    public String pname;
    int exp=0;
    public Location lastClickedBlock=null;
    public UUID id;
    public PlayerInfo(String p){
    	if(p==null)return;
    	pname=p;
    	id=Bukkit.getPlayer(p).getUniqueId();
    }
	public PlayerInfo(FileConfiguration conf, Player p){
		pname=p.getName();
		if(conf.contains("timers"))for(String st:conf.getConfigurationSection("timers").getKeys(false)){
			timers.put(st, conf.getInt("timers."+st));
		}
		if(conf.contains("fastTimers"))for(String st:conf.getConfigurationSection("fastTimers").getKeys(false)){
			fastTimers.put(st, conf.getInt("fastTimers."+st));
		}
		if(conf.contains("waits"))for(String st:conf.getConfigurationSection("waits").getKeys(false)){
			waits.put(st, conf.getInt("waits."+st));
		}
		if(conf.contains("bools"))bools=conf.getStringList("bools");
		if(conf.contains("realTimers"))for(String st:conf.getConfigurationSection("realTimers").getKeys(false)){
			realTimers.put(st,conf.getLong("realTimers."+st));
		}
		if(conf.contains("gebs"))for(String st:conf.getConfigurationSection("gebs").getKeys(false)){
			gebs.add(GepUtil.getLocFromConf(conf, "gebs."+st));
		}
		if(conf.contains("achs"))achievements=conf.getStringList("achs");
		if(conf.contains("collects"))collects=conf.getStringList("collects");
		
		exp=conf.getInt("exp");
		p.setLevel(exp);
	}
	public void save(FileConfiguration conf){
		conf.set("timers",null);
		for(String st:timers.keySet()){
			conf.set("timers."+st,timers.get(st));
		}
		conf.set("fastTimers",null);
		for(String st:fastTimers.keySet()){
			conf.set("fastTimers."+st,fastTimers.get(st));
		}
		conf.set("waits",null);
		for(String st:waits.keySet()){
			conf.set("waits."+st,waits.get(st));
		}
		conf.set("bools",bools);
		conf.set("realTimers",null);
		for(String st:realTimers.keySet()){
			conf.set("realTimers."+st,realTimers.get(st));
		}
		int i=0;
		conf.set("gebs", null);
		for(Location l:gebs){
			GepUtil.saveLocToConf(conf, "gebs."+i, l);
			i++;
		}
		conf.set("achs",achievements);
		conf.set("collects",collects);
		conf.set("exp",exp);
	}
    public void setbool(String bool, boolean set){
    	if(set)
    		if(!bools.contains(bool))bools.add(bool);
    	else if(bools.contains(bool))bools.remove(bool);
    }
    public void changeBool(String bool){
    	setbool(bool, !bools.contains(bool));
    }
    public int getExp(){
    	return exp;
    }
    public void addExp(int add){
    	exp+=add;
    	if(exp<0)exp=0;
    	Bukkit.getPlayer(pname).setLevel(exp);
    }
    public boolean addAchievement(String name){
    	if(achievements.contains(name))return false;
    	Player p=Bukkit.getPlayer(pname);
    	if(!main.pr.achs.containsKey(name)){
    		p.sendMessage(ChatColor.RED+"Ошибка при определении достижения '"+ChatColor.GOLD+name+ChatColor.RED+"'. Не надо пробовать ещё раз, сообщите об этом Гепи!");
    		return false;
    	}
    	p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
    	Achievement ach=main.pr.achs.get(name);
    	if(ach.isHard)p.sendTitle(ChatColor.DARK_PURPLE+"Получено "+ChatColor.DARK_RED+ChatColor.BOLD+"СЛОЖНОЕ"+ChatColor.DARK_PURPLE+" достижение!", ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+name, 20, 40, 20);
    	else p.sendTitle(ChatColor.LIGHT_PURPLE+"Получено достижение!", ChatColor.GOLD+name, 20, 40, 20);
    	p.sendMessage(ChatColor.DARK_PURPLE+"~~~~"+ChatColor.AQUA+"Достижение! "+ChatColor.YELLOW+ChatColor.BOLD+"["+ChatColor.GOLD+name+ChatColor.YELLOW+ChatColor.BOLD+"]"+ChatColor.DARK_PURPLE+"~~~~");
    	for(String st:ach.lore){
    		p.sendMessage(st);
    	}
    	if(ach.exp>0){
    		p.sendMessage(ChatColor.AQUA+"+"+ach.exp+" exp!");
    		addExp(ach.exp);
    		if(ach.rewards.size()>0){
    			p.sendMessage(ChatColor.GOLD+"+предметы, которые вы заберёте "+ChatColor.DARK_GREEN+"в пункте приёма на спавне!");
    			collects.add("Ach"+name);
    		}
    	}
    	if(ach.isHard)GepUtil.globMessage(ChatColor.GOLD+""+ChatColor.BOLD+p.getName()+ChatColor.DARK_RED+" ПОЛУЧИЛ СЛОЖНОЕ ДОСТИЖЕНИЕ '"+textUtil.chered(name, ChatColor.GOLD, ChatColor.YELLOW)+ChatColor.DARK_RED+"'!!!", Sound.BLOCK_FIRE_EXTINGUISH, 10, 2, null, null, 0, 0, 0);
    	else sendToMembers(ChatColor.DARK_GREEN+pname+ChatColor.AQUA+" получил достижение "+ChatColor.GOLD+name);
    	p.sendMessage(ChatColor.DARK_PURPLE+"~~~~~~~~~"+ChatColor.YELLOW+ChatColor.BOLD+"["+ChatColor.GOLD+name+ChatColor.YELLOW+ChatColor.BOLD+"]"+ChatColor.DARK_PURPLE+"~~~~~~~~~");
    	achievements.add(name);
    	return true;
    }
    public void sendToMembers(String mes){
		Island is=ASkyBlockAPI.getInstance().getIslandOwnedBy(id);
		for(UUID i:is.getMembers()){
			if(Bukkit.getPlayer(i)!=null&&i!=id){
				Player p=Bukkit.getPlayer(i);
				if(p.isOnline())p.sendMessage(mes);
			}
		}
	}
}
