/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jletty.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Ruben Laguna <ruben.laguna@gmail.com>
 */
public abstract class EqualsHashCodeTestCase {
    public static final int NUM_REPEATS = 10;
    private Object o1;
    private Object o2;
    private Object o3;
    private Object no;

    protected abstract Object createInstance() throws Exception;

    protected abstract Object createNotEqualInstance() throws Exception;

    
    @Before
    public void createInstances() throws Exception{
        o1 = createInstance();
        o2 = createInstance();
        o3 = createInstance();
        no = createNotEqualInstance();
    }
    @Test
    public void testEqualsAgainstNewObject() throws Exception {
        assertEquals(o1, o2);
    }

    @Test
    public void testEqualsAgainstNull() throws Exception {
        assertThat(o1, not(equalTo(null)));
    }

    @Test
    public void testEqualsAgainstUnequalObjects() throws Exception {
        assertThat(o1, not(equalTo(no)));
        assertThat(no, not(equalTo(o1)));
    }

    @Test
    public void testEqualsIsConsistentAcrossInvocations() throws Exception {
        for (int i = 0; i < NUM_REPEATS; i++) {
           testEqualsAgainstNewObject();
           testEqualsAgainstNull();
           testEqualsAgainstUnequalObjects();
           testEqualsIsReflexive();
           testEqualsIsSymmetricAndTransitive();
        }
    }

    @Test
    public void testEqualsIsReflexive() throws Exception {
        assertEquals(o1, o1);
        assertEquals(no, no);
    }

    @Test
    public void testEqualsIsSymmetricAndTransitive() throws Exception {
        assertEquals(o1, o2);
        assertEquals(o2, o1);

        assertEquals(o2, o3);
        assertEquals(o3, o2);

        assertEquals(o1, o3);
        assertEquals(o3, o1);
    }

    @Test
    public void testHashCodeContract() throws Exception {
        assertEquals(o1, o2);
    }

    @Test
    public void testHashCodeIsConsistentAcrossInvocations() throws Exception {
        for (int i = 0; i < NUM_REPEATS; i++) {
            assertEquals(o1.hashCode(), o2.hashCode());
        }
    }
}
