package me.luucka.neweconomy;

import com.google.common.io.Files;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class UUIDMap {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");
    private static final ScheduledExecutorService writeScheduler = Executors.newScheduledThreadPool(1);
    private static boolean pendingWrite;
    private static boolean loading = false;
    private final NewEconomy PLUGIN;
    private final File csvFile;
    private final Pattern splitPattern = Pattern.compile(",");
    private final Runnable writeTaskRunnable;

    public UUIDMap(final NewEconomy plugin) {
        this.PLUGIN = plugin;
        this.csvFile = new File(PLUGIN.getDataFolder(), "usermap.csv");
        pendingWrite = false;
        this.writeTaskRunnable = () -> {
            if (pendingWrite) {
                try {
                    new WriteRunner(PLUGIN.getDataFolder(), csvFile, PLUGIN.getUserMap().getNameUUID()).run();
                } catch (final Throwable t) {
                    t.printStackTrace();
                }
            }
        };
        writeScheduler.scheduleWithFixedDelay(writeTaskRunnable, 5, 5, TimeUnit.SECONDS);
    }

    public void loadNameUUID(final Map<String, UUID> nameUUID) {
        try {
            if (!csvFile.exists()) {
                csvFile.createNewFile();
            }

            if (loading) return;

            nameUUID.clear();
            loading = true;

            try (final BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                while (true) {
                    final String line = reader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        final String[] values = splitPattern.split(line);
                        if (values.length == 2) {
                            final String name = values[0];
                            final UUID uuid = UUID.fromString(values[1]);
                            nameUUID.put(name, uuid);
                        }
                    }
                }
            }
            loading = false;
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void writeUUIDMap() {
        pendingWrite = true;
    }

    public void forceWriteUUIDMap() {
        pendingWrite = true;
        writeTaskRunnable.run();
    }

    public void shutdown() {
        writeScheduler.submit(writeTaskRunnable);
        writeScheduler.shutdown();
    }

    private static final class WriteRunner implements Runnable {
        private final File location;
        private final File endFile;
        private final Map<String, UUID> nameUUID;

        public WriteRunner(final File location, final File endFile, final Map<String, UUID> nameUUID) {
            this.location = location;
            this.endFile = endFile;
            this.nameUUID = new HashMap<>(nameUUID);
        }

        @Override
        public void run() {
            pendingWrite = false;
            if (loading || nameUUID.isEmpty()) return;

            File tempFile = null;

            try {
                tempFile = File.createTempFile("usermap", ".tmp.csv", location);

                final BufferedWriter bWrite = new BufferedWriter(new FileWriter(tempFile));
                for (final Map.Entry<String, UUID> entry : nameUUID.entrySet()) {
                    bWrite.write(entry.getKey() + "," + entry.getValue().toString());
                    bWrite.newLine();
                }
                bWrite.close();
                Files.move(tempFile, endFile);
            } catch (final IOException e) {
                try {
                    if (tempFile != null && tempFile.exists()) {
                        Files.move(tempFile, new File(endFile.getParentFile(), "usermap.bak.csv"));
                    }
                } catch (IOException e2) {
                    LOGGER.log(Level.SEVERE, e2.getMessage(), e2);
                }
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

}
