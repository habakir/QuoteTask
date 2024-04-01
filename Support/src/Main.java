package HTML.Support.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    static final int PORT = 8081;
    static List<String> list = new ArrayList<>();
    static String currentQuote;

    static {
        list.add("Oscar Wilde: To live is the rarest thing in the world. Most people exist, that is all.");
        list.add("Emily Dickinson: That it will never come again is what makes life so sweet.");
        list.add("C.S. Lewis: Don't let your happiness depend on something you may lose.");
        list.add("George Eliot: It is never too late to be what you might have been.");
        list.add("Mark Twain: Keep away from people who try to belittle your ambitions. Small people always do that, but the really great make you feel that you, too, can become great.");
        list.add("E. E. Cummings: Trust our heart if the seas catch fire, live by love though the stars walk backwards.");
        list.add("Margaret Atwood: If I waited for perfection, I would never write a word.");

        Random rand = new Random();
        int index = rand.nextInt(list.size());
        currentQuote = list.get(index);
    }

    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("---Second service activated--- PORT: " + PORT);

            new Thread(new QuotePrinter()).start();

            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Request from server");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String request = in.readLine();
                if(request.contains("GET /quote-of-the-day")){
                    String jsonQuote = "{ \"quote\": \"" + currentQuote + "\" }";
                    out.println(jsonQuote);
                }

                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    static class QuotePrinter implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    long currentTime = System.currentTimeMillis();
                    long millisUntilMidnight = (24 * 60 * 60 * 1000) - (currentTime % (24 * 60 * 60 * 1000));
                    Thread.sleep(millisUntilMidnight);

                    Random rand = new Random();
                    int index = rand.nextInt(list.size());
                    String quote = list.get(index);
                    System.out.println("Quote of the day: " + quote);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}