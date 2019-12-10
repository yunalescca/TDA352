//   javac CryptoLibTest.java
// Running:
//   java CryptoLibTest

import java.math.BigDecimal;
import java.math.BigInteger;

public class CryptoLib {

	/**
	 * Returns an array "result" with the values "result[0] = gcd",
	 * "result[1] = s" and "result[2] = t" such that "gcd" is the greatest
	 * common divisor of "a" and "b", and "gcd = a * s + b * t".
	 **/
	public static int[] EEA(int a, int b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.
		int s = 0;
		int t = 1;
		int r = b;
		int old_s = 1;
		int old_t = 0;
		int old_r = a;
		int[] result = new int[3];

		if (a != b) {
			while (r != 0) {
				int quotient = old_r / r;
				int temp = old_r;
				old_r = r;
				r = temp - quotient * r;

				temp = old_s;
				old_s = s;
				s = temp - quotient * s;

				temp = old_t;
				old_t = t;
				t = temp - quotient * t;
			}

			result[0] = old_r; // GCD
			result[1] = old_s;
			result[2] = old_t;
		} else {
			result[0] = a; // GCD
			result[1] = 1; // s
			result[2] = 0; // t
 		}

		return result;
	}

	/**
	 * Returns Euler's Totient for value "n".
	 **/
	public static int EulerPhi(int n) {
		if (n < 1) {
			return 0;
		}

		int counts = 0;
		for(int i = 1; i < n; i++) {
			int gcd = EEA(i,n)[0];
			if (gcd == 1) {
				counts++;
			}
		}

		return counts;
	}

	/**
	 * Returns the value "v" such that "n*v = 1 (mod m)". Returns 0 if the
	 * modular inverse does not exist.
	 **/
	public static int ModInv(int n, int m) {
		int[] res = EEA(Math.floorMod(n, m), m);
		if (res[0] == 1) {
			return Math.floorMod(res[1], m);
		} else {
			return 0;
		}
	}

	/**
	 * Returns 0 if "n" is a Fermat Prime, otherwise it returns the lowest
	 * Fermat Witness. Tests values from 2 (inclusive) to "n/3" (exclusive).
	 **/
	public static int FermatPT(int n) {
		for (int i = 2; i < n/3; i++) {
			BigInteger pow = new BigInteger("" + i).pow(n-1);
			BigInteger res = pow.mod(new BigInteger("" + n));
			if (!res.equals(new BigInteger("1"))) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Returns the probability that calling a perfect hash function with
	 * "n_samples" (uniformly distributed) will give one collision (i.e. that
	 * two samples result in the same hash) -- where "size" is the number of
	 * different output values the hash function can produce.
	 **/
	public static double HashCP(double n_samples, double size) {

		BigDecimal num = BigDecimal.ONE;
		for (long i = (long) size; i > 0; i--) {
			num = num.multiply(BigDecimal.valueOf(i));
		}

		BigDecimal denom = BigDecimal.ONE;
		for (long i = (long) (size - n_samples); i > 0; i--) {
			denom = denom.multiply(BigDecimal.valueOf(i));
		}
		denom = denom.multiply(BigDecimal.valueOf((int) size).pow((int) n_samples));

		return 1 - (num.divide(denom, 10, BigDecimal.ROUND_CEILING)).doubleValue();
	}
}
