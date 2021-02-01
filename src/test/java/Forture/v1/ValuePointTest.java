package Forture.v1;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import javax.swing.JPanel;

import org.junit.Assert;
import org.junit.Test;
import org.patriques.output.timeseries.data.StockData;

import Forture.v1.reg_sys.ValuePoint;

public class ValuePointTest
{
    JPanel panel = new JPanel();
    ValuePoint p = new ValuePoint(100, 101, 20, null, null);

    @Test
    public void testConstructor()
    {
        assertEquals(100, p.getX(), 0.01);
        assertEquals(101, p.getY(), 0.01);
        assertNull( p.getData() );
        assertNull(p.getParComponent());
    }
    
    @Test
    public void testDisplayInfo()
    {
        assertEquals("", 0, 0);
    }
    
    @Test
    public void testGetX()
    {
        assertEquals(100, p.getX(), 0.01);
    }
    
    @Test
    public void testGetY()
    {
        assertEquals(101, p.getY(), 0.01);
    }
    
    @Test
    public void testGetData()
    {
        assertNull( p.getData() );
    }
    
    @Test
    public void testGetParComponent()
    {
        assertNull(p.getParComponent());
    }

}
