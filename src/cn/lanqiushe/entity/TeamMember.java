package cn.lanqiushe.entity;

public class TeamMember {
	private String team_member_head;
	private String team_member_name; 
	private String team_member_role;
	public TeamMember() { 
	}
	public TeamMember(String team_member_head, String team_member_name,
			String team_member_role) { 
		this.team_member_head = team_member_head;
		this.team_member_name = team_member_name;
		this.team_member_role = team_member_role;
	}
	public String getTeam_member_head() {
		return team_member_head;
	}
	public void setTeam_member_head(String team_member_head) {
		this.team_member_head = team_member_head;
	}
	public String getTeam_member_name() {
		return team_member_name;
	}
	public void setTeam_member_name(String team_member_name) {
		this.team_member_name = team_member_name;
	}
	public String getTeam_member_role() {
		return team_member_role;
	}
	public void setTeam_member_role(String team_member_role) {
		this.team_member_role = team_member_role;
	}
	
}
