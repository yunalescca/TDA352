import java.math.BigInteger;

public class EEA {

    public static BigInteger[] EEA(BigInteger a, BigInteger b) {
        BigInteger s = BigInteger.ZERO;
        BigInteger t = BigInteger.ONE;
        BigInteger r = b;
        BigInteger old_s = BigInteger.ONE;
        BigInteger old_t = BigInteger.ZERO;
        BigInteger old_r = a;
        BigInteger[] result = new BigInteger[3];

        if (!a.equals(b)) {
            while (!r.equals(BigInteger.ZERO)) {
                BigInteger quotient = old_r.divide(r);
                BigInteger temp = old_r;
                old_r = r;
                r = temp.subtract(quotient.multiply(r));

                temp = old_s;
                old_s = s;
                s = temp.subtract(quotient.multiply(s));

                temp = old_t;
                old_t = t;
                t = temp.subtract(quotient.multiply(t));
            }

            result[0] = old_r; // GCD
            result[1] = old_s;
            result[2] = old_t;
        } else {
            result[0] = a; // GCD
            result[1] = BigInteger.ONE; // s
            result[2] = BigInteger.ZERO; // t
        }
        return result;
    }
}
