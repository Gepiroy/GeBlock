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
	static String[] cancels={ChatColor.BLUE+"����� �����",ChatColor.LIGHT_PURPLE+"�������",ChatColor.LIGHT_PURPLE+"����������",ChatColor.AQUA+"��������� �������",ChatColor.GOLD+"���������������",ChatColor.LIGHT_PURPLE+"����� �������",ChatColor.BLUE+"���������",ChatColor.GOLD+"��������� �������",ChatColor.GOLD+"����������",ChatColor.LIGHT_PURPLE+"������� �����������",ChatColor.LIGHT_PURPLE+"�������� �������"};
	public static void load(){
		{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("WOOL", 32);
			Up up=new Up(1250, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"����", up);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("CARROT_ITEM", 64);
			Up up=new Up(1000, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"������", up);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("LEATHER", 16);
			Up up=new Up(1250, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"������", up);
		}{
			HashMap<String,Integer> Needs = new HashMap<>();
			Needs.put("SEEDS", 64);
			Up up=new Up(350, 1, Needs, 1);
			incubPrices.put(ChatColor.GOLD+"������� ����", up);
		}
	}
	public static void menu(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"�������");
		inv.setItem(0, ItemUtil.create(Material.EXP_BOTTLE, 1, 0, ChatColor.AQUA+"����������", new String[]{
				ChatColor.BLUE+"�� ��� '��������' ��������"
		}, null, 0));
		inv.setItem(13, ItemUtil.create(Material.BED, 1, 0, ChatColor.AQUA+"������", new String[]{
				ChatColor.BLUE+"������ ����������� ������� "+ChatColor.AQUA+"/is"
		}, null, 0));
		inv.setItem(15, ItemUtil.create(Material.EGG, 1, 0, ChatColor.AQUA+"�� �����", new String[]{
				ChatColor.BLUE+"� �� �����, ��� �� �������)",
				ChatColor.AQUA+"�� ������ ����� ��� ������,",
				ChatColor.AQUA+"�������, �� � ����� ��� ��."
		}, null, 0));
		p.openInventory(inv);
	}
	public static void geb(Player p, GeBlock b){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.AQUA+"��������� �������");
		{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"������ �������� ����� �������: "+GepUtil.CylDouble(b.spawnRate(true, false),"#0.00")+" ���. "+ChatColor.GRAY+"("+b.spawnRateLevel+"/6)");
			if(b.spawnRateLevel<6){
				lore.add(ChatColor.YELLOW+"��������� �������: "+GepUtil.CylDouble(b.spawnRate(true, false)-0.75,"#0.00")+" ���."+ChatColor.GRAY+"("+(b.spawnRateLevel+1)+"/6)");
				lore.add(ChatColor.GOLD+"���� ���������:");
				for(String st:b.speedUp.lore(p, b.spawnRateLevel)){
					lore.add(ChatColor.WHITE+" -"+st);
				}
			}
			else lore.add(ChatColor.DARK_GREEN+"��� - ������������ �������!");
			inv.setItem(11, ItemUtil.createal(Material.WATCH, 1, 0, ChatColor.AQUA+"��������", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			if(b.boost>0)lore.add(ChatColor.LIGHT_PURPLE+"������ �������! "+ChatColor.RED+b.boost/20.0+" ���.!");
			lore.add(ChatColor.GREEN+"������ ����� �������� � ������� ��������.");
			lore.add(ChatColor.GREEN+"��� ������� �������� ������ - ��� ������ ��������� �� �������.");
			lore.add(ChatColor.GREEN+"�� ������� ���� ��������� ��������:");
			lore.add(ChatColor.AQUA+"�������� ������� �� "+GepUtil.CylDouble(b.speedFor(Material.COBBLESTONE, 1),"#0.00")+" ���.");
			lore.add(ChatColor.AQUA+"������ ������� �� "+GepUtil.CylDouble(b.speedFor(Material.STONE, 1),"#0.00")+" ���.");
			lore.add(ChatColor.AQUA+"�������� ������� �� "+GepUtil.CylDouble(b.speedFor(Material.REDSTONE, 1),"#0.00")+" ���.");
			inv.setItem(13, ItemUtil.createal(Material.GOLD_PICKAXE, 1, 0, ChatColor.YELLOW+"���������", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"��������� ����� ������� � ����������� �� ������.");
			inv.setItem(15, ItemUtil.createal(Material.LEVER, 1, 0, ChatColor.AQUA+"���������", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"������� GUI ��������� ������.");
			inv.setItem(18, ItemUtil.createal(Material.EMERALD, 1, 0, ChatColor.GREEN+"�����", lore, null, 0));
		}if(b.owner.equals(p.getUniqueId())){
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"������� ������, ����� ����������� ���.");
			lore.add(ChatColor.AQUA+"�� ����������!");
			inv.setItem(26, ItemUtil.createal(Material.TNT, 1, 0, ChatColor.GOLD+"�������", lore, null, 0));
			
		}
		p.openInventory(inv);
	}
	public static void preobr(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"���������������");
		{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"�������� ������������� � ������ � �������� ���������.");
			HashMap<String,Integer> hm=main.pr.preobrChs.get("COBBLESTONE");
			for(String st:hm.keySet()){
				lore.add(ChatColor.WHITE+" -"+ChatColor.YELLOW+textUtil.ruMaterial(st, true)+ChatColor.AQUA+" "+GepUtil.CylDouble(GepUtil.chanceFromCoef(hm, st), "#0.00")+"%");
			}
			inv.setItem(10, ItemUtil.createal(Material.COBBLESTONE, 1, 0, ChatColor.GOLD+"��������", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GREEN+"������ ������������� � �������� � �������� ���������.");
			inv.setItem(12, ItemUtil.createal(Material.GRAVEL, 1, 0, ChatColor.GOLD+"������", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.DARK_GREEN+"���-���������:");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"������ "+ChatColor.DARK_GREEN+"(8 �����)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"������� "+ChatColor.DARK_GREEN+"(24 ����)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"������ "+ChatColor.DARK_GREEN+"(4 ����)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"������� "+ChatColor.DARK_GREEN+"(8 �����)");
			lore.add(ChatColor.WHITE+" -"+ChatColor.GREEN+"�������� "+ChatColor.DARK_GREEN+"(4 ����)");
			lore.add(ChatColor.AQUA+"�� ��� ����� �������� ����� ���� �������� � ����� �����!");
			inv.setItem(14, ItemUtil.createal(Material.DIRT, 1, 0, ChatColor.DARK_GREEN+"���", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"����� �������� ����, �����:");
			lore.add(ChatColor.YELLOW+"���������� ��������, ������� � ��. ���� �����;");
			lore.add(ChatColor.YELLOW+"��� ���� ������ ���� �� ����� 5-�� ������");
			lore.add(ChatColor.YELLOW+"���������. (�� ������ ���� +10% ����, ������� � 50)");
			inv.setItem(23, ItemUtil.createal(Material.LAVA_BUCKET, 1, 0, ChatColor.GOLD+"����", lore, null, 0));
		}
		p.openInventory(inv);
	}
	public static void creator(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"����������");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD+"��������� ������ ���������, �");
		lore.add(ChatColor.GOLD+"������� ����� ��������� ���������.");
		Up up=main.pr.upkey;
		lore.add(ChatColor.WHITE+"  ---����---  ");
		for(String s:up.lore(p, 1)){
			lore.add(ChatColor.WHITE+" -"+s);
		}
		inv.setItem(12, ItemUtil.createal(Material.GOLDEN_CARROT, 1, 0, ChatColor.GOLD+"���� ���������", lore, Enchantment.ARROW_DAMAGE, 10));
		inv.setItem(14, ItemUtil.create(Material.GOLDEN_CARROT, 1, 0, ChatColor.GOLD+"���� ���������", new String[]{
				ChatColor.GOLD+"��������� ������ ���������, �",
				ChatColor.GOLD+"������� ����� ��������� ���������.",
				ChatColor.WHITE+" -"+ChatColor.AQUA+"19 ���."
		}, Enchantment.ARROW_DAMAGE, 10));
		p.openInventory(inv);
	}
	public static void incub(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE+"���������");
		{
			Up up=incubPrices.get(ChatColor.GOLD+"����");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"����������:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(10, ItemUtil.createal(Material.MONSTER_EGG, 1, 91, ChatColor.GOLD+"����", lore, null, 0));
		}{
			Up up=incubPrices.get(ChatColor.GOLD+"������");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"����������:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(12, ItemUtil.createal(Material.MONSTER_EGG, 1, 101, ChatColor.GOLD+"������", lore, null, 0));
		}{
			Up up=incubPrices.get(ChatColor.GOLD+"������");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"����������:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(14, ItemUtil.createal(Material.MONSTER_EGG, 1, 92, ChatColor.GOLD+"������", lore, null, 0));
		}{
			Up up=incubPrices.get(ChatColor.GOLD+"������� ����");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"����������:");
			for(String st:up.lore(p, 0)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(16, ItemUtil.createal(Material.EGG, 1, 0, ChatColor.GOLD+"������� ����", lore, null, 0));
		}
		p.openInventory(inv);
	}
	public static void gebUpChances(Player p, GeBlock b){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"����� �������");
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
			lore.add(ChatColor.GREEN+"���� ������ ��������� �������� ����� �� ��������� ��������� ����.");
			lore.add(ChatColor.GREEN+"��������� ���� ����� ����� �������� � ������� ����� ����������� ����.");
			lore.add(ChatColor.AQUA+"�������� �����: "+ChatColor.GOLD+ChatColor.BOLD+b.chancesPoints);
			lore.add(ChatColor.GOLD+"���� ������ ����:");
			for(String st:b.cUp.lore(p, b.cUped)){
				lore.add(ChatColor.WHITE+" -"+st);
			}
			inv.setItem(22, ItemUtil.createal(Material.DIAMOND, 1, 0, ChatColor.AQUA+"���� ������", lore, Enchantment.PROTECTION_ENVIRONMENTAL, 10));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"����� � ������.");
			inv.setItem(18, ItemUtil.createal(Material.BARRIER, 1, 0, ChatColor.RED+"���������", lore, null, 0));
		}{
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD+"������� ���� ������ �� ��������� ������.");
			lore.add(ChatColor.GOLD+"��� ����� "+GepUtil.boolCol(pi.getExp()>=30)+"30 exp.");
			inv.setItem(26, ItemUtil.createal(Material.TNT, 1, 0, ChatColor.RED+"���������", lore, null, 0));
		}
		p.openInventory(inv);
	}
	public static void upgeb(Player p, GeBlock b){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"��������� �������");
		for(int i=0;i<27;i++){
			if(i>=12&&i<=14){
				if(b.upslots<i-11){
					inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 14, ChatColor.GOLD+"�������������", null, Enchantment.PROTECTION_ENVIRONMENTAL, 10));
				}
				else if(b.ups.size()>=i-11){
					String up=b.ups.get(i-12);
					inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 4, ChatColor.YELLOW+"��������� "+ChatColor.GOLD+up, new String[]{ChatColor.AQUA+"����, ����� ������!"}, Enchantment.PROTECTION_ENVIRONMENTAL, 10));
				}else{
					inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 5, ChatColor.GREEN+"��������!", new String[]{ChatColor.AQUA+"����, ����� ��������!"}, null, 0));
				}
			}
			else inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE, 1, 0, " ", null, null, 0));
		}
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN+"��������� ����� ��������� � ��������������� "+ChatColor.AQUA+"�� ������!");
		lore.add(ChatColor.GOLD+"���� ������ �����:");
		for(String st:b.sUp.lore(p, b.upslots)){
			lore.add(ChatColor.WHITE+" -"+st);
		}
		inv.setItem(26, ItemUtil.createal(Material.DIAMOND, 1, 0, ChatColor.AQUA+"������ ����", lore, null, 0));
		inv.setItem(18, ItemUtil.create(Material.BARRIER, 1, 0, ChatColor.RED+"���������", new String[]{ChatColor.GOLD+"����� � ������."}, null, 0));
		p.openInventory(inv);
	}
	public static void preobrPlace(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GOLD+"�������� �������� � ���������������");
		p.openInventory(inv);
	}
	public static void ench(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"������� �����������");
		inv.setItem(0, ItemUtil.create(Material.GOLD_SWORD, 1, 0, ChatColor.GOLD+"������", new String[]{
				ChatColor.YELLOW+"�����������, ��������� ��� ������.",
				ChatColor.GOLD+"�������, ������ � �������� ���.",
				ChatColor.AQUA+"  ���� ��� �������� � ������"
		}, null, 0));
		inv.setItem(2, ItemUtil.create(Material.BOW, 1, 0, ChatColor.GOLD+"����", new String[]{
				ChatColor.YELLOW+"�����������, ��������� ��� �����.",
				ChatColor.GOLD+"����, ����������� � �������� ���.",
				ChatColor.AQUA+"  ���� ��� �������� � ������"
		}, null, 0));
		inv.setItem(4, ItemUtil.create(Material.GOLD_PICKAXE, 1, 0, ChatColor.GOLD+"�����������", new String[]{
				ChatColor.YELLOW+"�����������, ��������� ��� ������������.",
				ChatColor.GOLD+"�������������, ����� � �������� ���.",
				ChatColor.GRAY+"(������� ���)",
				ChatColor.AQUA+"  ���� ��� �������� � ������"
		}, null, 0));
		inv.setItem(6, ItemUtil.create(Material.GOLD_CHESTPLATE, 1, 0, ChatColor.GOLD+"�����", new String[]{
				ChatColor.YELLOW+"�����������, ��������� ��� �����.",
				ChatColor.GOLD+"������, ���� � �������� ���.",
				ChatColor.AQUA+"  ���� ��� �������� � ������"
		}, null, 0));
		inv.setItem(8, ItemUtil.create(Material.FISHING_ROD, 1, 0, ChatColor.GOLD+"������", new String[]{
				ChatColor.YELLOW+"�����������, ��������� ��� ������.",
				ChatColor.GOLD+"���. �����, �������� � �������� ���.",
				ChatColor.AQUA+"  ���� ��� �������� � ������"
		}, null, 0));
		inv.setItem(22, ItemUtil.create(Material.BOOK, 1, 0, ChatColor.GOLD+"� ��� ������-��?", new String[]{
				ChatColor.GREEN+"������ �������� ������� �� ��������.",
				ChatColor.GOLD+"����� ������ ������!",
				ChatColor.AQUA+"���� ������ �� ��� ������, ������ ��� ������."
		}, null, 0));
		p.openInventory(inv);
	}
	public static void enchLvl(Player p, double coef){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"�������� �������");
		inv.setItem(10, ItemUtil.create(Material.SUGAR_CANE, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(20*coef))+""+(int)(20*coef)+" exp", new String[]{
				ChatColor.YELLOW+"���������� ����������� ����� ����.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"����� ����� � ���������!"
		}, null, 0));
		inv.setItem(12, ItemUtil.create(Material.PAPER, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(50*coef))+""+(int)(50*coef)+" exp", new String[]{
				ChatColor.YELLOW+"������� ������ �� 2, ��� ������.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"����� ����� � ���������!"
		}, null, 0));
		inv.setItem(14, ItemUtil.create(Material.BOOK, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(150*coef))+""+(int)(150*coef)+" exp", new String[]{
				ChatColor.YELLOW+"���������� �������, ������ �����",
				ChatColor.YELLOW+"� ������� ������.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"����� ����� � ���������!"
		}, null, 0));
		inv.setItem(16, ItemUtil.create(Material.ENCHANTMENT_TABLE, 1, 0, GepUtil.boolCol(pi.getExp()>=(int)(500*coef))+""+(int)(500*coef)+" exp", new String[]{
				ChatColor.YELLOW+"������ �������, 90% ���� �������",
				ChatColor.YELLOW+"�����������, 2-3 ���/��, ���. ���.",
				GepUtil.boolCol(GepUtil.haveItem(p, Material.BOOK, 1))+"����� ����� � ���������!"
		}, null, 0));
		p.openInventory(inv);
	}
	public static void achs(Player p){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE+"����������");
		int i=0;
		for(String st:main.pr.achs.keySet()){
			inv.setItem(i, main.pr.showAch(st, pi));
			i++;
		}
		p.openInventory(inv);
	}
	public static void getter(Player p){
		PlayerInfo pi=Events.plist.get(p.getName());
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE+"����� �����");
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
		if(e.getInventory().getName().equals(ChatColor.BLUE+"�������� � ����������")){
			GeBlock b=main.gebs.get(pi.lastClickedBlock);
			double added=0;
			for(int i=0;i<27;i++){
				if(e.getInventory().getItem(i)!=null){
					ItemStack item=e.getInventory().getItem(i);
					double add=b.speedFor(item.getType(), item.getAmount());
					if(add<=0){
						p.getInventory().addItem(item);
						p.sendMessage(ChatColor.GOLD+"������� "+item.getType()+" �� ������� �� ������� ���������. ���������� � ���������.");
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
						if(!id.equals(p.getUniqueId()))pl.sendMessage(ChatColor.GREEN+p.getName()+ChatColor.AQUA+" ������� ������ �� "+ChatColor.YELLOW+GepUtil.CylDouble(added, "#0.00")+" ���.!");
					}
				}
				p.sendMessage(ChatColor.AQUA+"������ ������� �� "+ChatColor.YELLOW+GepUtil.CylDouble(added, "#0.00")+" ���.!");
				if(pi.waits.containsKey("tutor")&&pi.waits.get("tutor")==3){
					p.sendMessage(textUtil.tut+textUtil.mark("������ ������� |� 2 ����| �� ��� �����.", ChatColor.LIGHT_PURPLE, ChatColor.GREEN));
					p.sendMessage(textUtil.nam+ChatColor.DARK_GREEN+"������, ������� �� ����� ������������� ������, � ������� �� �����.");
					p.sendMessage(textUtil.inf+ChatColor.BLUE+"����� ����� ������� ������� �������� ����, �� ��� �� �������� ����� ��������.");
					p.sendMessage(textUtil.tut+ChatColor.AQUA+"���� ��� ��� - ��. �������� ������ �� "+ChatColor.GOLD+"/GeQuests"+ChatColor.AQUA+" � ������� �������, � ���� ��� ���� ����� �� ����������� ���������:");
					pi.addExp(15);
					p.sendMessage(ChatColor.GREEN+"+15 exp!");
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
					GepUtil.HashMapReplacer(pi.waits, "tutor", 1, false, false);
				}
			}else{
				p.sendMessage(ChatColor.GOLD+"��������� �� ���������.");
			}
		}
		else if(e.getInventory().getName().equals(ChatColor.GOLD+"�������� �������� � ���������������")){
			Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN+"�������� �������������!");
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
						inv.addItem(ItemUtil.create(Material.LAVA_BUCKET, 1, 0, ChatColor.GOLD+"������������ ����", new String[]{
								ChatColor.LIGHT_PURPLE+"��� ���� � ������������, �� �������."
						}, null, 0));
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 2, 1);
						p.getWorld().spawnParticle(Particle.LAVA, p.getLocation().add(0, 1, 0), 100, 1, 1, 1, 0);
						pi.addAchievement("����� �� �����...");
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
				p.sendMessage(ChatColor.GOLD+"���� �� ������ �������� ������� �� �� ��������� �����, �� ������������ ��� �����������!");
				return;
			}
			PlayerInfo pi = Events.plist.get(p.getName());
			GeBlock b=main.gebs.get(pi.lastClickedBlock);
			if(pi.waits.containsKey("placeUp")){
				e.setCancelled(true);
				String up=null;
				if(b==null){
					p.sendMessage(ChatColor.RED+"������ ��� ����������� ���������� �������.");
					return;
				}
				if(GepUtil.nameContains(item, ChatColor.AQUA+"���������")){
					String st=GepUtil.getDisplName(item);
					up=st.substring(15, st.length()-1);
				}
				if(item.getType().equals(Material.FLINT)){
					up="��������";
				}
				pi.waits.remove("placeUp");
				if(up==null){
					p.sendMessage(ChatColor.GOLD+"��� �� ���������.");
				}else{
					if(b.ups.contains(up)){
						p.sendMessage(ChatColor.GOLD+"� ����� ������� ��� ���� ����� ���������.");
						return;
					}
					item.setAmount(item.getAmount()-1);
					b.ups.add(up);
					p.sendMessage(textUtil.inf+ChatColor.GREEN+"��������� �����������!");
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
				if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"�������")){
					if(item.getType().equals(Material.EXP_BOTTLE)){
						achs(p);
					}else if(item.getType().equals(Material.BED)){
						Bukkit.dispatchCommand(p, "is");
					}else if(item.getType().equals(Material.EGG)){
						Bukkit.dispatchCommand(p, "spawn");
					}
				}
				else if(e.getClickedInventory().getName().equals(ChatColor.BLUE+"����� �����")){
					if(GepUtil.loreContains(item, ChatColor.AQUA+"����������")){
						String name=item.getItemMeta().getDisplayName().substring(2);
						Achievement ach=main.pr.achs.get(name);
						int free=0;
						for(int i=0;i<36;i++){
							if(p.getInventory().getItem(i)==null||p.getInventory().getItem(i).getType().equals(Material.AIR))free++;
						}
						if(free<=ach.rewards.size()){
							p.sendMessage(ChatColor.RED+"������ ������ �������. ��������� ���������.");
							p.closeInventory();
							return;
						}
						for(ItemStack it:ach.rewards){
							p.getInventory().addItem(it);
						}
						pi.collects.remove("Ach"+name);
						p.sendMessage(ChatColor.GREEN+"������� ��������!");
						p.closeInventory();
					}
				}
				else if(e.getClickedInventory().getName().equals(ChatColor.AQUA+"��������� �������")){
					if(item.getType().equals(Material.WATCH)){
						if(b.spawnRateLevel>=6){
							p.sendMessage(ChatColor.AQUA+"��� ������������ �������!");
							return;
						}
						if(b.speedUp.canUp(p, b.spawnRateLevel, true)){
							b.speedUp.up(p, b.spawnRateLevel);
							b.spawnRateLevel++;
							geb(p,b);
							pi.addAchievement("������ ����� - ������ �����...");
							if(b.spawnRateLevel==6)pi.addAchievement("�������� ����������");
						}
					}
					else if(item.getType().equals(Material.GOLD_PICKAXE)){
						Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE+"�������� � ����������");
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
							lore.add(ChatColor.GREEN+"���� ������ ���������!");
							lore.add(ChatColor.AQUA+"������ ������� ������!");
							if(b.boost>0)lore.add(ChatColor.YELLOW+"���������: "+ChatColor.GOLD+b.boost/20+" ���!");
							if(b.chancesPoints>0||b.cCoefs.size()>0){
								int cp=b.chancesPoints;
								for(String st:b.cCoefs.keySet()){
									cp+=b.cCoefs.get(st);
								}
								lore.add(ChatColor.LIGHT_PURPLE+"���� ������: "+ChatColor.GREEN+cp);
							}
							if(b.spawnRateLevel>0)lore.add(ChatColor.AQUA+"������� ��������: "+ChatColor.WHITE+b.spawnRateLevel);
							p.getInventory().addItem(ItemUtil.createal(Material.BEDROCK, 1, 0, ChatColor.AQUA+"GeBlock", lore, null, 0));
							main.gebs.remove(b.loc);
							p.closeInventory();
						}else{
							p.sendMessage(ChatColor.GOLD+"30 ����� - ���� ������������... � ��, ���� 3-6 ����� - ������� ������ ������� �����!");
						}
					}else if(item.getType().equals(Material.BARRIER)){
						geb(p, b);
					}else if(item.getType().equals(Material.LEVER)){
						upgeb(p, b);
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"���������������")){
					preobrPlace(p);
				}else if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"����� �������")){
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
								p.sendMessage(ChatColor.GOLD+"��� ���� ����������.");
							}else{
								p.sendMessage(ChatColor.GOLD+"30 ����� - ���� ������������... � ��, ���� 3-6 ����� - ������� ������ ������� �����!");
							}
						}else{
							p.sendMessage(ChatColor.GOLD+"������ ��������.");
						}
					}else if(item.getType().equals(Material.BARRIER)){
						geb(p, b);
					}else{
						GepUtil.HashMapReplacer(b.cCoefs, item.getType().toString(), 1, false, false);
						b.chancesPoints--;
						gebUpChances(p, b);
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.BLUE+"���������")){
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
						p.sendMessage(ChatColor.RED+"��������� ������. ������� �������� ����.");
						return;
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"��������� �������")){
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
						if(item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+"��������!")){
							pi.waits.put("placeUp", 1);
							p.sendMessage(textUtil.inf+ChatColor.AQUA+"�������� �� ���������, ������� ������ ��������.");
						}else if(item.getItemMeta().getDisplayName().contains("���������")){
							String up=b.ups.get(e.getSlot()-12);
							if(p.getInventory().addItem(main.upToItem(up))==null){
								p.sendMessage(ChatColor.RED+"���������� ����� � ���������!");
								return;
							}
							p.sendMessage(textUtil.inf+ChatColor.GREEN+"��������� "+ChatColor.GOLD+up+ChatColor.GREEN+" ����������.");
							b.ups.remove(up);
							upgeb(p, b);
						}
					}
				}else if(e.getClickedInventory().getName().equals(ChatColor.GOLD+"����������")){
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
					p.sendMessage(ChatColor.RED+"��������� ������. ������� �������� ����.");
					return;
				}else if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"������� �����������")){
					GepUtil.HashMapReplacer(pi.waits, "enchType", e.getSlot()/2, false, true);
					double coef=1;
					if(ASkyBlockAPI.getInstance().playerIsOnIsland(p))coef=0.8;
					else{
						p.sendMessage(ChatColor.AQUA+"���� ���� ����������� ���� �����������, ���� �� ������ ������ �� 20%!");
					}
					enchLvl(p, coef);
					
				}else if(e.getClickedInventory().getName().equals(ChatColor.LIGHT_PURPLE+"�������� �������")){
					if(!GepUtil.haveItem(p, Material.BOOK, 1)){
						p.sendMessage(ChatColor.RED+"� ��� ��� �����.");
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
						p.sendMessage(ChatColor.RED+"� ��� �� ������� �����.");
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
