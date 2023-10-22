package src.kennarddh;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.InflaterInputStream;










class MSCHParser {
    static byte[] header = {'m', 's', 'c', 'h'};

    public static Object readObject(DataInputStream stream) throws IOException {
        return readObjectBoxed(stream, false);
    }

    public static String readString(DataInputStream stream) throws IOException {
        byte exists = stream.readByte();

        if (exists != 0) {
            return stream.readUTF();
        } else {
            return null;
        }
    }

    public static int[] readInts(DataInputStream stream) throws IOException {
        short length = stream.readShort();
        int[] out = new int[length];
        for (int i = 0; i < length; i++) {
            out[i] = stream.readInt();
        }
        return out;
    }

    public static Object readObjectBoxed(DataInputStream stream, boolean box) throws IOException {
        byte type = stream.readByte();
        return switch (type) {
            case 0 -> null;
            case 1 -> stream.readInt();
            case 2 -> stream.readLong();
            case 3 -> stream.readFloat();
            case 4 -> readString(stream);
            case 5 -> new Content(stream.readByte(), stream.readShort());
            case 6 -> {
                short length = stream.readShort();

                int[] arr = new int[length];

                for (int i = 0; i < length; i++) arr[i] = (stream.readInt());

                yield arr;
            }
            case 7 -> new Point2(stream.readInt(), stream.readInt());
            case 8 -> {
                byte len = stream.readByte();
                Point2[] out = new Point2[len];
                for (int i = 0; i < len; i++) out[i] = Point2.unpack(stream.readInt());
                yield out;
            }
            case 9 -> new Content(stream.readByte(), stream.readShort());
            case 10 -> stream.readBoolean();
            case 11 -> stream.readDouble();
            case 12 -> stream.readInt();
            case 13 -> stream.readShort();
            case 14 -> {
                int blen = stream.readInt();
                byte[] bytes = new byte[blen];
                stream.readFully(bytes);
                yield bytes;
            }
            //unit command
            case 15 -> {
                stream.readByte();
                yield null;
            }
            case 16 -> {
                int boollen = stream.readInt();
                boolean[] bools = new boolean[boollen];
                for (int i = 0; i < boollen; i++) bools[i] = stream.readBoolean();
                yield bools;
            }
            case 17 -> stream.readInt();
            case 18 -> {
                int len = stream.readShort();
                Vec2[] out = new Vec2[len];
                for (int i = 0; i < len; i++) out[i] = new Vec2(stream.readFloat(), stream.readFloat());
                yield out;
            }
            case 19 -> new Vec2(stream.readFloat(), stream.readFloat());
            case 20 -> stream.readUnsignedByte();
            case 21 -> readInts(stream);
            case 22 -> {
                int objlen = stream.readInt();
                Object[] objs = new Object[objlen];
                for (int i = 0; i < objlen; i++) objs[i] = readObjectBoxed(stream, box);
                yield objs;
            }
            case 23 -> stream.readUnsignedShort();
            default -> throw new IllegalArgumentException("Unknown object type: " + type);
        };
    }

    public static Schematic parseMSCH(String fileName) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(fileName));

        for (byte b : header) {
            if (input.read() != b) {
                throw new IOException("Not a schematic file (missing header).");
            }
        }

        int ver = input.read();

        try (DataInputStream stream = new DataInputStream(new InflaterInputStream(input))) {
            short width = stream.readShort(), height = stream.readShort();

            if (width > 128 || height > 128)
                throw new IOException("Invalid schematic: Too large (max possible size is 128x128)");

            HashMap<String, String> tagsMap = new HashMap<>();

            int tags = stream.readUnsignedByte();

            for (int i = 0; i < tags; i++) {
                tagsMap.put(stream.readUTF(), stream.readUTF());
            }

            HashMap<Integer, String> blocks = new HashMap<>();
            byte length = stream.readByte();

            for (int i = 0; i < length; i++) {
                String name = stream.readUTF();
                blocks.put(i, name);
            }

            int total = stream.readInt();

            if (total > 128 * 128) throw new IOException("Invalid schematic: Too many blocks.");

            Stile[] tiles = new Stile[total];

            for (int i = 0; i < total; i++) {
                String blockName = blocks.get((int) stream.readByte());
                int position = stream.readInt();
                Object config = readObject(stream);
                byte rotation = stream.readByte();
                tiles[i] = new Stile(blockName, Point2.unpack(position), config, rotation);
            }

            return new Schematic(ver, tiles, tagsMap, width, height);
        }
    }


}