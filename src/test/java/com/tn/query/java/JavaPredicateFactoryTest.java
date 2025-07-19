package com.tn.query.java;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.tn.query.PredicateFactory;
import com.tn.query.QueryException;

class JavaPredicateFactoryTest
{
  @Test
  void shouldMatchEqual()
  {
    shouldMatchPredicate("X", (predicateFactory, left) -> predicateFactory.equal(left, "X"));
    shouldNotMatchPredicate("X", (predicateFactory, left) -> predicateFactory.equal(left, "Y"));
  }

  @Test
  void shouldMatchNotEqual()
  {
    shouldMatchPredicate("X", (predicateFactory, left) -> predicateFactory.notEqual(left, "Y"));
    shouldNotMatchPredicate("X", (predicateFactory, left) -> predicateFactory.notEqual(left, "X"));
  }

  @Test
  void shouldMatchGreaterThan()
  {
    shouldMatchPredicate(1, (predicateFactory, left) -> predicateFactory.greaterThan(left, 0));
    shouldNotMatchPredicate(1, (predicateFactory, left) -> predicateFactory.greaterThan(left, 1));
    shouldFailWithException(new Object(), (predicateFactory, left) -> predicateFactory.greaterThan(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.greaterThan(left, new Object()));
    shouldFailWithException("X", (predicateFactory, left) -> predicateFactory.greaterThan(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.greaterThan(left, "X"));
  }

  @Test
  void shouldMatchGreaterThanOrEqual()
  {
    shouldMatchPredicate(1, (predicateFactory, left) -> predicateFactory.greaterThanOrEqual(left, 0));
    shouldMatchPredicate(1, (predicateFactory, left) -> predicateFactory.greaterThanOrEqual(left, 1));
    shouldNotMatchPredicate(1, (predicateFactory, left) -> predicateFactory.greaterThanOrEqual(left, 2));
    shouldFailWithException(new Object(), (predicateFactory, left) -> predicateFactory.greaterThanOrEqual(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.greaterThanOrEqual(left, new Object()));
    shouldFailWithException("X", (predicateFactory, left) -> predicateFactory.greaterThanOrEqual(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.greaterThanOrEqual(left, "X"));
  }

  @Test
  void shouldMatchLessThan()
  {
    shouldMatchPredicate(1, (predicateFactory, left) -> predicateFactory.lessThan(left, 2));
    shouldNotMatchPredicate(1, (predicateFactory, left) -> predicateFactory.lessThan(left, 1));
    shouldFailWithException(new Object(), (predicateFactory, left) -> predicateFactory.lessThan(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.lessThan(left, new Object()));
    shouldFailWithException("X", (predicateFactory, left) -> predicateFactory.lessThan(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.lessThan(left, "X"));
  }

  @Test
  void shouldMatchLessThanOrEqual()
  {
    shouldMatchPredicate(1, (predicateFactory, left) -> predicateFactory.lessThanOrEqual(left, 1));
    shouldMatchPredicate(1, (predicateFactory, left) -> predicateFactory.lessThanOrEqual(left, 1));
    shouldNotMatchPredicate(1, (predicateFactory, left) -> predicateFactory.lessThanOrEqual(left, 0));
    shouldFailWithException(new Object(), (predicateFactory, left) -> predicateFactory.lessThanOrEqual(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.lessThanOrEqual(left, new Object()));
    shouldFailWithException("X", (predicateFactory, left) -> predicateFactory.lessThanOrEqual(left, 0));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.lessThanOrEqual(left, "X"));
  }

  @Test
  void shouldMatchLike()
  {
    shouldMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.like(left, "Tes*"));
    shouldMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.like(left, "*est"));
    shouldMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.like(left, "T*st"));
    shouldNotMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.like(left, "X*"));
    shouldFailWithException("Test", (predicateFactory, left) -> predicateFactory.like(left, 1));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.like(left, "Test"));
  }

  @Test
  void shouldMatchNotLike()
  {
    shouldMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.notLike(left, "X*"));
    shouldNotMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.notLike(left, "Tes*"));
    shouldNotMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.notLike(left, "*est"));
    shouldNotMatchPredicate("Test", (predicateFactory, left) -> predicateFactory.notLike(left, "T*st"));
    shouldFailWithException("Test", (predicateFactory, left) -> predicateFactory.notLike(left, 1));
    shouldFailWithException(1, (predicateFactory, left) -> predicateFactory.notLike(left, "Test"));
  }

  @Test
  void shouldMatchIn()
  {
    shouldMatchPredicate("A", (predicateFactory, left) -> predicateFactory.in(left, List.of("A", "B", "C")));
    shouldNotMatchPredicate("Z", (predicateFactory, left) -> predicateFactory.in(left, List.of("A", "B", "C")));
  }

  @Test
  void shouldMatchAnd()
  {
    shouldMatchPredicate("A", (predicateFactory, left) -> predicateFactory.and(target -> true, target -> true));
    shouldNotMatchPredicate("A", (predicateFactory, left) -> predicateFactory.and(target -> false, target -> true));
    shouldNotMatchPredicate("A", (predicateFactory, left) -> predicateFactory.and(target -> true, target -> false));
    shouldNotMatchPredicate("A", (predicateFactory, left) -> predicateFactory.and(target -> false, target -> false));
  }

  @Test
  void shouldMatchOr()
  {
    shouldMatchPredicate("A", (predicateFactory, left) -> predicateFactory.or(target -> true, target -> true));
    shouldMatchPredicate("A", (predicateFactory, left) -> predicateFactory.or(target -> false, target -> true));
    shouldMatchPredicate("A", (predicateFactory, left) -> predicateFactory.or(target -> true, target -> false));
    shouldNotMatchPredicate("A", (predicateFactory, left) -> predicateFactory.or(target -> false, target -> false));
  }

  @Test
  void shouldMatchWithParenthesis()
  {
    shouldMatchPredicate("A", (predicateFactory, left) -> predicateFactory.parenthesis(target -> true));
    shouldNotMatchPredicate("A", (predicateFactory, left) -> predicateFactory.parenthesis(target -> false));
  }

  @Test
  void shouldFailWhenGetterMissing()
  {
    shouldFailWithException("Test", (predicateFactory, left) -> predicateFactory.equal("Missing", "Test"));
  }

  private <T> void shouldMatchPredicate(T actualValue, BiFunction<PredicateFactory<Predicate<Object>>, String, Predicate<Object>> predicateFactoryInvocation)
  {
    Object target = new Object();

    assertTrue(predicateFactoryInvocation.apply(new JavaPredicateFactory<>(List.of(mockGetter(target, actualValue))), "value").test(target));
  }

  private void shouldNotMatchPredicate(Object actualValue, BiFunction<PredicateFactory<Predicate<Object>>, String, Predicate<Object>> predicateFactoryInvocation)
  {
    Object target = new Object();

    assertFalse(predicateFactoryInvocation.apply(new JavaPredicateFactory<>(List.of(mockGetter(target, actualValue))), "value").test(target));
  }

  private void shouldFailWithException(Object actualValue, BiFunction<PredicateFactory<Predicate<Object>>, String, Predicate<Object>> predicateFactoryInvocation)
  {
    Object target = new Object();

    assertThrows(QueryException.class, () -> predicateFactoryInvocation.apply(new JavaPredicateFactory<>(List.of(mockGetter(target, actualValue))), "value").test(target));
  }

  private Getter<Object> mockGetter(Object target, Object value)
  {
    @SuppressWarnings("unchecked")
    Getter<Object> getter = mock(Getter.class);
    when(getter.name()).thenReturn("value");
    when(getter.get(target)).thenReturn(value);

    return getter;
  }
}
