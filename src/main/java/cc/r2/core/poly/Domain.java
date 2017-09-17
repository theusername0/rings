package cc.r2.core.poly;

import cc.r2.core.number.BigInteger;
import org.apache.commons.math3.random.RandomGenerator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Domain of elements. Mathematical operations defined in {@code Domain} include all <i>field</i> operations, though the
 * particular implementations may represent a more restricted sets (general rings, Euclidean rings etc.), in
 * which case some field operations (e.g. reciprocal) are not applicable (will throw exception).
 *
 * @param <E> the type of objects that may be operated by this domain
 * @author Stanislav Poslavsky
 * @since 1.0
 */
public interface Domain<E> extends
                           Comparator<E>,
                           Iterable<E>,
                           ToStringSupport<E>,
                           ElementParser<E>,
                           java.io.Serializable {
    /**
     * Returns whether this domain is a field
     *
     * @return whether this domain is a field
     */
    boolean isField();

    /**
     * Returns number of elements in this domain (cardinality) or null if domain is infinite
     *
     * @return number of elements in this domain (cardinality) or null if domain is infinite
     */
    BigInteger cardinality();

    /**
     * Returns characteristic of this domain
     *
     * @return characteristic of this domain
     */
    BigInteger characteristic();

    /**
     * Returns whether the cardinality is a perfect power
     *
     * @return whether the cardinality is a perfect power
     */
    boolean isPerfectPower();

    /**
     * Returns {@code base} so that {@code cardinality == base^exponent} or null if cardinality is not finite
     *
     * @return {@code base} so that {@code cardinality == base^exponent} or null if cardinality is not finite
     */
    BigInteger perfectPowerBase();

    /**
     * Returns {@code exponent} so that {@code cardinality == base^exponent} or null if cardinality is not finite
     *
     * @return {@code exponent} so that {@code cardinality == base^exponent} or null if cardinality is not finite
     */
    BigInteger perfectPowerExponent();

    /**
     * Returns whether this domain is finite
     *
     * @return whether this domain is finite
     */
    default boolean isFinite() {
        return cardinality() != null;
    }

    /**
     * Returns whether this domain is a finite finite
     *
     * @return whether this domain is a finite finite
     */
    default boolean isFiniteField() {
        return isField() && isFinite();
    }

    /**
     * Add two elements
     *
     * @param a the first element
     * @param b the second element
     * @return a + b
     */
    E add(E a, E b);

    /**
     * Sum the array of elements
     *
     * @param elements elements to sum
     * @return sum of the array
     */
    default E add(E... elements) {
        E r = elements[0];
        for (int i = 1; i < elements.length; i++)
            r = add(r, elements[i]);
        return r;
    }

    /**
     * Returns {@code element + 1}
     *
     * @param element the element
     * @return {@code element + 1}
     */
    default E increment(E element) {
        return add(element, getOne());
    }

    /**
     * Returns {@code element - 1}
     *
     * @param element the element
     * @return {@code element - 1}
     */
    default E decrement(E element) {
        return subtract(element, getOne());
    }

    /**
     * Subtracts {@code b} from {@code a}
     *
     * @param a the first element
     * @param b the second element
     * @return a - b
     */
    E subtract(E a, E b);

    /**
     * Multiplies two elements
     *
     * @param a the first element
     * @param b the second element
     * @return a * b
     */
    E multiply(E a, E b);

    /**
     * Multiplies the array of elements
     *
     * @param elements the elements
     * @return product of the array
     */
    default E multiply(E... elements) {
        E r = elements[0];
        for (int i = 1; i < elements.length; i++)
            r = multiply(r, elements[i]);
        return r;
    }

    /**
     * Negates the given element
     *
     * @param element the domain element
     * @return -val
     */
    E negate(E element);

    /**
     * Adds two elements and destroys the initial content of {@code a}.
     *
     * @param a the first element (may be destroyed)
     * @param b the second element
     * @return a + b
     */
    default E addMutable(E a, E b) {return add(a, b);}

    /**
     * Subtracts {@code b} from {@code a} and destroys the initial content of {@code a}
     *
     * @param a the first element (may be destroyed)
     * @param b the second element
     * @return a - b
     */
    default E subtractMutable(E a, E b) {return subtract(a, b);}

    /**
     * Multiplies two elements and destroys the initial content of {@code a}
     *
     * @param a the first element (may be destroyed)
     * @param b the second element
     * @return a * b
     */
    default E multiplyMutable(E a, E b) { return multiply(a, b);}

    /**
     * Negates the given element and destroys the initial content of {@code element}
     *
     * @param element the domain element (may be destroyed)
     * @return -element
     */
    default E negateMutable(E element) { return negate(element);}

    /**
     * Makes a deep copy of the specified element when element is mutable or returns element as is if it is represented
     * by immutable type.
     *
     * @param element the element
     * @return deep copy of specified element
     */
    E copy(E element);

    /**
     * Returns -1 if {@code element < 0}, 0 if {@code element == 0} and 1 if {@code element > 0}, where comparison is
     * specified by {@link #compare(Object, Object)}
     *
     * @param element the element
     * @return -1 if {@code element < 0}, 0 if {@code element == 0} and 1 otherwise
     */
    default int signum(E element) {
        return Integer.compare(compare(element, getZero()), 0);
    }

    /**
     * Returns quotient and remainder of {@code dividend / divider}
     *
     * @param dividend the dividend
     * @param divider  the divider
     * @return {@code {quotient, remainder}}
     */
    E[] divideAndRemainder(E dividend, E divider);

    /**
     * Returns the quotient of {@code dividend / divider}
     *
     * @param dividend the dividend
     * @param divider  the divider
     * @return the quotient of {@code dividend / divider}
     */
    default E quotient(E dividend, E divider) {
        return divideAndRemainder(dividend, divider)[0];
    }

    /**
     * Returns the remainder of {@code dividend / divider}
     *
     * @param dividend the dividend
     * @param divider  the divider
     * @return the remainder of {@code dividend / divider}
     */
    default E remainder(E dividend, E divider) {
        return divideAndRemainder(dividend, divider)[1];
    }

    /**
     * Divides {@code dividend} by {@code divider} or returns {@code null} if exact division is not possible
     *
     * @param dividend the dividend
     * @param divider  the divider
     * @return {@code dividend / divider} or {@code null} if exact division is not possible
     */
    default E divideOrNull(E dividend, E divider) {
        if (isOne(divider))
            return dividend;
        E[] qd = divideAndRemainder(dividend, divider);
        if (qd == null)
            return null;
        if (!isZero(qd[1]))
            return null;
        return qd[0];
    }

    /**
     * Divides {@code dividend} by {@code divider} or throws {@code ArithmeticException} if exact division is not possible
     *
     * @param dividend the dividend
     * @param divider  the divider
     * @return {@code dividend / divider}
     * @throws ArithmeticException if exact division is not possible
     */
    default E divideExact(E dividend, E divider) {
        E result = divideOrNull(dividend, divider);
        if (result == null)
            throw new ArithmeticException("not divisible: " + dividend + " / " + divider);
        return result;
    }

    /**
     * Gives the inverse element {@code element ^ (-1) }
     *
     * @param element the element
     * @return {@code element ^ (-1)}
     */
    E reciprocal(E element);

    /**
     * Returns greatest common divisor of two elements
     *
     * @param a the first element
     * @param b the second element
     * @return gcd
     */
    E gcd(E a, E b);

    /**
     * Returns least common multiple of two elements
     *
     * @param a the first element
     * @param b the second element
     * @return lcm
     */
    default E lcm(E a, E b) {
        if (isZero(a) || isZero(b))
            return getZero();
        return multiply(divideExact(a, gcd(a, b)), b);
    }

    /**
     * Returns greatest common divisor of specified elements
     *
     * @param elements the elements
     * @return gcd
     */
    default E gcd(E... elements) {
        return gcd(Arrays.asList(elements));
    }

    /**
     * Returns greatest common divisor of specified elements
     *
     * @param elements the elements
     * @return gcd
     */
    default E gcd(Iterable<E> elements) {
        E gcd = null;
        for (E e : elements) {
            if (gcd == null)
                gcd = e;
            else
                gcd = gcd(gcd, e);
        }
        return gcd;
    }

    /**
     * Returns zero element of this domain
     *
     * @return 0
     */
    E getZero();

    /**
     * Returns unit element of this domain (one)
     *
     * @return 1
     */
    E getOne();

    /**
     * Returns negative unit element of this domain (minus one)
     *
     * @return -1
     */
    default E getNegativeOne() {
        return negate(getOne());
    }

    /**
     * Tests whether specified element is zero
     *
     * @param element the domain element
     * @return whether specified element is zero
     */
    boolean isZero(E element);

    /**
     * Tests whether specified element is one (exactly)
     *
     * @param element the domain element
     * @return whether specified element is exactly one
     * @see #isUnit(Object)
     */
    boolean isOne(E element);

    /**
     * Tests whether specified element is a ring unit
     *
     * @param element the domain element
     * @return whether specified element is a ring unit
     * @see #isOne(Object)
     */
    boolean isUnit(E element);

    /**
     * Tests whether specified element is minus one
     *
     * @param e the domain element
     * @return whether specified element is minus one
     */
    default boolean isMinusOne(E e) {
        return negate(getOne()).equals(e);
    }

    /**
     * Returns domain element associated with specified {@code long}
     *
     * @param val machine integer
     * @return domain element associated with specified {@code long}
     */
    E valueOf(long val);

    /**
     * Returns domain element associated with specified integer
     *
     * @param val integer
     * @return domain element associated with specified integer
     */
    E valueOfBigInteger(BigInteger val);

    /**
     * Converts array of machine integers to domain elements via {@link #valueOf(long)}
     *
     * @param elements array of machine integers
     * @return array of domain elements
     */
    default E[] valueOf(long[] elements) {
        E[] array = createArray(elements.length);
        for (int i = 0; i < elements.length; i++)
            array[i] = valueOf(elements[i]);
        return array;
    }

    /**
     * Converts a value from other domain to this domain.
     *
     * @param val some element from any domain
     * @return this domain element associated with specified {@code val}
     */
    E valueOf(E val);

    /**
     * Applies {@link #valueOf(Object)} inplace to the specified array
     *
     * @param elements the array
     */
    default void setToValueOf(E[] elements) {
        for (int i = 0; i < elements.length; i++)
            elements[i] = valueOf(elements[i]);
    }

    /**
     * Parse string into domain element, by default throws {@code UnsupportedOperationException}
     *
     * @param string string
     * @return domain element
     */
    @Override
    default E parse(String string) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates generic array of domain elements of specified length
     *
     * @param length array length
     * @return array of domain elements of specified {@code length}
     */
    @SuppressWarnings("unchecked")
    default E[] createArray(int length) {
        return (E[]) Array.newInstance(getOne().getClass(), length);
    }

    /**
     * Creates 2d array of domain elements of specified length
     *
     * @param length array length
     * @return 2d array of domain elements of specified {@code length}
     */
    @SuppressWarnings("unchecked")
    default E[][] createArray2d(int length) {
        return (E[][]) Array.newInstance(createArray(0).getClass(), length);
    }

    /**
     * Creates 2d array of domain elements of specified shape
     *
     * @param m result length
     * @param n length of each array in the result
     * @return 2d array E[m][n]
     */
    @SuppressWarnings("unchecked")
    default E[][] createArray2d(int m, int n) {
        return (E[][]) Array.newInstance(getOne().getClass(), m, n);
    }

    /**
     * Creates array filled with zero elements
     *
     * @param length array length
     * @return array filled with zero elements of specified {@code length}
     */
    @SuppressWarnings("unchecked")
    default E[] createZeroesArray(int length) {
        E[] array = createArray(length);
        for (int i = 0; i < array.length; i++)
            // NOTE: getZero() is invoked each time in a loop in order to fill array with unique elements
            array[i] = getZero();
        return array;
    }

    /**
     * Creates generic array of {@code {a, b}}
     *
     * @param a the first element of array
     * @param b the second element of array
     * @return array {@code {a,b}}
     */
    @SuppressWarnings("unchecked")
    default E[] createArray(E a, E b) {
        E[] array = createArray(2);
        array[0] = a;
        array[1] = b;
        return array;
    }

    /**
     * Creates generic array with single element
     *
     * @param element the element
     * @return array with single specified element
     */
    @SuppressWarnings("unchecked")
    default E[] createArray(E element) {
        E[] array = createArray(1);
        array[0] = element;
        return array;
    }

    /**
     * Returns {@code base} in a power of {@code exponent} (non negative)
     *
     * @param base     base
     * @param exponent exponent (non negative)
     * @return {@code base} in a power of {@code exponent}
     */
    default E pow(E base, int exponent) {
        return pow(base, BigInteger.valueOf(exponent));
    }

    /**
     * Returns {@code base} in a power of {@code exponent} (non negative)
     *
     * @param base     base
     * @param exponent exponent (non negative)
     * @return {@code base} in a power of {@code exponent}
     */
    default E pow(E base, long exponent) {
        return pow(base, BigInteger.valueOf(exponent));
    }

    /**
     * Returns {@code base} in a power of {@code exponent} (non negative)
     *
     * @param base     base
     * @param exponent exponent (non negative)
     * @return {@code base} in a power of {@code exponent}
     */
    default E pow(E base, BigInteger exponent) {
        if (exponent.signum() < 0)
            throw new IllegalArgumentException();

        if (exponent.isOne())
            return base;

        E result = getOne();
        E k2p = copy(base); // <= copy the base (mutable operations are used below)
        for (; ; ) {
            if ((exponent.testBit(0)))
                result = multiplyMutable(result, k2p);
            exponent = exponent.shiftRight(1);
            if (exponent.isZero())
                return result;
            k2p = multiplyMutable(k2p, k2p);
        }
    }

    /**
     * Gives a product of {@code valueOf(1) * valueOf(2) * .... * valueOf(num) }
     *
     * @param num the number
     * @return {@code valueOf(1) * valueOf(2) * .... * valueOf(num) }
     */
    default E factorial(long num) {
        E result = getOne();
        for (int i = 2; i <= num; ++i)
            result = multiplyMutable(result, valueOf(i));
        return result;
    }

//    /**
//     * Returns the element which is next to the specified {@code element} (according to {@link #compare(Object, Object)})
//     * or {@code null} in the case of infinite cardinality
//     *
//     * @param element the element
//     * @return next element
//     */
//    E nextElement(E element);

    /**
     * Returns iterator over domain elements (for finite domains, otherwise throws exception)
     */
    @Override
    Iterator<E> iterator();

    /**
     * Returns a random element from this domain
     *
     * @param rnd the source of randomness
     * @return random element from this domain
     */
    default E randomElement(RandomGenerator rnd) { return valueOf(rnd.nextLong());}

    /**
     * Returns a random non zero element from this domain
     *
     * @param rnd the source of randomness
     * @return random non zero element from this domain
     */
    default E randomNonZeroElement(RandomGenerator rnd) {
        E el;
        do {
            el = randomElement(rnd);
        } while (isZero(el));
        return el;
    }

    @Override
    default String toString(E element) {
        return element.toString();
    }

//    /**
//     * Returns domain with larger cardinality that contains all elements of this or null if there is no such domain.
//     *
//     * @return domain with larger cardinality that contains all elements of this or null if there is no such domain
//     */
//    Domain<E> getExtension();
}
