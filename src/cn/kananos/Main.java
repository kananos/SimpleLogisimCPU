package cn.kananos;

import cn.kananos.error.AssemblyError;
import cn.kananos.error.AssemblyException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws AssemblyException, IOException {
	// write your code here

        if (args.length != 1) {
            System.out.println("usage: java *.jar infilePath");
            System.out.printf("cur: %d\n", args.length);
            return ;
        }

        String infilePath = args[0];
        String outfilePath = infilePath.substring(infilePath.lastIndexOf('.') + 1) + ".output";
        String outHexPath = infilePath.substring(infilePath.lastIndexOf('.') + 1) + ".hex";

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(new FileInputStream(infilePath), StandardCharsets.UTF_8));
        BufferedWriter writer =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfilePath), StandardCharsets.UTF_8));

        BufferedWriter hexWriter =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outHexPath), StandardCharsets.UTF_8));
        Assembly result;

        hexWriter.write("v2.0 raw\n");

        String curLine = null;
        while ((curLine = reader.readLine()) != null) {
            try {
                result = Assembly.assembly(curLine);
                writer.write(curLine + "\t" + result.toBinaryString() + "\t" + result.toHexString() + "\n");
                hexWriter.write(result.toHexString() + " ");
            } catch (AssemblyException e) {
                writer.write(curLine + "\t" + e.getErrorMsg() + "\n");
            }
        }
        writer.flush();
        writer.close();
        hexWriter.flush();
        hexWriter.close();
        reader.close();
//        System.out.println(res.toHexString());

    }
}
