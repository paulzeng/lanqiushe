package cn.lanqiushe.jiansuo;

public class SortModel {
	private String user_head;
	private String name; // ��ʾ������
	private String sortLetters; // ��ʾ����ƴ��������ĸ

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
