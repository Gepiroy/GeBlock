package GeBlock;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.events.IslandDeleteEvent;
import com.wasteofplastic.askyblock.events.IslandNewEvent;

import obj.GeBlock;
import obj.PlayerInfo;
import utilsGeB.GepUtil;
import utilsGeB.ItemUtil;
import utilsGeB.textUtil;

public class Events implements Listener{
	public static HashMap<String,PlayerInfo> plist=new HashMap<>();
	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p=e.getPlayer();
		doJoin(p);
	}
	public static void doJoin(Player p){
		File file = new File(main.instance.getDataFolder() + File.separator + "players"+File.separator+p.getName()+".yml");
		if(!file.exists()){
			plist.put(p.getName(), new PlayerInfo(p.getName()));
			p.teleport(Bukkit.getWorld("world").getSpawnLocation().clone().add(0.5, 0.5, 0.5));
			p.sendMessage(ChatColor.GREEN+"Чтобы начать, пропиши всеми любимый "+ChatColor.BLUE+"/is");
			return;
		}
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		plist.put(p.getName(), new PlayerInfo(conf,p));
	}
	@EventHandler
	public void leave(PlayerQuitEvent e){
		Player p=e.getPlayer();
		doLeave(p);
	}
	public static void doLeave(Player p){
		File file = new File(main.instance.getDataFolder() + File.separator + "players"+File.separator+p.getName()+".yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		plist.get(p.getName()).save(conf);
		GepUtil.saveCfg(conf, file);
		plist.remove(p.getName());
	}
	@EventHandler(ignoreCancelled=true)
	public void place(BlockPlaceEvent e){
		Player p=e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		ItemStack hitem=p.getInventory().getItemInMainHand();
		Block b=e.getBlock();
		if(hitem.hasItemMeta()&&hitem.getItemMeta().hasDisplayName()&&hitem.getItemMeta().getDisplayName().equals(ChatColor.AQUA+"GeBlock")){
			e.setCancelled(true);
			if(canPlaceGeB(b.getLocation(), p)){
				setGeBlock(p, b.getLocation(), pi, hitem);
				hitem.setAmount(hitem.getAmount()-1);
			}
		}
	}
	@EventHandler(ignoreCancelled=true)
	public void mine(BlockBreakEvent e){
		Player p=e.getPlayer();
		if(!p.getGameMode().equals(GameMode.CREATIVE)&&p.getWorld().getName().equals("world")){
			e.setCancelled(true);
			return;
		}
		PlayerInfo pi=plist.get(p.getName());
		//ItemStack hitem=p.getInventory().getItemInMainHand();
		Block b=e.getBlock();
		ItemStack hitem=p.getInventory().getItemInMainHand();
		if(b.getType().equals(Material.EMERALD_ORE)){
			pi.addAchievement("Не в изумрудах счастье...");
		}
		if(b.getType().equals(Material.DIAMOND_ORE)){
			pi.addAchievement("Откопанный лучший друг");
		}
		if(b.getType().equals(Material.STONE)&&new Random().nextDouble()<=0.1){
			pi.addExp(1);
		}
		for(ItemStack item:new ArrayList<>(b.getDrops())){
			if(item.getType().equals(Material.WHEAT)||(item.getType().equals(Material.CARROT_ITEM)&&item.getAmount()>1)||(item.getType().equals(Material.POTATO_ITEM)&&item.getAmount()>1)){
				if(GepUtil.isFullyItem(hitem, ChatColor.GOLD+"Опытная мотыга", Material.GOLD_HOE)){
					pi.addExp(1);
					b.getDrops().remove(item);
				}
				else if(new Random().nextDouble()<=0.5){
					if(GepUtil.getInt(pi.timers, "plantExp")<50){
						GepUtil.HashMapReplacer(pi.timers, "plantExp", 5, false, false);
						pi.addExp(1);
					}else{
						pi.addAchievement("Опытный фермер");
					}
				}
			}
		}
	}
	@EventHandler
	public void einteract(PlayerInteractEntityEvent e){
		Entity en=e.getRightClicked();
		Player p=e.getPlayer();
		if(en.getType().equals(EntityType.VILLAGER)&&p.getWorld().getName().equals("world")){
			e.setCancelled(true);
			GUI.getter(p);
		}
	}
	@EventHandler
	public void interact(PlayerInteractEvent e){
		Player p=e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		ItemStack hitem=p.getInventory().getItemInMainHand();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Block b=e.getClickedBlock();
			pi.lastClickedBlock=b.getLocation();
			if(b.getType().equals(Material.ENCHANTMENT_TABLE)){
				e.setCancelled(true);
				GUI.ench(p);
				return;
			}
			if(b.getType().equals(Material.ANVIL)&&!pi.bools.contains("anvilmes")){
				p.sendMessage(ChatColor.DARK_GREEN+"Использование наковальни на GeBlock-е бесплатное! (exp забирают временно)");
				pi.bools.add("anvilmes");
			}
			if(p.getWorld().getName().equals("world")){
				if(!p.getGameMode().equals(GameMode.CREATIVE))e.setCancelled(true);
				if(b.getType().equals(Material.IRON_BLOCK)){
					if(!pi.bools.contains("preobr")){
						if(hitem.getType().equals(Material.COBBLESTONE)&&hitem.getAmount()>=32){
							GUI.preobr(p);
							pi.bools.add("preobr");
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 2, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 0);
							hitem.setAmount(hitem.getAmount()-32);
							if(pi.bools.contains("preobr")&&pi.bools.contains("creator")&&pi.bools.contains("incub"))pi.addAchievement("Халявный анлокер");
						}else{
							p.sendMessage(ChatColor.GOLD+"Для разблокировки преобразователя нужно"+GepUtil.boolCol(GepUtil.haveItem(p, Material.COBBLESTONE, 32))+" 32 булыжника");
						}
						return;
					}
					GUI.preobr(p);
					return;
				}
				if(b.getType().equals(Material.EMERALD_BLOCK)){
					if(!pi.bools.contains("incub")){
						if(hitem.getType().equals(Material.ROTTEN_FLESH)&&hitem.getAmount()>=32){
							GUI.incub(p);
							pi.bools.add("incub");
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 2, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 0);
							hitem.setAmount(hitem.getAmount()-32);
							if(pi.bools.contains("preobr")&&pi.bools.contains("creator")&&pi.bools.contains("incub"))pi.addAchievement("Халявный анлокер");
						}else{
							p.sendMessage(ChatColor.GOLD+"Для разблокировки инкубатора нужно"+GepUtil.boolCol(GepUtil.haveItem(p, Material.ROTTEN_FLESH, 32))+" 32 мяса зомби.");
						}
						return;
					}
					GUI.incub(p);
					return;
				}
				if(b.getType().equals(Material.GOLD_BLOCK)){
					if(!pi.bools.contains("creator")){
						if(hitem.getType().equals(Material.EMERALD)){
							GUI.incub(p);
							pi.bools.add("creator");
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 2, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 0);
							hitem.setAmount(hitem.getAmount()-1);
						}else{
							p.sendMessage(ChatColor.GOLD+"Для разблокировки улучшайзера нужен"+GepUtil.boolCol(GepUtil.haveItem(p, Material.EMERALD, 1))+" изумруд");
						}
						return;
					}
					GUI.creator(p);
					return;
				}
				if(b.getType().equals(Material.ENDER_CHEST)&&b.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.GOLD_BLOCK)){
					e.setCancelled(true);
					if(GepUtil.isFullyItem(hitem, ChatColor.GOLD+"Ключ улучшений", Material.GOLDEN_CARROT)){
						hitem.setAmount(hitem.getAmount()-1);
						Inventory inv=Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Сундук улучшений");
						p.getWorld().playSound(b.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 0);
						p.getWorld().playSound(b.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0);
						p.getWorld().playSound(b.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 1, 0);
						String[] ups={"Дробилка","Страховка","Бешенство","Золотая лихорадка","Денежный поток","Крипанутость","Юбилейное"};
						inv.setItem(13, main.upToItem(ups[new Random().nextInt(ups.length)]));
						p.openInventory(inv);
					}else{
						p.sendMessage(ChatColor.GOLD+"Для открытия сундука улучшений нужен ключ улучшений. Вы можете "+ChatColor.AQUA+"сделать его в улучшайзере");
					}
					return;
				}
				if(b.getType().equals(Material.CHEST)){
					e.setCancelled(true);
					GUI.getter(p);
				}
			}
			if(!p.isSneaking()){
				for(Location l:main.gebs.keySet()){
					if(b.getLocation().equals(l)){
						GeBlock geb = main.gebs.get(l);
						if(pi.gebs.contains(l)||ASkyBlockAPI.getInstance().getIslandOwnedBy(geb.owner).getMembers().contains(p.getUniqueId())){
							GUI.geb(p, main.gebs.get(l));
							if(pi.waits.containsKey("tutor")&&pi.waits.get("tutor")==2){
								p.sendMessage(textUtil.tut+textUtil.mark("В меню настроек в будущем вы сможете применять разные |особые усиления|.", ChatColor.LIGHT_PURPLE, ChatColor.GREEN));
								p.sendMessage(textUtil.inf+ChatColor.BLUE+"Кстати, тут работает фишка Shift-ПКМ, которая позволяет не открывать меню настроек!");
								p.sendMessage(textUtil.tut+ChatColor.AQUA+"Чтобы ускорить ГеБлок, положите в него булыжник.");
								p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
								GepUtil.HashMapReplacer(pi.waits, "tutor", 1, false, false);
							}
						}else{
							p.sendMessage(ChatColor.GOLD+"У вас нет разрешения изменять настройки этого ГеБлока.");
						}
						return;
					}
				}
			}
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(hitem.getType().equals(Material.EYE_OF_ENDER)){
				e.setCancelled(true);
				GUI.menu(p);
			}
		}
	}
	@EventHandler
	public void Eexplode(EntityExplodeEvent e){
		Entity en=e.getEntity();
		if(en.getType().equals(EntityType.CREEPER)){
			if(en.getCustomName()!=null&&en.getCustomName().equals(ChatColor.GREEN+"Крипанутый")){
				e.setCancelled(true);
				e.setYield(0);
				Location l=en.getLocation().add(0, 1, 0);
				for(int i=0;i<new Random().nextInt(8)+3;i++){
					HashMap<String,Integer> chs=new HashMap<>();
					chs.put("IRON_INGOT", 100);
					chs.put("GOLD_INGOT", 50);
					chs.put("DIAMOND", 5);
					chs.put("EMERALD", 1);
					chs.put("REDSTONE", 30);
					chs.put("GLOWSTONE_DUST", 20);
					String c=GepUtil.chancesByCoef(chs);
					Item item=l.getWorld().dropItem(l, new ItemStack(Material.getMaterial(c)));
					item.setVelocity(new Vector(new Random().nextDouble()/2-0.25, new Random().nextDouble()/3, new Random().nextDouble()/2-0.25));
				}
			}
			else e.setYield((float) (e.getYield()/3.0*2.0));
		}
	}
	@EventHandler
	public void tryToSpawn(EntitySpawnEvent e){
		Entity en=e.getEntity();
		if(en instanceof Monster){
			Island is=ASkyBlockAPI.getInstance().getIslandAt(en.getLocation());
			if(e.getLocation().distance(is.getSpawnPoint())<=10){
				e.setCancelled(true);
				return;
			}
			int cm=countMonsters(is.getOwner());
			if(cm>=10){
				e.setCancelled(true);
				return;
			}
			//GepUtil.globMessage(ChatColor.BLUE+"На острове уже "+cm+"/10 монстров.");
		}
	}
	int countMonsters(UUID owner){
		int ret=0;
		for(Entity en:ASkyBlockAPI.getInstance().getIslandWorld().getEntities()){
			if(ASkyBlockAPI.getInstance().getIslandAt(en.getLocation()).getOwner().equals(owner)){
				ret++;
			}
		}
		return ret;
	}
	@EventHandler
	public void hurt(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p=(Player) e.getEntity();
			if(p.getWorld().getName().equals("world")){
				if(e.getCause().equals(DamageCause.VOID)){
					e.setCancelled(true);
					p.teleport(p.getWorld().getSpawnLocation().clone().add(0.5, 0.5, 0.5));
					return;
				}else{
					e.setCancelled(true);
					return;
				}
			}
			if(e.getCause().equals(DamageCause.FALL)){
				e.setDamage(e.getDamage()/2.0);
			}
			if(p.getHealth()-e.getFinalDamage()<=0){
				death(p);
				e.setCancelled(true);
			}
		}
	}
	void death(Player p){
		PlayerInfo pi=plist.get(p.getName());
		int lost=(int) (pi.getExp()*0.25+1);
		pi.addExp(-lost);
		p.sendMessage(ChatColor.RED+"При смерти вы потеряли "+lost+" exp. Теперь у вас "+pi.getExp()+" exp.");
		Location resp=p.getBedSpawnLocation();
		if(resp==null){
			p.sendMessage(ChatColor.GOLD+"Хмм... У вас нет спавнпоинта. Отправляем на спавн.");
			p.teleport(Bukkit.getWorld("world").getSpawnLocation().clone().add(0.5, 0.5, 0.5));
		}else{
			p.sendMessage(ChatColor.GOLD+"Спавнпоинт походу есть...");
			p.teleport(resp.clone().add(0.5, 0.5, 0.5));
		}
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()/2);
		p.setFallDistance(0);
	}
	@EventHandler
	public void die(PlayerDeathEvent e){
		Player p=e.getEntity();
		p.sendMessage(ChatColor.RED+"БАГОВАННАЯ СМЕРТЬ!!!!! Сообщите Гепи о том, как вы умерли.");
		death(p);
	}
	@EventHandler
	public void edie(EntityDeathEvent e){
		LivingEntity en=e.getEntity();
		Player p=en.getKiller();
		if(p!=null){
			PlayerInfo pi=plist.get(p.getName());
			if(en.getType().equals(EntityType.ZOMBIE)){
				for(ItemStack it:e.getDrops()){
					if(it.getType().equals(Material.CARROT_ITEM)){
						pi.addAchievement("Откуда у них морковка?");
					}
					if(it.getType().equals(Material.POTATO_ITEM)){
						pi.addAchievement("Может, это их батарейка?");
					}
				}
			}
		}
	}
	@EventHandler
	public void eat(PlayerItemConsumeEvent e){
		Player p=e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		ItemStack eaten=e.getItem();
		if(GepUtil.isFullyItem(eaten, ChatColor.GOLD+"Ключ улучшений", Material.GOLDEN_CARROT)){
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3));
			p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ANGRY, 2, 0);
			new BukkitRunnable() {
				int t=0;
				@Override
				public void run() {
					t++;
					if(t==60){
						p.playSound(p.getLocation(), Sound.ENTITY_LLAMA_SPIT, 2, 0);
						p.getWorld().dropItem(p.getLocation().add(0, 1.5, 0), ItemUtil.create(Material.GOLDEN_CARROT, 1, 0, ChatColor.GOLD+"Ключ улучшений", new String[]{
								ChatColor.GOLD+"Открывает сундук улучшений, в",
								ChatColor.GOLD+"котором лежит рандомное улучшение.",
								ChatColor.AQUA+"(Не только такие, как в создавателе!)"
						}, Enchantment.ARROW_DAMAGE, 10));
						p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation().add(0, 1.5, 0), 10, 0, 0, 0, 1);
						p.playSound(p.getLocation(), Sound.ENTITY_LLAMA_SPIT, 2, 1);
						p.setFoodLevel(p.getFoodLevel()-6);
						p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 300, 3));
						p.damage(5);
					}
					if(t>=100){
						pi.addAchievement("НЕ ЕШЬ ЕГО!");
						this.cancel();
					}
				}
			}.runTaskTimer(main.instance, 0,0);
		}
	}
	@EventHandler
	public void exp(PlayerExpChangeEvent e){
		Player p=e.getPlayer();
		PlayerInfo pi=plist.get(p.getName());
		int am=e.getAmount();
		if(am<0){
			pi.addExp(am);
			e.setAmount(0);
			p.sendMessage("-"+am+"exp.");
			return;
		}
		pi.addExp(GepUtil.ChanceToInt(3, am));
		Sound s=Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
		float power=(float) (0.5+0.15*e.getAmount());
		if(e.getAmount()>10){
			s=Sound.ENTITY_PLAYER_LEVELUP;
			power=(float) (0.5+0.15*e.getAmount()/2.5);
		}
		if(power>2)power=2;
		//power=(float) (2.5-power);
		p.getWorld().playSound(p.getLocation(), s, (float) (0.5+e.getAmount()/10.0), power);
		e.setAmount(0);
	}
	boolean canPlaceGeB(Location loc, Player p){
		for(Location l:main.gebs.keySet()){
			if(loc.equals(l)){
				p.sendMessage(ChatColor.GOLD+"Здесь уже есть ГеБлок.");
				return false;
			}
		}
		PlayerInfo pi=plist.get(p.getName());
		if(pi.gebs.size()>0){
			if(pi.bools.contains("extraGeb")){
				if(pi.gebs.size()<2){
					return true;
				}
				p.sendMessage(ChatColor.GOLD+"Достигнут лимит ГеБлоков. Неее, третий ГеБлок за донат не продам.");
				return false;
			}else{
				p.sendMessage(ChatColor.GOLD+"Достигнут лимит ГеБлоков. Хотите получить ещё один? "+ChatColor.AQUA+"/donate");
				return false;
			}
		}
		return true;
	}
	void setGeBlock(Player p, Location loc, PlayerInfo pi, ItemStack item){
		GeBlock b=new GeBlock(p, loc);
		if(item.hasItemMeta()&&item.getItemMeta().hasLore()){
			List<String> lore = item.getItemMeta().getLore();
			for(String st:lore){
				if(st.contains("Ускорение")){
					b.boost=GepUtil.intFromString(st)*20;
					p.sendMessage(ChatColor.AQUA+"Включено ускорение на "+b.boost/20+" сек.");
				}
				if(st.contains("Очки шансов")){
					b.chancesPoints=GepUtil.intFromString(st);
					b.cUped=b.chancesPoints;
					p.sendMessage(ChatColor.AQUA+"Установлено "+b.chancesPoints+" очков шансов.");
				}
				if(st.contains("Уровень скорости")){
					b.spawnRateLevel=GepUtil.intFromString(st);
					p.sendMessage(ChatColor.AQUA+"Установлен "+b.spawnRateLevel+" уровень скорости.");
				}
			}
		}
		main.gebs.put(loc, b); //messages in GeBlock
		pi.gebs.add(loc);
		if(pi.waits.containsKey("tutor")&&pi.waits.get("tutor")==1){
			p.sendMessage(textUtil.tut+ChatColor.GREEN+"Поздравляем! Ресурсами вы обеспечены.");
			p.sendMessage(textUtil.inf+ChatColor.BLUE+"Помимо камня тут иногда выпадают и руды.");
			p.sendMessage(textUtil.tut+ChatColor.AQUA+"Чтобы улучшать или настраивать ГеБлок, кликните по нему.");
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
			GepUtil.HashMapReplacer(pi.waits, "tutor", 1, false, false);
		}
	}
	//TODO ДАЛЕЕ ИВЕНТЫ АСБ
	@EventHandler
	public void sbrem(IslandDeleteEvent e){
		Player p=Bukkit.getPlayer(e.getPlayerUUID());
		if(!p.isOnline())return;
		PlayerInfo pi=Events.plist.get(p.getName());
		for(Location l:pi.gebs){
			main.gebs.remove(l);
		}
		pi.gebs.clear();
	}
	@EventHandler
	public void sbnew(IslandNewEvent e){
		Player p=e.getPlayer();
		new BukkitRunnable() {
			int time=-50;
			@Override
			public void run() {
				time++;
				if(time==30){
					p.sendTitle(ChatColor.GOLD+""+ChatColor.BOLD+"GE", "", 0, 20, 20);
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2, 2);
					p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 2, 2);
				}
				if(time==40){
					p.sendTitle(ChatColor.YELLOW+"Ge", "", 0, 20, 20);
					p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 2, 2);
				}
				if(time==50){
					p.sendTitle(ChatColor.YELLOW+"Ge     ", "", 0, 20, 20);
					p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2, 1);
				}
				if(time==60){
					p.sendTitle(ChatColor.YELLOW+"Ge"+ChatColor.GOLD+ChatColor.BOLD+"BLOCK", "", 0, 20, 20);
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2, 2);
				}
				if(time==70){//was 50
					p.sendTitle(ChatColor.YELLOW+"GeBlock", "", 0, 15, 35);
					p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 2, 2);
				}
				if(time==115){//was 85
					p.sendTitle(ChatColor.YELLOW+"GeBlock", ChatColor.GREEN+"By Gepiroy", 30, 200, 20);
					p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2, 0);
				}
				if(time>=145&&time<=166&&time%3==0){
					p.sendTitle(ChatColor.YELLOW+"GeBlock", ChatColor.GREEN+"By "+GepUtil.tanimWave("Gepiroy", (time-145)/3, 1, ChatColor.GOLD, ChatColor.GREEN), 0, 20, 20);
				}
				if(time>=169&&time<=190&&time%3==0){
					p.sendTitle(ChatColor.YELLOW+"GeBlock", ChatColor.GREEN+"By "+GepUtil.tanimWave("Gepiroy", (190-time)/3, 1, ChatColor.BLUE, ChatColor.GREEN), 0, 20, 20);
				}
				if(time==193){
					p.sendTitle(ChatColor.YELLOW+"GeBlock", ChatColor.GREEN+"By Gepiroy", 0, 20, 20);
				}
				if(time==228){
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 2);
					p.sendTitle(ChatColor.GREEN+"Спасибо:", "", 10, 50, 50);
				}
				if(time==258){
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2, 2);
					p.sendTitle(ChatColor.GREEN+"Спасибо:", ChatColor.BLUE+""+ChatColor.BOLD+"STEELLEAGUE", 0, 50, 50);
				}
				if(time==268){
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, (float) 1.75);
					p.sendTitle(ChatColor.GREEN+"Спасибо:", ChatColor.AQUA+"SteelLeague", 0, 50, 50);
				}
				if(time==278){
					p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2, 2);
					p.sendTitle(ChatColor.GREEN+"Спасибо:", ChatColor.AQUA+"SteelLeague "+ChatColor.DARK_GREEN+"-", 0, 50, 50);
				}
				if(time==288){
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 2, 0);
					p.sendTitle(ChatColor.GREEN+"Спасибо:", ChatColor.AQUA+"SteelLeague "+ChatColor.DARK_GREEN+"- спа  ", 0, 30, 30);
				}
				if(time==303){
					p.sendTitle(ChatColor.GREEN+"Спасибо:", ChatColor.AQUA+"SteelLeague "+ChatColor.DARK_GREEN+"- спав ", 0, 30, 30);
				}
				if(time==318){
					p.sendTitle(ChatColor.GREEN+"Спасибо:", ChatColor.AQUA+"SteelLeague "+ChatColor.DARK_GREEN+"- спавн", 0, 15, 20);
				}
				if(time>=338){
					PlayerInfo pi=Events.plist.get(p.getName());
					p.sendTitle(ChatColor.AQUA+"Добро пожаловать!", "", 10, 10, 10);
					p.getInventory().addItem(ItemUtil.create(Material.BEDROCK, 1, 0, ChatColor.AQUA+"GeBlock", new String[]{
							ChatColor.GREEN+"Твоя личная автошахта!",
							ChatColor.AQUA+"Просто поставь ГеБлок!"
					}, null, 0));
					p.sendMessage(textUtil.tut+ChatColor.GREEN+"ГеБлок - ваш источник ресурсов. Это замена генератора булыжника.");
					p.sendMessage(textUtil.inf+ChatColor.BLUE+"Не волнуйтесь! Лаву вы получите позже.");
					p.sendMessage(textUtil.tut+ChatColor.AQUA+"Поставьте ГеБлок. Не бойтесь, его можно убрать.");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
					if(pi.waits.containsKey("tutor"))pi.waits.remove("tutor");
					pi.waits.put("tutor", 1);
					this.cancel();
				}
			}
		}.runTaskTimer(main.instance, 1, 1);
	}
	static String enchType(ItemStack item){
		String mat=item.getType().toString();
		if(mat.contains("AXE")||mat.contains("SPADE")){
			return "tool";
		}
		if(mat.contains("AXE")||mat.contains("SWORD")){
			return "tool";
		}
		return null;
	}
}
