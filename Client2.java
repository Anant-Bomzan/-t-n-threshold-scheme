import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Client2 {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final int SHARE_ID = 2;
    private static final int SHARE_VALUE = 31; // The share value for client 1

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Generate the hash for the share
            String hash = hashFunction(SHARE_VALUE);

            // Send the share ID, value, and hash to the server
            out.println(SHARE_ID);
            out.println(SHARE_VALUE);
            out.println(hash);

            // Read the server's response
            System.out.println(in.readLine()); // Print server response
            System.out.println(in.readLine()); // Print access status

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String hashFunction(int share) throws NoSuchAlgorithmException {
        // Hash the share using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(Integer.toString(share).getBytes());
        StringBuilder hashString = new StringBuilder();
        for (byte b : hashBytes) {
            hashString.append(String.format("%02x", b));
        }
        return hashString.toString();
    }
}
