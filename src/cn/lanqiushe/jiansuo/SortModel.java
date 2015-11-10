package cn.lanqiushe.jiansuo;

public class SortModel {
	private String user_head;
	private String name; // 显示的数据
	private String sortLetters; // 显示数据拼音的首字母

	public SortModel() {
	}

	public SortModel(String user_head, String name, String sortLetters) {
		this.user_head = user_head;
		this.name = name;
		this.sortLetters = sortLetters;
	}

	public String getUser_head() {
		return user_head;
	}

	public void setUser_head(String user_head) {
		this.user_head = user_head;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
