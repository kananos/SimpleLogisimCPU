package cn.kananos;

import cn.kananos.error.AssemblyError;
import cn.kananos.error.AssemblyException;

import java.util.Collections;

public class MachineCodeConverter {

    private static final int regAddrBits = 5;
    private static final int imNumberBits = 10;
    private static final int aluCtlCodeBits = 6;

    public static String convertToRegAddr(int addr) throws AssemblyException {

        String body = Integer.toBinaryString(addr);

        if (body.length() > regAddrBits) {
            // TODO: 实际上已经检测过了，不会出现这种情况
            throw new AssemblyException(AssemblyError.RegOverflowError);
        }

        return String.join("", Collections.nCopies(regAddrBits - body.length(), "0")) + body;
    }

    public static String convertToImNumber(int imNumber) throws AssemblyException {

        String body = Integer.toBinaryString(imNumber);

        if (body.length() > imNumberBits) {
            throw new AssemblyException(AssemblyError.ImNumberOverflowError);
        }

        return String.join("", Collections.nCopies(imNumberBits - body.length(), "0")) + body;
    }

    public static String convertToAluCtlCode(int aluCtlCode) throws AssemblyException {

        String body = Integer.toBinaryString(aluCtlCode);

        if (body.length() > aluCtlCodeBits) {
            throw new AssemblyException(AssemblyError.ALUOverflowError);
        }

        return String.join("", Collections.nCopies(aluCtlCodeBits - body.length(), "0")) + body;
    }
}
