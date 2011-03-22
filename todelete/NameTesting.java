package jletty.db.jndiprovider;

import javax.naming.Name;
import javax.naming.NamingException;

public class NameTesting {

	/**
	 * @param args
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws NamingException {
		// TODO Auto-generated method stub
		final HierParser hierParser = new HierParser();
		Name name = hierParser.parse("cn=New Entry,o=organization,c=us");
		System.out.println("size: " +name.size());
		System.out.println("0: " +name.get(0));
		System.out.println("1: " +name.get(1));
		System.out.println("2 " +name.get(2));
		
		name = hierParser.parse("c=a,c=b");
		Name name2 = hierParser.parse("c=a,c=b");
		System.out.println("compare1: "+name.compareTo(name2));
		
		name2 = hierParser.parse("c=b,c=b");
		System.out.println("compare2: "+name.compareTo(name2));
		
		name2 = hierParser.parse("c=a,c=c");
		System.out.println("compare3: "+name.compareTo(name2));

		name2 = hierParser.parse("c=a,c=a");
		System.out.println("compare3: "+name.compareTo(name2));

		name2 = hierParser.parse("c=c,c=a");
		System.out.println("compare3: "+name.compareTo(name2));
		
		
		
	}

}
