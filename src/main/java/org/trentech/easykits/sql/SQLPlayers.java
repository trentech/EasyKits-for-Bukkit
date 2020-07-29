package org.trentech.easykits.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.bukkit.entity.Player;
import org.trentech.easykits.kits.KitUsage;

public class SQLPlayers extends SQLUtils{

	private static Object lock = new Object();

	public static void createTable(Player player) {
		synchronized (lock) {
			try {
				Connection connection = getConnection();
				connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `" + player.getUniqueId().toString() + "`(Kit TEXT, Date TEXT, Used INTEGER)");
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}			
	}

	public static void deleteTable(Player player) {
		synchronized (lock) {
			try {
				Connection connection = getConnection();
				connection.createStatement().executeUpdate("DROP TABLE `" + player.getUniqueId().toString() + "`");
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void add(Player player, KitUsage kitUsage) {
		synchronized (lock) {
			try {
				Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT into `" + player.getUniqueId().toString() + "` (Kit, Date, Used) VALUES (?, ?, ?)");

				statement.setString(1, kitUsage.getKitName());
				statement.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(kitUsage.getDate()));
				statement.setInt(3, kitUsage.getTimesUsed());
				statement.executeUpdate();
				
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void update(Player player, KitUsage kitUsage) {
		synchronized (lock) {
			try {
				Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("UPDATE `" + player.getUniqueId().toString() + "` SET Date = ?, Used = ? WHERE Kit = ?");
				
				statement.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(kitUsage.getDate()));
				statement.setInt(2, kitUsage.getTimesUsed());
				statement.setString(3, kitUsage.getKitName());				
				statement.executeUpdate();
				
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static KitUsage getKitUsage(Player player, String kitName) {
		KitUsage kitUsage = null;
		
		try {
			Connection connection = getConnection();
			ResultSet result = connection.createStatement().executeQuery("SELECT * FROM `" + player.getUniqueId().toString() + "`");

			while (result.next()) {
				if (result.getString("Kit").equalsIgnoreCase(kitName)) {
					kitUsage = new KitUsage(result.getString("Kit"), result.getInt("Used"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.getString("Date")));
				}
			}
			
			result.close();
			connection.close();
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		
		if(kitUsage == null) {
			return new KitUsage(kitName);
		}
		
		return kitUsage;
	}
}
