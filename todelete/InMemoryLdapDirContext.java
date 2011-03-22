/*
 * Created on 27-nov-2004
 *
 */
package org.jletty.db.experimental;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.Control;
import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.ExtendedResponse;
import javax.naming.ldap.LdapContext;

import org.jletty.util.NotImplementedException;


/**
 * @author Ruben
 * 
 */
public class InMemoryLdapDirContext implements LdapContext {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.ldap.LdapContext#getConnectControls()
	 */
	public Control[] getConnectControls() throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.ldap.LdapContext#getRequestControls()
	 */
	public Control[] getRequestControls() throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.ldap.LdapContext#getResponseControls()
	 */
	public Control[] getResponseControls() throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.ldap.LdapContext#reconnect(javax.naming.ldap.Control[])
	 */
	public void reconnect(Control[] connCtls) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.ldap.LdapContext#setRequestControls(javax.naming.ldap.Control[])
	 */
	public void setRequestControls(Control[] requestControls)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.ldap.LdapContext#extendedOperation(javax.naming.ldap.ExtendedRequest)
	 */
	public ExtendedResponse extendedOperation(ExtendedRequest request)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.ldap.LdapContext#newInstance(javax.naming.ldap.Control[])
	 */
	public LdapContext newInstance(Control[] requestControls)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getAttributes(java.lang.String)
	 */
	public Attributes getAttributes(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String,
	 *      int, javax.naming.directory.Attributes)
	 */
	public void modifyAttributes(String name, int mod_op, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name)
	 */
	public Attributes getAttributes(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name,
	 *      int, javax.naming.directory.Attributes)
	 */
	public void modifyAttributes(Name name, int mod_op, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getSchema(java.lang.String)
	 */
	public DirContext getSchema(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getSchemaClassDefinition(java.lang.String)
	 */
	public DirContext getSchemaClassDefinition(String name)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getSchema(javax.naming.Name)
	 */
	public DirContext getSchema(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getSchemaClassDefinition(javax.naming.Name)
	 */
	public DirContext getSchemaClassDefinition(Name name)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String,
	 *      javax.naming.directory.ModificationItem[])
	 */
	public void modifyAttributes(String name, ModificationItem[] mods)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name,
	 *      javax.naming.directory.ModificationItem[])
	 */
	public void modifyAttributes(Name name, ModificationItem[] mods)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(java.lang.String,
	 *      javax.naming.directory.Attributes)
	 */
	public NamingEnumeration search(String name, Attributes matchingAttributes)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(javax.naming.Name,
	 *      javax.naming.directory.Attributes)
	 */
	public NamingEnumeration search(Name name, Attributes matchingAttributes)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#bind(java.lang.String,
	 *      java.lang.Object, javax.naming.directory.Attributes)
	 */
	public void bind(String name, Object obj, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#rebind(java.lang.String,
	 *      java.lang.Object, javax.naming.directory.Attributes)
	 */
	public void rebind(String name, Object obj, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#bind(javax.naming.Name,
	 *      java.lang.Object, javax.naming.directory.Attributes)
	 */
	public void bind(Name name, Object obj, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#rebind(javax.naming.Name,
	 *      java.lang.Object, javax.naming.directory.Attributes)
	 */
	public void rebind(Name name, Object obj, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getAttributes(java.lang.String,
	 *      java.lang.String[])
	 */
	public Attributes getAttributes(String name, String[] attrIds)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name,
	 *      java.lang.String[])
	 */
	public Attributes getAttributes(Name name, String[] attrIds)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#createSubcontext(java.lang.String,
	 *      javax.naming.directory.Attributes)
	 */
	public DirContext createSubcontext(String name, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#createSubcontext(javax.naming.Name,
	 *      javax.naming.directory.Attributes)
	 */
	public DirContext createSubcontext(Name name, Attributes attrs)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(java.lang.String,
	 *      javax.naming.directory.Attributes, java.lang.String[])
	 */
	public NamingEnumeration search(String name, Attributes matchingAttributes,
			String[] attributesToReturn) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(javax.naming.Name,
	 *      javax.naming.directory.Attributes, java.lang.String[])
	 */
	public NamingEnumeration search(Name name, Attributes matchingAttributes,
			String[] attributesToReturn) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(java.lang.String,
	 *      java.lang.String, javax.naming.directory.SearchControls)
	 */
	public NamingEnumeration search(String name, String filter,
			SearchControls cons) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(javax.naming.Name,
	 *      java.lang.String, javax.naming.directory.SearchControls)
	 */
	public NamingEnumeration search(Name name, String filter,
			SearchControls cons) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(java.lang.String,
	 *      java.lang.String, java.lang.Object[],
	 *      javax.naming.directory.SearchControls)
	 */
	public NamingEnumeration search(String name, String filterExpr,
			Object[] filterArgs, SearchControls cons) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.directory.DirContext#search(javax.naming.Name,
	 *      java.lang.String, java.lang.Object[],
	 *      javax.naming.directory.SearchControls)
	 */
	public NamingEnumeration search(Name name, String filterExpr,
			Object[] filterArgs, SearchControls cons) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#close()
	 */
	public void close() throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#getNameInNamespace()
	 */
	public String getNameInNamespace() throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#destroySubcontext(java.lang.String)
	 */
	public void destroySubcontext(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#unbind(java.lang.String)
	 */
	public void unbind(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#getEnvironment()
	 */
	public Hashtable getEnvironment() throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
	 */
	public void destroySubcontext(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#unbind(javax.naming.Name)
	 */
	public void unbind(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#lookup(java.lang.String)
	 */
	public Object lookup(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#lookupLink(java.lang.String)
	 */
	public Object lookupLink(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
	 */
	public Object removeFromEnvironment(String propName) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
	 */
	public void bind(String name, Object obj) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
	 */
	public void rebind(String name, Object obj) throws NamingException {
		throw new NotImplementedException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#lookup(javax.naming.Name)
	 */
	public Object lookup(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#lookupLink(javax.naming.Name)
	 */
	public Object lookupLink(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
	 */
	public void bind(Name name, Object obj) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
	 */
	public void rebind(Name name, Object obj) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
	 */
	public void rename(String oldName, String newName) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#createSubcontext(java.lang.String)
	 */
	public Context createSubcontext(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#createSubcontext(javax.naming.Name)
	 */
	public Context createSubcontext(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
	 */
	public void rename(Name oldName, Name newName) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#getNameParser(java.lang.String)
	 */
	public NameParser getNameParser(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#getNameParser(javax.naming.Name)
	 */
	public NameParser getNameParser(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#list(java.lang.String)
	 */
	public NamingEnumeration list(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#listBindings(java.lang.String)
	 */
	public NamingEnumeration listBindings(String name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#list(javax.naming.Name)
	 */
	public NamingEnumeration list(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#listBindings(javax.naming.Name)
	 */
	public NamingEnumeration listBindings(Name name) throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#addToEnvironment(java.lang.String,
	 *      java.lang.Object)
	 */
	public Object addToEnvironment(String propName, Object propVal)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
	 */
	public String composeName(String name, String prefix)
			throws NamingException {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.naming.Context#composeName(javax.naming.Name,
	 *      javax.naming.Name)
	 */
	public Name composeName(Name name, Name prefix) throws NamingException {
		throw new NotImplementedException();
	}

}
