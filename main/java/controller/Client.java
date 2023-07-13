package controller;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String username;

    public Client(Socket socket, String username, DataOutputStream dataOutputStream,DataInputStream dataInputStream) {
        try {
            this.socket = socket;
            this.dataInputStream = dataInputStream;
            this.dataOutputStream = dataOutputStream;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
            closeAll(socket, dataInputStream, dataOutputStream);
        }
    }

    public void sendUserName() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            closeAll(socket, dataInputStream, dataOutputStream);
        }
    }

    public void closeAll(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
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
