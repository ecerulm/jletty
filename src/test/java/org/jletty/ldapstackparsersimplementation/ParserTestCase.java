/*
 * Created on 18-ago-2004
 *
 */
package org.jletty.ldapstackparsersimplementation;

import java.util.Arrays;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import jletty.performancetest.PerformanceMeasurable;
import junit.framework.TestCase;

import org.jletty.schema.Schema;
import org.jletty.util.Log4jTestsConfigurator;
import static org.junit.Assert.*;
/**
 * @author Administrator
 * 
 * You are suppose to override abstract methods and don't forget to configure
 * the parser to put results on resultReceived
 */
public abstract class ParserTestCase extends TestCase implements
		PerformanceMeasurable {

	protected Object resultReceived;

	private byte[][] buffers;

	private Parser parser;

	private int numCallsSetResult = 0;

	private int numCallsSetResultExpected = 0;

	private boolean setResultAssertEnabled;

	/**
	 * @param name
	 */
	public ParserTestCase(String name) {
		super(name);
	}

	public ParserTestCase() {
		super();
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected final void setUp() throws Exception {
		super.setUp();
		Log4jTestsConfigurator.configure();
		Schema.getInstance().clear();
		Schema schema = Schema.getInstance().loadSchemaFromResource(
				"/org/jletty/ldapstackparsersimplementation/core.schema");

		schema.resolveDependencies();
		resultReceived = null;
		buffers = getOkBuffers();
		assertEquals("buffers and results should be of same size",
				getOkBuffers().length, getExpectedResults().length);
		parser = getParser();
		numCallsSetResult = 0;
		numCallsSetResultExpected = buffers.length;
		setResultAssertEnabled = true;
	}

	public final void testSync() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] buffer = buffers[i];
			try {
				assertTrue("Returned false in buffer no" + i, parser
						.parse(buffer));
				checkResult(i, getExpectedResults()[i]);
			} catch (Exception e) {
				throw new RuntimeException(
						"While processing buffer[" + i + "]", e);
			}
		}

	}

	public final void testResetParser() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] buffer = buffers[i];

			ByteBuffer tmpBuffer = ByteBuffer.wrap(buffer);
			for (int j = (buffer.length - 1); j > 0; j--) {
				numCallsSetResult = 0;
				numCallsSetResultExpected = 1;
				tmpBuffer.limit(j);

				try {
					tmpBuffer.rewind();
					assertFalse("Returned true in buffer no" + i + " with j = "
							+ j, parser.parse(tmpBuffer));
					parser.reset();
					assertTrue("Returned false in buffer no" + i, parser
							.parse(buffer));
					checkResult(i, getExpectedResults()[i]);
				} catch (Exception e) {
					throw new RuntimeException("While processing buffer[" + i
							+ "]", e);
				}
			}
		}
	}

	public final void testSyncRepeated() {
		for (int i = 0; i < 3; i++) {
			numCallsSetResult = 0;
			testSync();
		}
	}

	public final void testAsync() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] buffer = buffers[i];
			boolean completedParse = false;
			for (int j = 0; j < buffer.length; j++) {
				assertFalse(completedParse);
				byte[] oneByteAtATime = new byte[] { buffer[j] };
				completedParse = parser.parse(oneByteAtATime);
				byte[] emptyBuffer = new byte[0];
				assertFalse(parser.parse(emptyBuffer));
			}
			assertTrue(completedParse);
			checkResult(i, getExpectedResults()[i]);
		}
	}

	public final void testAsyncRepeated() {
		for (int i = 0; i < 3; i++) {
			numCallsSetResult = 0;
			testAsync();
		}
	}

	public final void testSyncLengthPlusOne() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] origBuffer = (byte[]) buffers[i].clone();
			byte[] buffer = (byte[]) origBuffer.clone();
			if ((buffer[1] & 0x80) == 0) {
				buffer[1]++; // one byte length
			} else {
				buffer[2]++;
			}
			resultReceived = null;
			parser.reset();
			try {
				numCallsSetResult = 0;
				numCallsSetResultExpected = 1;
				boolean completed = parser.parse(buffer);
				assertFalse("Returned true when  parsing buffer[" + i
						+ "], resultReceived = " + resultReceived, completed);
				assertNull(
						"The result should be null when parsing buffer no" + i
								+ " but was " + resultReceived, resultReceived);
				// depending on the type of information
				// parser it could throw an exception or
				// simply return false
			} catch (ParserException e) {
				// ok it should throw an exception when the declared size
				// doesn't
				// match with the observed size
				// e.printStackTrace();
				buffers = getOkBuffers();
				numCallsSetResult = 0;
				numCallsSetResultExpected = buffers.length;
				testSync(); // It should accept a new message without being
				// reset
			}
		}

	}

	public final void testSyncLengthMinusOne() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] buffer = buffers[i];
			resultReceived = null;
			if ((buffer[1] & 0x80) == 0) {
				buffer[1]--; // one byte length
			} else {
				buffer[3]--;
			}

			try {
				numCallsSetResult = 0;
				numCallsSetResultExpected = 1;
				boolean completed = parser.parse(buffer);
				// fail("It should throw a parser exception. Returned " +
				// completed
				// + " instead.");
			} catch (ParserException e) {
				// ok it should throw an exception when the declared size
				// doesn't
				// match with the observed size
				// e.printStackTrace();
				assertNull(resultReceived);
				buffers = getOkBuffers();
				numCallsSetResult = 0;
				numCallsSetResultExpected = buffers.length;
				testSync(); // It should accept a new message without being
				// reset
			}

			// fail("Test case not implemented yet");
		}
	}

	public final void testASyncLengthPlusOne() {

		for (int i = 0; i < buffers.length; i++) {
			byte[] buffer = buffers[i];

			buffer[1]++;
			resultReceived = null;
			parser.reset();

			try {
				boolean completedParse = false;
				for (int j = 0; j < buffer.length; j++) {
					assertFalse(completedParse);
					byte[] oneByteAtATime = new byte[] { buffer[j] };
					completedParse = parser.parse(oneByteAtATime);
					byte[] emptyBuffer = new byte[0];
					assertFalse(parser.parse(emptyBuffer));
				}
				assertFalse(completedParse);
				assertNull(resultReceived);
				// depending on the type of information
				// parser it could throw an exception or
				// simply return false
			} catch (ParserException e) {
				// ok it should trow an exception
				buffers = getOkBuffers();
				numCallsSetResult = 0;
				numCallsSetResultExpected = buffers.length;
				testSync(); // It should accept a new message without being
				// reset
			}
		}
	}

	public final void testASyncLengthMinusOne() {

		for (int i = 0; i < buffers.length; i++) {
			byte[] buffer = buffers[i];
			resultReceived = null;
			// parser.reset();
			if (buffer[1] == 0)
				return; // dont test if the length is zero null object
			buffer[1]--;
			try {
				boolean completedParse = false;
				for (int j = 0; j < buffer.length; j++) {
					// assertFalse(completedParse);
					byte[] oneByteAtATime = new byte[] { buffer[j] };
					numCallsSetResult = 0;
					numCallsSetResultExpected = 1;
					completedParse = parser.parse(oneByteAtATime);
					if (completedParse)
						break;
					byte[] emptyBuffer = new byte[0];
					completedParse = parser.parse(emptyBuffer);

					// assertFalse(completedParse);
				}
				// fail("It should thow a Parser Exception");
			} catch (ParserException e) {
				// it can throw an exception
				if (resultReceived != null) {
					e.printStackTrace();
					System.out.println("resultReceived = " + resultReceived);
				}
				assertNull(resultReceived);
			}

			buffers = getOkBuffers();
			numCallsSetResult = 0;
			numCallsSetResultExpected = buffers.length;
			testSync(); // It should accept a new message without being reset
		}
	}

	public final void testWrongTag() {

		for (int i = 0; i < buffers.length; i++) {
			byte[] buffer = buffers[i];

			buffer[0] = getWrongTag();
			try {
				parser.parse(buffer);
				fail("It should throw an exception");
			} catch (ParserException e) {
				// ok it should throw an exception
			}
		}
	}

	public final void testImplicit() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] bufferTmp = buffers[i];
			byte[] buffer = new byte[bufferTmp.length - 1];
			System.arraycopy(bufferTmp, 1, buffer, 0, buffer.length);
			try {
				parser.implicit(-1);
				assertTrue("Returned false in buffer no" + i, parser
						.parse(buffer));
				checkResult(i, getExpectedResults()[i]);
			} catch (Exception e) {
				throw new RuntimeException(
						"While processing buffer[" + i + "]", e);
			}
		}
	}

	public final void testImplicitException() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] bufferTmp = buffers[i];
			if (bufferTmp.length <= 3)
				continue;
			ByteBuffer buffer = ByteBuffer.wrap(bufferTmp);
			buffer.limit(3);
			try {
				assertFalse("Returned true in buffer no" + i
						+ " and result was " + resultReceived, parser
						.parse(buffer));
				parser.implicit(-1);
				fail("should throw an exception");
			} catch (ParserException e) {
				// ignore
			}
		}
	}

	public final void testWrongBufferException() {
		byte[][] wrongBuffers = getBuffersThatShouldThrowAnException();

		for (int i = 0; i < wrongBuffers.length; i++) {
			byte[] bufferTmp = wrongBuffers[i];
			try {
				assertFalse("Returned true in buffer no" + i
						+ " and result was " + resultReceived, parser
						.parse(bufferTmp));
				fail("should throw an exception");
			} catch (ParserException e) {
				// ignore
				// e.printStackTrace();
			}
		}
	}

	public final void testIndefiniteLengthException() {
		for (int i = 0; i < buffers.length; i++) {
			byte[] bufferTmp = (byte[]) buffers[i].clone();
			bufferTmp[1] = (byte) 0x80;
			try {
				assertFalse("Returned true in buffer no" + i
						+ " and result was " + resultReceived, parser
						.parse(bufferTmp));
				fail("should throw an exception");
			} catch (ParserException e) {
				// ignore
			}
		}
	}

	public final Map getPerformanceMeasures(int numCycles) {
		// byte[] buffer = buffers[0];
		boolean checkAsserts = true;
		try {
			setUp();
			setResultAssertEnabled = false;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		int bytesToAlloc = 0;
		for (int i = 0; i < buffers.length; i++) {
			bytesToAlloc = bytesToAlloc + buffers[i].length;
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(bytesToAlloc);
		for (int i = 0; i < buffers.length; i++) {
			byteBuffer.put(buffers[i]);
		}

		// ByteBuffer byteBuffer=ByteBuffer.wrap(buffers[0]);
		byte[] justToTest = new byte[bytesToAlloc];
		byteBuffer.rewind();
		byteBuffer.get(justToTest);

		long start = System.currentTimeMillis();
		long iterations = 0;
		long calls = 0;
		while ((calls < numCycles)
				|| (System.currentTimeMillis() - start) < 1000) {
			resultReceived = null;
			byteBuffer.rewind();
			while (byteBuffer.hasRemaining()) {
				final boolean completed = parser.parse(byteBuffer);
				if (checkAsserts) {
					assertTrue(completed);
					assertNotNull(resultReceived);
				}
			}
			if (checkAsserts)
				assertEquals(0, byteBuffer.remaining());

			iterations = iterations + buffers.length;
			calls++;
		}
		long stop = System.currentTimeMillis();
		long duration = stop - start;

		Map result = new HashMap();

		// System.out.println("bytesPerCall:" +byteBuffer.capacity());
		// System.out.println("duration:" +duration);

		result.put("bytesPerOperation", new Long(byteBuffer.capacity()
				/ buffers.length));
		final long bytesPerSecond = (byteBuffer.capacity() * calls * 1000)
				/ duration;
		result.put("bytesPerSecond", new Long(bytesPerSecond));

		final long opsPerSecond = (iterations * 1000) / duration;
		result.put("operationsPerSecond", new Long(opsPerSecond));

		return result;
	}

	// private long bytesProcessedPerCall() {
	// long bytesProcessedPerCall = 0;
	// for (int i = 0; i < buffers.length; i++) {
	// bytesProcessedPerCall += buffers[i].length;
	// }
	// return bytesProcessedPerCall;
	// }

	/**
	 * @param value
	 */
	private final void checkResult(int i, Object expectedResult) {
		assertNotNull("Parser result is null while processing buffer " + i,
				resultReceived);
		boolean expectedResultIsArray = expectedResult.getClass().isArray();
		if (expectedResultIsArray) {
			final Class componentTypeClass = expectedResult.getClass()
					.getComponentType();
			if (componentTypeClass.getName() == "byte") {
				assertArrayEquals("while processing buffer " + i,
						(byte[]) expectedResult, (byte[]) resultReceived);
			} else if (componentTypeClass.isArray()) { // byte[][]
				assertEquals("byte", componentTypeClass.getComponentType()
						.getName());
				Collection tmp1 = new HashSet();
				byte[][] arr1 = (byte[][]) expectedResult;
				for (int j = 0; j < arr1.length; j++) {
					byte[] bs = arr1[j];
					tmp1.add(ByteBuffer.wrap(bs));
				}

				Collection tmp2 = new HashSet();
				byte[][] arr2 = (byte[][]) resultReceived;
				for (int j = 0; j < arr2.length; j++) {
					byte[] bs = arr2[j];
					tmp2.add(ByteBuffer.wrap(bs));
				}

				assertEquals(tmp1, tmp2);

			} else {
                            
//				ArrayAssert.assertEquivalenceArrays("while processing buffer "
//						+ i + "\nexpected: " + expectedResult + "\nactual: "
//						+ resultReceived, (Object[]) expectedResult,
//						(Object[]) resultReceived);
				assertEquals("while processing buffer "
						+ i + "\nexpected: " + expectedResult + "\nactual: "
						+ resultReceived,  Arrays.asList((Object[]) expectedResult),
						Arrays.asList((Object[]) resultReceived));
			}
		} else {
			assertEquals("while processing buffer " + i, expectedResult,
					resultReceived);
		}
	}

	protected byte getWrongTag() {
		// to be overriden
		return 0;
	}

	protected final void setResult(Object result) {
		if (setResultAssertEnabled) {
			assertTrue(
					"numCallsSetResult:" + numCallsSetResult
							+ " numCallsSetResultExpected:"
							+ numCallsSetResultExpected,
					numCallsSetResult < numCallsSetResultExpected);
			numCallsSetResult++;
		}
		this.resultReceived = result;
	}

	protected final Object getResult() {
		return this.resultReceived;
	}

	protected byte[][] getOkBuffers() {
		return new byte[][] { getOkBuffer() };
	}

	protected byte[][] getBuffersThatShouldThrowAnException() {
		return new byte[0][];
	}

	protected byte[] getOkBuffer() {
		throw new RuntimeException("This method shoudl be overriden");
	}

	protected Object[] getExpectedResults() {
		return new Object[] { getExpectedResult() };
	}

	protected Object getExpectedResult() {
		throw new RuntimeException("This method shoudl be overriden");
	}

	protected abstract Parser getParser();

	public abstract void testPrueba();

}