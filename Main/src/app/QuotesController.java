package HTML.Main.src.app;

import HTML.Main.src.html.ServerThread;
import HTML.Main.src.html.Request;
import HTML.Main.src.html.response.*;

import java.util.List;

public class QuotesController extends Controller{
    private List<String> quotes;
    public QuotesController(Request request, List<String> quotes) {
        super(request);
        this.quotes = quotes;
    }



    @Override
    public Response doGet() {
        String htmlBody = "" +
                "<form method=\"POST\" action=\"/save-quote\">" +
                "<label>Author</label><br><input name=\"author\" type=\"text\"><br>" +
                "<label>Quote</label><br><input name=\"quote\" type=\"text\"><br><br>" +
                "<button>Save quote</button></form>";

        String famousQuotes = QuoteOfTheDayService.getQuoteOfTheDay();
        String s = "" + "<label><b>Quote of the day:</b><br>\"" + famousQuotes + "\"</label>";
        String s1 = "<br><br>" + "<label><b>Saved Quotes</b></label><br>";

        if(quotes.isEmpty()){
            s1 += "There are no saved quotes.";
        }
        for (int i = 0; i < quotes.size(); i++) {
                s1 += "<label>" + quotes.get(i) + "</label><br>";
        }

        String content = "<html><head><title>Main Server</title></head>\n";
        content += "<body>" + htmlBody + s + s1 + "</body></html>";
        return new HTMLResponse(content);
    }

    @Override
    public Response doPost() {
        System.out.println("Quote saved.");
        String quote = ServerThread.getNewQuote();
        System.out.println(quote);

        quotes.add(quote);
        return new RedirectResponse("/quotes");
    }


}
