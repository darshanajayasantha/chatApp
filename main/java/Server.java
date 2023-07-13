import controller.MyFile;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    // Server socket that accepts client and handles data flow.
    private ServerSocket serverSocket;
    DataInputStream dataInputStream;
    static ArrayList<MyFile> myFiles = new ArrayList<>();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void starServer() {
        try {
            while (!serverSocket.isClosed()) {
                // New connected client socket.
                Socket socket = serverSocket.accept();
                System.out.println("A new client has been connected!");
                ClientHandler clientHandler = new ClientHandler(socket);

                dataInputStream = new DataInputStream(socket.getInputStream());
                int fileNameLength = dataInputStream.readInt();
                if(fileNameLength > 0){
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes,0,fileNameBytes.length);
                    String fileName = new String(fileNameBytes);
                    System.out.println("filename : "+fileName);

                    int fileContentLength = dataInputStream.readInt();

                    if(fileContentLength > 0){
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes,0,fileContentLength);

                    if(getFileExtension(fileName).equalsIgnoreCase("txt")){
                     //   System.out.println(fileId);
                    }
                    }
                }

                // Start a new thread for new connected client.
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeServerSocket();
        }
    }

    public static String getFileExtension(String fileName){
        int i = fileName.lastIndexOf('.');
        if(i>0){
            return fileName.substring(i+1);
        }else{
            return "no extension found.";
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Server server = new Server(serverSocket);
        server.starServer();
    }
}


































//import java.io.IOException;
//        import java.net.ServerSocket;
//        import java.net.Socket;
//
//public class Server {
//
//    // Server socket that accepts client and handles data flow.
//    private ServerSocket serverSocket;
//
//    public Server(ServerSocket serverSocket) {
//        this.serverSocket = serverSocket;
//    }
//
//    public void starServer() {
//        try {
//            while (!serverSocket.isClosed()) {
//                // New connected client socket.
//                Socket socket = serverSocket.accept();
//                System.out.println("A new client has been connected!");
//                ClientHandler clientHandler = new ClientHandler(socket);
//
//                // Start a new thread for new connected client.
//                Thread thread = new Thread(clientHandler);
//                thread.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            closeServerSocket();
//        }
//    }
//
//    public void closeServerSocket() {
//        try {
//            if (serverSocket != null) {
//                serverSocket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(5000);
//        Server server = new Server(serverSocket);
//        server.starServer();
//    }
//}