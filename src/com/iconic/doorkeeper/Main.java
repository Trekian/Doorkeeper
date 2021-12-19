package com.iconic.doorkeeper;

import com.iconic.doorkeeper.eventlistener.OnMessageReceived;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static JDA jda;
    public static String prefix = "!";

    public static void main(String[] args) throws LoginException {


        String the_token = get_token();
        jda = JDABuilder.createDefault(the_token).build();
        jda.getPresence().setActivity(Activity.watching("dir über die Schultern"));

        jda.addEventListener(new OnMessageReceived());
    }

    private static String get_token(){

        try{

            Path filename = Path.of("src/com/iconic/doorkeeper/bot_token.txt");
            File file = filename.toFile();
            Scanner sc = new Scanner(file);
            //Skipping first line as it is only a comment
            sc.nextLine();
            return sc.nextLine();


        }catch (Exception e){
            System.out.println("Something is wrong with the Token. Maybe expired, or a new one was generated");
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

}
