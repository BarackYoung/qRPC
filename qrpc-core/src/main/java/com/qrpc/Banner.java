package com.qrpc;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
public class Banner {
    public static void print() {
        System.out.println(
                "   ----           -----        -----       -------|       \n" +
                "  / -- \\\\       / ---- \\\\     /     \\\\   / /------|       \n" +
                " | |    | |     | |    | |   | |    | |  | |            \n" +
                " | |    | |     | | ---| |   | |---/ /   | |            \n" +
                " | |    | |\\\\   | |\\\\ \\\\     | |         | |------|       \n" +
                "  \\ --// \\\\--\\\\ | |  \\\\-\\\\   | |          \\ \\----|       \n");
    }

    public static void printStarted(int port) {
        System.out.println("qRPC Server started at port: " + port);
    }
}
