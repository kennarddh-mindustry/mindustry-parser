package src.kennarddh;

import java.util.HashMap;

public class Schematic {
    public int version;
    public Stile[] tiles;
    public HashMap<String, String> tags;
    public int width;
    public int height;

    public Schematic(int version, Stile[] tiles, HashMap<String, String> tags, int width, int height) {
        this.version = version;
        this.tiles = tiles;
        this.tags = tags;
        this.width = width;
        this.height = height;
    }
}