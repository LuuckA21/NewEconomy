package me.luucka.neweconomy.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ParsingException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BaseConfiguration {

    private static final Logger LOGGER = Logger.getLogger("NewEconomy");

    private Class<?> resourceClass = BaseConfiguration.class;
    private final File configFile;
    private final YamlConfigurationLoader loader;
    private final String templateName;
    private CommentedConfigurationNode configurationNode;

    public BaseConfiguration(final File configFile) {
        this(configFile, null);
    }

    public BaseConfiguration(final File configFile, final String templateName, final Class<?> resourceClass) {
        this(configFile, templateName);
        this.resourceClass = resourceClass;
    }

    public BaseConfiguration(final File configFile, final String templateName) {
        this.configFile = configFile;
        this.loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .file(configFile)
                .build();
        this.templateName = templateName;
    }

    public CommentedConfigurationNode getRootNode() {
        return configurationNode;
    }

    public File getFile() {
        return configFile;
    }

    public void setProperty(final String path, final String value) {
        setInternal(path, value);
    }

    public String getString(final String path, final String def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) return def;
        return node.getString();
    }

    public void setProperty(final String path, final boolean value) {
        setInternal(path, value);
    }

    public boolean getBoolean(final String path, final boolean def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) return def;
        return node.getBoolean();
    }

    public boolean isBoolean(final String path) {
        final CommentedConfigurationNode node = getInternal(path);
        return node != null && node.raw() instanceof Boolean;
    }

    public void setProperty(final String path, final long value) {
        setInternal(path, value);
    }

    public long getLong(final String path, final long def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) return def;
        return node.getLong();
    }

    public void setProperty(final String path, final int value) {
        setInternal(path, value);
    }

    public int getInt(final String path, final int def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) return def;
        return node.getInt();
    }

    public void setProperty(final String path, final double value) {
        setInternal(path, value);
    }

    public double getDouble(final String path, final double def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) return def;
        return node.getDouble();
    }

    public void setProperty(final String path, final float value) {
        setInternal(path,value);
    }

    public float getFloat(final String path, final float def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) return def;
        return node.getFloat();
    }

    public void setRaw(final String path, final Object value) {
        setInternal(path, value);
    }

    public Object get(final String path) {
        final CommentedConfigurationNode node = getInternal(path);
        return node == null ? null : node.raw();
    }

    public CommentedConfigurationNode getSection(final String path) {
        final CommentedConfigurationNode node = toSplitRoot(path, configurationNode);
        if (node.virtual()) return null;
        return node;
    }

    public CommentedConfigurationNode newSection() {
        return loader.createNode();
    }

    public void removeProperty(String path) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node != null) {
            try {
                node.set(null);
            } catch (SerializationException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private void setInternal(final String path, final Object value) {
        try {
            toSplitRoot(path, configurationNode).set(value);
        } catch (SerializationException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private CommentedConfigurationNode getInternal(final String path) {
        final CommentedConfigurationNode node = toSplitRoot(path, configurationNode);
        if (node.virtual()) return null;
        return node;
    }

    public boolean hasProperty(final String path) {
        return !toSplitRoot(path, configurationNode).isNull();
    }

    public CommentedConfigurationNode toSplitRoot(String path, final CommentedConfigurationNode node) {
        if (path == null) return node;

        path = path.startsWith(".") ? path.substring(1) : path;
        return node.node(path.contains(".") ? path.split("\\.") : new Object[]{path});
    }

    public void load() {
        if (configFile.getParentFile() != null && !configFile.getParentFile().exists()) {
            if (!configFile.getParentFile().mkdirs()) {
                LOGGER.log(Level.SEVERE, "Failed to create config: ", configFile.toString());
            }
        }

        if (!configFile.exists()) {
            try {
                if (templateName != null) {
                    Files.copy(resourceClass.getResourceAsStream(templateName), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    this.configFile.createNewFile();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create config " + configFile, e);
            }
        }

        try {
            configurationNode = loader.load();
        } catch (final ParsingException e) {
            final File broken = new File(configFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            if (configFile.renameTo(broken)) {
                LOGGER.log(Level.SEVERE, "The file " + configFile + " is broken, it has been renamed to " + broken, e.getCause());
                return;
            }
            LOGGER.log(Level.SEVERE, "The file " + configFile + " is broken. A backup file has failed to be created", e.getCause());
        } catch (final ConfigurateException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);;
        } finally {
            if (configurationNode == null) {
                configurationNode = loader.createNode();
            }
        }
    }

    public synchronized void save() {
        try {
            loader.save(configurationNode);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

}