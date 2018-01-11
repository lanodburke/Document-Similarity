package ie.gmit.sw;

/**
 * Object that stores a string of words of shingleSize
 * 
 * @author Donal Burke
 * @version 1.0 
 *
 */

public class Shingle {
	private int docId;
	private int hashCode;
	
	public Shingle() {
		super();
	}
	
	/**
	 * Generates a new {@link Shingle} object with given parameters.
	 * 
	 * @param docId		the document ID.
	 * @param hashCode	the hash code.
	 */
	
	public Shingle(int docId, int hashCode) {
		super();
		this.docId = docId;
		this.hashCode = hashCode;
	}
	
	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	
}
