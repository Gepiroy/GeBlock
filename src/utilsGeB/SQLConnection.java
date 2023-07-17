package utilsGeB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import GeBlock.main;

public class SQLConnection {
	private static Connection c;
	String host,port,login,pass;
	String database;
	String table;
	public void connect()
	{
		File file = new File(main.instance.getDataFolder() + File.separator + "mysql.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		if(!conf.contains("Host")){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"No MySQL cfg.");
			conf.set("Host", "localhost");
			conf.set("User", "mysql");
			conf.set("Pass", "mysql");
			conf.set("BaseName", "gepcraft");
			GepUtil.saveCfg(conf, file);
		}
		
		host = conf.getString("Host");port = "3306";login = conf.getString("User");pass = conf.getString("Pass");database = conf.getString("BaseName");
		table = "Honour";
			if (c != null)
			{
			    close();
			}
			try
			{
			    c = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true" , login, pass);
			}
			catch (SQLException ex)
			{
			    ex.printStackTrace();
			}
			TryToCreateTable();
	}
	public void close()
	{
	        try {
	            c.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	}
	public void TryToCreateTable()
	{
		Statement state;
		
		try {
			state = c.createStatement();
			state.executeUpdate("CREATE TABLE IF NOT EXISTS " + database + "." + table +
					" (PlayerName TEXT(20),Honour INT NOT NULL DEFAULT 0, Max INT NOT NULL DEFAULT 0, Faster BOOL DEFAULT 0, Up INT NOT NULL DEFAULT 60, Rew INT NOT NULL DEFAULT 60);"
					);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void Add(String name)
	{
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + name + "';");
			int up=res.getInt("Up");
			int rew=res.getInt("Rew");
			up++;
			if(res.getBoolean("Faster"))up++;
			rew++;
			state.executeUpdate("UPDATE "+ database + "." + table + " SET Up = '" + up + "' WHERE PlayerName = '" + name +"';");
			state.executeUpdate("UPDATE "+ database + "." + table + " SET Rew = '" + rew + "' WHERE PlayerName = '" + name +"';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public ResultSet getRes(String name){
		Statement state;
		ResultSet res = null;
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + name + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	public boolean inTable(String name){
		if(getRes(name)==null){
			return false;
		}else{
			return true;
		}
	}
	public int GetMoney(String name)
	{
		Statement state;
		ResultSet res;
		int ret=0;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + name + "';");
			if(!res.next())ret=0;
			else ret = res.getInt("Money");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	public int GetG(String name)
	{
		Statement state;
		ResultSet res;
		int ret=0;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + name + "';");
			if(!res.next())ret=0;
			else ret = res.getInt("G");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
