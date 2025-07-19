package com.tn.query.java;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import jakarta.annotation.Nonnull;

import com.tn.query.PredicateFactory;
import com.tn.query.QueryException;

public class JavaPredicateFactory<T> implements PredicateFactory<Predicate<T>>
{
  private static final String REGEX_ANY = ".*";
  private static final String WILDCARD = "*";

  private final Map<String, Function<T, ?>> getters;

  public JavaPredicateFactory(Collection<Getter<T>> getters)
  {
    this.getters = getters.stream().collect(toMap(Getter::name, getter -> getter::get));
  }

  @Override
  public Predicate<T> equal(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> Objects.equals(getter.apply(target), right);
  }

  @Override
  public Predicate<T> notEqual(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> !Objects.equals(getter.apply(target), right);
  }

  @Override
  public Predicate<T> greaterThan(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) > 0;
  }

  @Override
  public Predicate<T> greaterThanOrEqual(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) >= 0;
  }

  @Override
  public Predicate<T> lessThan(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) < 0;
  }

  @Override
  public Predicate<T> lessThanOrEqual(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> compare(getter.apply(target), right) <= 0;
  }

  @Override
  public Predicate<T> like(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> like(getter.apply(target), right);
  }

  @Override
  public Predicate<T> notLike(String left, Object right)
  {
    Function<T, ?> getter = getter(left);
    return target -> !like(getter.apply(target), right);
  }

  @Override
  public Predicate<T> in(String left, List<?> right)
  {
    Function<T, ?> getter = getter(left);
    return target -> right.contains(getter.apply(target));
  }

  @Override
  public Predicate<T> and(Predicate<T> left, Predicate<T> right)
  {
    return value -> left.test(value) && right.test(value);
  }

  @Override
  public Predicate<T> or(Predicate<T> left, Predicate<T> right)
  {
    return value -> left.test(value) || right.test(value);
  }

  @Override
  public Predicate<T> parenthesis(Predicate<T> node)
  {
    //Parenthesis is handled implicitly when parsing queries.
    return node;
  }

  private <T1> int compare(T1 obj1, T1 obj2)
  {
    try
    {
      if (!(obj1 instanceof Comparable)) throw new QueryException("Cannot compare: " + obj1);
      if (!(obj2 instanceof Comparable)) throw new QueryException("Cannot compare: " + obj2);

      //noinspection unchecked
      return ((Comparable<T1>)obj1).compareTo(obj2);
    }
    catch (ClassCastException e)
    {
      throw new QueryException("Type mismatch: " + obj1 + " and " + obj2);
    }
  }

  private @Nonnull Function<T, ?> getter(String left)
  {
    Function<T, ?> getter = this.getters.get(left);
    if (getter == null) throw new QueryException("Getter missing for: " + left);

    return getter;
  }

  private boolean like(Object left, Object right)
  {
    if (!(left instanceof String)) throw new QueryException("Like comparisons only work for string values, received: " + left);
    if (!(right instanceof String)) throw new QueryException("Like comparisons only work for string values, received: " + right);

    return ((String)left).matches(((String)right).replace(WILDCARD, REGEX_ANY));
  }
}
