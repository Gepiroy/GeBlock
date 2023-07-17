package utilsGeB;

import org.bukkit.ChatColor;

public class textUtil {
	public static final String tut = ChatColor.BLUE+"["+ChatColor.GOLD+"Обучение"+ChatColor.BLUE+"] ";
	public static final String inf = ChatColor.BLUE+"["+ChatColor.AQUA+"Info"+ChatColor.BLUE+"] ";
	public static final String nam = ChatColor.GREEN+"<"+ChatColor.LIGHT_PURPLE+"Намёк ;)"+ChatColor.GREEN+"> ";
	public static String mark(String text, ChatColor mark, ChatColor def){
		String ret=def+"";
		boolean marking=false;
		for(int i=0;i<text.length();i++){
			String s=text.charAt(i)+"";
			if(s.equals("|")){
				marking=!marking;
				if(!marking)ret+=mark;
				else ret+=def;
			}else{
				ret+=s;
			}
		}
		return ret;
	}
	public static String ruMaterial(String mat, boolean big){
		String ret=mat;
		if(mat.equals("PISTON_BASE"))ret="Поршень";
		if(mat.equals("FLINT"))ret="Кремень";
		if(mat.equals("COBBLESTONE"))ret= "Булыжник";
		if(mat.equals("STONE"))ret= "Камень";
		if(mat.equals("CARROT_ITEM"))ret= "Морковь";
		if(mat.equals("EMERALD"))ret= "Изумруд";
		if(mat.equals("DIAMOND"))ret= "Алмаз";
		if(mat.equals("LEATHER"))ret= "Кожа";
		if(mat.equals("WOOL"))ret= "Шерсть";
		if(mat.equals("SEEDS"))ret= "Семяна пшеницы";
		if(mat.equals("GOLDEN_APPLE"))ret= "Золотое яблоко";
		if(mat.equals("-"))ret= "Ничего";
		if(mat.equals("WATCH"))ret= "Часы";
		if(mat.equals("PAPER"))ret= "Бумага";
		if(mat.equals("SUGAR_CANE"))ret= "Тростник";
		if(!big)ret.toLowerCase();
		return ret;
	}
	public static String chered(String st, ChatColor c1, ChatColor c2){
		String ret="";
		for(int i=0;i<st.length();i++){
			if(i%2==0)ret+=c1;
			else ret+=c2;
			ret+=st.charAt(i);
		}
		return ret;
	}
}
