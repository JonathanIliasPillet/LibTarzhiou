package net.chouppy.tarzhiou;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for net.chouppy.tarzhiou");
    //$JUnit-BEGIN$
    suite.addTest(BaseTestSuite.suite());
    //$JUnit-END$
    return suite;
  }

}
