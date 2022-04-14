import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    static int postNum;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(1122);
            DataBase.getSingleTone().addDataBase("ClientAccounts", new Controller("C:\\Users\\EXO\\Desktop\\Project\\RedditJava\\DataBase\\ClientAccounts.txt"));
            DataBase.getSingleTone().addDataBase("Community", new Controller("C:\\Users\\EXO\\Desktop\\Project\\RedditJava\\DataBase\\Community.txt"));
            DataBase.getSingleTone().addDataBase("Post", new Controller("C:\\Users\\EXO\\Desktop\\Project\\RedditJava\\DataBase\\Post.txt"));
            DataBase.getSingleTone().addDataBase("ClientAccountsPost", new Controller("C:\\Users\\EXO\\Desktop\\Project\\RedditJava\\DataBase\\ClientAccountsPost.txt"));
            DataBase.getSingleTone().addDataBase("postComments", new Controller("C:\\Users\\EXO\\Desktop\\Project\\RedditJava\\DataBase\\postComments.txt"));
            DataBase.getSingleTone().addDataBase("FavPost", new Controller("C:\\Users\\EXO\\Desktop\\Project\\RedditJava\\DataBase\\FavPost.txt"));
            DataBase.getSingleTone().addDataBase("postNum", new Controller("C:\\Users\\EXO\\Desktop\\Project\\RedditJava\\DataBase\\postNum.txt"));
            String tmp = DataBase.getSingleTone().getController("postNum").readFile();
            postNum = Integer.parseInt(String.valueOf(tmp.charAt(0)));
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

class RequestHandler extends Thread {
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    void writer(String write) {
        if (write != null && !write.isEmpty()) {
            try {
                dos.writeUTF(write);
                System.out.println("write: " + write);
            } catch (IOException e) {
                try {
                    dis.close();
                    dos.close();
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }
            return;
        }
        System.out.println("invalid write");
    }

    String listener() {
        StringBuilder num = new StringBuilder();
        StringBuilder listen = new StringBuilder();
        char i;
        try {
            while ((i = (char) dis.read()) != '$') {
                num.append(i);
            }
            int counter = Integer.parseInt(num.toString());
            for (int j = 0; j < counter; j++) {
                listen.append((char) dis.read());
            }
        } catch (IOException e) {
            try {
                dis.close();
                dos.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
        return listen.toString();
    }

    @Override
    public void run() {
        System.out.println("Server-ready");
        String command = "";
        try {
            command = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("command is: " + command);
        String[] split = command.split("-");
        HashMap<String, String> data;
        User clientAccount;
        Community communityAccount;
        StringBuilder finalWrite = new StringBuilder();
        switch (split[0]) {
            case "LoginUser":
                data = new HashMap<>(
                        Map.of("username", split[1], "password", split[2]));
                clientAccount = new User(data);
                writer(clientAccount.signIn());
                break;

            case "SignUpUser":
                data = new HashMap<>(
                        Map.of("email", split[1], "username", split[2], "password", split[3]));
                clientAccount = new User(data);
                writer(clientAccount.signUp());
                break;

            case "EditProfile":
                switch (split[1]) {
                    case "username":
                        data = new HashMap<>(
                                Map.of("username", split[2], "newName", split[3]));
                        clientAccount = new User(data);
                        writer(clientAccount.editUsername());
                        break;
                    case "email":
                        data = new HashMap<>(
                                Map.of("username", split[2], "newEmail", split[3]));
                        clientAccount = new User(data);
                        writer(clientAccount.editEmail());
                        break;

                    case "password":
                        data = new HashMap<>(
                                Map.of("username", split[2],
                                        "oldPassword", split[3], "newPassword", split[4], "confirmPassword", split[5]));
                        clientAccount = new User(data);
                        writer(clientAccount.editPassword());
                        break;
                }
                break;
            case "showFeed":
                data = new HashMap<>(
                        Map.of("username", split[1]));
                clientAccount = new User(data);
                writer(clientAccount.showFeed());
                break;

            case "showProfile":
                data = new HashMap<>(
                        Map.of("username", split[1]));
                clientAccount = new User(data);
                writer(clientAccount.showProfile());
                break;

            case "addFavPost":
                data = new HashMap<>(
                        Map.of("username", split[1], "postId", split[2]));
                clientAccount = new User(data);
                writer(clientAccount.addFavPost());
                break;

            case "listFavPost":
                data = new HashMap<>(
                        Map.of("username", split[1]));
                clientAccount = new User(data);
                writer(clientAccount.getFavPost());
                break;

            case "addPost":
                data = new HashMap<>(
                        Map.of("username", split[1], "community", split[2], "title", split[3], "description", split[4]));
                clientAccount = new User(data);
                writer(clientAccount.addPost());
                break;

            case "likePost":
                data = new HashMap<>(
                        Map.of("username", split[1], "postId", split[2]));
                clientAccount = new User(data);
                writer(clientAccount.likePost());
                break;

            case "dislikePost":
                data = new HashMap<>(
                        Map.of("username", split[1], "postId", split[2]));
                clientAccount = new User(data);
                writer(clientAccount.dislikePost());

            case "addComment":
                data = new HashMap<>(
                        Map.of("username", split[1], "postId", split[2], "comment", split[3]));
                clientAccount = new User(data);
                System.out.println(split[3]);
                writer(clientAccount.addComment());
                break;

            case "postComment":
                data = new HashMap<>(
                        Map.of("username", split[1], "postId", split[2]));
                clientAccount = new User(data);
                writer(clientAccount.getPostComment());
                break;


            case "postDetail":
                data = new HashMap<>(
                        Map.of("username", split[1], "postId", split[2]));
                clientAccount = new User(data);
                writer(clientAccount.postDetail());
                break;


            case "community":
                switch (split[1]) {
                    case "admin":
                        switch (split[2]) {
                            case "removePost":
                                data = new HashMap<>(
                                        Map.of("username", split[1], "postId", split[2]));
                                communityAccount = new Community(data);
                                writer(communityAccount.removePost());
                                break;
                            case "addAdmin":
                                data = new HashMap<>(
                                        Map.of("username", split[1], "user", split[2]));
                                communityAccount = new Community(data);
                                writer(communityAccount.addAdmin());
                                break;
                            case "edit":
                                data = new HashMap<>(
                                        Map.of("username", split[1], "community", split[2], "info", split[3]));
                                communityAccount = new Community(data);
                                writer(communityAccount.editCommunity());
                                break;
                        }
                    case "list":
                        data = new HashMap<>(
                                Map.of("username", split[1]));
                        communityAccount = new Community(data);
                        writer(communityAccount.allCommunity());
                        break;

                    case "showProfile":
                        data = new HashMap<>(
                                Map.of("username", split[1], "communityName", split[2]));
                        communityAccount = new Community(data);
                        writer(communityAccount.communityProfile());
                        break;

                    case "createCommunity":
                        data = new HashMap<>(
                                Map.of("username", split[1], "communityName", split[2]));
                        communityAccount = new Community(data);
                        writer(communityAccount.createCommunity());
                        break;

                    default:
                        break;
                }
        }
    }
}
