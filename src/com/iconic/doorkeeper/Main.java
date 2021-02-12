package com.iconic.doorkeeper;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    public static JDA jda;
    public static String prefix = "!";

    public static void main(String[] args) throws LoginException {
        //jda = new JDABuilder(AccountType.BOT).setToken("").buildAsync();
        jda = JDABuilder.createDefault("ODA5ODIzNDc0NDkwOTMzMjk4.YCas4g.5i7m9a6b1H-I4T7ZEU1eA-RL9wk").build();
        jda.getPresence().setActivity(Activity.watching("dir über die Schultern"));

        jda.addEventListener(new Commands());
    }

}
