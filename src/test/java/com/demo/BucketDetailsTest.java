package com.demo;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class BucketDetailsTest {

    //TODO: Add more test for each of the functionality.
  @Test
  public void test() throws IOException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    new BucketDetails().doGet(null, response);
    Assert.assertEquals("text/html;charset=UTF-8", response.getContentType());
    Assert.assertEquals(2952, response.getWriterContent().toString().length());
  }
}
