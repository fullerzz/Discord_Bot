package com.github.fullerzz;

import java.time.*;
import java.net.URL;
import java.net.MalformedURLException;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        opggManager man = new opggManager();
        man.loadData();
        RequestManager rqMan = new RequestManager();
        rqMan.loadData();

        String token = "Place token here";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // Creates invite link for the bot
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());

        // Listener to respond to !ping
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
                System.out.println("Pong! sent.");
            }
        });

        // Listener to respond to !hi
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!hi")) {
                String user = event.getMessageAuthor().getDisplayName();
                event.getChannel().sendMessage("Hello there " + user + "!");
                event.getChannel().sendMessage("Let me know if I can be of assistance.");
                System.out.println("!hi command responded to.");
            }
        });

        // Listener for when new members join the server
        api.addServerMemberJoinListener(event -> {
            MessageBuilder msg = new MessageBuilder();
            msg.append("Welcome to the server " + event.getServer().getName() + "!");
            msg.send(event.getUser());
            System.out.println("Greeting message sent to new server member.");
        });

        // !tierList command
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!tierList")) {
                String url = "https://u.gg/lol/tier-list";
                event.getChannel().sendMessage("Here are the most up to date tier rankings: " + url);
                System.out.println("!tierList request completed.");
            }
        });


        // !opgg command
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("!opgg")){
                String target = event.getMessageAuthor().getDiscriminatedName();
                System.out.println("!opgg command received.");

                if (man.search(target)) { // If user already exists
                    event.getChannel().sendMessage(man.getLink(target));
                    System.out.println("!opgg request completed.");
                }
                else { // If user does not exist yet
                    String namePrompt = "Looks like you're not in the system yet! Please reply ";
                    namePrompt += "with: \n!opgg 'your summoner name'";
                    event.getChannel().sendMessage(namePrompt);
                    System.out.println("!opgg request completed.");
                }

            }
        });

        // !opgg command set up for new users
        api.addMessageCreateListener(event -> {
            boolean userExists = man.search(event.getMessageAuthor().getDiscriminatedName());
            boolean didISendIt = false;

            if (event.getMessageAuthor().getDisplayName().equals("iBot"))
                didISendIt = true;

            if ((event.getMessageContent().contains("!opgg ")) && (!userExists) && (!didISendIt)){
                String sName = event.getMessageContent();
                String[] parts = sName.split(" ", 2);

                OPGG user = new OPGG(event.getMessageAuthor().getDiscriminatedName(), parts[1]);
                man.addObject(user);
                man.populateFile();

                event.getChannel().sendMessage("Congrats! Your !opgg command will now work properly.");
                System.out.println("!opgg request completed. New OPGG object created for user.");
            }
        });

        // !memberAge command to see how long the user account has existed
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!memberAge")){
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(Locale.US)
                        .withZone(ZoneId.systemDefault());

                Instant joinInst = event.getMessageAuthor().getCreationTimestamp();
                String join = formatter.format(joinInst);

                event.getChannel().sendMessage(event.getMessageAuthor().getDisplayName() + " joined " + join);
                System.out.println("!memberAge request completed.");
            }
        });

        // !request command to store user requests for new features
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().startsWith("!request")){

                if (event.getMessageContent().equals("!request")){
                    event.getChannel().sendMessage("Looks like you didn't submit a request.");
                    event.getChannel().sendMessage("Type your request directly following !request");
                    System.out.println("Incorrect !request command received and responded to.");
                }
                else {
                    String name = event.getMessageAuthor().getDisplayName();
                    String content = event.getMessageContent();
                    Request usrRequest = new Request(name, content);

                    rqMan.addRequest(usrRequest);
                    rqMan.populateFile();

                    event.getChannel().sendMessage("Your request has been recorded. Thanks for the input!");
                    System.out.println("!request command received and completed.");
                }
            }
        });

        // !help command to list all available commands
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equals("!help")){
                event.getChannel().sendMessage("List of commands: !ping !hi !tierList !opgg !memberAge !request");
                System.out.println("!help request completed.");
            }
        });

    }

}
