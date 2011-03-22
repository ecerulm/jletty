/*
 * Created on 11-oct-2004
 *
 */
package org.jletty.tcpserverstage;

import org.jletty.framework.PrintStage;

/**
 * @author Ruben
 * 
 */
public class TcpServerTest {

	public static void main(String[] args) throws Exception {
		new TcpServerStage(2222, new PrintStage());
		System.out.println("Press any key to stop");
		System.in.read();
		System.out.println("Stopped");
		System.exit(0);
	}
}
