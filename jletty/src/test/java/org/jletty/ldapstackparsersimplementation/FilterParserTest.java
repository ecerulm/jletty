package org.jletty.ldapstackparsersimplementation;

import org.jletty.ldapstackldapops.BerTags;
import org.jletty.ldapstackldapops.Filter;
import org.jletty.ldapstackldapops.FilterListener;

import junit.framework.TestCase;

public class FilterParserTest extends TestCase {
	
	//this test allows to execute the otherwise unreachable throw clause 
	//in the  FilterParser switch statement. If checkExpectedTag is correctly
	//implemented then the default: case in the switch is never executed because
	// the incorrect tag is catch in the checkExpectedTag
	public void testUnreachableThrow() {
		try {
			FilterListener listener = new FilterListener() {
				public void data(Filter f) {
					fail("should never suceed");
				}
			};
			FilterParser parser = new FilterParser(listener) {
				@Override
				protected boolean checkExpectedTag(byte tag) {
					return true;
				}
				
			};
			byte tag =  (BerTags.CONTEXT_SPECIFIC | BerTags.TAG_VALUE_10);
			byte[] buf = {tag,0x2,0x01,0x01};
			parser.parse(buf);
			fail();
		} catch (ParserException e) {
			//ignore it should throw an Exception
		}
		
	}

}
