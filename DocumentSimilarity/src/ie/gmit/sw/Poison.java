package ie.gmit.sw;

/**
 * Extends from {@link Shingle}
 * <p>
 * Object that is added to a BlockingQueue to mark that you are moving onto
 * a new file.
 * 
 * @author Donal Burke
 * @version 1.0 
 *
 */

public class Poison extends Shingle{
	/**
	 * Generates a new {@link Poison} object and passes the docId and hashcode to the
	 * {@link Shingle} object.
	 * 
	 * @param docId		the document ID.
	 * @param hashCode	the hash code.
	 */
	public Poison(int docId, int hashCode) {
		super(docId, hashCode);
	}
	
}
