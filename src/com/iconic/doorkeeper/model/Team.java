package com.iconic.doorkeeper.model;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;

/*
This Team class will help to organize a bunch of Teams and make them more customizable.
 */

public class Team {

    private String team_name;
    private List<Member> members;
    private int index;
    private VoiceChannel team_channel;

    public Team(String team_name, int index, List<Member> members){
        this.team_name = team_name;
        this.index = index;
        this.members = members;
    }


    public String getTeam_name() {
        return team_name;
    }

    public String get_team_members(){
        StringBuilder all_members_by_name = new StringBuilder();
        all_members_by_name.append("[").append(index).append("]").append(team_name).append(": + ");
        for (Member member: members) {
            all_members_by_name.append(member.getUser().getName()).append(" +");
        }
        return all_members_by_name.toString();
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void addMember(Member member){
        members.add(member);
    }

    public void removeMember(Member member){
        members.remove(member);
    }

    public boolean isMember(Member member){
        return members.contains(member);
    }

    public VoiceChannel getTeam_channel(){
        return team_channel;
    }

    public void setTeam_channel(VoiceChannel team_channel){
        this.team_channel = team_channel;
    }
}
