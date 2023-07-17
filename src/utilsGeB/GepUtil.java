package utilsGeB;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class GepUtil {
	public static boolean HashMapReplacer(HashMap<String,Integer> hm, String key, int val, boolean zero, boolean set){
		if(hm.containsKey(key)){
			if(set)hm.replace(key, val);
			else hm.replace(key, hm.get(key)+val);
		}
		else{
			hm.put(key, val);
		}
		if(zero){
			if(hm.get(key)<=0){
				hm.remove(key);
				return true;
			}
		}
		return false;
	}
	public static HashMap<String, Integer> dropsByCoefs(HashMap<String,Integer> drops,int points){
		HashMap<String,Integer> dropped = new HashMap<>();
		for(;points>0;points--){
			GepUtil.HashMapReplacer(dropped, chancesByCoef(drops), 1, false, false);
		}
		return dropped;
	}
	public static int getInt(HashMap<String,Integer> hm, String key){
		if(!hm.containsKey(key))return 0;
		return hm.get(key);
	}
	public static boolean HashMapReplacer(HashMap<String,Double> hm, String key, double val, boolean zero, boolean set){
		if(hm.containsKey(key)){
			if(set)hm.replace(key, val);
			else hm.replace(key, hm.get(key)+val);
		}
		else{
			hm.put(key, val);
		}
		if(zero){
			if(hm.get(key)<=0){
				hm.remove(key);
				return true;
			}
		}
		return false;
	}
	public static boolean HashMapReplacer(HashMap<Location,Integer> hm, Location key, int val, boolean zero, boolean set){
		if(hm.containsKey(key)){
			if(set)hm.replace(key, val);
			else hm.replace(key, hm.get(key)+val);
		}
		else{
			hm.put(key, val);
		}
		if(zero){
			if(hm.get(key)<=0){
				hm.remove(key);
				return true;
			}
		}
		return false;
	}
	public static String chancesByCoef(String[] sts, int[] coefs){
		int coef=0;
		for(int d:coefs){
			coef+=d;
		}
		int r = new Random().nextInt(coef);
		int ch = 0;
		for(int i=0;i<sts.length;i++){
			if(r>=ch&&r<=ch+coefs[i]){
				return sts[i];
			}
			ch+=coefs[i];
		}
		return ""+r;
	}
	public static String chancesByCoef(HashMap<String, Integer> sts){
		int coef=0;
		for(int d:sts.values()){
			coef+=d;
		}
		int r = new Random().nextInt(coef);
		int ch = 0;
		for(String st:sts.keySet()){
			if(r>=ch&&r<ch+sts.get(st)){
				return st;
			}
			ch+=sts.get(st);
		}
		return ""+r;
	}
	public static double chanceFromCoef(HashMap<String, Integer> sts, String st){
		if(!sts.containsKey(st))return 0;
		int coef=0;
		for(int d:sts.values()){
			coef+=d;
		}
		double ch = 100.0/coef*sts.get(st);
		return ch;
	}
	public static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
	public static int intFromString(String st){
		String rets = "";
		int ret = 0;
		boolean negative=false;
		boolean ignore=false;
		for(int i=0;i<st.length();i++){
			if(!ignore&&isNumeric(st.charAt(i)+"")){
				rets = rets+st.charAt(i)+"";
			}
			if((st.charAt(i)+"").equals("-"))negative=true;
			if((st.charAt(i)+"").equals("§")||(st.charAt(i)+"").equals("&"))ignore=true;
			else ignore=false;
		}
		ret = Integer.parseInt(rets);
		if(negative)ret=-ret;
		return ret;
	}
	public static boolean loreContains(ItemStack item,String str){
		if(item==null)return false;
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasLore())return false;
		for(String st:item.getItemMeta().getLore()){
			if(st.contains(str)){
				return true;
			}
		}
		return false;
	}
	public static boolean haveItem(Player p, Material mat, int am){
		if(countItem(p, mat)>=am)return true;
		return false;
	}
	public static int countItem(Player p, Material mat){
		int am=0;
		for(ItemStack item:p.getInventory()){
			if(item!=null&&item.getType().equals(mat)){
				am+=item.getAmount();
			}
		}
		return am;
	}
	public static int sellItems(Player p, Material mat, String name, int am){
		int sold=0;
		for(ItemStack item:p.getInventory()){
			if(item!=null&&item.getType().equals(mat)){
				if(name!=null){
					if(!item.hasItemMeta()||item.getItemMeta().getDisplayName().contains(name))continue;
				}
				if(item.getAmount()<=am){
					am-=item.getAmount();
					sold+=item.getAmount();
					item.setAmount(0);
				}
				else{
					item.setAmount(item.getAmount()-am);
					sold+=am;
					break;
				}
			}
		}
		return sold;
	}
	public static int intFromLore(ItemStack item,String str){
		String rets = "";
		int ret = 0;
		boolean negative=false;
		if(!item.hasItemMeta())return 0;
		if(!item.getItemMeta().hasLore())return 0;
		for(String st:item.getItemMeta().getLore()){
			if(st.contains(str)){
				boolean ignore=false;
				for(int i=0;i<st.length();i++){
					if(!ignore&&isNumeric(st.charAt(i)+"")){
						rets = rets+st.charAt(i)+"";
					}
					if((st.charAt(i)+"").equals("-"))negative=true;
					if((st.charAt(i)+"").equals("§")||(st.charAt(i)+"").equals("&"))ignore=true;
					else ignore=false;
				}
			}
		}
		if(!rets.equals(""))ret = Integer.parseInt(rets);
		if(negative)ret=-ret;
		return ret;
	}
	public static void globMessage(String mes, Sound sound, float vol, float speed, String title, String subtitle, int spawn, int hold, int remove){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(mes!=null)p.sendMessage(mes);
			if(sound!=null){
				p.playSound(p.getLocation(), sound, vol, speed);
			}
			if(title!=null||subtitle!=null) {
				p.sendTitle(title, subtitle, spawn, hold, remove);
			}
		}
	}
	public static ChatColor boolCol(boolean arg){
		if(arg)return ChatColor.GREEN;
		else return ChatColor.RED;
	}
	public static ChatColor boolCol(ChatColor Tcolor, ChatColor Fcolor, boolean arg){
		if(arg)return Tcolor;
		else return Fcolor;
	}
	public static void debug(String message, String whoCaused, String type){
		String prefix = ChatColor.GRAY+"[DEBUG";
		if(whoCaused!=null)prefix+="(from "+ChatColor.YELLOW+whoCaused+ChatColor.GRAY+")";
		prefix+="]";
		if(type.equals("error"))prefix+=ChatColor.RED;
		if(type.equals("info"))prefix+=ChatColor.AQUA;
		if(Bukkit.getPlayer("Gepiroy")!=null){
			Bukkit.getPlayer("Gepiroy").sendMessage(prefix+message);
		}
		Bukkit.getConsoleSender().sendMessage(prefix+message);
	}
	public static boolean chance(int ch){
		return new Random().nextInt(100)+1<=ch;
	}
	public static boolean chance(double ch){
		return new Random().nextDouble()<=ch;
	}
	public static String chances(String[] sts, double[] chs){
		double r = new Random().nextInt(100)+new Random().nextDouble();
		double ch = 0.000;
		for(int i=0;i<sts.length;i++){
			if(r>ch&&r<=ch+chs[i]){
				return sts[i];
			}
			ch+=chs[i];
		}
		return "";
	}
	public static boolean itemName(ItemStack item, String name) {
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasDisplayName())return false;
		if(item.getItemMeta().getDisplayName().equals(name))return true;
		return false;
	}
	public static boolean isFullyItem(ItemStack item, String name, Material mat){
		if(mat!=null&&!item.getType().equals(mat))return false;
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasDisplayName())return false;
		if(!item.getItemMeta().hasLore())return false;
		if(name!=null&&!item.getItemMeta().getDisplayName().equals(name))return false;
		return true;
	}
	public static boolean nameContains(ItemStack item, String text){
		if(!item.hasItemMeta())return false;
		if(!item.getItemMeta().hasDisplayName())return false;
		if(!item.getItemMeta().getDisplayName().contains(text))return false;
		return true;
	}
	public static String getDisplName(ItemStack item){
		if(!item.hasItemMeta())return "";
		if(!item.getItemMeta().hasDisplayName())return "";
		return item.getItemMeta().getDisplayName();
	}
	public static ArrayList<String> stringToArrayList(String st){
		ArrayList<String> ret = new ArrayList<>();
		String toadd = "";
		for(int i=0;i<st.length();i++){
			String c = st.charAt(i)+"";
			if(!c.equals(";")){
				toadd=toadd+c;
			}
			else{
				ret.add(toadd);
				toadd="";
			}
		}
		return ret;
	}
	public static String ArrayListToString(ArrayList<String> ara){
		String ret = "";
		for(String st:ara){
			ret = ret+st+";";
		}
		return ret;
	}
	public static void saveCfg(FileConfiguration conf, File file) {
	    try {
	        conf.save(file);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void saveLocToConf(FileConfiguration conf, String st, Location loc){
		conf.set(st+".world",loc.getWorld().getName());
		conf.set(st+".x",loc.getX());
		conf.set(st+".y",loc.getY());
		conf.set(st+".z",loc.getZ());
	}
	public static Location getLocFromConf(FileConfiguration conf, String st){
		if(!conf.contains(st)){
			debug("no loc "+st+"in config!",null,"error");
			return null;
		}
		return new Location(Bukkit.getWorld(conf.getString(st+".world")),conf.getDouble(st+".x"),conf.getDouble(st+".y"),conf.getDouble(st+".z"));
	}
	public static String toTime(int i){
		return i/60+":"+i%60;
	}
	public static String CylDouble(double d, String cyl){
		return new DecimalFormat(cyl).format(d).replaceAll(",", ".");
	}
	public static void globMessage(String mes) {
		globMessage(mes, null, 0, 0, null, null, 0, 0, 0);
	}
	public static String maxFromHM(HashMap<String,Integer> hm){
		String ret="";
		int max=Integer.MIN_VALUE;
		for(String st:hm.keySet()){
			if(hm.get(st)>max){
				max=hm.get(st);
				ret=st;
			}
		}
		return ret;
	}
	public static String intToRoman(int number){
        String romanValue = "";
        int N = number;
        int numbers[]  = {1, 4, 5, 9, 10, 50, 100, 500, 1000 };
        String letters[]  = { "I", "IV", "V", "IX", "X", "L", "C", "D", "M"};
        while ( N > 0 ){
        for (int i = 0; i < numbers.length; i++){
        if ( N < numbers[i] ){
        N -= numbers[i-1];
        romanValue += letters[i-1];
        break;
        }
        }
        }
        return romanValue;
    }
	public static int letterToNumber(String letter){
        if(letter.equals("I") )
            return 1;
        else if(letter.equals("II"))
            return 2;
        else if(letter.equals("III"))
            return 3;
        else if(letter.equals("IV"))
            return 4;
        else if(letter.equals("V"))
            return 5;
        else if(letter.equals("IX"))
            return 9;
        else if(letter.equals("X"))
            return 10;
        else if(letter.equals("L"))
            return 50;
        else if(letter.equals("C"))
            return 100;
        else if(letter.equals("D"))
            return 500;
        else if(letter.equals("M"))
            return 1000;
        else return -1;
    }
	public static int ChanceToInt(double coef, double got){
		int ret=(int) (got/coef);
		double add=got%coef;
		if(new Random().nextDouble()<=1/coef*add){
			ret++;
		}
		return ret;
	}
	public static String tanimWave(String text, int c, int s, ChatColor wave, ChatColor def){
		String ret="";
		for(int i=0;i<text.length();i++){
			if(c==i){
				ret+=wave;
			}
			if(c+s==i){
				ret+=def;
			}
			ret+=text.charAt(i);
		}
		return ret;
	}
	public static void randFirework(Location l){
		//Spawn the Firework, get the FireworkMeta.
        Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
       
        //Our random generator
        Random r = new Random();   

        //Get the type
        int rt = r.nextInt(4) + 1;
        Type type = Type.BALL;       
        if (rt == 1) type = Type.BALL;
        if (rt == 2) type = Type.BALL_LARGE;
        if (rt == 3) type = Type.BURST;
        if (rt == 4) type = Type.CREEPER;
        if (rt == 5) type = Type.STAR;
       
        //Get our random colours   
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);
       
        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
       
        //Then apply the effect to the meta
        fwm.addEffect(effect);
       
        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
       
        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);
	}
	static Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}
}