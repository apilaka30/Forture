package Forture.v1;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Test;

import Forture.v1.reg_sys.Crosshairs;
import Forture.v1.reg_sys.StockGraph;

public class CrosshairsTest
{
    ArrayList<Point2D.Double> list = new ArrayList<>();


    @Test
    public void testConstructor()
    {
        list.add(new Point2D.Double(0, 0));
        Crosshairs c = new Crosshairs(20, 30, 20);
        assertEquals(100 ,c.getX(), 0);
        assertEquals(100 ,c.getY(), 0);
    }

}
