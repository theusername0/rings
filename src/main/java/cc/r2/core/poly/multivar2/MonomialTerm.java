package cc.r2.core.poly.multivar2;

import cc.r2.core.poly.Domain;
import cc.r2.core.util.ArraysUtil;

/**
 * @author Stanislav Poslavsky
 * @since 1.0
 */
public final class MonomialTerm<E> extends DegreeVector<MonomialTerm<E>> {
    /** the factor */
    final E coefficient;

    MonomialTerm(int[] exponents, int totalDegree, E coefficient) {
        super(exponents, totalDegree);
        this.coefficient = coefficient;
    }

    MonomialTerm(int nVariables, int position, int exponent, E coefficient) {
        super(nVariables, position, exponent);
        this.coefficient = coefficient;
    }

    MonomialTerm(int[] exponents, E coefficient) {
        this(exponents, ArraysUtil.sum(exponents), coefficient);
    }

    @Override
    protected MonomialTerm<E> setDegreeVector(int[] newDegree, int newTotalDegree) {
        return new MonomialTerm<E>(newDegree, newTotalDegree, coefficient);
    }

    MonomialTerm<E> setDomain(Domain<E> newDomain) {
        E e = newDomain.valueOf(coefficient);
        return coefficient == e ? this : new MonomialTerm<>(exponents, totalDegree, e);
    }

    public MonomialTerm<E> setCoefficient(E value) {
        return new MonomialTerm<>(exponents, totalDegree, value);
    }

    public MonomialTerm<E> multiply(MonomialTerm<E> oth, E coefficient) {
        int[] newExponents = new int[exponents.length];
        for (int i = 0; i < exponents.length; i++)
            newExponents[i] = exponents[i] + oth.exponents[i];
        return new MonomialTerm<E>(newExponents, totalDegree + oth.totalDegree, coefficient);
    }

    public MonomialTerm<E> divide(MonomialTerm<E> oth, E coefficient) {
        int[] newExponents = new int[exponents.length];
        for (int i = 0; i < exponents.length; i++) {
            newExponents[i] = exponents[i] - oth.exponents[i];
            if (newExponents[i] < 0)
                return null;
        }
        return new MonomialTerm<>(newExponents, totalDegree - oth.totalDegree, coefficient);
    }

    MonomialTerm<E> without(int i) {
        return without(i, coefficient);
    }

    MonomialTerm<E> without(int i, E coefficient) {
        if (exponents.length == 1) {
            assert i == 0;
            return new MonomialTerm<>(new int[0], 0, coefficient);
        }
        return new MonomialTerm<>(ArraysUtil.remove(exponents, i), totalDegree - exponents[i], coefficient);
    }

    @Override
    MonomialTerm<E> setZero(int i) {
        return setZero(i, coefficient);
    }

    MonomialTerm<E> setZero(int i, E coefficient) {
        if (exponents.length == 1) {
            assert i == 0;
            return new MonomialTerm<>(new int[0], 0, coefficient);
        }
        int[] newExponents = exponents.clone();
        newExponents[i] = 0;
        return new MonomialTerm<>(newExponents, totalDegree - exponents[i], coefficient);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MonomialTerm<?> that = (MonomialTerm<?>) o;

        return coefficient != null ? coefficient.equals(that.coefficient) : that.coefficient == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (coefficient != null ? coefficient.hashCode() : 0);
        return result;
    }

    public static <E> MonomialTerm<E> withZeroExponents(int nVariables, E coefficient) {
        int[] exponents = nVariables < zeroDegreeVectors.length ? zeroDegreeVectors[nVariables] : new int[nVariables];
        return new MonomialTerm<>(exponents, 0, coefficient);
    }
}
