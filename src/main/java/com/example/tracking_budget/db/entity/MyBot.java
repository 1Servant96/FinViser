package com.example.tracking_budget.db.entity;

import com.example.tracking_budget.db.service.CategoryService;
import com.example.tracking_budget.db.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class MyBot extends TelegramLongPollingBot {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TransactionService transactionService;
    private boolean isAddingCategory = false;

    private final String BOT_TOKEN = "6879291185:AAGPL0eYS6g0m-2UDoT5vhI3AEbmmb_Kvkc";

    @Override
    public String getBotUsername() {
        return "FinViser_Bot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;

    }

    private void askIncomeOrExpense(Long chatId, Double amount) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        InlineKeyboardButton incomeButton = new InlineKeyboardButton();
        incomeButton.setText("Income");
        incomeButton.setCallbackData("Income:" + amount);

        InlineKeyboardButton expenseButton = new InlineKeyboardButton();
        expenseButton.setText("Expense");
        expenseButton.setCallbackData("Expense:" + amount);

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(incomeButton);
        row.add(expenseButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        markup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Is it an Income or Expense?");
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void askForCategory(Long chatId, Double amount, String type) {
        List<Category> categories = categoryService.findAll();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (Category category : categories) {
            InlineKeyboardButton categoryButton = new InlineKeyboardButton();
            categoryButton.setText(category.getCategoryName());
            categoryButton.setCallbackData(type + ":" + category.getCategoryName() + ":" + amount);
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(categoryButton);
            keyboard.add(row);
        }

        markup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Choose a category for the transaction:");
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCategoryResponse(Long chatId, String categoryName, Double amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        Category category = categoryService.findByName(categoryName);
        transaction.setCategory(category);

        transactionService.save(transaction);

        sendMessage(chatId, "Transaction saved! You can add a description or continue with another transaction.");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (isAddingCategory) {
                Category category = new Category();
                category.setCategoryName(messageText);
                categoryService.save(category);
                sendMessage(chatId, "Category added!");
                isAddingCategory = false;
            } else if (messageText.equals("/start")) {
                String welcomeMsg = "Welcome to the Budget Tracker Bot!\n";
                welcomeMsg += "You can add a new category using /addCategory.\n";
                welcomeMsg += "View your budget infographic using /infographic.";
                sendMessage(chatId, welcomeMsg);
            } else if (messageText.equals("/addCategory")) {
                sendMessage(chatId, "Please enter the name of the new category:");
                isAddingCategory = true;
            } else if (messageText.equals("/infographic")) {
                // Fetch user's transactions and generate a summary
                double totalIncome = transactionService.getTotalIncomeForUser(chatId);  // Placeholder method
                double totalExpense = transactionService.getTotalExpenseForUser(chatId); // Placeholder method
                double savings = totalIncome - totalExpense;

                String infographicMsg = "Your Budget Infographic:\n";
                infographicMsg += "Total Income: $" + totalIncome + "\n";
                infographicMsg += "Total Expense: $" + totalExpense + "\n";
                infographicMsg += "Savings: $" + savings;

                sendMessage(chatId, infographicMsg);
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            // Check if the message is a numeric value
            if (isNumeric(messageText)) {
                double amount = Double.parseDouble(messageText);
                // Store the amount temporarily (you might want to use a cache or database)

                // Ask user if it's an income or expense
                SendMessage message = new SendMessage();
                        message.setChatId(update.getMessage().getChatId());
                        message.setText("Is this an income or an expense? Reply with 'Income' or 'Expense'.");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                // ... subsequent logic ...

            }
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        String welcomeMessage = "Welcome! Please input the amount for your transaction.";
        sendMessage(chatId, welcomeMessage);
    }

    private void handleAmount(Message message) {
        try {
            Double amount = Double.parseDouble(message.getText());
            askIncomeOrExpense(message.getChatId(), amount);
        } catch (NumberFormatException e) {
            sendMessage(message.getChatId(), "Please enter a valid amount.");
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
    



