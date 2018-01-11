package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.Poison;

/**
 * Consumer consumes shingles off of the blocking queue and computes the jaccard index of the Union between the two files.
 * 
 * @author Donal Burke
 * @version 1.0 
 *
 */

public class Consumer implements Runnable {
	private BlockingQueue<Shingle> queue;
	private int k;
	private int minhashes [];
	private ConcurrentMap<Integer,List<Integer>> map;
	private ExecutorService pool;
	
	/**
	 * Instantiates new Consumer object with given parameters.
	 * 
	 * @param queue 	the blocking queue with shingles.
	 * @param k			size of the MinHash array.
	 * @param poolSize	size of the thread pool.
	 * 
	 */

	public Consumer(BlockingQueue<Shingle> queue, int k, int poolSize) {
		this.queue = queue;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		map = new ConcurrentHashMap<Integer, List<Integer>>();
		init();
	}
	
	
	/**
	 * Instantiates an array of random hashes.
	 */
	public void init() {
		Random random = new Random();
		minhashes = new int[k];
		for(int i=0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}
	
	
	/**
	 * Method that runs when the thread starts.
	 * <p>
	 * Takes a {@link Shingle} off the blocking queue and does an XOR operation on the {@link Shingle} 
	 * hashCode k times until it finds the smallest hash(MinHash) for each shingle and adds 
	 * them to a new list of {@link Integer} objects for each document. List is then mapped to a {@link ConcurrentMap} 
	 * with the corresponding <code>docId</code>.
	 */
	public void run() {
		int docCount = 2;
		while(docCount > 0) {
			try {
				Shingle s = queue.take();
				if(s instanceof Poison) {
					docCount--;
				} else {
					pool.execute(new Runnable() {
						@Override
						public void run() {
							List<Integer>list = map.get(s.getDocId());
							for(int i=0;i<minhashes.length;i++) {
								int value = s.getHashCode() ^ minhashes[i];
								list = map.get(s.getDocId());
								if(list == null) {
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
									map.put(s.getDocId(),list);
								}
								else {		
									if(list.get(i)>value) {
										list.set(i, value);
									}
								}
							} 
							map.put(s.getDocId(), list);			
						}
					});
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the Jaccard Index between the two lists.
	 * 
	 * @return	the Jaccard Similarity index of the union between the two lists.
	 */
	public float getJaccardDist() {
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jaccard = (float)intersection.size()/(k*2-(float)intersection.size());
		
		return jaccard * 100;
	}
}

