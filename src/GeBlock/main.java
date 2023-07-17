package GeBlock;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import cmds.sendexp;
import obj.GeBlock;
import obj.PlayerInfo;
import obj.Up;
import utilsGeB.GepUtil;
import utilsGeB.ItemUtil;
import utilsGeB.Prices;
import utilsGeB.SQLConnection;
import utilsGeB.textUtil;

public class main extends JavaPlugin{
	public static main instance;
	public static SQLConnection con = null;
	public static HashMap<Location, GeBlock> gebs = new HashMap<>();
	public static int toEvent=9;
	public static HashMap<String,Integer> boosts=new HashMap<>();
	public static Prices pr;
	public void onEnable(){
		instance = this;
		pr=new Prices();
		con = new SQLConnection();
		con.connect();
		Bukkit.getPluginCommand("spawn").setExecutor(new cmds.spawn());
		Bukkit.getPluginCommand("menu").setExecutor(new cmds.menu());
		Bukkit.getPluginCommand("sendexp").setExecutor(new sendexp());
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getPluginManager().registerEvents(new GUI(), this);
		GUI.load();
		new GeBlock();
		new PlayerInfo(null);
		new Up(0, 0, null, 0);
		new ItemUtil();
		new GepUtil();
		new textUtil();
		new GUI();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			int secRate=0;
			public void run(){
				secRate++;
				if(secRate>=20){
					toEvent--;
					if(toEvent<=0){
						toEvent=700+new Random().nextInt(300);
						String[] evs={"chance","rage"};
						String ev=evs[new Random().nextInt(evs.length)];
						if(ev.equals("chance")){
							String[] types={"STONE","COAL_ORE","IRON_ORE","GOLD_ORE","LAPIS_ORE","REDSTONE_ORE","DIAMOND_ORE","EMERALD_ORE"};
							String type=types[new Random().nextInt(types.length)];
							GepUtil.globMessage(ChatColor.GREEN+"На 5 минут шанс появления "+ChatColor.GOLD+type+ChatColor.GREEN+" увеличен вдвое!", Sound.ENTITY_WITHER_DEATH, 100, 0, ChatColor.BLUE+"ГЛОБАЛЬНЫЙ ИВЕНТ!", ChatColor.GREEN+"Накрутка шанса "+ChatColor.GOLD+type, 20, 50, 20);
							boosts.put(type, 300);
						}else if(ev.equals("rage")){
							for(Entity en:Bukkit.getWorld("world").getEntities()){
								if(en instanceof Monster&&!(en instanceof Creeper)){
									((Monster) en).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, true), true);
									((Monster) en).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0, false, false), true);
									((Monster) en).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0, false, false), true);
								}
							}
							GepUtil.globMessage(ChatColor.RED+"Все существовавшие враждебные мобы (кроме криперов) впали в ярость: стали сильнее, быстрее и выносливее.", Sound.ENTITY_WITHER_DEATH, 100, (float) 0.7, ChatColor.BLUE+"ГЛОБАЛЬНЫЙ ИВЕНТ!", ChatColor.RED+"Ярость!", 10, 50, 20);
						}
					}
					for(String st:new ArrayList<>(boosts.keySet())){
						if(GepUtil.HashMapReplacer(boosts, st, -1, true, false)){
							
						}
					}
					secRate=0;
					for(Player p:Bukkit.getOnlinePlayers()){
						PlayerInfo pi=Events.plist.get(p.getName());
						for(String st:new ArrayList<>(pi.timers.keySet())){
							if(GepUtil.HashMapReplacer(pi.timers, st, -1, true, false)){
								
							}
						}
					}
				}
				for(Location loc:gebs.keySet()){
					if(loc==null){
						GepUtil.debug("loc is null.", null, "error");
						continue;
					}
					GeBlock gb=gebs.get(loc);
					if(gb==null){
						GepUtil.debug("GB at "+loc+" is null.", null, "error");
						continue;
					}
					gb.run();
				}
			}
		}, 1, 1);
		for(Player p:Bukkit.getOnlinePlayers()){
			Events.doJoin(p);
		}
		File file = new File(this.getDataFolder()+File.separator+"gebs.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(conf.contains("gebs"))for(String st:conf.getConfigurationSection("gebs").getKeys(false)){
					GeBlock b = new GeBlock(conf, "gebs."+st);
					gebs.put(b.loc, b);
				}
				GepUtil.globMessage(ChatColor.GREEN+"Успешно загружены данные о "+gebs.size()+" ГеБлоках в мире.");
			}
		}.runTaskLater(this, 20);
	}
	public void onDisable(){
		for(Player p:Bukkit.getOnlinePlayers()){
			Events.doLeave(p);
		}
		File file = new File(this.getDataFolder()+File.separator+"gebs.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		conf.set("gebs", null);
		for(Location l:gebs.keySet()){
			GeBlock b=gebs.get(l);
			b.save(conf, "gebs."+l.getBlockX()+"-"+l.getBlockY()+"-"+l.getBlockZ());
		}
		GepUtil.saveCfg(conf, file);
	}
	public static ItemStack upToItem(String up){
		String name=ChatColor.AQUA+"Улучшение "+ChatColor.GOLD+"'"+up+"'";
		ItemStack ret=ItemUtil.create(Material.EGG, 1, 0, ChatColor.GRAY+"Ошибка.", new String[]{"Всё пропало. Баг. Сообщите Гепи."}, null, 0);
		if(up.equals("Дробилка"))ret=ItemUtil.createal(Material.FLINT, 1, 0, name, upToLore(up), Enchantment.ARROW_DAMAGE, 10);if(up.equals("Страховка"))ret=ItemUtil.createal(Material.PAPER, 1, 0, name, upToLore(up), Enchantment.ARROW_DAMAGE, 10);
		if(up.equals("Бешенство"))ret=ItemUtil.createal(Material.TNT, 1, 0, name, upToLore(up), Enchantment.ARROW_DAMAGE, 10);
		if(up.equals("Золотая лихорадка"))ret=ItemUtil.createal(Material.GOLD_BLOCK, 1, 0, name, upToLore(up), Enchantment.ARROW_DAMAGE, 10);
		if(up.equals("Денежный поток"))ret=ItemUtil.createal(Material.EMERALD, 1, 0, name, upToLore(up), Enchantment.ARROW_DAMAGE, 10);
		if(up.equals("Крипанутость"))ret=ItemUtil.createal(Material.SULPHUR, 1, 0, name, upToLore(up), Enchantment.ARROW_DAMAGE, 10);
		if(up.equals("Юбилейное"))ret=ItemUtil.createal(Material.COOKIE, 1, 0, name, upToLore(up), Enchantment.ARROW_DAMAGE, 10);
		return ret;
	}
	public static List<String> upToLore(String up){
		List<String> ret=new ArrayList<>();
		if(up.equals("Дробилка")){
			ret.add(ChatColor.YELLOW+"Превращает булыжник, выходящий");
			ret.add(ChatColor.YELLOW+"из ГеБлока, в гравий.");
			ret.add(ChatColor.AQUA+"Удобно при автоматизации!");
		}if(up.equals("Страховка")){
			ret.add(ChatColor.YELLOW+"Отключает все ивенты в ГеБлоке");
			ret.add(ChatColor.GRAY+"Утечка, поломка, взрыв.");
			ret.add(ChatColor.AQUA+"Удобно при автоматизации!");
		}if(up.equals("Бешенство")){
			ret.add(ChatColor.YELLOW+"Ускоряет ГеБлок в 2 раза, "+ChatColor.GOLD+"НО:");
			ret.add(ChatColor.LIGHT_PURPLE+"ГеБлок ставит блоки не только в центре;");
			ret.add(ChatColor.RED+"Ставя блоки, он может сломать другие;");
			ret.add(ChatColor.GOLD+"Он не остановится, пока в центре пусто!");
			ret.add(ChatColor.GRAY+"НЕ удобно при автоматизации!");
			ret.add(ChatColor.GREEN+"Выгодно при ручной работе!");
		}if(up.equals("Золотая лихорадка")){
			ret.add(ChatColor.YELLOW+"Вместо золотой руды появляются золотые блоки, "+ChatColor.GOLD+"НО:");
			ret.add(ChatColor.LIGHT_PURPLE+"После этого ГеБлок получает поломку на 30 сек.");
		}if(up.equals("Денежный поток")){
			ret.add(ChatColor.YELLOW+"Вместо алмазов будут появляться изумруды,");
			ret.add(ChatColor.AQUA+"а так же кусочки опыта.");
		}if(up.equals("Крипанутость")){
			ret.add(ChatColor.YELLOW+"Есть шанс появления крипера, который");
			ret.add(ChatColor.YELLOW+"взрывается ресурсами (без разрушений, но с уроном).");
		}if(up.equals("Юбилейное")){
			ret.add(ChatColor.YELLOW+"После каждой 10-ой руды появляется фейерверк.");
			ret.add(ChatColor.GREEN+"И золотой слиток в придачу!");
		}
		return ret;
	}
	public static void openGUIlater(Player p, int ticks, Inventory inv){
		new BukkitRunnable() {
			@Override
			public void run() {
				p.openInventory(inv);
			}
		}.runTaskLater(instance, ticks);
	}
}
