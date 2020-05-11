package model;

public class Card {
	
	private String name;
	private String set;
	private String price;
	
	public Card() {
	}
	
	public Card(String name, String set, String price) {
		super();
		this.name = name;
		this.set = set;
		this.price = price;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSet() {
		return set;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
