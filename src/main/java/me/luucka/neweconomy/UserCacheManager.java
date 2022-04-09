package me.luucka.neweconomy;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserCacheManager {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private final NewEconomy PLUGIN;

    private final File csvFile;

    private final Map<String, String> userCache = new HashMap<>();

    public UserCacheManager(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        csvFile = new File(this.PLUGIN.getDataFolder(), "usercache.csv");
        if (!csvFile.exists()) {
            try {
                csvFile.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create config " + csvFile, e);
            }
        }
    }

    public synchronized void readCSV() {
        List<String[]> lines;
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            lines = reader.readAll();
            for (String[] line : lines) {
                userCache.put(line[0].toLowerCase(), line[1]);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            for (Map.Entry<String, String> values : userCache.entrySet()) {
                writer.writeNext(new String[]{values.getKey(), values.getValue()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OfflinePlayer getPlayer(final String name) {
        final String uuid = userCache.get(name.toLowerCase());
        if (uuid == null) return null;
        return PLUGIN.getServer().getOfflinePlayer(UUID.fromString(uuid));
    }

    public void addPlayer(final Player player) {
        userCache.put(player.getName().toLowerCase(), player.getUniqueId().toString());
    }

}
