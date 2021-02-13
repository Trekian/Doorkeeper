package com.iconic.doorkeeper;

import com.iconic.doorkeeper.eventlistener.OnMessageReceived;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;

public class Main {
    public static JDA jda;
    public static String prefix = "!";

    public static void main(String[] args) throws LoginException {


        jda = JDABuilder.createDefault(get_token()).build();
        jda.getPresence().setActivity(Activity.watching("dir über die Schultern"));

        jda.addEventListener(new OnMessageReceived());
    }

    private static String get_token(){

        try{
            URL path = Main.class.getResource("bot_token.txt");
            File file = new File(path.getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null){
                if (line.startsWith("token")){
                    int start_index = line.indexOf('=')+1;
                    return line.substring(start_index);
                }
            }

        }catch (Exception e){
            System.out.println("Something is wrong with the Token. Maybe expired, or a new one was generated");
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return null;

    }

}
