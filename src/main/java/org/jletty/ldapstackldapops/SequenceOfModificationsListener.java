/*
 * Created on 24-sep-2004
 *
 */
package org.jletty.ldapstackldapops;

/**
 * @author Ruben
 * 
 */
public interface SequenceOfModificationsListener {
	public void data(Modification[] value);
}