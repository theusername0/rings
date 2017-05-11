package cc.r2.core.poly;

import java.util.Collection;

/**
 * @author Stanislav Poslavsky
 * @since 1.0
 */
public interface IGeneralPolynomial<Poly extends IGeneralPolynomial<Poly>> {
    /**
     * Return the degree of this
     *
     * @return the degree
     */
    int degree();

    /**
     * Returns {@code true} if this is zero
     *
     * @return whether {@code this} is zero
     */
    boolean isZero();

    /**
     * Returns {@code true} if this is one
     *
     * @return whether {@code this} is one
     */
    boolean isOne();

    /**
     * Returns {@code true} if this polynomial is monic
     *
     * @return whether {@code this} is monic
     */
    boolean isMonic();

    /**
     * Returns true if constant term is a unit
     *
     * @return whether constant term is unit
     */
    boolean isUnitCC();

    /**
     * Returns {@code true} if this polynomial has only constant term
     *
     * @return whether {@code this} is constant
     */
    boolean isConstant();

    /**
     * Returns {@code true} if this polynomial has only one monomial term
     *
     * @return whether {@code this} has the form {@code c*x^i} (one term)
     */
    boolean isMonomial();

    /**
     * Returns whether the coefficient domain of this polynomial is a field
     *
     * @return whether the coefficient domain of this polynomial is a field
     */
    boolean isOverField();

    /**
     * Sets {@code this} to its monic part (that is {@code this} divided by its leading coefficient), or returns
     * {@code null} (causing loss of internal data) if some of the elements can't be exactly
     * divided by the {@code lc()}. NOTE: is {@code null} is returned, the content of {@code this} is destroyed.
     *
     * @return monic {@code this} or {@code null}
     */
    Poly monic();

    /**
     * Gives signum of leading coefficient
     *
     * @return signum of leading coefficient
     */
    int signum();

    /**
     * Sets this to zero
     *
     * @return this := zero
     */
    Poly toZero();

    /**
     * Sets the content of this to {@code oth}
     *
     * @param oth the polynomial
     * @return this := oth
     */
    Poly set(Poly oth);

    /**
     * Reduces poly to its primitive part (primitive part will always have positive l.c.)
     *
     * @return primitive part (poly will be modified)
     */
    Poly primitivePart();

    /**
     * Reduces poly to its primitive part preserving signum of l.c.
     *
     * @return primitive part (poly will be modified)
     */
    Poly primitivePartSameSign();

    /**
     * Adds 1 to this
     *
     * @return {@code this + 1}
     */
    Poly increment();

    /**
     * Subtracts 1 from this
     *
     * @return {@code this - 1}
     */
    Poly decrement();

    /**
     * Returns 0 (new instance)
     *
     * @return new instance of 0
     */
    Poly createZero();

    /**
     * Returns 1 (new instance)
     *
     * @return new instance of 1
     */
    Poly createOne();

    /**
     * Adds {@code oth} to {@code this}.
     *
     * @param oth the polynomial
     * @return {@code this + oth}
     */
    Poly add(Poly oth);

    /**
     * Subtracts {@code oth} from {@code this}.
     *
     * @param oth the polynomial
     * @return {@code this - oth}
     */
    Poly subtract(Poly oth);

    /**
     * Negates this and returns
     *
     * @return this negated
     */
    Poly negate();

    /**
     * Sets this to {@code this * oth }
     *
     * @param oth the polynomial
     * @return {@code this * oth }
     */
    Poly multiply(Poly oth);

    @SuppressWarnings("unchecked")
    default Poly multiply(Poly... oth) {
        for (Poly t : oth)
            multiply(t);
        return (Poly) this;
    }

    @SuppressWarnings("unchecked")
    default Poly multiply(Collection<Poly> oth) {
        for (Poly t : oth)
            multiply(t);
        return (Poly) this;
    }

    /**
     * Raises {@code this} by the {@code factor}
     *
     * @param factor the factor
     * @return {@code} this multiplied by the {@code factor}
     */
    Poly multiply(long factor);

    /**
     * Square of {@code this}
     *
     * @return {@code this * this}
     */
    Poly square();
}
