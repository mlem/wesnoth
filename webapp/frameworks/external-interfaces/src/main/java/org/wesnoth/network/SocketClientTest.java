package org.wesnoth.network;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.itadaki.bzip2.BZip2InputStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class SocketClientTest {

    public void receiveSomething() throws IOException {
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", 15000);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Scanner scanner = new Scanner(System.in);
        //  prompt for the user's name
        System.out.print("Enter something what you want to send to server: ");
        String sentence = scanner.next();
        outToServer.writeBytes(sentence + '\n');

        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);
        clientSocket.close();

    }

    public static void main(String... args) {


      /*
      new Thread(
                () -> {
                    ServerSocket welcomeSocket = null;
                    try {
                        welcomeSocket = new ServerSocket(25000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (true) {
                        Socket connectionSocket = null;
                        try {
                            connectionSocket = welcomeSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedReader inFromClient = null;
                        try {
                            String clientSentence;
                            String capitalizedSentence;
                            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                            clientSentence = inFromClient.readLine();
                            System.out.println("Received: " + clientSentence);
                            capitalizedSentence = clientSentence.toUpperCase() + '\n';
                            outToClient.writeBytes(capitalizedSentence);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                */

        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 15000);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

           //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            //  prompt for the user's name
            System.out.print("Enter something what you want to send to server: ");
            String sentence = scanner.next();
            outToServer.write(sentence.getBytes());

            BufferedReader inFromServer = getBufferedReaderForCompressedFile(clientSocket.getInputStream());
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + modifiedSentence);


            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CompressorException e) {
            e.printStackTrace();
        }

    }

    public static BufferedReader getBufferedReaderForCompressedFile(InputStream fileIn) throws IOException, CompressorException {
        //BZip2InputStream inputStream = new BZip2InputStream(fileIn, false);
        java.util.zip.GZIPInputStream inputStream = new java.util.zip.GZIPInputStream(fileIn);
        //CompressorInputStream inputStream = new CompressorStreamFactory().createCompressorInputStream(fileIn);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(inputStream));
        return br2;
    }
}
