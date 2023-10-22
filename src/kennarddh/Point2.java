package src.kennarddh;

public class Point2 {
    public int x;
    public int y;

    /**
     * Constructs a new 2D grid point.
     */
    public Point2() {
    }

    /**
     * Constructs a new 2D grid point.
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Point2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return a point unpacked from an integer.
     */
    public static Point2 unpack(int pos) {
        return new Point2((short) (pos >>> 16), (short) (pos & 0xFFFF));
    }

    /**
     * @return this point packed into a single int by casting its components to shorts.
     */
    public static int pack(int x, int y) {
        return (((short) x) << 16) | (((short) y) & 0xFFFF);
    }

    /**
     * @return the x component of a packed position.
     */
    public static short x(int pos) {
        return (short) (pos >>> 16);
    }

    /**
     * @return the y component of a packed position.
     */
    public static short y(int pos) {
        return (short) (pos & 0xFFFF);
    }

    /**
     * @return this point packed into a single int by casting its components to shorts.
     */
    public int pack() {
        return pack(x, y);
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}