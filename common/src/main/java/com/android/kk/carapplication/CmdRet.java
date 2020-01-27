package com.android.kk.carapplication;

public class CmdRet {
    public int exitValue;
    public String outStream;

    @Override
    public String toString() {
        return "CmdRet{" +
                "exitValue=" + exitValue +
                ", outStream='" + outStream + '\'' +
                ", errStream='" + errStream + '\'' +
                '}';
    }

    public String errStream;
}
