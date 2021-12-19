package com.iconic.doorkeeper.eventlistener;

import com.iconic.doorkeeper.Main;
import com.iconic.doorkeeper.model.Model;

import com.iconic.doorkeeper.model.Team;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;


/*
Current Methods:
        onGuildMessageReceived (initial Call)
           |-> reset_all()                      (MessageReceivedEvent event)
           |-> show_active_members              (MessageReceivedEvent event)
           |-> create_random_teams              (MessageReceivedEvent event, int amount_teams)
           |-> clear_teams                      (MessageReceivedEvent event)
           |-> get_active_members               (MessageReceivedEvent event)
           |-> get_random_noun()
           |-> create_team_voice_channels       (MessageReceivedEvent event)
           |-> remove_category                  (MessageReceivedEvent event)
           |-> set_moderators                   (MessageReceivedEvent event, String[] names)
           |-> getKneipenQuizID                 (MessageReceivedEvent event)
           |-> move_teams_to_teamchannel        (MessageReceivedEvent event)
           |-> move_teams_to_stage              (MessageReceivedEvent event)
           |-> remove_old_channels              (MessageReceivedEvent event)
           |-> remove_category                  (MessageReceivedEvent event)
           |-> add_member_to_team               (MessageReceivedEvent event, String name, String string_team_index)

 */

public class OnMessageReceived extends ListenerAdapter {

    private static Model model;

    public OnMessageReceived() {
        model = new Model();
    }

    // Distribute the command to the right Method
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Main.prefix + "show_members")) {
            show_active_members(event);

        } else if (args[0].equalsIgnoreCase(Main.prefix + "create_random_teams")) {

            if (get_active_members(event).size() == 0){
                event.getChannel().sendMessage("Only people who are in voicechannels will be eligible.")
                        .queue();
                return;
            }

            try {
                int amount_of_teams = Integer.parseInt(args[1]);
                create_random_teams(event, amount_of_teams);

            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("No valid number entered. Please choose a number for the amount of teams you want to create.")
                        .queue();
            } catch (Exception e){
                System.out.println(Arrays.toString(e.getStackTrace()));
            }


        } else if (args[0].equalsIgnoreCase(Main.prefix + "clear_teams")) {
            clear_teams();

        } else if (args[0].equalsIgnoreCase(Main.prefix + "moderator")){
            if( args.length > 1){
                set_moderators(event,args);
            }else {
                event.getChannel().sendMessage("Please state at least one username who is in a voicechannel")
                        .queue();
            }

        } else if( args[0].equalsIgnoreCase(Main.prefix + "initialise")){

            System.out.println("Test");
            if (model.get_all_teams().size() != 0){
                // Creats a new Category with 1 "presenter" voicechannel and random teams and their team channels. Also creates a new C
                //create_random_teams(event,1);
                try {
                    create_team_voice_channels(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                event.getChannel().sendMessage("No teams are set up yet.")
                        .queue();
            }



        }else if( args[0].equalsIgnoreCase(Main.prefix + "reset")){
            // Just for test various things
            reset_all(event);


        } else if(args[0].equalsIgnoreCase(Main.prefix + "spread")){
            move_teams_to_teamchannel(event);
        }else if(args[0].equalsIgnoreCase(Main.prefix + "drag")){
            move_teams_to_stage(event);
        }else if(args[0].equalsIgnoreCase(Main.prefix + "add_member")){
            if(args.length > 2){
                add_member_to_team(event, args[1], args[2]);
            }else {
                event.getChannel().sendMessage("Usage: '!add_member <Name> <Index of Team>'")
                        .queue();
            }

        }else if(args[0].equalsIgnoreCase(Main.prefix + "ablauf")){
            event.getChannel().sendMessage("1. Set moderators: !moderator <Names> \n" +
                                                "2. Create random teams: !create_random_teams <Number of Teams>\n" +
                                                "3. Create teams channels : !initialise \n" +
                                                "4. Move teams to Stage[0]: !drag \n" +
                                                "5. Move teams to their channels: !spread \n" +
                                                "6. Clear all teams/channels: !reset")
                    .queue();
        }else if (args[0].equalsIgnoreCase(Main.prefix + "help")){
            event.getChannel().sendMessage("Find more information here: https://github.com/Trekian/Doorkeeper")
                    .queue();
        }
    }

    private void reset_all(MessageReceivedEvent event) {

        // Order is important
        clear_teams();
        model.delete_all_Moderators();
        remove_category(event);

        event.getChannel().sendMessage("All Teams and Channels have been removed.")
                .queue();
    }

    private void set_moderators(MessageReceivedEvent event, String[] names){
        model.delete_all_Moderators();
        List<Member> memberList = get_active_members(event);

        // i=i because 0 will be the !moderators
        for (int i = 1; i< names.length; i++){
            boolean found_member=false;
            for (Member member : memberList) {
                if(member.getUser().getName().equalsIgnoreCase(names[i])){
                    model.addModerator(member);
                    found_member=true;
                    break;
                }
            }

            if (!found_member){
                event.getChannel().sendMessage("At least one name was not found. No moderators are set yet")
                        .queue();
                model.delete_all_Moderators();
                return;
            }
        }
        event.getChannel().sendMessage("Moderator(s): "+ model.getModeratorNames())
                .queue();
    }


    /**
     * Shows and count all User, who are in a VoiceChannel
     *
     * @param event The main object to interact with the Discord server methods.
     */
    private void show_active_members(MessageReceivedEvent event) {
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


    // TODO: Will be later feature to create own teams.
    private void create_teams(MessageReceivedEvent event, String team, int amount_teams, List<Member> member) {

    }

    private void add_member_to_team(MessageReceivedEvent event, String name, String string_team_index){
        int team_index = Integer.parseInt(string_team_index);

        Member member = get_member_by_name(event, name);
        if(member == null){
            event.getChannel().sendMessage("Name not found")
                    .queue();
            return;
        }
        for (Team team :model.get_all_teams()) {
            if(team.getIndex() == team_index){
                team.addMember(member);
                return;
            }
        }
        event.getChannel().sendMessage("Team Index not found")
                .queue();



    }

    private Member get_member_by_name(MessageReceivedEvent event, String name){

        for (Member member: get_active_members(event)) {
            if (member.getUser().getName().equalsIgnoreCase(name)){
                return member;
            }
        }
        return null;
    }

    private void create_random_teams(MessageReceivedEvent event, int amount_teams) {
        clear_teams();
        List<Member> members = get_active_members(event);
        Collections.shuffle(members);
        int amount_members = members.size();
        model.delete_all_teams();

        if (amount_members == 0){
            event.getChannel().sendMessage("No active members found. Join a voicechannel, to be active :)")
                    .queue();
            return;
        }else if (amount_members < amount_teams) {
            event.getChannel().sendMessage("Only " + amount_members + " Teams are created, because there are less people than teams ordered")
                    .queue();
            amount_teams = amount_members;
        }
        //Minimun amount of members for each team
        int amount_of_each_team = amount_teams / amount_members;
        //Remaining members, because "amount_team % amount_members" could be !=0.
        int remaining_members = amount_members - ( amount_teams * amount_of_each_team);
        // The pointer of the List of Members
        int index=0;

        // Creating the amount of teams and add active members to each team.
        for (int i = 1; i <= amount_teams; i++){

            List<Member> new_created_team = new ArrayList<>();
            int remaining = 0;

            // Adding one remaining member to this team
            if(remaining_members > 0){
                remaining =1;
                remaining_members--;
            }
            for(int j = 0; j < (amount_of_each_team + remaining); j++){
                new_created_team.add(members.get(index));
                index++;
            }
            model.create_team(get_random_noun()+"-"+get_random_noun(), i ,new_created_team);

        }

        event.getChannel().sendMessage(model.show_teams())
                .queue();


    }

    /**
     * First it will create a new Category of VoiceChannel and adds for each team 1 channel and one additional Stage Voicechannel
     *
     * @param event The main object to interact with the Discord server methods.
     */
    private void create_team_voice_channels(MessageReceivedEvent event) throws InterruptedException {
        if(!model.isTeam_list_empty()){
            String category_name = "Kneipen Quiz";
            event.getGuild().createCategory(category_name).queue();
            TimeUnit.SECONDS.sleep(2);
            long category_id = getKneipenQuizID(event);
            event.getGuild().getCategoryById(category_id).createVoiceChannel("[0] Stage").queue();
            TimeUnit.SECONDS.sleep(2);
            model.setStage_channel(event.getGuild().getVoiceChannelById(getIDofVoiceChannel(event, "[0] Stage")));

            for (Team team : model.get_all_teams()){
                String channel_name = "["+team.getIndex()+"] "+team.getTeam_name();
                event.getGuild().getCategoryById(category_id).createVoiceChannel(channel_name).queue();
                TimeUnit.SECONDS.sleep(2);

                team.setTeam_channel(event.getGuild().getVoiceChannelById(getIDofVoiceChannel(event, channel_name)));
                System.out.println(team.getTeam_channel().toString());
            }
            TimeUnit.SECONDS.sleep(2);


        }else {
            event.getChannel().sendMessage("No teams created yet.")
                    .queue();
        }

    }

    // Get the ID of the new Category, where all Voicechannels are in
    private long getKneipenQuizID(MessageReceivedEvent event){

        List<Category> categories = event.getGuild().getCategories();
        for (Category category: categories) {
            if(category.getName().contains("Kneipen Quiz")){
                return category.getIdLong();
            }
        }
        return -1;
    }

    private long getIDofVoiceChannel(MessageReceivedEvent event, String ChannelName){
         return event.getGuild().getVoiceChannelsByName(ChannelName,true).get(0).getIdLong();
    }


    private void move_teams_to_teamchannel(MessageReceivedEvent event){

        if(!model.isTeam_list_empty())

        for (Team team: model.get_all_teams()) {
            for (Member member:team.getMembers()) {
                event.getGuild().moveVoiceMember(member,team.getTeam_channel()).queue();
            }
        }
    }

    /**
     * Will move the teams back to "Stage [0]"
     *
     * @param event The main object to interact with the Discord server methods.
     */
    private void move_teams_to_stage(MessageReceivedEvent event){

        if(!model.isTeam_list_empty())

            for (Team team: model.get_all_teams()) {
                for (Member member:team.getMembers()) {
                    event.getGuild().moveVoiceMember(member, model.getStage_channel()).queue();
                }
            }

    }


    /*
    This will remove all already created team voice-channels
     */
    private void remove_old_channels(MessageReceivedEvent event){

        if (event.getGuild().getCategoryById(getKneipenQuizID(event)) != null){
            List<VoiceChannel> channels = Objects.requireNonNull(event.getGuild().getCategoryById(getKneipenQuizID(event))).getVoiceChannels();

            for (VoiceChannel voice: channels) {
                voice.delete().queue();
            }

        }
    }

    private void remove_category(MessageReceivedEvent event){
        remove_old_channels(event);
        try {
            event.getGuild().getCategoryById(getKneipenQuizID(event)).delete().queue();
        }catch (Exception e){
            System.out.println("Category is already deleted");
        }


    }

    private void clear_teams(){
        model.delete_all_teams();
    }


    /**
     * Get all users who are CURRENTLY in a VoiceChannel EXCEPT the moderators!
     *
     * @param event The main object to interact with the Discord server methods.
     * @return List<Member>
     */
    private List<Member> get_active_members(MessageReceivedEvent event) {

        List<VoiceChannel> voice_list = event.getGuild().getVoiceChannels();
        List<Member> members_list = new ArrayList<>();


        for (VoiceChannel channel : voice_list) {
            int channel_size = channel.getMembers().size();

            for (int i = 0; i < channel_size; i++) {
                members_list.addAll(channel.getMembers());
            }
        }
        members_list = new ArrayList<Member>(new HashSet<Member>(members_list));
        // Removing the moderators from the list
        members_list.removeAll(model.getModerators_list());

        return members_list;

    }

    private String get_random_noun() {
        try {
            String random_noun ="";
            URL path = OnMessageReceived.class.getResource("nouns.txt");
            File file = new File(path.getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            Random random = new Random();
            //6801 nouns are in the list, the 6802 is EXCLUDED!
            int random_line = random.nextInt(6802);
            for (int i = 0; i <= random_line; i++) {
                random_noun = br.readLine();
                if(i == random_line){
                    return random_noun;
                }

            }

        } catch (Exception e) {
            System.out.println("The nouns.txt could not read properly");
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return null;
    }
}
