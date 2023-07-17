package obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import GeBlock.main;
import utilsGeB.GepUtil;
import utilsGeB.ItemUtil;

public class GeBlock {
	public Location loc;
	int toSpawn=0;
	public int spawnRateLevel=0;
	public int boost=0;
	public Up speedUp;
	public Up cUp;
	public int cUped;
	public int event=9;
	public UUID owner;
	public HashMap<String,Integer> cCoefs = new HashMap<>();
	public int chancesPoints=0;
	HashMap<String,Integer> timers=new HashMap<>();
	HashMap<String,Integer> waits=new HashMap<>();
	public List<String> ups=new ArrayList<>();
	public int upslots=1;
	public Up sUp;
	public GeBlock(){}
	
	public GeBlock(Player p, Location Loc){
		p.sendMessage(ChatColor.AQUA+"ГеБлок установлен!");
		owner=p.getUniqueId();
		loc=Loc;
		loadPrices();
	}
	public GeBlock(FileConfiguration conf, String st){
		if(conf==null)return;
		spawnRateLevel=conf.getInt(st+".spawnRateLevel");
		boost=conf.getInt(st+".boost");
		loc=GepUtil.getLocFromConf(conf, st+".loc");
		chancesPoints=conf.getInt(st+".chancesPoints");
		cUped=conf.getInt(st+".cUped");
		if(conf.contains(st+".cCoefs"))for(String s:conf.getConfigurationSection(st+".cCoefs").getKeys(false)){
			cCoefs.put(s, conf.getInt(st+".cCoefs."+s));
		}
		if(conf.contains(st+".ups")){
			ups=conf.getStringList(st+".ups");
		}
		owner=UUID.fromString(conf.getString(st+".owner"));
		loadPrices();
	}
	void loadPrices(){
		{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("COBBLESTONE", 16);
			speedUp=new Up(10, 5, Needs, 4);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("EMERALD", 4);
			cUp=new Up(125, 2, Needs, 2);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("DIAMOND", 4);
			sUp=new Up(125, 4, Needs, 4);
		}
	}
	int secRate=0;
	public void run(){
		secRate++;
		if(secRate>=20){
			secRate=0;
			event--;
			for(String st:new ArrayList<>(timers.keySet())){
				if(GepUtil.HashMapReplacer(timers, st, -1, true, false)){
					
				}
			}
			if(event<=0){
				event=700+new Random().nextInt(300);
				if(ups.contains("Страховка"))return;
				String[] evs={"break","water"};
				String ev=evs[new Random().nextInt(evs.length)];
				if(ev.equals("water")){
					if(loc.getBlock().getType().equals(Material.AIR)){
						String[] types={"water","water","water","water","water","water","water","water","lava","slime"};
						String type=types[new Random().nextInt(types.length)];
						String pref=ChatColor.AQUA+"["+ChatColor.YELLOW+"УТЕЧКА"+ChatColor.AQUA+"] ";
						if(type.equals("water")){
							sendToIs(pref+ChatColor.AQUA+"Из ГеБлока вылилась вода!");
							loc.getBlock().setType(Material.STATIONARY_WATER);
						}
						if(type.equals("lava")){
							sendToIs(pref+ChatColor.GOLD+"Из ГеБлока вылилась лава!");
							loc.getBlock().setType(Material.STATIONARY_LAVA);
						}
						if(type.equals("slime")){
							sendToIs(pref+ChatColor.DARK_GREEN+"Из ГеБлока вылилась слизь!");
							loc.getBlock().setType(Material.SLIME_BLOCK);
						}
					}else{
						sendToIs(ChatColor.AQUA+"["+ChatColor.YELLOW+"УТЕЧКА"+ChatColor.AQUA+"] "+ChatColor.GRAY+"Из ГеБлока могла произойти утечка. Это - шанс получить новую лаву или слизь! Но она была предотвращена блоком, стоящим на месте ГеБлока.");
					}
				}else if(ev.equals("break")){
					sendToMembers(ChatColor.AQUA+"["+ChatColor.GOLD+"ПОЛОМКА"+ChatColor.AQUA+"] "+ChatColor.RED+"ГеБлок временно выведен из строя. Он сам починится через минуту, но сейчас он продолжает тратить свои очки ускорения.");
					//timers.put("break", 60);
				}
			}
		}
		if(ups.contains("Бешенство")){
			toSpawn--;
		}
		if(boost>0){
			boost--;
		}
		if(timers.containsKey("break")){
			loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, center(), 1, 0.3, 0.3, 0.3, 0.0);
			return;
		}
		if(loc.getBlock()!=null&&loc.getBlock().getType().equals(Material.AIR)){
			if(boost>0){
				toSpawn--;
			}
			toSpawn--;
			loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, center(), 1, 0.2, 0.2, 0.2, 0.01);
			if(toSpawn<=0){
				spawn();
				toSpawn=100-spawnRateLevel*15;
			}
		}else{
			loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, center(), 1, 0.3, 0.3, 0.3, 0.0);
		}
	}
	public void spawn(){
		loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, center(), 25, 0.1, 0.1, 0.1, 0.1);
		loc.getWorld().playSound(center(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 2);
		HashMap<String,Integer> chances=new HashMap<>();
		chances.put("STONE", 1000);
		chances.put("COAL_ORE", 100);
		chances.put("IRON_ORE", 50);
		chances.put("GOLD_ORE", 20);
		chances.put("LAPIS_ORE", 25);
		chances.put("REDSTONE_ORE", 5);
		chances.put("DIAMOND_ORE", 2);
		chances.put("EMERALD_ORE", 1);
		for(String st:cCoefs.keySet()){
			chances.replace(st, chances.get(st)*(cCoefs.get(st)+1));
		}
		for(String st:main.boosts.keySet()){
			chances.replace(st, chances.get(st)*2);
		}
		String drop=GepUtil.chancesByCoef(chances);
		if(!drop.equals("STONE")&&ups.contains("Юбилейное")){
			GepUtil.HashMapReplacer(waits, "ubil", 1, false, false);
			if(waits.get("ubil")==10){
				waits.remove("ubil");
				GepUtil.randFirework(loc.clone().add(0.5, 0.5, 0.5));
				loc.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT));
			}
		}
		if(drop.equals("GOLD_ORE")&&ups.contains("Золотая лихорадка")){
			drop="GOLD_BLOCK";
			sendToOwner(ChatColor.AQUA+"["+ChatColor.GOLD+"ЗОЛОТАЯ ЛИХОРАДКА"+ChatColor.AQUA+"] "+ChatColor.GOLD+"ГеБлок забился золотом и перегружен на 30 секунд!");
			timers.put("break", 30);
		}
		if(drop.equals("STONE")&&ups.contains("Дробилка"))drop="GRAVEL";
		Location lset=loc.clone().add(0.5, 0.5, 0.5);
		if(drop.equals("DIAMOND_ORE")&&ups.contains("Денежный поток")){
			drop="EMERALD_ORE";
			ExperienceOrb orb=(ExperienceOrb) loc.getWorld().spawnEntity(lset, EntityType.EXPERIENCE_ORB);
			orb.setExperience(5);
		}
		Material mat=Material.getMaterial(drop);
		if(ups.contains("Бешенство")){
			lset.add(new Random().nextInt(3)-1, new Random().nextInt(3)-1, new Random().nextInt(3)-1);
		}
		if(!lset.getBlock().getType().equals(Material.AIR)){
			if(new Random().nextBoolean()){
				lset.getWorld().playSound(lset,Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 2, 1);
				lset.getBlock().setType(mat);
				lset.getWorld().spawnParticle(Particle.SMOKE_LARGE, lset, 50, 0.25, 0.25, 0.25, 0.2);
			}else{
				lset.getWorld().playSound(lset,Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 2, 1);
				lset.getWorld().spawnParticle(Particle.SMOKE_LARGE, lset, 25, 0.25, 0.25, 0.25, 0);
			}
		}
		if(ups.contains("Крипанутость")&&new Random().nextDouble()<=0.0075){
			Creeper c=(Creeper) loc.getWorld().spawnEntity(lset, EntityType.CREEPER);
			c.setCustomName(ChatColor.GREEN+"Крипанутый");
		}
	else lset.getBlock().setType(mat);
	}
	public Location center(){
		return loc.clone().add(0.5, 0.5, 0.5);
	}
	public void save(FileConfiguration conf, String st){
		conf.set(st+".spawnRateLevel", spawnRateLevel);
		conf.set(st+".boost", boost);
		GepUtil.saveLocToConf(conf, st+".loc", loc);
		conf.set(st+".chancesPoints", chancesPoints);
		conf.set(st+".cUped", cUped);
		conf.set(st+".cCoefs", null);
		for(String s:cCoefs.keySet()){
			conf.set(st+".cCoefs."+s, cCoefs.get(s));
		}
		conf.set(st+".ups", ups);
		conf.set(st+".owner", owner.toString());
		
	}
	public double spawnRate(boolean inSec, boolean up){
		double ret=100-spawnRateLevel*15;
		if(up){
			ret*=0.5;
		}
		if(inSec){
			ret/=20;
		}
		return ret;
	}
	public ItemStack coefChMat(String mat){
		List<String> lore = new ArrayList<>();
		int lvl=1;
		if(cCoefs.containsKey(mat))lvl=cCoefs.get(mat)+1;
		lore.add(ChatColor.YELLOW+"Сейчас шанс появления этого блока "+GepUtil.boolCol(ChatColor.AQUA, ChatColor.YELLOW, lvl>1)+"x"+lvl);
		if(chancesPoints>0)lore.add(ChatColor.GREEN+""+ChatColor.BOLD+"Нажмите, чтобы увеличить этот шанс на +1x!");
		else lore.add(ChatColor.GOLD+"Нажмите на алмазик, чтобы купить очко шансов.");
		if(lvl>1)lore.add(ChatColor.YELLOW+"Клик по барьеру вернёт вложенные очки.");
		Enchantment ench = null;
		if(lvl>1)ench=Enchantment.LOOT_BONUS_BLOCKS;
		return ItemUtil.createal(Material.getMaterial(mat), 1, 0, null, lore, ench, lvl-1);
	}
	public double speedFor(Material mat, int am){
		double ret=0;
		if(mat.equals(Material.COBBLESTONE)){
			ret=5*am;
		}
		if(mat.equals(Material.STONE)){
			ret=7.5*am;
		}
		if(mat.equals(Material.REDSTONE)){
			ret=15*am;
		}
		double upCoef=1-spawnRateLevel*0.0065;
		ret*=upCoef;
		return ret;
	}
	public void sendToIs(String mes){
		sendToOwner(mes);
		sendToMembers(mes);
	}
	public void sendToMembers(String mes){
		Island is=ASkyBlockAPI.getInstance().getIslandOwnedBy(owner);
		for(UUID id:is.getMembers()){
			if(Bukkit.getPlayer(id)!=null&&id!=owner){
				Player p=Bukkit.getPlayer(id);
				if(p.isOnline())p.sendMessage(mes);
			}
		}
	}
	public void sendToOwner(String mes){
		if(Bukkit.getPlayer(owner)!=null){
			Player p=Bukkit.getPlayer(owner);
			if(p.isOnline())p.sendMessage(mes);
		}
	}
}
