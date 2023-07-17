package obj;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Achievement {
	public Material mat;
	public ArrayList<ItemStack> rewards;
	public List<String> lore;
	public int exp;
	public boolean isHard;
	public Achievement(Material Mat, ArrayList<ItemStack> Rewards, List<String> Lore, int Exp, boolean IsHard){
		mat=Mat;
		rewards=Rewards;
		lore=Lore;
		exp=Exp;
		isHard=IsHard;
	}
}
