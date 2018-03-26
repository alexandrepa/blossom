package com.blossomproject.core.common.dto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractAssociationDTOTest {

  @Test
  public void should_set_and_get_A() {
    AbstractAssociationImpl impl = new AbstractAssociationImpl();

    AbstractDTO abstractDTO = mock(AbstractDTO.class);
    impl.setA(abstractDTO);
    AbstractDTO result = impl.getA();
    assertEquals(abstractDTO, result);
  }

  @Test
  public void should_set_and_get_B() {
    AbstractAssociationImpl impl = new AbstractAssociationImpl();

    AbstractDTO abstractDTO = mock(AbstractDTO.class);
    impl.setB(abstractDTO);
    AbstractDTO result = impl.getB();
    assertEquals(abstractDTO, result);
  }

  private class AbstractAssociationImpl extends AbstractAssociationDTO {
    public AbstractAssociationImpl() {
    }
  }
}
