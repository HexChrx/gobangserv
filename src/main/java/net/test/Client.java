package net.test;

import net.controller.MessageManager;
import net.model.MessageModel;
import net.util.Json;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    public static void main(String[] argv) {

        try {
            Socket socket = new Socket("10.95.25.20", 8741);
            MessageModel messageModel = new MessageModel();
            messageModel.setType(0);
            HashMap<String, String> msg = new HashMap<>();
            msg.put("uid", "uid" + Math.round(Math.random() * 100));
            msg.put("pwd", "das");
            messageModel.setContent(msg);
            OutputStream os = socket.getOutputStream();//字节输出流
            PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
            String send = Json.encode(messageModel);
            System.out.println(send + "\n");
            pw.write(send);
            pw.flush();
            //3.获取输入流，并读取服务器端的响应信息
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info;
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            do {
                if ((info = br.readLine()) != null) {
                    System.out.println("我是客户端，服务器说：" + info);
                }
                System.out.print("请输入id:");
                String to = input.readLine();
                messageModel.setType(MessageManager.SENDMSG);
                msg.clear();
                msg.put("to", to);
                System.out.print("请输入ms:");
                String content = input.readLine();
                msg.put("message", content);
                messageModel.setContent(msg);
                send = Json.encode(messageModel);
                System.out.println(send + "\n");
                pw.write(send);
                pw.flush();
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
