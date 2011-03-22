/*
 * Created on 12-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jletty.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author rlm
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SocketDump {

	public static void main(String[] args) {
		int _port = 389;
		if (args.length == 0) {
			printUsage();
			System.exit(0);
		}
		if (args.length >= 1) {
			_port = Integer.parseInt(args[0]);
		}
		Socket remote;
		try {
			remote = new Socket(args[1], Integer.parseInt(args[2]));

			Socket s = acceptIncommingConnection(_port);
			System.out.println("Connection accepted");
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			OutputStream inremote = remote.getOutputStream();
			InputStream outremote = remote.getInputStream();

			boolean running = true;
			while (running) {
				running = isSocketOk(s);

				running = receiveInput(in, inremote);

				running = receiveRemoteOutput(out, outremote);
				Thread.sleep(1000);
				// out.write(0);
				System.out.print("#");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);

		}
		System.out.println("Exit");
	}

	private static boolean receiveRemoteOutput(OutputStream out,
			InputStream outremote) throws IOException {
		int c;
		int counter;
		counter = 1;
		boolean running = true;
		while (outremote.available() > 0) {
			if (counter == 1) {
				System.out.println("\nOutput");
			}
			if ((c = outremote.read()) == -1) {
				running = false;
			}
			System.out.print(HexUtils.toHexString((byte) c) + " ");
			if ((counter++ % 10) == 0) {
				System.out.println();
			}
			out.write(c);
		}
		return running;
	}

	private static boolean receiveInput(InputStream in, OutputStream inremote)
			throws IOException {
		int c;
		int counter;
		counter = 1;
		boolean running = true;
		while (in.available() > 0) {
			if (counter == 1) {
				System.out.println("\nInput");
			}
			if ((c = in.read()) == -1) {
				running = false;
			}
			System.out.print(HexUtils.toHexString((byte) c) + " ");
			if ((counter++ % 10) == 0) {
				System.out.println();
			}
			inremote.write(c);
		}
		return running;
	}

	private static Socket acceptIncommingConnection(int _port)
			throws IOException, SocketException {
		ServerSocket server = new ServerSocket(_port);
		System.out.println("Started on port " + _port);
		Socket s = server.accept();
		s.setKeepAlive(true);
		return s;
	}

	private static boolean isSocketOk(Socket s) {
		boolean running = true;
		if (s.isClosed()) {
			running = false;
		}
		if (!s.isBound()) {
			running = false;
		}
		if (!s.isConnected()) {
			running = false;
		}
		if (s.isInputShutdown()) {
			running = false;
		}
		if (s.isOutputShutdown()) {
			running = false;
		}
		return running;
	}

	/**
	 * 
	 */
	private static void printUsage() {
		System.out
				.println("Usage:java SocketDump localport remoteHost remotePort");
	}
}