package org.trentech.easykits.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.trentech.easykits.kits.Kit;

public abstract class SQLKits extends SQLUtils {

    private static Object lock = new Object();

	public static boolean tableExist() {
		boolean bool = false;
		
		try {
			Statement statement = getConnection().createStatement();
			DatabaseMetaData metaData = statement.getConnection().getMetaData();
			ResultSet result = metaData.getTables(null, null, "kits" , null);
			
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

	public static void createTable() {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("CREATE TABLE kits( id INTEGER PRIMARY KEY, Kit TEXT, Inventory BLOB, Armor BLOB, Price DOUBLE, Cooldown LONG, Limits INTEGER)");
				statement.executeUpdate();
				
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}			
	}

	public static void create(Kit kit) {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("INSERT into kits (Kit, Inventory, Armor, Price, Cooldown, Limits) VALUES (?, ?, ?, ?, ?, ?)");
				
				statement.setString(1, kit.getName());
				statement.setBytes(2, serialize(kit.getInventory()));
				statement.setBytes(3, serialize(kit.getEquipment()));
				statement.setDouble(4, kit.getPrice());
				statement.setLong(5, kit.getCooldown());
				statement.setInt(6, kit.getLimit());
				statement.executeUpdate();
				
				statement.close();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void update(Kit kit) {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("UPDATE kits SET Inventory = ?, Armor = ?, Price = ?, Cooldown = ?, Limits = ? WHERE Kit = ?");
				
				statement.setBytes(1, serialize(kit.getInventory()));
				statement.setBytes(2, serialize(kit.getEquipment()));
				statement.setDouble(3, kit.getPrice());
				statement.setLong(4, kit.getCooldown());
				statement.setInt(5, kit.getLimit());
				statement.setString(6, kit.getName());
				statement.executeUpdate();
				
				statement.close();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Optional<Kit> get(String name) {	
		Optional<Kit> kit = Optional.empty();
		
		try {
			PreparedStatement statement = prepare("SELECT * FROM kits");
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				if (result.getString("Kit").equalsIgnoreCase(name)) {
					kit = Optional.of(new Kit(result.getString("Kit"), deserialize(result.getBytes("Inventory")), deserialize(result.getBytes("Armor")), result.getLong("Cooldown"), result.getDouble("Price"), result.getInt("Limits")));
				}
			}
		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return kit;
	}
	
	public static void delete(String kitName) {
		try {
			PreparedStatement statement = prepare("DELETE from kits WHERE Kit = ?");
			statement.setString(1, kitName);
			statement.executeUpdate();
			
			statement.close();
		}catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public static ConcurrentHashMap<String, Kit> all() {	
		ConcurrentHashMap<String, Kit> kitList = new ConcurrentHashMap<>();
		
		try {
			PreparedStatement statement = prepare("SELECT * FROM kits");
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				Kit kit = new Kit(result.getString("Kit"), deserialize(result.getBytes("Inventory")), deserialize(result.getBytes("Armor")), result.getLong("Cooldown"), result.getDouble("Price"), result.getInt("Limits"));
				kitList.put(kit.getName(), kit);
			}
		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return kitList;
	}
	
	private static ItemStack[] deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ItemStack[] contents = null;

		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
        BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(arrayInputStream);
        
        contents =  (ItemStack[]) objectInputStream.readObject();
        
        arrayInputStream.close();
        objectInputStream.close();

		return contents;
	}
	
	private static byte[] serialize(ItemStack[] contents) throws IOException {
		ByteArrayOutputStream arrayOutputStream= new ByteArrayOutputStream();

    	BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(arrayOutputStream);
    	objectOutputStream.writeObject(contents);
    	
    	arrayOutputStream.close();
    	objectOutputStream.close();
    	
    	return arrayOutputStream.toByteArray();
	}
}