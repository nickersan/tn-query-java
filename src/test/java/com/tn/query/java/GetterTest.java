package com.tn.query.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GetterTest
{
  private static final Object target = new Object();

  @Test
  void shouldGetBoolean()
  {
    Getter<Object> getter = Getter.booleanValue("booleanValue", object -> object.equals(target));

    assertEquals("booleanValue", getter.name());
    assertEquals(true, getter.get(target));
  }

  @Test
  void shouldGetByte()
  {
    Getter<Object> getter = Getter.byteValue("byteValue", object -> object.equals(target) ? (byte)0 : (byte)1);

    assertEquals("byteValue", getter.name());
    assertEquals((byte)0, getter.get(target));
  }

  @Test
  void shouldGetChar()
  {
    Getter<Object> getter = Getter.charValue("charValue", object -> object.equals(target) ? 'y' : 'n');

    assertEquals("charValue", getter.name());
    assertEquals('y', getter.get(target));
  }

  @Test
  void shouldGetComparable()
  {
    Getter<Object> getter = Getter.comparableValue("comparableValue", object -> object.equals(target) ? 0 : 1);

    assertEquals("comparableValue", getter.name());
    assertEquals(0, getter.get(target));
  }

  @Test
  void shouldGetDouble()
  {
    Getter<Object> getter = Getter.doubleValue("doubleValue", object -> object.equals(target) ? 0d : 1d);

    assertEquals("doubleValue", getter.name());
    assertEquals(0d, getter.get(target));
  }

  @Test
  void shouldGetFloat()
  {
    Getter<Object> getter = Getter.floatValue("floatValue", object -> object.equals(target) ? 0f : 1f);

    assertEquals("floatValue", getter.name());
    assertEquals(0f, getter.get(target));
  }

  @Test
  void shouldGetInt()
  {
    Getter<Object> getter = Getter.intValue("intValue", object -> object.equals(target) ? 0 : 1);

    assertEquals("intValue", getter.name());
    assertEquals(0, getter.get(target));
  }

  @Test
  void shouldGetLong()
  {
    Getter<Object> getter = Getter.longValue("longValue", object -> object.equals(target) ? 0L : 1L);

    assertEquals("longValue", getter.name());
    assertEquals(0L, getter.get(target));
  }

  @Test
  void shouldGetShort()
  {
    Getter<Object> getter = Getter.shortValue("shortValue", object -> object.equals(target) ? (short)0 : (short)1);

    assertEquals("shortValue", getter.name());
    assertEquals((short)0, getter.get(target));
  }
}
