package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

/**
 * Parses given document and generates shingles of a specified size and adds them a {@link BlockingQueue}.
 * 
 * @author Donal Burke
 * @version 1.0 
 *
 */

public class DocumentParser implements Runnable, Parserator{
	private String file;
	private int shingleSize;
	private BlockingQueue<Shingle> queue;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;
	
	/**
	 * Instantiates new DocumentParser object with given parameters.
	 * 
	 * @param file			the name of the file.
	 * @param shingleSize	size of a shingle.
	 * @param queue			the {@link BlockingQueue}.
	 * @param docId			the document ID of the file.
	 * 
	 */
	
	public DocumentParser(String file, int shingleSize, BlockingQueue<Shingle> queue, int docId) {
		super();
		this.file = file;
		this.shingleSize = shingleSize;
		this.queue = queue;
		this.docId = docId;
	}
	
	/**
	 * Method that runs when the thread starts.
	 * 
	 */
	public void run() {
		parse();
	}
	
	/**
	 * Reads each line from a file and skips blank lines, converts the line to upper case and splits the
	 * line into words whenever there is a space between characters. 
	 * Words are then passed to the <code>addWordsToBuffer()</code> method. 
	 * Creates a new Shingle object and adds it to a {@link BlockingQueue}.
	 */
	@Override
	public void parse() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine())!= null) {
				if(line.length()>0) {
					String uppercase = line.toUpperCase();
					String words[] = uppercase.split("\\s+");
					addWordsToBuffer(words);
					Shingle s = getNextShingle();
					queue.put(s);
				}
			}
		
			flushBuffer();
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	

	 /**
	  * Adds words to a @{Deque} of type {@LinkedList} 
	  * 
	  * @param words	array of words passed from run method
	  */
	
	private void addWordsToBuffer(String[] words) {
		for (String s: words) {
			buffer.add(s);
		}
	}
	
	/**
	 * Method that generates a new Shingle
	 * 
	 * @return	a new {@link Shingle} with docId and the <code>hashCode()</code> of a string of words of shingleSize.
	 * @see Shingle
	 */
	private Shingle getNextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek()!=null) {
				sb.append(buffer.poll());
				counter++;
			}
			else {
				counter = shingleSize;
			}
			
		}
		
		if(sb.length() > 0) {
			return (new Shingle(docId,sb.toString().hashCode()));
		}
		else {
			return null;
		}
	}
	
	/**
	 * Inserts a {@link Poison} object into the blocking queue.
	 * 
	 * @throws InterruptedException
	 * @see Poison
	 */
	private void flushBuffer() throws InterruptedException {
		while(buffer.size() > 0) {
			Shingle s = getNextShingle();
			if(s != null) {
				queue.put(s);
			}
		}
		queue.put(new Poison(docId, 0));
	}

}
