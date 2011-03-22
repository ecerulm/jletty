/*
 * Created on 23-oct-2004
 *
 */
package org.jletty.framework;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;


/**
 * @author Ruben
 * 
 */
public final class MultiThreadStage extends BaseStage implements Stage {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(MultiThreadStage.class);

	// TODO revise queue implementation
	private BlockingQueue queue = new ArrayBlockingQueue(5);

	private Stage delegate;

	public MultiThreadStage(Stage stage) {
		this(stage, 1);
	}

	public MultiThreadStage(Stage stage, int numThreads) {
		this.delegate = stage;
		ThreadGroup group = new DefaultThreadGroup("ThreadGroup for stage "
				+ stage);

		for (int i = 0; i < numThreads; i++) {
			new Thread(group, "Thread-" + i) {
				public void run() {
					try {
						while (true) {
							Object event = queue.take();
							delegate.enqueue(event);
						}
					} catch (InterruptedException e) {
						// TODO catch block
						throw new RuntimeException(e);
					}

				}
			}.start();
		}
	}

	public void enqueue(Object event) {
		try {
			queue.put(event);
		} catch (InterruptedException e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

}
