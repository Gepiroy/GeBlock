package utilsGeB;

import org.bukkit.ChatColor;

public class textUtil {
	public static final String tut = ChatColor.BLUE+"["+ChatColor.GOLD+"��������"+ChatColor.BLUE+"] ";
	public static final String inf = ChatColor.BLUE+"["+ChatColor.AQUA+"Info"+ChatColor.BLUE+"] ";
	public static final String nam = ChatColor.GREEN+"<"+ChatColor.LIGHT_PURPLE+"���� ;)"+ChatColor.GREEN+"> ";
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
		if(mat.equals("PISTON_BASE"))ret="�������";
		if(mat.equals("FLINT"))ret="�������";
		if(mat.equals("COBBLESTONE"))ret= "��������";
		if(mat.equals("STONE"))ret= "������";
		if(mat.equals("CARROT_ITEM"))ret= "�������";
		if(mat.equals("EMERALD"))ret= "�������";
		if(mat.equals("DIAMOND"))ret= "�����";
		if(mat.equals("LEATHER"))ret= "����";
		if(mat.equals("WOOL"))ret= "������";
		if(mat.equals("SEEDS"))ret= "������ �������";
		if(mat.equals("GOLDEN_APPLE"))ret= "������� ������";
		if(mat.equals("-"))ret= "������";
		if(mat.equals("WATCH"))ret= "����";
		if(mat.equals("PAPER"))ret= "������";
		if(mat.equals("SUGAR_CANE"))ret= "��������";
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
