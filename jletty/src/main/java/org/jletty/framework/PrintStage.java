/*
 * Created on 11-oct-2004
 *
 */
package org.jletty.framework;

/**
 * @author Ruben
 * 
 */
public class PrintStage extends BaseStage implements Stage {

	public void enqueue(Object e) {
		System.out.println(e);
	}

}
