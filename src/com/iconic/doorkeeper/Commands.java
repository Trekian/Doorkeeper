package com.iconic.doorkeeper;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Commands extends ListenerAdapter {

    // Distribute the command to the right Method
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        System.out.println("Ich bin hier");

        if(args[0].equalsIgnoreCase(Main.prefix+"show_members")){
            System.out.println("Ich bin hier2");
            show_active_members(event);
        }
        System.out.println("Ich bin hier3");
    }


    private void show_active_members(GuildMessageReceivedEvent event){
        int current_amount = 0;
        StringBuilder online_members= new StringBuilder();

        List<VoiceChannel> voice_list = event.getGuild().getVoiceChannels();

        System.out.println("Amount Voicechannels: "+voice_list.size());

        for (VoiceChannel channel: voice_list) {

            System.out.println("Name von Voicechannels: "+channel.getName());
            int channel_size = channel.getMembers().size();
            current_amount += channel_size;
            for(int i = 0; i < channel_size;i++){
                online_members.append(channel.getMembers().get(i).getUser().getName()).append(" ");
            }

        }
        if(current_amount == 0){
            event.getChannel().sendMessage("Current amount of active Members: "+current_amount+ "\n" +
                    "Even YOU are not in a VoiceChannel :(")
                    .queue();
        }else {
            event.getChannel().sendMessage("Current amount of active Members: "+current_amount+ "\n" +
                    "Active Members: "+ online_members.toString())
                    .queue();
        }


    }
}
