package Forture.v1;

import static org.junit.Assert.*;

import javax.swing.JPanel;

import org.junit.Test;

import Forture.v1.reg_sys.StockGraph;
import Forture.v1.reg_sys.ValuePoint;

public class ValuePointManagerTest
{
    
    ValuePointManager mgr = new ValuePointManager();
    ValuePoint p = new ValuePoint(100, 100, 10, null, new StockGraph());
    
    @Test
    public void testNearestPoint()
    {
        mgr.register(p);
        assertEquals(p.getX(), mgr.getNearestPoint( 100, 100 ).getX(), 0.01);
    }
    
    @Test
    public void testRegister()
    {
        mgr.register(p);
        assertNotNull(mgr.getNearestPoint( 100, 100 ));
    }
    
    @Test
    public void testEmptyPoints()
    {
        mgr.register(p);
        assertNotNull(mgr.getNearestPoint( 100, 100 ));
        mgr.emptyPoints();
        assertNull(mgr.getNearestPoint( 100, 100 ));
    }

}
