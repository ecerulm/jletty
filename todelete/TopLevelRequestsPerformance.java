/*
 * Created on 26-sep-2004
 *
 */
package com.rubenlaguna.tests.performance;

import jletty.ldapstack.ldapops.LDAPRequest;
import jletty.ldapstack.ldapops.RequestListener;
import jletty.ldapstack.parsers.implementation.BindRequestParser;

import com.rubenlaguna.utils.HexUtils;

/**
 * @author Ruben
 *  
 */
public class TopLevelRequestsPerformance {

	//private static final int NITERATIONS = 5000000;
	private static final int TESTDURATION = 10000;

	public static void main(String[] args) {
		TopLevelRequestsPerformance instance = new TopLevelRequestsPerformance();
		instance.testBindRequest();
	}

	private void testBindRequest() {
		byte[] berBindRequest = HexUtils
				.fromHexString("60 29 02 01 03 04 1C 63 6E 3D 61"
						+ "64 6D 69 6E 2C 6F 3D 6C 69 6E 75 78 70 6F 77 65"
						+ "72 65 64 2C 63 3D 75 73 80 06 73 65 63 72 65 74");
		final RequestListener l = new RequestListener() {

			public void data(LDAPRequest req) {
				//System.out.println("Received bind request " + req);
			}
		};

		final BindRequestParser parser = new BindRequestParser(l);

		long start = System.currentTimeMillis();

		long iterations = 0;
		while ((System.currentTimeMillis() - start) < TESTDURATION) {
			//System.out.println("Parsing message nº"+i);
			boolean completed = parser.parse(berBindRequest);
			//System.out.println("returned " + completed);
			iterations++;

		}
		long stop = System.currentTimeMillis();
		long duration = stop - start;

		System.out.println("BindRequest op/s " + (iterations * 1000) / duration
				+ " iterations " + iterations + " durations (ms) " + duration);

	}
}