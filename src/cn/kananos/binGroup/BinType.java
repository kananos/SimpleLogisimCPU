package cn.kananos.binGroup;

public enum BinType {

    Reg("Register"),
    Im("ImmediateNumber"),
    Mem("Memory"),
    ;
    String msg;

    BinType(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
