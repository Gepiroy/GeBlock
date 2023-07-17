package utilsGeB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import obj.Achievement;
import obj.PlayerInfo;
import obj.Up;

public class Prices {
	public Up upkey;
	public HashMap<String,Achievement> achs=new HashMap<>();
	public HashMap<String,HashMap<String,Integer>> preobrChs=new HashMap<>();
	public static HashMap<String,Up> enchs=new HashMap<>();
	public Prices(){
		String col=ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC;
		{
			HashMap<String,Integer> needs=new HashMap<>();
			needs.put("EMERALD", 5);
			needs.put("PAPER", 32);
			needs.put("WATCH", 1);
			upkey= new Up(200, 1, needs, 1);
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.COBBLESTONE, 8, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Увеличте скорость ГеБлока хотя бы на 1 уровень.");
			achs.put("Больше камня - больше камня...", new Achievement(Material.COBBLESTONE, Rewards, lore, 10, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.IRON_PICKAXE, 1, 500, ChatColor.AQUA+"Дюпокирка", null, Enchantment.LOOT_BONUS_BLOCKS, 4));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Прокачайте скорость ГеБлока на максимум.");
			achs.put("Яростный скорострел", new Achievement(Material.GOLD_PICKAXE, Rewards, lore, 100, true));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.IRON_DOOR, 1, 0, null, null, null, 0));
			Rewards.add(ItemUtil.create(Material.REDSTONE, 5, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Открой "+ChatColor.GOLD+"все машины на спавне"+col+"!");
			achs.put("Халявный анлокер", new Achievement(Material.IRON_FENCE, Rewards, lore, 25, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.GOLDEN_APPLE, 1, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Съешь "+ChatColor.GOLD+"Ключ улучшений");
			achs.put("НЕ ЕШЬ ЕГО!", new Achievement(Material.GOLDEN_CARROT, Rewards, lore, 25, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.EMERALD, 1, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Преобразуй фулл инвент камня за раз "+ChatColor.GOLD+"(27x64=1728)");
			achs.put("Переобразованный", new Achievement(Material.SMOOTH_BRICK, Rewards, lore, 100, true));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.EMERALD_ORE, 1, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Выкопай изумрудную руду");
			achs.put("Не в изумрудах счастье...", new Achievement(Material.EMERALD_ORE, Rewards, lore, 15, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.DIAMOND_ORE, 1, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Выкопай алмазную руду");
			achs.put("Откопанный лучший друг", new Achievement(Material.DIAMOND_ORE, Rewards, lore, 15, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.createTool(Material.GOLD_HOE, ChatColor.GOLD+"Опытная мотыга", new String[]{
					ChatColor.AQUA+"Даёт exp, "+ChatColor.GOLD+"НО",
					ChatColor.YELLOW+"уничтожает урожай."
			}, null, null));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Собери такой урожай, где за 10 секунд");
			lore.add(col+"ты получишь более 10 exp.");
			lore.add(col+"Опытной мотыгой не в счёт.");
			achs.put("Опытный фермер", new Achievement(Material.GOLD_HOE, Rewards, lore, 50, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.ROTTEN_FLESH, 16, 0, null, null, null, 0));
			Rewards.add(ItemUtil.create(Material.CARROT_ITEM, 3, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Выбей морковь из зомби");
			achs.put("Откуда у них морковка?", new Achievement(Material.CARROT_ITEM, Rewards, lore, 35, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.POTATO_ITEM, 16, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Выбей картофель из зомби");
			achs.put("Может, это их батарейка?", new Achievement(Material.POTATO_ITEM, Rewards, lore, 35, false));
		}{
			ArrayList<ItemStack> Rewards = new ArrayList<>();
			Rewards.add(ItemUtil.create(Material.OBSIDIAN, 2, 0, null, null, null, 0));
			List<String> lore = new ArrayList<>();
			lore.add(col+"Получите лаву в преобразователе на спавне.");
			achs.put("Пусть всё горит...", new Achievement(Material.LAVA_BUCKET, Rewards, lore, 35, false));
		}{
			HashMap<String,Integer> drops = new HashMap<>();
			drops.put("GRAVEL", 600);
			drops.put("SAND", 35);
			drops.put("IRON_ORE", 7);
			drops.put("GOLD_ORE", 3);
			drops.put("FLINT", 1);
			drops.put("-", 500);
			preobrChs.put("COBBLESTONE", drops);
		}{
			HashMap<String,Integer> drops = new HashMap<>();
			drops.put("COBBLESTONE", 600);
			drops.put("SAND", 85);
			drops.put("FLINT", 10);
			drops.put("SULPHUR", 8);
			drops.put("GLOWSTONE_DUST", 1);
			drops.put("REDSTONE", 1);
			drops.put("COAL", 7);
			drops.put("-", 500);
			preobrChs.put("GRAVEL", drops);
		}
	}
	public ItemStack showAch(String st,PlayerInfo pi){
		Achievement ach=achs.get(st);
		if(ach==null)return ItemUtil.create(Material.BARRIER, 1, 0, st, null, null, 0);
		List<String> lore = new ArrayList<>(ach.lore);
		lore.add(ChatColor.WHITE+"  --Награды--  ");
		if(ach.exp>0)lore.add(ChatColor.WHITE+"   +"+ChatColor.AQUA+ach.exp+ChatColor.BLUE+" exp");
		for(ItemStack item:ach.rewards){
			String post="";
			if(item.getAmount()>1)post=ChatColor.WHITE+" x"+ChatColor.GREEN+item.getAmount();
			if(item.hasItemMeta()&&item.getItemMeta().hasDisplayName()){
				lore.add(ChatColor.WHITE+" -"+ChatColor.YELLOW+item.getItemMeta().getDisplayName()+post);
			}else{
				lore.add(ChatColor.WHITE+" -"+ChatColor.YELLOW+textUtil.ruMaterial(item.getType().toString(), true)+post);
			}
			for(Enchantment e:item.getItemMeta().getEnchants().keySet()){
				lore.add(ChatColor.DARK_PURPLE+"  *"+ChatColor.LIGHT_PURPLE+ChatColor.ITALIC+e.getName()+" "+ChatColor.AQUA+GepUtil.intToRoman(item.getItemMeta().getEnchants().get(e)));
			}
		}
		Enchantment ench=null;
		if(pi.achievements.contains(st)){
			ench=Enchantment.ARROW_DAMAGE;
			lore.add(ChatColor.GREEN+"Выполнено!");
		}
    	return ItemUtil.createal(ach.mat, 1, 0, GepUtil.boolCol(ChatColor.DARK_RED, ChatColor.LIGHT_PURPLE, ach.isHard)+st, lore, ench, 10);
    }
	public ItemStack showAchRew(String st,PlayerInfo pi){
		Achievement ach=achs.get(st);
		if(ach==null)return ItemUtil.create(Material.BARRIER, 1, 0, st, null, null, 0);
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.AQUA+"Достижение");
		for(ItemStack item:ach.rewards){
			String post="";
			if(item.getAmount()>1)post=ChatColor.WHITE+" x"+ChatColor.GREEN+item.getAmount();
			if(item.hasItemMeta()&&item.getItemMeta().hasDisplayName()){
				lore.add(ChatColor.WHITE+" -"+ChatColor.YELLOW+item.getItemMeta().getDisplayName()+post);
			}else{
				lore.add(ChatColor.WHITE+" -"+ChatColor.YELLOW+textUtil.ruMaterial(item.getType().toString(), true)+post);
			}
			for(Enchantment e:item.getItemMeta().getEnchants().keySet()){
				lore.add(ChatColor.DARK_PURPLE+"  *"+ChatColor.LIGHT_PURPLE+ChatColor.ITALIC+e.getName()+" "+ChatColor.AQUA+GepUtil.intToRoman(item.getItemMeta().getEnchants().get(e)));
			}
		}
    	return ItemUtil.createal(ach.mat, 1, 0, GepUtil.boolCol(ChatColor.DARK_RED, ChatColor.LIGHT_PURPLE, ach.isHard)+st, lore, Enchantment.LUCK, 10);
    }
}
