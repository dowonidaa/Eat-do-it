package com.project.eat.member;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class User_pwSHA512 {

	public static String Salt() {

		String salt = "";
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16];
			random.nextBytes(bytes);
			salt = new String(Base64.getEncoder().encode(bytes));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return salt;
	}

	public static String getSHA512(String password, String hash) {
		String salt = hash + password;
		String hex = null;

		try {

			MessageDigest msg = MessageDigest.getInstance("SHA-512");
			msg.update(salt.getBytes());

			hex = String.format("%128x", new BigInteger(1, msg.digest()));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hex;
	}

}
