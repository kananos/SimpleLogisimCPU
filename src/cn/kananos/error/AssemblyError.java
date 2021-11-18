package cn.kananos.error;

public enum AssemblyError implements CommonError {

    NoError("00000", "成功"),
    UnknownError("00001", "未知错误"),
    UninitializedError("00002", "未初始化错误"),
    ALUCtlCodeError("00003", "ALU控制码错误"),

    NoCMDError("10001", "未知命令"),
    NoBlankError("10002", "未检测到命令空格"),
    NoCommaError("10003", "未检测到逗号分割"),
    EmptyStatementError("10004", "空语句"),

    RegOverflowError("20001", "指令寄存器范围超限0-7"),
    ImNumberOverflowError("20002", "立即数范围超限0-10bits"),
    ALUOverflowError("20003", "ALU功能码超限0-6bits"),
    InstructionLenError("20004", "指令长度错误"),

    PatternError("40001", "操作单元不匹配"),
    RegError("40002", "应当使用寄存器地址的场合"),
    ImError("40003", "应当使用立即数的场合")
    ;


    AssemblyError(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private String errorCode;
    private String errorMsg;

    @Override
    public String toString() {
        return errorMsg;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
