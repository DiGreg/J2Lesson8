/*
Простой чат-клиент

@author Grishin Dmitriy
@version 0.2 dated 16.01.18
@link null
*/

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleClient implements IConstants{
    //константы берутся из интерфейса IConstants

    Socket socket;
    Scanner scanner;
    PrintWriter writer;
    BufferedReader reader;
    String message;

    public static void main(String[] args) {
        new SimpleClient();
    }

    SimpleClient() {
        scanner = new Scanner(System.in);//сканер для ввода с клавиатуры
        try{
            socket = new Socket(SERVER_ADDR, SERVER_PORT);//сокет клиента
            writer = new PrintWriter(socket.getOutputStream());//исходящий поток
            System.out.println(CONNECT_TO_SERVER);//соединение установлено

            //для ЧТЕНИЯ входящих сообщений
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println(getLoginAndPassword(scanner));//отправка на сервер строки аутентификации
            writer.flush();//сброс буфера исходящего потока для гарантии отправки сообщения

            //Нить для чтения сообщений сервера
            new Thread (new ServerListener(reader)).start();

            do{
                message = scanner.nextLine();//считываем сообщение из консоли
                writer.println(message);//отправка сообщения серверу
                writer.flush();//сброс буфера исходящего потока
            }while(!message.equalsIgnoreCase(EXIT_COMMAND));

            //закрываю ресурсы: сканер, ридер и райтер:
            writer.close();
            socket.close();
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(CONNECT_CLOSED);
    }

    //метод, формирующий строку АУТЕНТИФИКАЦИИ (идентификации пользователя)
    private String getLoginAndPassword(Scanner scanner){
        System.out.print(LOGIN_PROMPT);//подсказка для набора логина
        String login = scanner.nextLine();
        System.out.print(PASSWD_PROMPT);
        return AUTH_SIGN + " " + login + " " + scanner.nextLine();//возвращает строку аутентификации
    }

    //Класс для получения сообщений с сервера (который для создания нити)
    class ServerListener implements Runnable {
        BufferedReader reader;

        ServerListener(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            String serverMessage;
            try{
                while((serverMessage = reader.readLine()) != null) {
                    //проверка на наличие знака "\0" для печати знака-подсказки ">" клиенту или самого сообщения
                    System.out.print(serverMessage.equals("\0") ? CLIENT_PROMPT : serverMessage + "\n");
                    //терминируем клиента (отключаем) при неудачной аутентификации
                    if (serverMessage.equals(AUTH_FAIL)) System.exit(-1);
                }
            } catch (IOException ex){
                System.out.println(ex.getMessage());
            }

        }
    }
}
