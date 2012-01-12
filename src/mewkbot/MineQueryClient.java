package mewkbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author wolf
 */
public class MineQueryClient {

    public static void query(String address, int port, int timeout) {
        try {
            Socket socket = new Socket(address, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.print("QUERY\n");

            while(true) {
                System.out.println(socket.getInputStream().read());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
