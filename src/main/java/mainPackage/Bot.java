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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    // Метод для создания клавиатуры
    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // Инициализация клавиатуры
        sendMessage.setReplyMarkup(replyKeyboardMarkup); // Создание разметки клавиатуры
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true); // Автоматическая подгонка размера клавиатуры
        replyKeyboardMarkup.setOneTimeKeyboard(false); // Клавиатура не будет скрыта после нажатия кнопки

        List<KeyboardRow> keyboardRowList = new ArrayList<>(); // Список строк (состоящих из кнопок)

        KeyboardRow firstKeyboardRow = new KeyboardRow(); // Создание первой строки клавиатуры
        KeyboardRow secondKeyboardRow = new KeyboardRow(); // Создание второй строки клавиатуры

        // Добавление кнопок в строки (по 3 кнопки в строке)
        firstKeyboardRow.add(new KeyboardButton("Россия"));
        firstKeyboardRow.add(new KeyboardButton("Молдова"));
        firstKeyboardRow.add(new KeyboardButton("США"));

        secondKeyboardRow.add(new KeyboardButton("Канада"));
        secondKeyboardRow.add(new KeyboardButton("Испания"));

        // Добавление строк в список строк
        keyboardRowList.add(firstKeyboardRow);
        keyboardRowList.add(secondKeyboardRow);

        // Добавление списка строк в клавиатуру
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    // Метод для отправки сообщений пользователю
    public void sendMessage(Long chatId, String text) {
        if (!text.isEmpty()) {
            SendMessage sendMsg = new SendMessage();
            sendMsg.setChatId(chatId.toString());
            sendMsg.setText(text);
            try {
                setButtons(sendMsg);
                execute(sendMsg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод вывода в чат списка доступных стран
    public void listAllCountries(Long chatId) {
        String[] countryArray = new String[5];
        countryArray[0] = "Россия";
        countryArray[1] = "Молдова";
        countryArray[2] = "США";
        countryArray[3] = "Канада";
        countryArray[4] = "Испания";

        StringBuilder stringBuilder = new StringBuilder();

        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(chatId.toString());
        for (String s : countryArray) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }
        stringBuilder.trimToSize();
        sendMsg.setText(stringBuilder.toString());
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Парсинг источника (WEB-сайта) с помощью библиотеки jsoup
    public static void parseWeb() throws IOException {
        Document document = Jsoup.connect("https://geo.koltyrin.ru/strany_mira.php").userAgent("Safari").referrer("https://www.google.ru").get();
        Elements countryInfo = document.select("div.field_center");
        for (Element element : countryInfo.select("tr")) {
            System.out.println(element.text());
        }
    }

    // Метод для приема сообщений
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        switch (message.getText()) {
            case "/start":
                sendMessage(message.getChatId(), "Привет! Я - бот-страновед.\n" + "Я готов предоставить вам информацию о выбранной вами стране.\n" +
                        "Нажмите на название интересующей вас страны на клавиатуре под текстовым полем ввода." +
                        "Список всех стран доступен по команде /countries");
                break;
            case "Россия":
                sendMessage(message.getChatId(), "Страна: Россия\n" + "Столица: Москва\n" + "Население страны: 146 171 015 чел.\n" + "Площадь страны: 17 125 191 км²");
                break;
            case "Молдова":
                sendMessage(message.getChatId(), "Страна: Молдова\n" + "Столица: Кишинев\n" + "Население страны: 3 550 900 чел.\n" + "Площадь страны: 33 846 км²");
                break;
            case "США":
                sendMessage(message.getChatId(), "Страна: США\n" + "Столица: Вашингтон\n" + "Население страны: 331 216 157 чел.\n" + "Площадь страны: 9 519 431 км²");
                break;
            case "Канада":
                sendMessage(message.getChatId(), "Страна: Канада\n" + "Столица: Оттава\n" + "Население страны: 37 602 103 чел.\n" + "Площадь страны: 9 984 670 км²");
                break;
            case "Испания":
                sendMessage(message.getChatId(), "Страна: Испания\n" + "Столица: Мадрид\n" + "Население страны: 47 351 567 чел.\n" + "Площадь страны: 505 990 км²");
                break;
            case "/countries":
                listAllCountries(message.getChatId());
                break;
            default:
                sendMessage(message.getChatId(), "Пожалуйста, для взаимодействия со мной нажмите на интересующую вас страну на клавиатуре.\n"
                        + "Со списком всех стран можно ознакомиться с помощью /countries.");
        }
    }
}
