import java.io.Serializable;

/**
 * 
 * @author pwamsley
 * class implements Serializable so this object can be stored in a file
 */
@SuppressWarnings("serial")
public class Score implements Serializable {

	/**
	 * value of the score
	 */
	private int score; 

	/**
	 * @param score value
	 */
	public Score(int s) {
		score = s; 
	}
	//i dont think i ever use this but good to have i guess
	public Score() {
		this(0); 
	}
	/**
	 * @return the value of this score
	 */
	public int getVal() {
		return score; 
	}
	/**
	 * @return a String of the value of this score
	 */
	@Override
	public String toString() {
		return "" + score; 
	}
}
