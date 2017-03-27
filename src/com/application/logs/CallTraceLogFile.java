package com.application.logs;


import java.io.File;

public class CallTraceLogFile {
    private static File file;
    private static String fileName = "L-Instrumentation_call_trace.txt";

    CallTraceLogFile() {
        // Get the resources
        // http://stackoverflow.com/a/21722773/3690248
        file = new File(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile());
    }
    public static File getFile() {
        return file;
    }

    public static void setFile(File newFile) {
        file = newFile;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String newFileName) {
        fileName = newFileName;
    }
}