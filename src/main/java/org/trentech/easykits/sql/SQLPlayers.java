package org.trentech.easykits.sql;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.bukkit.entity.Player;
import org.trentech.easykits.kits.KitUsage;

public class SQLPlayers extends SQLUtils{

	private static Object lock = new Object();

	public static boolean tableExist(Player player) {
		boolean bool = false;
		
		try {
			Statement statement = getConnection().createStatement();
			DatabaseMetaData metaData = statement.getConnection().getMetaData();
			ResultSet result = metaData.getTables(null, null, player.getUniqueId().toString(), null);
			
			if (result.next()) {
				bool = true;	
			}
			
			statement.close();
			result.close();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		
		return bool;
	}

	public static void createTable(Player player) {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("CREATE TABLE `" + player.getUniqueId().toString() + "`( id INTEGER PRIMARY KEY, Kit TEXT, Date TEXT, Used INTEGER)");
				statement.executeUpdate();
				
				statement.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}			
	}

	public static void deleteTable(Player player) {
		synchronized (lock) {
			try {;
				PreparedStatement statement = prepare("DROP TABLE `" + player.getUniqueId().toString() + "`");
				statement.executeUpdate();
				
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void add(Player player, KitUsage kitUsage) {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("INSERT into `" + player.getUniqueId().toString() + "` (Kit, Date, Used) VALUES (?, ?, ?)");

				statement.setString(1, kitUsage.getKitName());
				statement.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(kitUsage.getDate()));
				statement.setInt(3, kitUsage.getTimesUsed());
				statement.executeUpdate();
				
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void update(Player player, KitUsage kitUsage) {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("UPDATE `" + player.getUniqueId().toString() + "` SET Date = ?, Used = ? WHERE Kit = ?");
				
				statement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(kitUsage.getDate()));
				statement.setInt(2, kitUsage.getTimesUsed());
				statement.setString(3, kitUsage.getKitName());				
				statement.executeUpdate();
				
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static KitUsage getKitUsage(Player player, String kitName) {
		KitUsage kitUsage = null;
		
		try {
			PreparedStatement statement = prepare("SELECT * FROM `" + player.getUniqueId().toString() + "`");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				if (result.getString("Kit").equalsIgnoreCase(kitName)) {
					kitUsage = new KitUsage(result.getString("Kit"), result.getInt("Used"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.getString("Date")));
				}
			}
			
			statement.close();
			result.close();
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		
		if(kitUsage == null) {
			return new KitUsage(kitName);
		}
		
		return kitUsage;
	}

//	public static void setObtained(Player player, String kitName, String obtainedBefore) {
//		synchronized (lock) {
//			try {
//				PreparedStatement statement = prepare("UPDATE `" + player.getUniqueId().toString() + "` SET Obtained = ? WHERE Kit = ?");
//				
//				statement.setString(1, obtainedBefore);
//				statement.setString(2, kitName);
//				statement.executeUpdate();
//				
//				statement.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void setLimit(Player player, String kitName, int limit) {
//		synchronized (lock) {
//			try {
//				PreparedStatement statement = prepare("UPDATE `" + player.getUniqueId().toString() + "` SET Limits = ? WHERE Kit = ?");
//				
//				statement.setInt(1, limit);
//				statement.setString(2, kitName);
//				statement.executeUpdate();
//				
//				statement.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static List<String> getPlayerList() {
//		List<String> playerList = new ArrayList<String>();
//		
//		try {
//			Statement statement = getConnection().createStatement();
//			DatabaseMetaData metaData = statement.getConnection().getMetaData();
//			ResultSet result = metaData.getTables(null, null, "%", null);
//			while (result.next()) {
//				if(!result.getString(3).equalsIgnoreCase("kits")){
//					playerList.add(result.getString(3));
//				}
//			}
//		} catch (SQLException e) {
//			Main.getPlugin().getLogger().severe(e.getMessage());
//		}
//		return playerList;
//	}
}
