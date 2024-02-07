package edu.escuelaing.arem.ASE.app;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(35000);
        System.out.println("Listo para recibir ...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection from " + clientSocket);

            try (InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                    OutputStream outputStream = clientSocket.getOutputStream()) {

                    BufferedReader in = new BufferedReader(inputStreamReader);

                    // Read the request line
                    String request = in.readLine();
                    String[] parts = request.split(" ");
                    String method = parts[0];
                    String path = parts[1].substring(1); // Remove leading "/"

                    System.out.println("Request: " + request);

                    if (method.equals("GET")) {
                        handleGetRequest(path, outputStream);
                    } else {
                        outputStream.write("HTTP/1.1 501 Not Implemented\r\n\r\n".getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                }
            }
    }

    private static void handleGetRequest(String path, OutputStream outStream) throws IOException {
        String contentType = guessContentType(path);
        if (contentType == null) {
            return;
        }
    
        // Open the file as a FileInputStream to read binary data for images
        // For text-based files like HTML, open them as BufferedReader to read text content
        try {
            if (contentType.startsWith("text")) {
                try (BufferedReader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
                    String line;
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + contentType + "\r\n\r\n";
                    outStream.write(response.getBytes());
                    while ((line = reader.readLine()) != null) {
                        outStream.write(line.getBytes());
                    }
                }
            } else {
                try (InputStream fileStream = new FileInputStream(path)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + contentType + "\r\n\r\n";
                    outStream.write(response.getBytes());
                    while ((bytesRead = fileStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static String guessContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        } else {
            return null; // Unsupported file type
        }
    }
}
