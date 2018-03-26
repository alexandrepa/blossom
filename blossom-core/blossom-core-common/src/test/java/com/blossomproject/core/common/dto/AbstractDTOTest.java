package com.blossomproject.core.common.dto;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractDTOTest {

  @Test
  public void should_be_equals() {
    AbstractDTO impl = new AbstractDTOImpl();
    boolean result = impl.equals(impl);
    assertTrue(result);
  }

  @Test
  public void should_not_be_equals() {
    AbstractDTO impl = new AbstractDTOImpl();
    AbstractDTO impl2 = new AbstractDTOImpl();
    boolean result = impl.equals(impl2);
    assertFalse(result);
  }

  @Test
  public void should_not_be_equals_with_null() {
    AbstractDTO impl = new AbstractDTOImpl();
    boolean result = impl.equals(null);
    assertFalse(result);
  }

  @Test
  public void should_not_be_equals_with_null_id() {
    AbstractDTO impl = new AbstractDTOImpl();
    AbstractDTO impl2 = new AbstractDTOImpl();
    impl2.setId(null);
    boolean result = impl.equals(impl2);
    assertFalse(result);
  }

  @Test
  public void should_not_be_equals_with_own_null_id() {
    AbstractDTO impl = new AbstractDTOImpl();
    AbstractDTO impl2 = new AbstractDTOImpl();
    impl.setId(null);
    boolean result = impl.equals(impl2);
    assertFalse(result);
  }

  @Test
  public void should_be_equals_with_same_id() {
    AbstractDTO impl = new AbstractDTOImpl();
    AbstractDTO impl2 = new AbstractDTOImpl();
    impl.setId(1L);
    impl2.setId(1L);
    boolean result = impl.equals(impl2);
    assertTrue(result);
  }

  @Test
  public void should_return_hashcode() {
    AbstractDTO impl = new AbstractDTOImpl();
    impl.setId(1L);
    int result = impl.hashCode();
    assertEquals(1, result);
  }

  @Test
  public void should_return_hashcode_with_null_id() {
    AbstractDTO impl = new AbstractDTOImpl();
    int result = impl.hashCode();
    assertEquals(0, result);
  }

  private class AbstractDTOImpl extends AbstractDTO {
    public AbstractDTOImpl() {
    }
  }
}
