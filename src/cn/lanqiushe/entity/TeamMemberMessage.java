package cn.lanqiushe.entity;

import java.io.Serializable;

public class TeamMemberMessage implements Serializable {
	private String team_member_head;
	private String team_member_name;
	private String team_member_info;
	private String team_member_role;

	public TeamMemberMessage() {
	}

	public TeamMemberMessage(String team_member_head, String team_member_name,
			String team_member_info, String team_member_role) {
		this.team_member_head = team_member_head;
		this.team_member_name = team_member_name;
		this.team_member_info = team_member_info;
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

	public String getTeam_member_info() {
		return team_member_info;
	}

	public void setTeam_member_info(String team_member_info) {
		this.team_member_info = team_member_info;
	}

	public String getTeam_member_role() {
		return team_member_role;
	}

	public void setTeam_member_role(String team_member_role) {
		this.team_member_role = team_member_role;
	}

}
