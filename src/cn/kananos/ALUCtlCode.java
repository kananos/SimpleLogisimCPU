package cn.kananos;

public enum ALUCtlCode {
    ALU_ADD(2),
    ALU_SUB(3),
    ;

    private final int code;

    ALUCtlCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
