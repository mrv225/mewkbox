package mewkbot;

import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Mewes
 */
public class MineQueryClient {

    //TODO
    public static String[] query(String address, int port, int timeout) throws SocketTimeoutException {
        byte[] buffer = new byte[256];
        Socket socket = null;
        
        try {
            socket = new Socket(address, port);
            socket.setSoTimeout(timeout);
            socket.getOutputStream().write(254);
            
            int len = socket.getInputStream().read(buffer);
            
            String data = new String(buffer, 4, len - 3, "UTF-16LE");
            return data.split("§");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}