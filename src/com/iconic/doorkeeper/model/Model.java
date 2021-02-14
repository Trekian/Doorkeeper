package com.iconic.doorkeeper.model;

/*
This class is a kind of Database, to store data and to make a better overview and accessibility
IMPORTANT: It stores data ONLY during runtime. As the running process is stopped, all data is gone!
 */

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private int current_active_member;
    private int amount_of_teams;
    private List<Team> team_list;

    public Model(){
        team_list = new ArrayList<Team>();
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

    public Team get_team_members_by_team_name(String name){
        for(int i=0 ; i<team_list.size();i++){
            if (team_list.get(i).getTeam_name().equalsIgnoreCase(name)){
                return team_list.get(i);
            }
        }
        // null needs to be checked by the method who calls this method
        return null;
    }

}
