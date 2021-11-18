package cn.kananos.error;

public class AssemblyException extends Throwable implements CommonError {

    AssemblyError error;

    public AssemblyException(AssemblyError error) {
        super();
        this.error = error;
    }

    public AssemblyException(AssemblyError error, String errorMsg) {
        super();
        this.error = error;
        error.setErrorMsg(errorMsg);
    }

    @Override
    public String getErrorCode() {
        return error.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return error.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        return error.setErrorMsg(errorMsg);
    }
}
