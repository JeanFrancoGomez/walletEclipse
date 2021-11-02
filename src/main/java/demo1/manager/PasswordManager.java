package demo1.manager;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.SecureRandom;

public final class PasswordManager {
	public static final String ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA256";

	// The following constants may be changed without breaking existing hashes.
	public static final int SALT_BYTES = 24;
	public static final int HASH_BYTES = 24;
	public static final int NUMBER_OF_ALGORITHM_ITERATIONS = 20000;

	public static final int INDEX_OF_NUMBER_OF_ITERATIONS = 0;
	public static final int INDEX_OF_SALT = 1;
	public static final int INDEX_OF_HASH = 2;

	/**
	 * Returns a salted hash of the password.
	 *
	 * @param password the password to hash
	 * @return a salted hash of the password
	 */
	public static String createHash(String password) {
		return createHash(password.toCharArray());
	}

	/**
	 * Returns a salted hash of the password.
	 *
	 * @param password the password to hash
	 * @return a salted hash of the password
	 */
	public static String createHash(char[] password) {
		// Generate a random salt
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTES];
		random.nextBytes(salt);

		// Hash the password
		byte[] hash = pbkdf2(password, salt, NUMBER_OF_ALGORITHM_ITERATIONS, HASH_BYTES);
		// format iterations:salt:hash
		return NUMBER_OF_ALGORITHM_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
	}

	/**
	 * Validates a password using a hash.
	 *
	 * @param password the password to check
	 * @param goodHash the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(String password, String goodHash) {
		return validatePassword(password.toCharArray(), goodHash);
	}

	/**
	 * Validates a password using a hash.
	 *
	 * @param password the password to check
	 * @param goodHash the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(char[] password, String goodHash) {
		// Decode the hash into its parameters
		String[] params = goodHash.split(":");
		int iterations = Integer.parseInt(params[INDEX_OF_NUMBER_OF_ITERATIONS]);
		byte[] salt = fromHex(params[INDEX_OF_SALT]);
		byte[] hash = fromHex(params[INDEX_OF_HASH]);
		// Compute the hash of the provided password, using the same salt,
		// iteration count, and hash length
		byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return slowEquals(hash, testHash);
	}

	/**
	 * Compares two byte arrays in length-constant time. This comparison method is
	 * used so that password hashes cannot be extracted from an on-line system using
	 * a timing attack and then attacked off-line.
	 *
	 * @param a the first byte array
	 * @param b the second byte array
	 * @return true if both byte arrays are the same, false if not
	 */
	private static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}

	/**
	 * Computes the PBKDF2 hash of a password.
	 *
	 * @param password   the password to hash.
	 * @param salt       the salt
	 * @param iterations the iteration count (slowness factor)
	 * @param bytes      the length of the hash to compute in bytes
	 * @return the hash of the password
	 */
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
			return skf.generateSecret(spec).getEncoded();
		} catch (Exception ex) {
			throw new RuntimeException("Unexpected error performing password encryption.", ex);
		}
	}

	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 *
	 * @param hex the hex string
	 * @return the hex string decoded into a byte array
	 */
	private static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	/**
	 * Converts a byte array into a hexadecimal string.
	 *
	 * @param array the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	public static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		else
			return hex;
	}

}