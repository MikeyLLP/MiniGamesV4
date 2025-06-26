package de.mikeyllp.miniGamesV4.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Database {

    private final JavaPlugin plugin;
    private Connection connection;
    // Holds the main Tread Clear
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    public Database(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        try {
            File dbFile = new File(plugin.getDataFolder(), "data.db");
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            plugin.getLogger().severe("No Connection to SQLite: " + e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                "uuid TEXT PRIMARY KEY, " +
                "language TEXT, " +
                "invites_toggle INTEGER DEFAULT 0)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe("An unexpected error while creating a Table: " + e.getMessage());
        }
    }

    // A method to set the Lang
    public void setLanguage(UUID uuid, String language) {
        String sql = "INSERT INTO player_data(uuid, language) VALUES(?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET language = excluded.language";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, language);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("Error while saving Data in Language: " + e.getMessage());
        }
    }

    // Get the Lang Async that reduce lags
    public CompletableFuture<Void> setLanguageAsync(UUID uuid, String language) {
        return CompletableFuture.runAsync(() -> setLanguage(uuid, language), dbExecutor);
    }

    // A method to get the Lang
    public String getLanguage(UUID uuid) {
        String sql = "SELECT language FROM player_data WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("language");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Error while loading from Data in Language: " + e.getMessage());
        }
        return null;
    }

    public CompletableFuture<String> getLanguageAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getLanguage(uuid), dbExecutor);
    }

    public void setToggle(UUID uuid, int toggle) {
        String sql = "INSERT INTO player_data(uuid, invites_toggle) VALUES(?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET invites_toggle = excluded.invites_toggle";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, toggle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("Error while saving Data in toggle: " + e.getMessage());
        }
    }

    public CompletableFuture<Void> setToggleAsync(UUID uuid, int toggle) {
        return CompletableFuture.runAsync(() -> setToggle(uuid, toggle), dbExecutor);
    }

    public int getToggle(UUID uuid) {
        String sql = "SELECT invites_toggle FROM player_data WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("invites_toggle");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Error while loading from Data in toggle: " + e.getMessage());
        }
        return 0;
    }

    public CompletableFuture<Integer> getToggleAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getToggle(uuid), dbExecutor);
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("An unexpected error while shouting down the SQLite " + e.getMessage());
        }
        dbExecutor.shutdown();
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
