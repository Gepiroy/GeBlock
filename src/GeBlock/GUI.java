package GeBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import obj.Achievement;
import obj.GeBlock;
import obj.PlayerInfo;
import obj.Up;
import utilsGeB.GepUtil;
import utilsGeB.ItemUtil;
import utilsGeB.textUtil;

public class GUI implements Listener{
	static HashMap<String,Up> incubPrices=new HashMap<>();
	static String[] cancels={ChatColor.BLUE+"Пункт приёма",ChatColor.LIGHT_PURPLE+"Менюшка",ChatColor.LIGHT_PURPLE+"Достижения",ChatColor.AQUA+"Настройки ГеБлока",ChatColor.GOLD+"Преобразователь",ChatColor.LIGHT_PURPLE+"Шансы ГеБлока",ChatColor.BLUE+"Инкубатор",ChatColor.GOLD+"Улучшения ГеБлока",ChatColor.GOLD+"Улучшайзер",ChatColor.LIGHT_PURPLE+"Создать зачарование",ChatColor.LIGHT_PURPLE+"Выберите уровень"};
	public static void load(){
		{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("WOOL", 32);
			Up up=new Up(1250, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"Овца", up);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("CARROT_ITEM", 64);
			Up up=new Up(1000, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"Кролик", up);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("LEATHER", 16);
			Up up=new Up(1250, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"Корова", up);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("SEEDS", 64);
			Up up=new Up(350, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"Куриное яйцо", up);
		}
	}
	public static void menu(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Менюшка");
		inv.setItem(0, ItemUtil.create(Material.EXP_BOTTLE, 1, 0, ChatColor.AQUA+"Достижения", new String[]{
				ChatColor.BLUE+"Их ещё 'ачивками' называют"
		}, null, 0));
		inv.setItem(13, ItemUtil.create(Material.BED, 1, 0, ChatColor.AQUA+"Остров", new String[]{
				ChatColor.BLUE+"Просто выполняется команда "+ChatColor.AQUA+"/is"
		}, null, 0));
		inv.setItem(15, ItemUtil.create(Material.EGG, 1, 0, ChatColor.AQUA+"На спавн", new String[]{
				ChatColor.BLUE+"В то место, где ты родился)",
				ChatColor.AQUA+"На спавне стоят все машины,",
				ChatColor.AQUA+"сундуки, да и народ там же."
		}, null, 0));
		p.openInventory(inv);
	}
	public static void geb(Player p, GeBlock b){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.AQUA+"Настройки ГеБлока");
		{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"Сейчас скорость респы ГеБлока: "+GepUtil.CylDouble(b.spawnRate(true, false),"#0.00")+" сек. "+ChatColor.GRAY+"("+b.spawnRateLevel+"/6)");
			if(b.spawnRateLevel<6){
				lore.add(ChatColor.YELLOW+"Следующий уровень: "+GepUtil.CylDouble(b.spawnRate(true, false)-0.75,"#0.00")+" сек."+ChatColor.GRAY+"("+(b.spawnRateLevel+1)+"/6)");
				lore.add(ChatColor.GOLD+"Цена улучшения:");
				for(String st:b.speedUp.lore(p, b.spawnRateLevel)){
					lore.add(ChatColor.WHITE+" -"+st);
				}
			}
			else lore.add(ChatColor.DARK_GREEN+"Это - максимальный уровень!");
			inv.setItem(11, ItemUtil.createal(Material.WATCH, 1, 0, ChatColor.AQUA+"Скорость", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			if(b.boost>0)lore.add(ChatColor.LIGHT_PURPLE+"ГеБлок ускорен! "+ChatColor.RED+b.boost/20.0+" сек.!");
			lore.add(ChatColor.GREEN+"ГеБлок можно ускорять с помощью предмеов.");
			lore.add(ChatColor.GREEN+"Чем сильнее прокачен ГеБлок - тем больше предметов он требует.");
			lore.add(ChatColor.GREEN+"На сколько дают ускорение предметы:");
			lore.add(ChatColor.AQUA+"Булыжник ускорит на "+GepUtil.CylDouble(b.speedFor(Material.COBBLESTONE, 1),"#0.00")+" сек.");
			lore.add(ChatColor.AQUA+"Камень ускорит на "+GepUtil.CylDouble(b.speedFor(Material.STONE, 1),"#0.00")+" сек.");
			lore.add(ChatColor.AQUA+"Редстоун ускорит на "+GepUtil.CylDouble(b.speedFor(Material.REDSTONE, 1),"#0.00")+" сек.");
			inv.setItem(13, ItemUtil.createal(Material.GOLD_PICKAXE, 1, 0, ChatColor.YELLOW+"Ускорение", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Улучшения можно создать в улучшайзере на спавне.");
			inv.setItem(15, ItemUtil.createal(Material.LEVER, 1, 0, ChatColor.AQUA+"Улучшения", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"Открыть GUI настройки шансов.");
			inv.setItem(18, ItemUtil.createal(Material.EMERALD, 1, 0, ChatColor.GREEN+"Шансы", lore, null, 0));
		}if(b.owner.equals(p.getUniqueId())){
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Сломать ГеБлок, чтобы переставить его.");
			lore.add(ChatColor.AQUA+"Всё сохранится!");
			inv.setItem(26, ItemUtil.createal(Material.TNT, 1, 0, ChatColor.GOLD+"Сломать", lore, null, 0));
			
		}
		p.openInventory(inv);
	}
	public static void preobr(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Преобразователь");
		{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"Булыжник преобразуется в гравий и побочные материалы.");
			HashMap<String,Integer> hm=main.pr.preobrChs.get("COBBLESTONE");
			for(String st:hm.keySet()){
				lore.add(ChatColor.WHITE+" -"+ChatColor.YELLOW+textUtil.ruMaterial(st, true)+ChatColor.AQUA+" "+GepUtil.CylDouble(GepUtil.chanceFromCoef(hm, st), "#0.00")+"%");
			}
			inv.setItem(10, ItemUtil.createal(Material.COBBLESTONE, 1, 0, ChatColor.GOLD+"Булыжник", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"Гравий преобразуется в булыжник и побочные материалы.");
			inv.setItem(12, ItemUtil.createal(Material.GRAVEL, 1, 0, ChatColor.GOLD+"Гравий", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.DARK_GREEN+"Био-материалы:");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"Дерево "+ChatColor.DARK_GREEN+"(8 очков)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"Саженец "+ChatColor.DARK_GREEN+"(24 очка)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"Семяна "+ChatColor.DARK_GREEN+"(4 очка)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"Пшеница "+ChatColor.DARK_GREEN+"(8 очков)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"Тростник "+ChatColor.DARK_GREEN+"(4 очка)");
			lore.add(ChatColor.AQUA+"Из них можно получить новые виды растений и блоки земли!");
			inv.setItem(14, ItemUtil.createal(Material.DIRT, 1, 0, ChatColor.DARK_GREEN+"Био", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Чтобы получить лаву, нужно:");
			lore.add(ChatColor.YELLOW+"Преобразуя предметы, держать в гл. руке ведро;");
			lore.add(ChatColor.YELLOW+"При этом должно быть не менее 5-ти стаков");
			lore.add(ChatColor.YELLOW+"булыжника. (за каждый стак +10% шанс, начиная с 50)");
			inv.setItem(23, ItemUtil.createal(Material.LAVA_BUCKET, 1, 0, ChatColor.GOLD+"Лава", lore, null, 0));
		}
		p.openInventory(inv);
	}
	public static void creator(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Улучшайзер");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD+"Открывает сундук улучшений, в");
		lore.add(ChatColor.GOLD+"котором лежит рандомное улучшение.");
		Up up=main.pr.upkey;
		lore.add(ChatColor.WHITE+"  ---Цена---  ");
		for(String s:up.lore(p, 1)){
			lore.add(ChatColor.WHITE+" -"+s);
		}
		inv.setItem(12, ItemUtil.createal(Material.GOLDEN_CARROT, 1, 0, ChatColor.GOLD+"Ключ улучшений", lore, Enchantment.ARROW_DAMAGE, 10));
		inv.setItem(14, ItemUtil.create(Material.GOLDEN_CARROT, 1, 0, ChatColor.GOLD+"Ключ улучшений", new String[]{
				ChatColor.GOLD+"Открывает сундук улучшений, в",
				ChatColor.GOLD+"котором лежит рандомное улучшение.",
				ChatColor.WHITE+" -"+ChatColor.AQUA+"19 гер."
		}, Enchantment.ARROW_DAMAGE, 10));
		p.openInventory(inv);
	}
	public static void incub(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE+"Инкубатор");
		{
			Up up=incubPrices.get(ChatColor.GOLD+"Овца");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Требования:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(10, ItemUtil.createal(Material.MONSTER_EGG, 1, 91, ChatColor.GOLD+"Овца", lore, null, 0));
		}{
			Up up=incubPrices.get(ChatColor.GOLD+"Кролик");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Требования:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(12, ItemUtil.createal(Material.MONSTER_EGG, 1, 101, ChatColor.GOLD+"Кролик", lore, null, 0));
		}{
			Up up=incubPrices.get(ChatColor.GOLD+"Корова");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Требования:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(14, ItemUtil.createal(Material.MONSTER_EGG, 1, 92, ChatColor.GOLD+"Корова", lore, null, 0));
		}{
			Up up=incubPrices.get(ChatColor.GOLD+"Куриное яйцо");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Требования:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(16, ItemUtil.createal(Material.EGG, 1, 0, ChatColor.GOLD+"Куриное яйцо", lore, null, 0));
		}
		p.openInventory(inv);
	}
	public static void gebUpChances(Player p, GeBlock b){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Шансы ГеБлока");
		inv.setItem(1, b.coefChMat("STONE"));
		inv.setItem(3, b.coefChMat("COAL_ORE"));
		inv.setItem(5, b.coefChMat("IRON_ORE"));
		inv.setItem(7, b.coefChMat("GOLD_ORE"));
		inv.setItem(10, b.coefChMat("LAPIS_ORE"));
		inv.setItem(12, b.coefChMat("REDSTONE_ORE"));
		inv.setItem(14, b.coefChMat("DIAMOND_ORE"));
		inv.setItem(16, b.coefChMat("EMERALD_ORE"));
		{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"Очко шансов позволяет умножить шансы на выпадения выбранной руды.");
			lore.add(ChatColor.GREEN+"Выбранные руды можно будет отменить и вернуть назад потраченные очки.");
			lore.add(ChatColor.AQUA+"Доступно очков: "+ChatColor.GOLD+ChatColor.BOLD+b.chancesPoints);
			lore.add(ChatColor.GOLD+"Цена нового очка:");
			for(String st:b.cUp.lore(p, b.cUped)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(22, ItemUtil.createal(Material.DIAMOND, 1, 0, ChatColor.AQUA+"Очко шансов", lore, Enchantment.PROTECTION_ENVIRONMENTAL, 10));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Назад в ГеБлок.");
			inv.setItem(18, ItemUtil.createal(Material.BARRIER, 1, 0, ChatColor.RED+"Вернуться", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"Вернуть очки шансов из выбранных блоков.");
			lore.add(ChatColor.GOLD+"Это стоит "+GepUtil.boolCol(pi.getExp()>=30)+"30 exp.");
			inv.setItem(26, ItemUtil.createal(Material.TNT, 1, 0, ChatColor.RED+"Обнуление", lore, null, 0));
		}
		p.openInventory(inv);
	}
	public static void upgeb(Player p, GeBlock b){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Улучшения ГеБлока");
		for(int i=0;i<27;i++){
			if(i>=12&&i<=14){
				if(b.upslots<i-11){
					inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 14, ChatColor.GOLD+"Заблокировано", null, Enchantment.PROTECTION_ENVIRONMENTAL, 10));
				}
				else if(b.ups.size()>=i-11){
					String up=b.ups.get(i-12);
					inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 4, ChatColor.YELLOW+"Улучшение "+ChatColor.GOLD+up, new String[]{ChatColor.AQUA+"Клик, чтобы убрать!"}, Enchantment.PROTECTION_ENVIRONMENTAL, 10));
				}else{
					inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 5, ChatColor.GREEN+"Свободно!", new String[]{ChatColor.AQUA+"Клик, чтобы вставить!"}, null, 0));
				}
			}
			else inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 0, " ", null, null, 0));
		}
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN+"Улучшения можно создавать в преобразователе "+ChatColor.AQUA+"на спавне!");
		lore.add(ChatColor.GOLD+"Цена нового слота:");
		for(String st:b.sUp.lore(p, b.upslots)){
			lore.add(ChatColor.WHITE+" -"+st);
		}
		inv.setItem(26, ItemUtil.createal(Material.DIAMOND, 1, 0, ChatColor.AQUA+"Купить слот", lore, null, 0));
		inv.setItem(18, ItemUtil.create(Material.BARRIER, 1, 0, ChatColor.RED+"Вернуться", new String[]{ChatColor.GOLD+"Назад в ГеБлок."}, null, 0));
		p.openInventory(inv);
	}
	public static void preobrPlace(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"Положить предметы в преобразователь");
		p.openInventory(inv);
	}
	public static void ench(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Создать зачарование");
		inv.setItem(0, ItemUtil.create(Material.GOLD_SWORD, 1, 0, ChatColor.GOLD+"Оружие", new String[]{
				ChatColor.YELLOW+"Зачарования, пригодные для оружия.",
				ChatColor.GOLD+"Острота, отдача и подобное тут.",
				ChatColor.AQUA+"  Клик для перехода к выбору"
		}, null, 0));
		inv.setItem(2, ItemUtil.create(Material.BOW, 1, 0, ChatColor.GOLD+"Луки", new String[]{
				ChatColor.YELLOW+"Зачарования, пригодные для луков.",
				ChatColor.GOLD+"Сила, откидывание и подобное тут.",
				ChatColor.AQUA+"  Клик для перехода к выбору"
		}, null, 0));
		inv.setItem(4, ItemUtil.create(Material.GOLD_PICKAXE, 1, 0, ChatColor.GOLD+"Инструменты", new String[]{
				ChatColor.YELLOW+"Зачарования, пригодные для инструментов.",
				ChatColor.GOLD+"Эффективность, удача и подобное тут.",
				ChatColor.GRAY+"(Починка тут)",
				ChatColor.AQUA+"  Клик для перехода к выбору"
		}, null, 0));
		inv.setItem(6, ItemUtil.create(Material.GOLD_CHESTPLATE, 1, 0, ChatColor.GOLD+"Броня", new String[]{
				ChatColor.YELLOW+"Зачарования, пригодные для брони.",
				ChatColor.GOLD+"Защиты, шипы и подобное тут.",
				ChatColor.AQUA+"  Клик для перехода к выбору"
		}, null, 0));
		inv.setItem(8, ItemUtil.create(Material.FISHING_ROD, 1, 0, ChatColor.GOLD+"Удочка", new String[]{
				ChatColor.YELLOW+"Зачарования, пригодные для удочки.",
				ChatColor.GOLD+"Вез. рыбак, приманка и подобное тут.",
				ChatColor.AQUA+"  Клик для перехода к выбору"
		}, null, 0));
		inv.setItem(22, ItemUtil.create(Material.BOOK, 1, 0, ChatColor.GOLD+"А как чарить-то?", new String[]{
				ChatColor.GREEN+"Просто кликните книжкой по предмету.",
				ChatColor.GOLD+"Снять зачары нельзя!",
				ChatColor.AQUA+"Если чарить на лвл больше, старый чар вернут."
		}, null, 0));
		p.openInventory(inv);
	}
	public static void enchLvl(Player p, double coef){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Выберите уровень");
		inv.setItem(10, ItemUtil.create(Material.SUGAR_CANE, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(20*coef))+""+(int)(20*coef)+" exp", new String[]{
				ChatColor.YELLOW+"Простейшее зачарование этого типа.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"Нужна книга в инвентаре!"
		}, null, 0));
		inv.setItem(12, ItemUtil.create(Material.PAPER, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(50*coef))+""+(int)(50*coef)+" exp", new String[]{
				ChatColor.YELLOW+"Простые зачары до 2, без редких.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"Нужна книга в инвентаре!"
		}, null, 0));
		inv.setItem(14, ItemUtil.create(Material.BOOK, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(150*coef))+""+(int)(150*coef)+" exp", new String[]{
				ChatColor.YELLOW+"Нормальный вариант, равные шансы",
				ChatColor.YELLOW+"и средние уровни.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"Нужна книга в инвентаре!"
		}, null, 0));
		inv.setItem(16, ItemUtil.create(Material.ENCHANTMENT_TABLE, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(500*coef))+""+(int)(500*coef)+" exp", new String[]{
				ChatColor.YELLOW+"Лучший вариант, 90% шанс редкого",
				ChatColor.YELLOW+"зачарования, 2-3 чар/кн, выс. лвл.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"Нужна книга в инвентаре!"
		}, null, 0));
		p.openInventory(inv);
	}
	public static void achs(Player p){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"Достижения");
		int i=0;
		for(String st:main.pr.achs.keySet()){
			inv.setItem(i, main.pr.showAch(st, pi));
			i++;
		}
		p.openInventory(inv);
	}
	public static void getter(Player p){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE+"Пункт приёма");
		int i=0;
		for(String st:pi.collects){
			if(st.contains("Ach"))inv.setItem(i, main.pr.showAchRew(st.substring(3), pi));
			i++;
		}
		p.openInventory(inv);
	}
	@EventHandler
	public void close(InventoryCloseEvent e){
		Player p=(Player) e.getPlayer();
		PlayerInfo pi=Events.plist.get(p.getName());
		if(pi.waits.containsKey("placeUp"))pi.waits.remove("placeUp");
		if(e.getInventory().getName().equals(ChatColor.BLUE+"Положить в ускоритель")){
			GeBlock b=main.gebs.get(pi.lastClickedBlock);
			double added=0;
			for(int i=0;i<27;i++){
				if(e.getInventory().getItem(i)!=null){
					ItemStack item=e.getInventory().getItem(i);
					double add=b.speedFor(item.getType(), item.getAmount());
					if(add<=0){
						p.getInventory().addItem(item);
						p.sendMessage(ChatColor.GOLD+"Предмет "+item.getType()+" не добавит ни секунды ускорения. Возвращаем в инвентарь.");
						continue;
					}
					b.boost+=add*20;
					added+=add;
				}
			}
			if(added>0){
				b.center().getWorld().playSound(b.center(), Sound.BLOCK_LAVA_AMBIENT, 2, 2);
				Island is=ASkyBlockAPI.getInstance().getIslandOwnedBy(p.getUniqueId());
				for(UUID id:is.getMembers()){
					if(Bukkit.getPlayer(id)!=null){
						Player pl=Bukkit.getPlayer(id);
						if(!id.equals(p.getUniqueId()))pl.sendMessage(ChatColor.GREEN+p.getName()+ChatColor.AQUA+" ускорил ГеБлок на "+ChatColor.YELLOW+GepUtil.CylDouble(added, "#0.00")+" сек.!");
					}
				}
				p.sendMessage(ChatColor.AQUA+"ГеБлок ускорен на "+ChatColor.YELLOW+GepUtil.CylDouble(added, "#0.00")+" сек.!");
				if(pi.waits.containsKey("tutor")&&pi.waits.get("tutor")==3){
					p.sendMessage(textUtil.tut+textUtil.mark("ГеБлок ускорен |в 2 раза| на это время.", ChatColor.LIGHT_PURPLE, ChatColor.GREEN));
					p.sendMessage(textUtil.nam+ChatColor.DARK_GREEN+"Кстати, поршень не будет останавливать ГеБлок, в отличии от кирки.");
					p.sendMessage(textUtil.inf+ChatColor.BLUE+"Можно таким образом быстрее добиться руды, но апы за булыжник тогда подождут.");
					p.sendMessage(textUtil.tut+ChatColor.AQUA+"Пока что это - всё. Выполняй квесты из "+ChatColor.GOLD+"/GeQuests"+ChatColor.AQUA+" и получай награды, а пока вот тебе бонус за прохождение туториала:");
					pi.addExp(15);
					p.sendMessage(ChatColor.GREEN+"+15 exp!");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
					GepUtil.HashMapReplacer(pi.waits, "tutor", 1, false, false);
				}
			}else{
				p.sendMessage(ChatColor.GOLD+"Ускорение не произошло.");
			}
		}
		else if(e.getInventory().getName().equals(ChatColor.GOLD+"Положить предметы в преобразователь")){
			Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN+"Предметы преобразованы!");
			ItemStack hitem = p.getInventory().getItemInMainHand();
			int cobble=0;
			for(int i=0;i<27;i++){
				if(e.getInventory().getItem(i)!=null){
					boolean used=false;
					ItemStack item=e.getInventory().getItem(i);
					if(item.getType().equals(Material.GRAVEL)){
						used=true;
						HashMap<String,Integer> drops = new HashMap<>();
						drops.put("COBBLESTONE", 60);
						drops.put("SAND", 10);
						drops.put("FLINT", 1);
						drops.put("-", 50);
						HashMap<String,Integer> dropped=GepUtil.dropsByCoefs(drops, item.getAmount());
						if(dropped.containsKey("-"))dropped.remove("-");
						for(String st:dropped.keySet()){
							inv.addItem(new ItemStack(Material.getMaterial(st),dropped.get(st)));
						}
					}
					if(item.getType().equals(Material.COBBLESTONE)){
						cobble+=item.getAmount();
						used=true;
						HashMap<String,Integer> drops = new HashMap<>();
						drops.put("GRAVEL", 60);
						drops.put("IRON_ORE", 1);
						drops.put("FLINT", 1);
						drops.put("-", 50);
						HashMap<String,Integer> dropped=GepUtil.dropsByCoefs(drops, item.getAmount());
						if(dropped.containsKey("-"))dropped.remove("-");
						for(String st:dropped.keySet()){
							inv.addItem(new ItemStack(Material.getMaterial(st),dropped.get(st)));
						}
					}
					HashMap<String,Integer> bios=new HashMap<>();
					bios.put("LOG", 8);
					bios.put("SAPLING", 24);
					bios.put("SEEDS", 4);
					bios.put("WHEAT", 8);
					bios.put("SUGAR_CANE", 4);
					for(String st:bios.keySet()){
						if(item.getType().toString().contains(st)){
							used=true;
							HashMap<String,Integer> drops = new HashMap<>();
							drops.put("DIRT", 50);
							drops.put("SEEDS", 3);
							drops.put("GRASS", 1);
							drops.put("SAPLING", 3);
							drops.put("BROWN_MUSHROOM", 1);
							drops.put("RED_MUSHROOM", 1);
							drops.put("-", 1000);
							HashMap<String,Integer> dropped=GepUtil.dropsByCoefs(drops, item.getAmount()*bios.get(st));
							if(dropped.containsKey("-"))dropped.remove("-");
							if(dropped.containsKey("SEEDS")){
								dropped.remove("SEEDS");
								String[] ids={"SEEDS","PUMPKIN_SEEDS","MELON_SEEDS","SUGAR_CANE"};
								dropped.put(ids[new Random().nextInt(ids.length)], 1);
							}
							for(String s:dropped.keySet()){
								if(s.equals("SAPLING")){
									inv.addItem(new ItemStack(Material.getMaterial(s),dropped.get(s), (byte) new Random().nextInt(6)));
								}
								else inv.addItem(new ItemStack(Material.getMaterial(s),dropped.get(s)));
							}
							break;
						}
					}
					if(!used){
						inv.addItem(item);
					}
				}
				if(cobble>=64*5&&hitem.getType().equals(Material.BUCKET)){
					double ch=cobble/640.0;
					if(new Random().nextDouble()<=ch){
						hitem.setAmount(hitem.getAmount()-1);
						inv.addItem(ItemUtil.create(Material.LAVA_BUCKET, 1, 0, ChatColor.GOLD+"Образованная лава", new String[]{
								ChatColor.LIGHT_PURPLE+"Она хоть и образованная, но вредина."
						}, null, 0));
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 2, 1);
						p.getWorld().spawnParticle(Particle.LAVA, p.getLocation().add(0, 1, 0), 100, 1, 1, 1, 0);
						pi.addAchievement("Пусть всё горит...");
					}
				}
			}
			main.openGUIlater(p, 1, inv);
		}
	}
	@EventHandler
	public void click(InventoryClickEvent e){
		if(e.getClickedInventory() != null) {
			Player p = (Player) e.getWhoClicked();
			ItemStack item = e.getCurrentItem();
			if(e.getClick().isKeyboardClick()&&(item==null||item.getType().equals(Material.AIR))){
				e.setCancelled(true);
				p.sendMessage(ChatColor.GOLD+"Если не хочешь потерять предмет из за возможных багов, не перекладывай его клавиатурой!");
				return;
			}
			PlayerInfo pi = Events.plist.get(p.getName());
			GeBlock b=main.gebs.get(pi.lastClickedBlock);
			if(pi.waits.containsKey("placeUp")){
				e.setCancelled(true);
				String up=null;
				if(b==null){
					p.sendMessage(ChatColor.RED+"Ошибка при определении выбранного ГеБлока.");
					return;
				}
				if(GepUtil.nameContains(item, ChatColor.AQUA+"Улучшение")){
					String st=GepUtil.getDisplName(item);
					up=st.substring(15, st.length()-1);
				}
				if(item.getType().equals(Material.FLINT)){
					up="Дробилка";
				}
				pi.waits.remove("placeUp");
				if(up==null){
					p.sendMessage(ChatColor.GOLD+"Это не улучшение.");
				}else{
					if(b.ups.contains(up)){
						p.sendMessage(ChatColor.GOLD+"В вашем ГеБлоке уже есть такое улучшение.");
						return;
					}
					item.setAmount(item.getAmount()-1);
					b.ups.add(up);
					p.sendMessage(textUtil.inf+ChatColor.GREEN+"Улучшение установлено!");
					upgeb(p, b);
				}
				return;
			}
			boolean cancel=false;
			for(String st:cancels){
				if(e.getInventory().getName().equals(st)){
					cancel=true;
					e.setCancelled(true);
					break;
				}
			}
			if(cancel){
				if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"Менюшка")){
					if(item.getType().equals(Material.EXP_BOTTLE)){
						achs(p);
					}else if(item.getType().equals(Material.BED)){
						Bukkit.dispatchCommand(p, "is");
					}else if(item.getType().equals(Material.EGG)){
						Bukkit.dispatchCommand(p, "spawn");
					}
				}
				else if(e.getClickedInventory().getName().equals(ChatColor.BLUE+"Пункт приёма")){
					if(GepUtil.loreContains(item, ChatColor.AQUA+"Достижение")){
						String name=item.getItemMeta().getDisplayName().substring(2);
						Achievement ach=main.pr.achs.get(name);
						int free=0;
						for(int i=0;i<36;i++){
							if(p.getInventory().getItem(i)==null||p.getInventory().getItem(i).getType().equals(Material.AIR))free++;
						}
						if(free<=ach.rewards.size()){
							p.sendMessage(ChatColor.RED+"Некуда класть награду. Почистите инвентарь.");
							p.closeInventory();
							return;
						}
						for(ItemStack it:ach.rewards){
							p.getInventory().addItem(it);
						}
						pi.collects.remove("Ach"+name);
						p.sendMessage(ChatColor.GREEN+"Награда получена!");
						p.closeInventory();
					}
				}
				else if(e.getClickedInventory().getName().equals(ChatColor.AQUA+"Настройки ГеБлока")){
					if(item.getType().equals(Material.WATCH)){
						if(b.spawnRateLevel>=6){
							p.sendMessage(ChatColor.AQUA+"Это максимальный уровень!");
							return;
						}
						if(b.speedUp.canUp(p, b.spawnRateLevel, true)){
							b.speedUp.up(p, b.spawnRateLevel);
							b.spawnRateLevel++;
							geb(p,b);
							pi.addAchievement("Больше камня - больше камня...");
							if(b.spawnRateLevel==6)pi.addAchievement("Яростный скорострел");
						}
					}
					else if(item.getType().equals(Material.GOLD_PICKAXE)){
						Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE+"Положить в ускоритель");
						p.openInventory(inv);
					}else if(item.getType().equals(Material.EMERALD)){
						gebUpChances(p, b);
					}else if(item.getType().equals(Material.TNT)){
						int price=0;
						if(b.cCoefs.size()>0)price=30;
						if(pi.gebs.contains(b.loc)&&pi.getExp()>=price){
							pi.addExp(-price);
							pi.gebs.remove(b.loc);
							List<String> lore = new ArrayList<>();
							lore.add(ChatColor.GREEN+"Твоя личная автошахта!");
							lore.add(ChatColor.AQUA+"Просто поставь ГеБлок!");
							if(b.boost>0)lore.add(ChatColor.YELLOW+"Ускорение: "+ChatColor.GOLD+b.boost/20+" сек!");
							if(b.chancesPoints>0||b.cCoefs.size()>0){
								int cp=b.chancesPoints;
								for(String st:b.cCoefs.keySet()){
									cp+=b.cCoefs.get(st);
								}
								lore.add(ChatColor.LIGHT_PURPLE+"Очки шансов: "+ChatColor.GREEN+cp);
							}
							if(b.spawnRateLevel>0)lore.add(ChatColor.AQUA+"Уровень скорости: "+ChatColor.WHITE+b.spawnRateLevel);
							p.getInventory().addItem(ItemUtil.createal(Material.BEDROCK, 1, 0, ChatColor.AQUA+"GeBlock", lore, null, 0));
							main.gebs.remove(b.loc);
							p.closeInventory();
						}else{
							p.sendMessage(ChatColor.GOLD+"30 опыта - цель недостижимая... А ну, убей 3-6 мобов - хороший способ фармить икспу!");
						}
					}else if(item.getType().equals(Material.BARRIER)){
						geb(p, b);
					}else if(item.getType().equals(Material.LEVER)){
						upgeb(p, b);
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"Преобразователь")){
					preobrPlace(p);
				}else if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"Шансы ГеБлока")){
					if(item.getType().equals(Material.DIAMOND)){
						if(b.cUp.canUp(p, b.cUped, true)){
							b.cUp.up(p, b.cUped);
							b.chancesPoints++;
							b.cUped++;
							gebUpChances(p,b);
							p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2, 2);
						}
					}else if(item.getType().equals(Material.TNT)){
						if(b.chancesPoints<b.cUped){
							if(pi.getExp()>=30){
								pi.addExp(-30);
								b.chancesPoints=b.cUped;
								b.cCoefs=new HashMap<>();
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 2, 2);
								p.closeInventory();
								p.sendMessage(ChatColor.GOLD+"Все очки возвращены.");
							}else{
								p.sendMessage(ChatColor.GOLD+"30 опыта - цель недостижимая... А ну, убей 3-6 мобов - хороший способ фармить икспу!");
							}
						}else{
							p.sendMessage(ChatColor.GOLD+"Нечего обнулять.");
						}
					}else if(item.getType().equals(Material.BARRIER)){
						geb(p, b);
					}else{
						GepUtil.HashMapReplacer(b.cCoefs, item.getType().toString(), 1, false, false);
						b.chancesPoints--;
						gebUpChances(p, b);
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.BLUE+"Инкубатор")){
					try{
						Up up=incubPrices.get(item.getItemMeta().getDisplayName());
						if(up.canUp(p, 0, true)){
							up.up(p, 0);
							ItemStack ret=item.clone();
							ItemMeta meta=ret.getItemMeta();
							meta.setLore(null);
							ret.setItemMeta(meta);
							p.getInventory().addItem(ret);
							p.getWorld().playSound(pi.lastClickedBlock.add(0.5, 0.5, 0.5), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 2, 2);
							p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,pi.lastClickedBlock.add(0.5, 0.5, 0.5), 100, 0, 0, 0, 0.1);
							p.closeInventory();
						}
					}catch(Exception ewafsh){
						p.sendMessage(ChatColor.RED+"Произошла ошибка. Просьба сообщить Гепи.");
						return;
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"Улучшения ГеБлока")){
					if(item.getType().equals(Material.DIAMOND)){
						if(b.sUp.canUp(p, b.upslots, true)){
							b.sUp.up(p, b.upslots);
							b.upslots++;
							upgeb(p,b);
							p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2, 2);
						}
					}else if(item.getType().equals(Material.BARRIER)){
						geb(p, b);
					}else{
						if(item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+"Свободно!")){
							pi.waits.put("placeUp", 1);
							p.sendMessage(textUtil.inf+ChatColor.AQUA+"Кликните на улучшение, которое хотите добавить.");
						}else if(item.getItemMeta().getDisplayName().contains("Улучшение")){
							String up=b.ups.get(e.getSlot()-12);
							if(p.getInventory().addItem(main.upToItem(up))==null){
								p.sendMessage(ChatColor.RED+"Освободите место в инвентаре!");
								return;
							}
							p.sendMessage(textUtil.inf+ChatColor.GREEN+"Улучшение "+ChatColor.GOLD+up+ChatColor.GREEN+" возвращено.");
							b.ups.remove(up);
							upgeb(p, b);
						}
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"Улучшайзер")){
					if(item.getType().equals(Material.GOLDEN_CARROT)){
						if(main.pr.upkey.canUp(p, 1, true)){
							main.pr.upkey.up(p, 1);
							p.getInventory().addItem(item);
						}
						return;
					}
					Up up=incubPrices.get(item.getItemMeta().getDisplayName());
					if(up.canUp(p, 0, true)){
						up.up(p, 0);
						ItemStack ret=item.clone();
						ItemMeta meta=ret.getItemMeta();
						meta.setLore(null);
						ret.setItemMeta(meta);
						p.getInventory().addItem(ret);
						p.getWorld().playSound(pi.lastClickedBlock.add(0.5, 0.5, 0.5), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 2, 2);
						p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,pi.lastClickedBlock.add(0.5, 0.5, 0.5), 100, 0, 0, 0, 0.1);
						p.closeInventory();
					}
					p.sendMessage(ChatColor.RED+"Произошла ошибка. Просьба сообщить Гепи.");
					return;
				}else if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"Создать зачарование")){
					GepUtil.HashMapReplacer(pi.waits, "enchType", e.getSlot()/2, false, true);
					double coef=1;
					if(ASkyBlockAPI.getInstance().playerIsOnIsland(p))coef=0.8;
					else{
						p.sendMessage(ChatColor.AQUA+"Имея свой собственный стол зачарований, цены на зачары мешьне на 20%!");
					}
					enchLvl(p, coef);
					
				}else if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"Выберите уровень")){
					if(!GepUtil.haveItem(p, Material.BOOK, 1)){
						p.sendMessage(ChatColor.RED+"У вас нет книги.");
						return;
					}
					int level=(e.getSlot()-10)/2;
					int exp=0;
					if(level==0)exp=20;
					if(level==1)exp=50;
					if(level==2)exp=150;
					if(level==3)exp=500;
					double coef=1;
					if(ASkyBlockAPI.getInstance().playerIsOnIsland(p))coef=0.8;
					if(pi.getExp()<exp*coef){
						p.sendMessage(ChatColor.RED+"У вас не хватает опыта.");
						return;
					}
					ArrayList<Enchantment> bases=new ArrayList<>();
					ArrayList<Enchantment> advs=new ArrayList<>();
					HashMap<Enchantment,Integer> enchs=new HashMap<>();
					if(pi.waits.get("enchType")==0){//weapons
						bases.add(Enchantment.DAMAGE_ALL);
						bases.add(Enchantment.DAMAGE_ARTHROPODS);
						bases.add(Enchantment.DAMAGE_UNDEAD);
						bases.add(Enchantment.KNOCKBACK);
						advs.add(Enchantment.LOOT_BONUS_MOBS);
						advs.add(Enchantment.FIRE_ASPECT);
						advs.add(Enchantment.SWEEPING_EDGE);
					}
					else if(pi.waits.get("enchType")==1){//bows
						bases.add(Enchantment.ARROW_DAMAGE);
						advs.add(Enchantment.ARROW_FIRE);
						advs.add(Enchantment.ARROW_INFINITE);
						advs.add(Enchantment.ARROW_KNOCKBACK);
					}
					else if(pi.waits.get("enchType")==2){//tools
						bases.add(Enchantment.DIG_SPEED);
						bases.add(Enchantment.DURABILITY);
						advs.add(Enchantment.LOOT_BONUS_BLOCKS);
						advs.add(Enchantment.MENDING);
						advs.add(Enchantment.SILK_TOUCH);
					}
					else if(pi.waits.get("enchType")==3){//armor
						bases.add(Enchantment.PROTECTION_ENVIRONMENTAL);
						bases.add(Enchantment.PROTECTION_EXPLOSIONS);
						bases.add(Enchantment.PROTECTION_PROJECTILE);
						bases.add(Enchantment.PROTECTION_FIRE);
						bases.add(Enchantment.PROTECTION_FALL);
						advs.add(Enchantment.FROST_WALKER);
						advs.add(Enchantment.OXYGEN);
						advs.add(Enchantment.THORNS);
						advs.add(Enchantment.WATER_WORKER);
						advs.add(Enchantment.DEPTH_STRIDER);
					}
					else if(pi.waits.get("enchType")==4){//rods
						bases.add(Enchantment.LURE);
						advs.add(Enchantment.LUCK);
					}
					int basAm=0;
					int advAm=0;
					double lvl=0;
					if(level==0){
						basAm=1;
						lvl=0.1;
					}if(level==1){
						basAm=GepUtil.ChanceToInt(1, 1.5);
						lvl=0.35;
					}if(level==2){
						basAm=GepUtil.ChanceToInt(1, 0.75);
						basAm+=GepUtil.ChanceToInt(1, 1.5);
						advAm=GepUtil.ChanceToInt(1, 0.5);
						lvl=0.8;
					}if(level==3){
						basAm=GepUtil.ChanceToInt(1, 2.5);
						advAm=GepUtil.ChanceToInt(1, 0.9);
						lvl=1.15;
					}
					Collections.shuffle(bases);
					Collections.shuffle(advs);
					if(bases.size()<basAm)basAm=bases.size();
					while(basAm>0){
						Enchantment ench=bases.get(basAm-1);
						basAm--;
						int setlvl=ench.getMaxLevel();
						setlvl=(int) (setlvl*(lvl*new Random().nextDouble())+1);
						enchs.put(ench, setlvl);
					}
					while(advAm>0){
						Enchantment ench=bases.get(advAm-1);
						advAm--;
						int setlvl=ench.getMaxLevel();
						setlvl=(int) (setlvl*(lvl*new Random().nextDouble())+1);
						enchs.put(ench, setlvl);
					}
					ItemStack book=ItemUtil.create(Material.ENCHANTED_BOOK, 1, 0, null, null, null, 0);
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
					for(Enchantment ench:enchs.keySet()){
						meta.addStoredEnchant(ench, enchs.get(ench), true);
					}
					book.setItemMeta(meta);
					GepUtil.sellItems(p, Material.BOOK, null, 1);
					p.getInventory().addItem(book);
					pi.addExp((int) (-exp*coef));
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2, 1);
				}
			}
		}
	}
}
