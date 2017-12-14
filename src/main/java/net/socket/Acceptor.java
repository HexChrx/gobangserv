package net.socket;


import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static net.log.Log.logE;

public class Acceptor extends Handler{
    private ServerSocketChannel serverSocketChannel;
    private Selector selector = null;

    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }
    @Override
    public void exec() {
        try {
            SocketChannel sc = this.serverSocketChannel.accept();
            System.out.println("connected");
            new Session(sc, selector);
        } catch (IOException e) {
            logE(e);
        }
    }

}
