package com.iconic.doorkeeper;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Commands extends ListenerAdapter {

    private static Model model;

    public Commands(){
        model = new Model();
    }

    // Distribute the command to the right Method
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if(args[0].equalsIgnoreCase(Main.prefix+"show_members")){
            show_active_members(event);
        }else if (args[0].equalsIgnoreCase(Main.prefix+"create_teams")){

            if(args[1].equalsIgnoreCase("random")){
                try{
                    int amount_of_teams = Integer.parseInt(args[2]);
                    create_teams(event,"random", null);

                }catch (Exception e){
                    event.getChannel().sendMessage("No valid number entered. Use '!create_teams ?' for more information")
                            .queue();
                }
            }

        }else if(args[0].equalsIgnoreCase(Main.prefix+"increase")){
            model.increase_Just_test();
            event.getChannel().sendMessage("just_test was "+ (model.getJust_test()-1) + " and is now" + model.getJust_test()  )
                    .queue();
        }
    }


    // Shows and count all User, who are in a VoiceChannel
    private void show_active_members(GuildMessageReceivedEvent event){
        StringBuilder online_members= new StringBuilder();

        List<VoiceChannel> voice_list = event.getGuild().getVoiceChannels();
        List<Member> members_list = get_active_members(event);
        int current_amount = members_list.size();

        System.out.println("Amount Voicechannels: "+voice_list.size());

        for (Member member: members_list) {
            online_members.append(member.getUser().getName()).append(" ");
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


    private void create_teams(GuildMessageReceivedEvent event, String team, List<Member> member){

    }

    // Get all users who are CURRENTLY in a VoiceChannel.
    private List<Member> get_active_members(GuildMessageReceivedEvent event){

        List<VoiceChannel> voice_list = event.getGuild().getVoiceChannels();
        List<Member> members_list = new ArrayList<Member>();

        System.out.println("Amount Voicechannels: "+voice_list.size());

        for (VoiceChannel channel: voice_list) {
            System.out.println("Name von Voicechannels: "+channel.getName());
            int channel_size = channel.getMembers().size();
            for(int i = 0; i < channel_size;i++){
                members_list.addAll(channel.getMembers());
            }
        }

        return members_list;

    }
}
