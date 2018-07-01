/*
 * Java. Level 2. Lesson 8
 * Graphic client for chat (Графический сервер для чата)
 *
 * @author Dmitriy Grishin by lesson 8 of Sergey Iryupin
 * @version 0.2 dated Mar 19, 2018
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;//для обработки событий
import java.io.*;
import java.net.*;

public class ChatClientL8 extends JFrame implements ActionListener, IConstants {
    //поля класса
    final String AUTH_INVITATION = "You must login using command\n" + "auth <login> <password>"; //Вы должны подключиться, используя команду...
    JTextArea dialogue;
    JTextField message;
    JButton butSend;
    boolean isAuthorised; //флаг авторизации

    Socket socket;
    PrintWriter writer;
    BufferedReader reader;


    public static void main(String[] args) {
        new ChatClientL8();
    }

    //Конструктор окна чат-клиента
    public ChatClientL8(){
        setTitle("Чат-Клиент"); //заголовок
        setBounds(500,200,350, 400); //местоположение и размеры
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //прослушиватель на закрытие окна:
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) { // if window closed - если окно закрыли
                try {
                    writer.println(EXIT_COMMAND);//отправка команды для закрытия соединения
                    writer.flush();
                    socket.close();
                } catch (Exception ex) {}
            }
            public void windowDeactivated(WindowEvent event) {}
            public void windowActivated(WindowEvent event) {}
            public void windowDeiconified(WindowEvent event) {}
            public void windowIconified(WindowEvent event) {}
            public void windowClosed(WindowEvent event) {}
            public void windowOpened(WindowEvent event) {}
        });

        //компоненты для диалога
        dialogue = new JTextArea(); //многострочная текстовая область
        //dialogue.setBackground(Color.GRAY);
        dialogue.setEditable(false);//запрет на редактирование
        JScrollPane scrollBar = new JScrollPane(dialogue);//оборачиваю текстовую область в полосы прокрутки jsp

        message = new JTextField();//однострочное текстовое поле
        //message.setBackground(Color.LIGHT_GRAY);
        message.addActionListener(this);//добавляю прослушиватель на поле message
        butSend = new JButton("SEND"); //кнопка для отправки сообщений в чат
        butSend.addActionListener(this);//добавляю прослушиватель на кнопку
        //панель для размещения объектов на "ЮГе"
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));//задаю компоновщик для панели, располагать элементы по оси Х
        //добавляю компоненты на эту панель
        southPanel.add(message);
        southPanel.add(butSend);

        //добавляю компоненты на окно
        add(BorderLayout.CENTER, scrollBar);//в ЦЕНТР - область диалога (в обёртке из полос прокрутки scrollBar)
        add(BorderLayout.SOUTH, southPanel);//панель на ЮГ
        setVisible(true);
        connect();// метод для подключения к Серверу
    }
    //метод подключения к серверу:
    void connect() {
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            new Thread(new ServerListener()).start();//Стартуем нить для чтения сообщений сервера
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        dialogue.append(AUTH_INVITATION + "\n");//Сообщение-приглашение к авторизации
        isAuthorised = false;//при первом соединении с сервером флаг становится false (авторизация ещё не пройдена)
    }

    //ПРОСЛУШИВАТЕЛЬ
    public void actionPerformed(ActionEvent event){
        //проверка - не пустая ли строка сообщения
        if (message.getText().trim().length() > 0) {
            writer.println(message.getText()); //отправляем сообщение клиента на сервер
            writer.flush();
        }
        message.setText("");//очищаю строку сообщения
        message.grabFocus();//фокусировка курсора на строке после нажатия кнопки
    }

    //Класс (внутренний) для получения сообщений с сервера (который используется для создания нити)
    class ServerListener implements Runnable {
        String serverMessage;

        @Override
        public void run() {
            try{
                while((serverMessage = reader.readLine()) != null) {
                    //проверка на успешность авторизации:
                    if(serverMessage.startsWith("Hello, "))
                        isAuthorised = true;
                    //добавляем сообщение в окно диалога, если сообщение не пустое
                    if (!serverMessage.equals("\0") && isAuthorised)
                        dialogue.append(serverMessage + "\n");
                    //терминируем клиента (отключаем) при неудачной аутентификации
                    if (serverMessage.equals(AUTH_FAIL)) System.exit(-1);
                }
            } catch (IOException ex){
                System.out.println(ex.getMessage());
            }

            System.exit(-1);//для закрытия окна клиента после отправки команды exit
        }
    }
}
