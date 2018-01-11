package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Creates a {@link Thread} for each document and instantiates a new {@link DocumentParser} object 
 * and passes the given parameters.
 * Creates a {@link Thread} and instantiates a {@link Consumer} object on the thread.
 * The threads are joined together so they can run concurrently.
 * 
 * @author Donal Burke
 * @version 1.0 
 *
 */

public class Launcher {
	
	private BlockingQueue<Shingle> queue = new LinkedBlockingQueue<Shingle>(100);
	private Consumer consumer;
	/**
	 * Launch method that creates the threads.
	 * 
	 * @param f1			name of the first file.
	 * @param f2			name of the second file.
	 * @param shingleSize	the size of a shingle.
	 * @param k				number of min hashes.
	 * @param poolSize		size of the thread pool.
	 * @throws InterruptedException throws an error when thread is interrupted.
	 */
	public void Launch(String f1, String f2, int shingleSize, int k, int poolSize) throws InterruptedException {
		Thread t1 = new Thread(new DocumentParser(f1, shingleSize, queue, 1), "T1");
		Thread t2 = new Thread(new DocumentParser(f2, shingleSize, queue, 2), "T2");
		Thread t3 = new Thread(consumer = new Consumer(queue, k, poolSize), "T3");
		
		t1.start();
		t2.start();
		t3.start();
		
		t1.join();
		t2.join();
		t3.join();
		System.out.println("J: " + consumer.getJaccardDist());
	}
}
