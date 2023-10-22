package src.kennarddh;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Schematic schematic = MSCHParser.parseMSCH("test.msch");

        System.out.println("Version: " + schematic.version);
        System.out.println("Tags: " + schematic.tags);
        System.out.println("Width: " + schematic.width);
        System.out.println("Height: " + schematic.height);

        System.out.println("\nTiles: ");
        for (Stile tile : schematic.tiles) {
            System.out.println("Position: \"" + tile.position + "\": " + tile.blockName + ", Rotation: \"" + tile.rotation + "\", Config: \"" + tile.config + "\"");
        }
    }
}
