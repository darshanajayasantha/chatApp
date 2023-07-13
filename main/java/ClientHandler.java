import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    private String clientUserName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadCastMessage( "\n"+clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeAll(socket, bufferedReader, dataInputStream, dataOutputStream);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = dataInputStream.readUTF();
                broadCastMessage(messageFromClient);
            } catch (IOException e) {
                closeAll(socket, bufferedReader, dataInputStream, dataOutputStream);
                break;
            }
        }
    }

    // Send a message to everyone except me.
    public void broadCastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUserName.equals(this.clientUserName)) {
                    clientHandler.dataOutputStream.writeUTF(messageToSend+"\n");
                    clientHandler.dataOutputStream.flush();
                }
            } catch (IOException e) {
                closeAll(socket, bufferedReader, dataInputStream,dataOutputStream);
            }
        }
    }

    // controller.Client left the chat.
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadCastMessage("\n"+clientUserName + " has left the chat!");
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if(dataOutputStream != null){
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}