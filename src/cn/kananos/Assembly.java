package cn.kananos;

import cn.kananos.error.AssemblyError;
import cn.kananos.error.AssemblyException;
import cn.kananos.binGroup.BinGroup;
import cn.kananos.binGroup.BinType;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembly {

    String regWriteEnable = "0";
    String memSelectEnable = "0";
    String memWriteEnable = "0";
    final String placeholder = "000";
    String srcRegAddr = "00000";
    String dstRegAddr = "00000";
    String directNumber = "0000000000";
    String aluControlCode = "000000";

    static Pattern regOffsetPattern = Pattern.compile("^(\\d+)\\((.+)\\)$");

    private Assembly() {}

    public void setRegWriteEnable(String regWriteEnable) {
        this.regWriteEnable = regWriteEnable;
    }

    public void setMemSelectEnable(String memSelectEnable) {
        this.memSelectEnable = memSelectEnable;
    }

    public void setMemWriteEnable(String memWriteEnable) {
        this.memWriteEnable = memWriteEnable;
    }

    public void setSrcRegAddr(String srcRegAddr) {
        this.srcRegAddr = srcRegAddr;
    }

    public void setDstRegAddr(String dstRegAddr) {
        this.dstRegAddr = dstRegAddr;
    }

    public void setDirectNumber(String directNumber) {
        this.directNumber = directNumber;
    }

    public void setAluControlCode(String aluControlCode) {
        this.aluControlCode = aluControlCode;
    }


    public String toBinaryString() {
        String binString = regWriteEnable + memSelectEnable + memWriteEnable + placeholder +
                srcRegAddr + dstRegAddr + directNumber + aluControlCode;

        return binString;
    }

    public String toHexString() throws AssemblyException {

        String binString = regWriteEnable + memSelectEnable + memWriteEnable + placeholder +
                srcRegAddr + dstRegAddr + directNumber + aluControlCode;

        StringBuilder sb = new StringBuilder();

        // 仅支持32Bits

        if (binString.length() != 32) {
            throw new AssemblyException(AssemblyError.InstructionLenError);
        }

        for (int i = 0; i < 8; i++) {
            int number = Integer.parseInt(binString, 4 * i, 4 * i + 4, 2);
            sb.append(Integer.toHexString(number));
        }

        // 带填充

        return sb.toString();
    }

    private static void ParseMovi(Assembly res, String body) throws AssemblyException {
        String[] patterns = body.split(",");

        if (patterns.length != 2) {
            throw new AssemblyException(AssemblyError.PatternError, "movi指令需要两个操作单元");
        }

        BinGroup reg = ParseReg(patterns[0]);
        BinGroup im = ParseIm(patterns[1]);

        res.setRegWriteEnable("1");
        res.setMemSelectEnable("0");
        res.setMemWriteEnable("0");
        // 使用第7号寄存器
        res.setSrcRegAddr(MachineCodeConverter.convertToRegAddr(7));
        res.setDstRegAddr(MachineCodeConverter.convertToRegAddr(reg.getValue()));
        res.setDirectNumber(MachineCodeConverter.convertToImNumber(im.getValue()));
        res.setAluControlCode(MachineCodeConverter.convertToAluCtlCode(ALUCtlCode.ALU_ADD.getCode()));
    }

    private static BinGroup ParseReg(String body) throws AssemblyException {
        if (body.equals("") ||body.charAt(0) != 'r') {
            throw new AssemblyException(AssemblyError.RegError);
        }


        int reg = 0;
        try {
            reg = Integer.parseInt(body.substring(1));
        } catch (NumberFormatException e) {
            throw new AssemblyException(AssemblyError.RegError);
        }
        if (reg < 0 || reg > 7) {
            throw new AssemblyException(AssemblyError.RegOverflowError);
        }
        return new BinGroup(BinType.Reg, reg);
    }

    private static BinGroup ParseIm(String body) throws AssemblyException {
        int value = 0;
        try {
            value =Integer.parseInt(body);
        } catch (NumberFormatException e) {
            throw new AssemblyException(AssemblyError.ImError);
        }
        return new BinGroup(BinType.Im, value);
    }

    private static void ParseAddi(Assembly res, String body) throws AssemblyException {
        ParseAU(res, body, ALUCtlCode.ALU_ADD);
    }

    private static void ParseSubi(Assembly res, String body) throws AssemblyException {
        ParseAU(res, body, ALUCtlCode.ALU_SUB);
    }

    private static void ParseAU(Assembly res, String body, ALUCtlCode code) throws AssemblyException {
        String[] patterns = body.split(",");

        if (code != ALUCtlCode.ALU_ADD && code != ALUCtlCode.ALU_SUB) {
            throw new AssemblyException(AssemblyError.ALUCtlCodeError);
        }

        if (patterns.length != 3) {
            throw new AssemblyException(AssemblyError.PatternError, "AU需要三个操作单元");
        }

        BinGroup dstReg = ParseReg(patterns[0]);
        BinGroup srcReg = ParseReg(patterns[1]);
        BinGroup im = ParseIm(patterns[2]);

        res.setRegWriteEnable("1");
        res.setMemSelectEnable("0");
        res.setMemWriteEnable("0");
        res.setSrcRegAddr(MachineCodeConverter.convertToRegAddr(srcReg.getValue()));
        res.setDstRegAddr(MachineCodeConverter.convertToRegAddr(dstReg.getValue()));
        res.setDirectNumber(MachineCodeConverter.convertToImNumber(im.getValue()));

        switch (code) {
            case ALU_ADD:
                res.setAluControlCode(MachineCodeConverter.convertToAluCtlCode(ALUCtlCode.ALU_ADD.getCode()));
                break;
            case ALU_SUB:
                res.setAluControlCode(MachineCodeConverter.convertToAluCtlCode(ALUCtlCode.ALU_SUB.getCode()));
                break;
            default:
                throw new AssemblyException(AssemblyError.ALUCtlCodeError);
        }
    }

    private static void ParseMem(Assembly res, String body, MemCtlCode code) throws AssemblyException {
        String[] patterns = body.split(",");
        if (patterns.length != 2) {
            throw new AssemblyException(AssemblyError.PatternError, "内存操作需要两个处理单元");
        }
        Matcher m = regOffsetPattern.matcher(patterns[1]);
        if (!m.find()) {
            throw new AssemblyException(AssemblyError.RegError);
        }
        BinGroup dstReg = ParseReg(patterns[0]);
        BinGroup offset = ParseIm(m.group(1));
        BinGroup srcReg = ParseReg(m.group(2));

        res.setSrcRegAddr(MachineCodeConverter.convertToRegAddr(srcReg.getValue()));
        res.setDstRegAddr(MachineCodeConverter.convertToRegAddr(dstReg.getValue()));
        res.setDirectNumber(MachineCodeConverter.convertToImNumber(offset.getValue()));
        res.setAluControlCode(MachineCodeConverter.convertToAluCtlCode(ALUCtlCode.ALU_ADD.getCode()));

        switch (code) {
            case MEM_LOAD:
                res.setMemSelectEnable("1");
                res.setMemWriteEnable("0");
                res.setRegWriteEnable("1");
                break;
            case MEM_STORE:
                res.setMemSelectEnable("1");
                res.setMemWriteEnable("1");
                res.setRegWriteEnable("0");
                break;
            default:
                throw new AssemblyException(AssemblyError.NoCMDError);
        }
    }

    private static void ParseLw(Assembly res, String body) throws AssemblyException {
        ParseMem(res, body, MemCtlCode.MEM_LOAD);
    }

    private static void ParseSw(Assembly res, String body) throws AssemblyException {
        ParseMem(res, body, MemCtlCode.MEM_STORE);
    }


    public static Assembly assembly(String line) throws AssemblyException {

        Assembly res = new Assembly();
        line = line.toLowerCase(Locale.ROOT);
        line = line.replaceAll("\\s+", " ");
        line = line.trim();


        if (line.length() == 0 || line.startsWith("//")) {
            throw new AssemblyException(AssemblyError.EmptyStatementError);
        }

        if (!line.contains(" ")) {
            throw new AssemblyException(AssemblyError.NoBlankError);
        }

        String[] toks = line.split(" ", 2);

        String cmd = toks[0];
        String body = toks[1].replaceAll(" ", "");

        switch (cmd) {
            case "movi":
                ParseMovi(res, body);
                break;
            case "addi":
                ParseAddi(res, body);
                break;
            case "subi":
                ParseSubi(res, body);
                break;
            case "lw":
                ParseLw(res, body);
                break;
            case "sw":
                ParseSw(res, body);
                break;
            default:
                throw new AssemblyException(AssemblyError.NoCMDError);
        }

        return res;
    }
}
