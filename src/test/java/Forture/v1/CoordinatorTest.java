package Forture.v1;

import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.junit.Test;

public class CoordinatorTest
{

    Coordinator c = new Coordinator();
    @Test
    public void testStart()
    {
        c.start();
        JFrame frame = c.getSystem().getInstance();
        assertNotNull( frame );
    }
    
    @Test
    public void testGetSystem()
    {
        assertNotNull(c.getSystem());
    }

}
