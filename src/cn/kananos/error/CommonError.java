package cn.kananos.error;

public interface CommonError {
    String getErrorCode();
    String getErrorMsg();
    CommonError setErrorMsg(String errorMsg);
}
