package com.iconic.doorkeeper.model;

/*
This class is a kind of Database, to store data and to make a better overview and accessibility
IMPORTANT: It stores data ONLY during runtime. As the running process is stopped, all data is gone!
 */

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private int current_active_member;
    private int amount_of_teams;
    private List<Team> team_list;
    private List<Member> moderators_list;

    public Model(){
        team_list = new ArrayList<Team>();
        moderators_list = new ArrayList<Member>();
    }


    public void create_team(String team_name,int index, List<Member> new_team_members){
        Team team = new Team(team_name,index,new_team_members);
        team_list.add(team);
    }

    public void delete_all_teams(){
        team_list.clear();
    }

    public String show_teams(){
        StringBuilder all_teams= new StringBuilder();
        all_teams.append("All current teams:\n");

        if(team_list.size()==0){
            return "No Teams are set yet.";
        }
        else{
            for (Team team:team_list) {
                all_teams.append(team.get_team_members()).append("\n");
            }
        }
        return all_teams.toString();

    }

    @Nullable
    public Team get_team_members_by_team_name(String name){
        for (Team team : team_list) {
            if (team.getTeam_name().equalsIgnoreCase(name)) {
                return team;
            }
        }
        // null needs to be checked by the method who calls this method
        return null;
    }

    public List<Team> get_all_teams(){
        return team_list;
    }

    public boolean isTeam_list_empty(){
        return team_list.isEmpty();
    }

    public VoiceChannel get_team_channel(Team team){
        return team.getTeam_channel();
    }

    public void addModerator(Member member){
        moderators_list.add(member);
    }
    public void delete_all_Moderators(){
        moderators_list.clear();
    }

    public List<Member> getModerators_list(){
        return moderators_list;
    }

    public String getModeratorNames(){
        StringBuilder all_moderators= new StringBuilder();
        all_moderators.append("**Moderator(s):**\n");

        if(moderators_list.size()==0){
            return "No moderators are set yet.";
        }
        else{
            for (Member moderator:moderators_list) {
                all_moderators.append(moderator.getUser().getName()).append("\n");
            }
        }
        return all_moderators.toString();
    }

}
