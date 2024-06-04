import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final int THRESHOLD = 3;
    private static final int TOTAL_SHARES = 50;
    private static final BigInteger PRIME = BigInteger.valueOf(257);

    private static Map<Integer, BigInteger> receivedShares = new HashMap<>();
    private static Map<Integer, BigInteger> shares = new HashMap<>();
    private static BigInteger secret;
    private static ServerSocket serverSocket;
    private static String originalSecretHash;
    private static boolean keepRunning = true;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        long startTime = System.currentTimeMillis(); // Start time
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT);
        generateShares();
        originalSecretHash = hashFunction(secret.intValue());
        System.out.println("Hash of the generated secret: " + originalSecretHash);

        try {
            while (keepRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                handleClient(clientSocket);
            }
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            long endTime = System.currentTimeMillis(); // End time
            long runtime = endTime - startTime; // Calculate runtime
            System.out.println("Runtime: " + runtime + " ms"); // Print runtime
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
    
            int id = Integer.parseInt(in.readLine());
            BigInteger share = new BigInteger(in.readLine());
            String clientHash = in.readLine();
    
            // Check if the share ID is valid and if the hash matches
            if (shares.containsKey(id) && hashFunction(share.intValue()).equals(clientHash)) {
                receivedShares.put(id, share);
                System.out.println("Received valid share: " + id + ", " + share);
                out.println("Share accepted.");
            } else {
                BigInteger recoveredSecret = recoverSecret(receivedShares);
                // If the share is invalid, print the hash and exit
                String invalidShareHash = hashFunction(share.intValue());
                System.out.println("Received invalid share or hash: " + id + ", " + share);
                System.out.println("Hash of the invalid share: " + invalidShareHash);
                System.out.println("Share rejected. Secret: " + recoveredSecret);
                keepRunning = false; // Set the flag to false to stop the server
            }
    
            // If enough shares have been received, attempt to recover the secret
            if (receivedShares.size() >= THRESHOLD) {
                BigInteger recoveredSecret = recoverSecret(receivedShares);
                String recoveredSecretHash = hashFunction(recoveredSecret.intValue());
    
                // Print the recovered secret's hash
                System.out.println("Hash of the recovered secret: " + recoveredSecretHash);
    
                // Grant or deny access based on the hash comparison
                if (originalSecretHash.equals(recoveredSecretHash)) {
                    System.out.println("Access granted. Secret: " + recoveredSecret);
                    out.println("Access granted. Secret: " + recoveredSecret);
                    keepRunning = false;
                } else {
                    System.out.println("Access denied. Secret: " + recoveredSecret);
                    out.println("Access denied. Secret: " + recoveredSecret);
                    keepRunning = false; // Set the flag to false to stop the server
                }
    
                // Reset the receivedShares for the next set of shares
                receivedShares.clear();
            }
    
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            clientSocket.close(); // Close the client socket after handling
        }
    }
    


    private static void generateShares() {
        Random random = new Random(12345); // Use a fixed seed
        secret = BigInteger.valueOf(random.nextInt(PRIME.intValue()));
        System.out.println("Generated secret: " + secret);

        BigInteger[] coefficients = new BigInteger[THRESHOLD - 1];
        for (int i = 0; i < THRESHOLD - 1; i++) {
            coefficients[i] = BigInteger.valueOf(random.nextInt(PRIME.intValue()));
        }

        for (int i = 1; i <= TOTAL_SHARES; i++) {
            BigInteger share = secret;
            for (int j = 0; j < coefficients.length; j++) {
                share = share.add(coefficients[j].multiply(BigInteger.valueOf(i).pow(j + 1))).mod(PRIME);
            }
            shares.put(i, share);
        }
        System.out.println("Generated shares: " + shares);
    }

    private static BigInteger recoverSecret(Map<Integer, BigInteger> shares) {
        BigInteger secret = BigInteger.ZERO;
        for (Integer i : shares.keySet()) {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;
            for (Integer j : shares.keySet()) {
                if (!i.equals(j)) {
                    BigInteger bigI = BigInteger.valueOf(i);
                    BigInteger bigJ = BigInteger.valueOf(j);
                    numerator = numerator.multiply(bigJ.negate()).mod(PRIME);
                    denominator = denominator.multiply(bigI.subtract(bigJ)).mod(PRIME);
                }
            }
            BigInteger value = shares.get(i);
            BigInteger term = value.multiply(numerator).multiply(modInverse(denominator, PRIME)).mod(PRIME);
            secret = secret.add(term).mod(PRIME);
        }
        return secret;
    }
    

    private static BigInteger modInverse(BigInteger a, BigInteger p) {
        return a.modInverse(p);
    }

    private static String hashFunction(int value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(Integer.toString(value).getBytes());
        StringBuilder hashString = new StringBuilder();
        for (byte b : hashBytes) {
            hashString.append(String.format("%02x", b));
        }
        return hashString.toString();
    }

}