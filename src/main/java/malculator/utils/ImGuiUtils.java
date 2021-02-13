package malculator.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImGuiUtils {
    /**
     * Loads bytes from a resource. Used to load fonts
     * @param fileName The file name
     * @return The content of the file as byte array
     */
    public static byte[] loadFromResources(String fileName) {
        try (var inputStream = ClassLoader.getSystemResourceAsStream(fileName); var buffer = new ByteArrayOutputStream()) {
            var data = new byte[16 * 1024];
            int nRead;
            assert inputStream != null;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            System.err.println("Failed loading resource: "+fileName);
            e.printStackTrace();
        }
        return null;
    }
}
