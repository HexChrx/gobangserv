package net.socket;

import jdk.nashorn.internal.ir.LexicalContextNode;
import net.log.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class SocketCtrler extends Thread {

    private final int ACCPET_PORT = 8741;
    private final int TIME_OUT = 1000;
    private ServerSocketChannel serverSocketChannel;
    private ServerSocket serverSocket;
    private InetSocketAddress address;
    private Selector selector;

    public SocketCtrler() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            if (serverSocketChannel == null) {
                System.out.println("can't open server socket channel");
            }
            serverSocket = serverSocketChannel.socket();
            address = new InetSocketAddress(ACCPET_PORT);
            serverSocket.bind(address);
            Log.logD("server bind port is " + ACCPET_PORT);
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            key.attach(new Acceptor(this.serverSocketChannel, this.selector));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Log.logD("server is listening...");
        while(!Thread.interrupted()) {
             try {
                 if(selector.select(TIME_OUT) > 0) {
                     Set<SelectionKey> keys = selector.selectedKeys();
                     Iterator<SelectionKey> iterator = keys.iterator();
                     SelectionKey key;
                     while(iterator.hasNext()) {
                         key = iterator.next();
                         Handler at = (Handler) key.attachment();
                         if(at != null) {
                             at.exec();
                         }
                         iterator.remove();
                     }
                 }
             } catch (IOException e) {
                 Log.logE(e.getMessage());
             }
        }
    }

}
