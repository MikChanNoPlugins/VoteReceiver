package dev.mikchan.mcnp.votereceiver.velocity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

class Downloader {
    public static void download(URL url, File location) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        location.getParentFile().mkdirs();

        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream output = new FileOutputStream(location)) {
            byte[] buffer = new byte[1024];
            int bytes;
            while ((bytes = in.read(buffer, 0, 1024)) != -1) {
                output.write(buffer, 0, bytes);
            }
        }
    }
}
