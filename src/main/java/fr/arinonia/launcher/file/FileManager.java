package fr.arinonia.launcher.file;

import fr.arinonia.launcher.utils.Constants;
import fr.arinonia.launcher.utils.OsDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);
    private final Path rootPath;
    private final Path launcherPath;
    private final Path runtimePath;

    public FileManager() {
        this.rootPath = this.initRootPath();
        this.launcherPath = this.rootPath.resolve("launcher");
        this.runtimePath = this.rootPath.resolve("runtime");
    }

    public void createDirectories() {
        try {
            if (!Files.exists(this.rootPath)) {
                Files.createDirectories(this.rootPath);
            }
            if (!Files.exists(this.launcherPath)) {
                Files.createDirectories(this.launcherPath);
            }
            if (!Files.exists(this.runtimePath)) {
                Files.createDirectories(this.runtimePath);
            }
        } catch (final Exception e) {
            throw new RuntimeException("Failed to create directories", e);
        }
    }

    private Path initRootPath() {
        switch (OsDetector.getCurrentPlatform()) {
            case WINDOWS:
                return Paths.get(System.getenv("APPDATA"), Constants.LAUNCHER_FOLDER_NAME);
            case MACOS:
                return Paths.get(System.getProperty("user.home"), "Library", "Application Support", Constants.LAUNCHER_FOLDER_NAME.replaceAll("\\.", ""));
            case LINUX:
            default:
                return Paths.get(System.getProperty("user.home"), Constants.LAUNCHER_FOLDER_NAME);
        }
    }

    public Path getAuthConfigPath() {
        return this.launcherPath.resolve("auth.json");
    }

    public Path getRootPath() {
        return this.rootPath;
    }
}
