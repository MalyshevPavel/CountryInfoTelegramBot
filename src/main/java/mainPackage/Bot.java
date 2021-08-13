package mainPackage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) throws IOException {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        parseWeb();
    }

    // Вернуть имя бота, указанного при регистрации
    @Override
    public String getBotUsername() {
        return "CountryInfoUserBot";
    }

    // Токен, полученный от BotFather
    @Override
    public String getBotToken() {
        BotToken actualBotToken = new BotToken();
        return actualBotToken.getToken();
    }

    // Метод для отправки сообщений пользователю
    public void sendMessage(Long chatId, String text) {
        if (!text.isEmpty()) {
            SendMessage sendMsg = new SendMessage();
            sendMsg.setChatId(chatId.toString());
            sendMsg.setText(text);
            try {
                execute(sendMsg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    static Map<String, Country> hashMap = new HashMap<>();

    // Парсинг источника (WEB-сайта) с помощью библиотеки jsoup
    public static void parseWeb() throws IOException {
        Document document = Jsoup
                .connect("https://geo.koltyrin.ru/strany_mira.php")
                .userAgent("Safari")
                .referrer("https://www.google.ru")
                .get();

        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();

        Country country = new Country();
        Elements countryInfo = document.select("td.list");
        for (Element element : countryInfo) {

            switch (i) {
                case 0: {
                    stringBuilder.append("Страна: ").append(element.text()).append("\n");
                    i++;
                    country.setName(element.text());
                    break;
                }
                case 1: {
                    stringBuilder.append("Столица: ").append(element.text()).append("\n");
                    i++;
                    country.setCapital(element.text());
                    break;
                }
                case 2: {
                    stringBuilder.append("Площадь: ").append(element.text()).append("\n");
                    i++;
                    country.setArea(element.text());
                    break;
                }
                case 3: {
                    stringBuilder.append("Население: ").append(element.text()).append("\n");
                    i++;
                    country.setPopulation(element.text());
                    break;
                }
                case 4: {
                    stringBuilder.append("Континент: ").append(element.text()).append("\n");
                    stringBuilder.append("\n\n");
                    country.setContinent(element.text());
                    hashMap.put(country.getName(), country);
                    country = new Country();
                    i = 0;
                    break;
                }

            }
        }

        // System.out.println(stringBuilder.toString());
    }

    // Метод для приема сообщений
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if ("/start".equals(message.getText())) {
            sendMessage(message.getChatId(), "Привет! Я - бот-страновед.\n" + "Я готов предоставить вам информацию об интересующей вас стране.\n" +
                    "Введите название интересующей вас страны и отправьте мне.");
        } else {
            if (!hashMap.containsKey(message.getText())) {
                sendMessage(message.getChatId(), "О такой стране я ничего не знаю.\nЛибо написание не совпадает с моей базой.");
            }
            sendMessage(message.getChatId(), hashMap.get(message.getText()).returnInfo());
        }

    }
}
