package homework.hw3.socks;

public class Sock {
	static enum color {red, green, blue, orange};
	private color myColor;
	
	Sock(color c)
	{
		setMyColor(c);
	}

	public color getMyColor() {
		return myColor;
	}

	public void setMyColor(color myColor) {
		this.myColor = myColor;
	}
	
}