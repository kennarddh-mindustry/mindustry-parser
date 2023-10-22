package src.kennarddh;

public class Stile {
    public String blockName;
    public Point2 position;
    public Object config;
    public byte rotation;

    Stile(String blockName, Point2 position, Object config, byte rotation) {
        this.blockName = blockName;
        this.position = position;
        this.config = config;
        this.rotation = rotation;
    }
}