package com.iconic.doorkeeper.eventlistener;

import com.iconic.doorkeeper.Main;
import com.iconic.doorkeeper.model.Model;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnMessageReceived extends ListenerAdapter {

    private static Model model;

    public OnMessageReceived() {
        model = new Model();
    }

    // Distribute the command to the right Method
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Main.prefix + "show_members")) {
            show_active_members(event);

        } else if (args[0].equalsIgnoreCase(Main.prefix + "create_random_teams")) {


            try {
                int amount_of_teams = Integer.parseInt(args[1]);
                create_random_teams(event,amount_of_teams);

            } catch (Exception e) {
                event.getChannel().sendMessage("No valid number entered. Use '!create_teams ?' for more information")
                        .queue();
            }


        } else if (args[0].equalsIgnoreCase(Main.prefix + "increase")) {
            model.increase_Just_test();
            event.getChannel().sendMessage("just_test was " + (model.getJust_test() - 1) + " and is now" + model.getJust_test())
                    .queue();
        }
    }


    // Shows and count all User, who are in a VoiceChannel
    private void show_active_members(GuildMessageReceivedEvent event) {
        StringBuilder online_members = new StringBuilder();

        List<VoiceChannel> voice_list = event.getGuild().getVoiceChannels();
        List<Member> members_list = get_active_members(event);
        int current_amount = members_list.size();

        System.out.println("Amount Voicechannels: " + voice_list.size());

        for (Member member : members_list) {
            online_members.append(member.getUser().getName()).append(" ");
        }


        if (current_amount == 0) {
            event.getChannel().sendMessage("Current amount of active Members: " + current_amount + "\n" +
                    "Even YOU are not in a VoiceChannel :(")
                    .queue();
        } else {
            event.getChannel().sendMessage("Current amount of active Members: " + current_amount + "\n" +
                    "Active Members: " + online_members.toString())
                    .queue();
        }
    }


    private void create_teams(GuildMessageReceivedEvent event, String team, int amount_teams, List<Member> member) {


    }

    private void create_random_teams(GuildMessageReceivedEvent event, int amount_teams){
        List<Member> members = get_active_members(event);
        int amount_members = members.size();

        if(amount_members < amount_teams){
            event.getChannel().sendMessage("Only "+amount_members+ " Teams are created, because there are less people than teams ordered")
                    .queue();
            amount_teams = amount_members;
        }




    }



    // Get all users who are CURRENTLY in a VoiceChannel.
    private List<Member> get_active_members(GuildMessageReceivedEvent event) {

        List<VoiceChannel> voice_list = event.getGuild().getVoiceChannels();
        List<Member> members_list = new ArrayList<Member>();

        System.out.println("Amount Voicechannels: " + voice_list.size());

        for (VoiceChannel channel : voice_list) {
            System.out.println("Name von Voicechannels: " + channel.getName());
            int channel_size = channel.getMembers().size();
            for (int i = 0; i < channel_size; i++) {
                members_list.addAll(channel.getMembers());
            }
        }

        return members_list;

    }
}
