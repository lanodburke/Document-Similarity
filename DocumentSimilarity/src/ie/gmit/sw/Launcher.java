package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	public void Launch(String f1, String f2, int shingleSize, int k, int poolSize) throws InterruptedException {
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<Shingle>(100);
		Consumer c;
		
		Thread t1 = new Thread(new DocumentParser(f1, k, shingleSize, q, 1), "T1");
		Thread t2 = new Thread(new DocumentParser(f2, k, shingleSize, q, 2), "T2");
		Thread t3 = new Thread(c = new Consumer(q, k, poolSize), "T3");
		
		t1.start();
		t2.start();
		t3.start();
		
		t1.join();
		t2.join();
		t3.join();
		System.out.println("J: " + c.getJaccardDist());
	}
}
