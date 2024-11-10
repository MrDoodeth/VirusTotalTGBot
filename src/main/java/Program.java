import com.MyDo.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Program {
    private static final String API_TOKEN = "7513757273:AAGp28NpxTj2Fp_Jm6mAFwDOMFemqb_kJN8";
    private static final String BOT_USERNAME = "super_virus_total_bot";

    private static final String VIRUS_TOTAL_API_TOKEN = "12abc3bc8bcaf59eca1e796cff3464d5e19ba4dc1d7e4bd970bfb942ebc5ca68";

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot.setINSTANCE(new Bot(API_TOKEN, BOT_USERNAME, VIRUS_TOTAL_API_TOKEN));
        botsApi.registerBot(Bot.getINSTANCE());
        System.out.println("Bot started");
    }
}