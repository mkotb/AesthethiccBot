package com.mkotb.aesthethicc;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import com.jtelegram.api.events.inline.InlineQueryEvent;
import com.jtelegram.api.inline.input.InputTextMessageContent;
import com.jtelegram.api.inline.result.InlineResultArticle;
import com.jtelegram.api.requests.inline.AnswerInlineQuery;
import com.jtelegram.api.requests.message.framework.ParseMode;
import com.jtelegram.api.update.PollingUpdateProvider;
import com.mkotb.aesthethicc.transform.ChillAesthethicc;
import com.mkotb.aesthethicc.transform.ClapperAesthethicc;
import com.mkotb.aesthethicc.transform.ClassicAesthethicc;
import com.mkotb.aesthethicc.transform.YellAesthethicc;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AesthethiccBot {
    private TelegramBot bot;
    private List<TextTransformer> transformers = new ArrayList<>();

    private AesthethiccBot(String apiKey) {
        transformers.add(new ClassicAesthethicc());
        transformers.add(new ClapperAesthethicc());
        transformers.add(new ChillAesthethicc());
        transformers.add(new YellAesthethicc());

        TelegramBotRegistry
                .builder()
                .updateProvider(new PollingUpdateProvider())
                .build()
                .registerBot(apiKey, (bot, error) -> {
                    if (error != null) {
                        System.out.println("Unable to start bot! Shutting down..");
                        error.printStackTrace();

                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException ignored) {
                            return;
                        }

                        System.exit(-1);
                        return;
                    }

                    System.out.println("Logged in as @" + bot.getBotInfo().getUsername());
                    this.bot = bot;

                    bot.getEventRegistry().registerEvent(InlineQueryEvent.class, this::handleIncomingQuery);
                });
    }

    public static void main(String[] args) {
        new AesthethiccBot(System.getenv("TELEGRAM_KEY"));
    }

    private void handleIncomingQuery(InlineQueryEvent event) {
        String input = event.getQuery().getQuery();

        if (input.length() <= 1) {
            return;
        }

        AnswerInlineQuery.AnswerInlineQueryBuilder builder = AnswerInlineQuery.builder();

        for (int i = 0; i < transformers.size(); i++) {
            TextTransformer transformer = transformers.get(i);
            String output = transformer.transform(input);

            builder.addResult(InlineResultArticle.builder()
                    .id(String.valueOf(i))
                    .title(transformer.getTitle())
                    .description(output)
                    .inputMessageContent(
                            InputTextMessageContent.builder()
                                    .messageText(output)
                                    .parseMode(ParseMode.MARKDOWN)
                                    .build()
                    )
                    .build());
        }

        bot.perform(builder.queryId(event.getQuery().getId())
                .callback(() -> {})
                .errorHandler((c) -> {
                    System.out.println("Didn't work because " + c.getDescription());
                })
                .build());
    }
}
