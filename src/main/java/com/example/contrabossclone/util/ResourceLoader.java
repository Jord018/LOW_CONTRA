package com.example.contrabossclone.util;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;



public class ResourceLoader {

    private static final Logger logger = LogManager.getLogger(ResourceLoader.class);

    private static final Map<String, Image> imageCache = new HashMap<>();

    public static Image loadImage(String path) {
        if (path == null || path.isEmpty()) {
            logger.error("ResourceLoader.loadImage() called with empty path!");
            return null;
        }

        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try (InputStream is = ResourceLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                logger.error("Image not found: {}", path);
                return null;
            }

            Image image = new Image(is);
            imageCache.put(path, image);
            logger.info("Loaded image: {}", path);
            return image;
        } catch (Exception e) {
            logger.error("Failed to load image: {} - {}", path, e.getMessage());
            return null;
        }
    }

}
