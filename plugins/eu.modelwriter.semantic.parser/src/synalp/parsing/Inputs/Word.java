package synalp.parsing.Inputs;


public class Word {

	private int index;
	
	private String token;
	
	
	public Word(int index, String token) {
		this.index = index;
		this.token = token;
	}
	
	
	public String toString() {
		return token+"("+index+")";
	}
	
	
	public String getToken() {
		return token;
	}
	
	
	public int getIndex() {
		return index;
	}
	
}
