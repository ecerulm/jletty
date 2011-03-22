package org.jletty.ldapstackldapops;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.jletty.ldapstackldapops.SearchResultEntry;
import org.jletty.messageprocessorstage.ResponseHandler;

public class SearchResultEntryTest extends TestCase {

	public void testFakeTest() {
		MockControl control = MockControl.createControl(ResponseHandler.class);
		ResponseHandler mock = (ResponseHandler) control.getMock();

		final SearchResultEntry exp1 = new SearchResultEntry("a", null);
		mock.sendResponse(exp1);
		mock.sendResponse(new SearchResultEntry("b", null));
		mock.sendResponse(new SearchResultEntry("c", null));
		mock.sendResponse(new SearchResultEntry("d", null));
		control.replay();
		final SearchResultEntry act1 = new SearchResultEntry("a", null);
		assertEquals(exp1,act1);
		assertEquals(act1,exp1);
		assertEquals(exp1.hashCode(),act1.hashCode());
		mock.sendResponse(act1);
		mock.sendResponse(new SearchResultEntry("b", null));
		mock.sendResponse(new SearchResultEntry("c", null));
		mock.sendResponse(new SearchResultEntry("d", null));
		control.verify();

	}


}
