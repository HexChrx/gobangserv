package net.socket;


import net.controller.MessageManager;
import net.log.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Calendar;

public class Session extends Handler{

    private SocketChannel socketChannel;
    private SelectionKey selectionKey;
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(10240);
    private Charset charset = Charset.forName("UTF-8");
    private CharsetDecoder charsetDecoder = charset.newDecoder();
    private CharsetEncoder charsetEncoder = charset.newEncoder();
    private long lastPant;//最后活动时间
    private final int TIME_OUT = 1000 * 60 * 5; //Session超时时间
    private String key;
    private String sendData = "";
    private String receiveData = null;

    public static final int READING = 0,SENDING = 1;
    int state = READING;

    public Session(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        this.selectionKey = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        this.selectionKey.attach(this);
        this.state = READING;
        selector.wakeup();
        this.lastPant = Calendar.getInstance().getTimeInMillis();
    }

    public void distory(){
        try {
            socketChannel.close();
            selectionKey.cancel();
        } catch (IOException e) {
            Log.logE(e.getMessage());
        }

    }

    @Override
    public void exec() {
        if (this.selectionKey.isReadable()){
            this.read();
        } else if (this.selectionKey.isWritable()) {
            this.send();
        }
    }

    private synchronized void read() {
        this.receiveBuffer.clear();
        try {
            socketChannel.read(this.receiveBuffer);
            receiveBuffer.flip();
            this.receiveData = charset.decode(this.receiveBuffer).toString();
            if (MessageManager.messageProc(this.receiveData, this)) {
                receiveBuffer.compact();
                Log.logD(receiveData);
                this.setAlive();
            } else {
                Log.logE("receiveData is empty");
                this.selectionKey.cancel();
            }
        } catch (IOException e) {
            Log.logE(e.getMessage());
            this.selectionKey.cancel();
        }
    }

    private void send() {
        try {
            if (this.socketChannel == null) {
                return;
            }
            socketChannel.write(charsetEncoder.encode(CharBuffer.wrap(sendData)));
            sendData = null;
            selectionKey.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public boolean isKeekAlive() {
        return lastPant + TIME_OUT > Calendar.getInstance().getTimeInMillis();
    }

    public void setAlive() {
        lastPant = Calendar.getInstance().getTimeInMillis();
    }


    public ByteBuffer getReceiveBuffer() {
        return receiveBuffer;
    }

    public void setReceiveBuffer(ByteBuffer receiveBuffer) {
        this.receiveBuffer = receiveBuffer;
    }

    public long getLastPant() {
        return lastPant;
    }

    public void setLastPant(long lastPant) {
        this.lastPant = lastPant;
    }

    public String getSendData() {
        return sendData;
    }

    public void sendData(String sendData) {
        this.state = SENDING;
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        this.sendData = sendData + "\n";
    }

    public String getReceiveData() {
        return receiveData;
    }

    public void setReceiveData(String receiveData) {
        this.receiveData = receiveData;
    }
}
