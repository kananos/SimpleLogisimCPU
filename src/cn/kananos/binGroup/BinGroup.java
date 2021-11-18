package cn.kananos.binGroup;

public class BinGroup {
    BinType type;
    int value;

    public BinGroup(BinType type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
