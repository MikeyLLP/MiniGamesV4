package de.mikeyllp.miniGamesV4.database

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Supplier

class Database(private val plugin: JavaPlugin) {
    private var connection: Connection? = null

    // Holds the main Tread Clear
    private val dbExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun connect() {
        try {
            val dbFile = File(plugin.getDataFolder(), "data.db")
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs()
            val url = "jdbc:sqlite:" + dbFile.getAbsolutePath()
            connection = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            plugin.getLogger().severe("No Connection to SQLite: " + e.message)
        }
    }

    fun disconnect() {
        try {
            if (connection != null && !connection!!.isClosed()) {
                connection!!.close()
            }
        } catch (e: SQLException) {
            plugin.getLogger().warning("An unexpected error while shouting down the SQLite " + e.message)
        }
        dbExecutor.shutdown()
    }

    fun createTable() {
        val sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                "uuid TEXT PRIMARY KEY, " +
                "language TEXT, " +
                "invites_toggle INTEGER DEFAULT 0)"
        try {
            connection!!.createStatement().use { stmt ->
                stmt.execute(sql)
            }
        } catch (e: SQLException) {
            plugin.getLogger().severe("An unexpected error while creating a Table: " + e.message)
        }
    }

    // A method to set the Lang
    fun setLanguage(uuid: UUID, language: String?) {
        val sql = "INSERT INTO player_data(uuid, language) VALUES(?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET language = excluded.language"
        try {
            connection!!.prepareStatement(sql).use { stmt ->
                stmt.setString(1, uuid.toString())
                stmt.setString(2, language)
                stmt.executeUpdate()
            }
        } catch (e: SQLException) {
            plugin.getLogger().warning("Error while saving Data in Language: " + e.message)
        }
    }

    // Get the Lang Async that reduce lags
    fun setLanguageAsync(uuid: UUID, language: String?): CompletableFuture<Void?> {
        return CompletableFuture.runAsync(Runnable { setLanguage(uuid, language) }, dbExecutor)
    }

    // A method to get the Lang
    fun getLanguage(uuid: UUID): String? {
        val sql = "SELECT language FROM player_data WHERE uuid = ?"
        try {
            connection!!.prepareStatement(sql).use { stmt ->
                stmt.setString(1, uuid.toString())
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return rs.getString("language")
                    }
                }
            }
        } catch (e: SQLException) {
            plugin.getLogger().warning("Error while loading from Data in Language: " + e.message)
        }
        return null
    }

    fun getLanguageAsync(uuid: UUID): CompletableFuture<String?> {
        return CompletableFuture.supplyAsync<String?>(Supplier { getLanguage(uuid) }, dbExecutor)
    }

    fun setToggle(uuid: UUID, toggle: Int) {
        val sql = "INSERT INTO player_data(uuid, invites_toggle) VALUES(?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET invites_toggle = excluded.invites_toggle"
        try {
            connection!!.prepareStatement(sql).use { stmt ->
                stmt.setString(1, uuid.toString())
                stmt.setInt(2, toggle)
                stmt.executeUpdate()
            }
        } catch (e: SQLException) {
            plugin.getLogger().warning("Error while saving Data in toggle: " + e.message)
        }
    }

    fun setToggleAsync(uuid: UUID, toggle: Int): CompletableFuture<Void?> {
        return CompletableFuture.runAsync(Runnable { setToggle(uuid, toggle) }, dbExecutor)
    }

    fun getToggle(uuid: UUID): Int {
        val sql = "SELECT invites_toggle FROM player_data WHERE uuid = ?"
        try {
            connection!!.prepareStatement(sql).use { stmt ->
                stmt.setString(1, uuid.toString())
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return rs.getInt("invites_toggle")
                    }
                }
            }
        } catch (e: SQLException) {
            plugin.getLogger().warning("Error while loading from Data in toggle: " + e.message)
        }
        return 0
    }

    fun getToggleAsync(uuid: UUID): CompletableFuture<Int?> {
        return CompletableFuture.supplyAsync<Int?>(Supplier { getToggle(uuid) }, dbExecutor)
    }

    val isConnected: Boolean
        get() {
            try {
                return connection != null && !connection!!.isClosed()
            } catch (e: SQLException) {
                return false
            }
        }
}
