package org.jletty.messageprocessorstage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

	private PasswordUtil() {
		super();
	}
	/**
     * Splits a byte array into two.
     *
     * @param src byte array to split
     * @param n location to split the array
     * @return a two dimensional array of the split
     */
    private static byte[][] split(byte[] src, int n) {
        byte[] l;
        byte[] r;

        if (src.length <= n) {
            l = src;
            r = new byte[0];

        } else {
            l = new byte[n];
            r = new byte[src.length - n];
            System.arraycopy(src, 0, l, 0, n);
            System.arraycopy(src, n, r, 0, r.length);

        }

        byte[][] lr = { l, r };

        return lr;

    }


	/**
     * Validates if a plaintext password matches a
     * hashed version.
     *
     * @param digest digested version
     * @param password plaintext password
     * @return if the two match
     */
    public static boolean verifyPassword(String digest, String password) {
        String alg = null;
        int size = 0;

        if (digest.regionMatches(true, 0, "{SHA}", 0, 5)) {
            digest = digest.substring(5);
            alg = "SHA-1";
            size = 20;

        } else if (digest.regionMatches(true, 0, "{CRYPT}", 0, 6)) {
            digest = digest.substring(7);
            alg = "CRYPT";
            size = 13;
        } else if (digest.regionMatches(true, 0, "{SSHA}", 0, 6)) {
        	digest = digest.substring(6);
        	alg = "SHA-1";
        	size = 20;

        } else if (digest.regionMatches(true, 0, "{MD5}", 0, 5)) {
            digest = digest.substring(5);
            alg = "MD5";
            size = 16;

        } else if (digest.regionMatches(true, 0, "{SMD5}", 0, 6)) {
            digest = digest.substring(6);
            alg = "MD5";
            size = 16;

        } else {
        	alg = "CRYPT";
        	size = 13;
        }

        try {
        	
        	if ("CRYPT".equals(alg)) {
        		return UnixCrypt.matches(digest,password);
        	} else {
        		MessageDigest sha = MessageDigest.getInstance(alg);
        		
        		if (sha == null) {
        			return false;
        			
        		}
        		
        		byte[][] hs = split(org.jletty.messageprocessorstage.Base64.decode(digest), size);
        		byte[] hash = hs[0];
        		byte[] salt = hs[1];
        		sha.reset();
        		sha.update(password.getBytes());
        		sha.update(salt);
        		
        		byte[] pwhash = sha.digest();
        		
        		return MessageDigest.isEqual(hash, pwhash);
        	}
        } catch (NoSuchAlgorithmException nsae) {
        	throw new RuntimeException(
        			"failed to find " + "algorithm for password hashing.",
        			nsae);
        }

    }

}
