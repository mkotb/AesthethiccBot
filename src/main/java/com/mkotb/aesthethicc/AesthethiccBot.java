package com.mkotb.aesthethicc;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import com.jtelegram.api.events.inline.InlineQueryEvent;
import com.jtelegram.api.inline.InlineQuery;
import com.jtelegram.api.inline.input.InputTextMessageContent;
import com.jtelegram.api.inline.result.InlineResultArticle;
import com.jtelegram.api.requests.inline.AnswerInlineQuery;
import com.jtelegram.api.requests.message.framework.ParseMode;
import com.jtelegram.api.update.PollingUpdateProvider;
import com.mkotb.aesthethicc.transform.*;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class AesthethiccBot {
    private final ExecutorService service = Executors.newWorkStealingPool(3);
    private TelegramBot bot;
    private RestHighLevelClient elasticClient;
    private List<TextTransformer> transformers = new ArrayList<>();

    private AesthethiccBot(String apiKey) {
        transformers.add(new ClassicAesthethicc());
        transformers.add(new ClapperAesthethicc());
        transformers.add(new ChillAesthethicc());
        transformers.add(new YellAesthethicc());
        transformers.add(new MockingAesthethicc());

        elasticClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("elasticsearch", 9200, "http")
                )
        );

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

    private void logQuery(InlineQuery query) {
        Date time = new Date(System.currentTimeMillis());

        service.execute(() -> {
            try {
                IndexRequest request = new IndexRequest("aesthethicc-queries", "doc");
                request.source(
                        "userId", query.getFrom().getId(),
                        "query", query.getQuery(),
                        "queryDate", time
                );
                elasticClient.index(request, RequestOptions.DEFAULT);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void handleIncomingQuery(InlineQueryEvent event) {
        service.execute(() -> {
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

            logQuery(event.getQuery());

            bot.perform(builder.queryId(event.getQuery().getId())
                    .callback(() -> {})
                    .errorHandler((c) -> {
                        System.out.println("Didn't work because " + c.getDescription());
                    })
                    .build());
        });
    }
}
