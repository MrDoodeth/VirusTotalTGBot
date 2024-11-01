import com.mydo.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Program {
    private static final String API_TOKEN = "7513757273:AAGp28NpxTj2Fp_Jm6mAFwDOMFemqb_kJN8";
    private static final String BOT_USERNAME = "super_virus_total_bot";

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot(API_TOKEN, BOT_USERNAME));
        System.out.println("Bot started");
    }
}