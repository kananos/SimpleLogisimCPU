package cn.kananos;

public enum MemCtlCode {
    MEM_LOAD(0),
    MEM_STORE(1),
    ;

    private final int code;

    MemCtlCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
