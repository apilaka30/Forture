package Forture.v1;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.patriques.output.timeseries.data.StockData;

import Forture.v1.predict_sys.PredictionSystem;
import Forture.v1.reg_sys.StockGraph;

public class PredictionSystemTest
{
    PredictionSystem s = new PredictionSystem();
    ActionEvent e;

    @Test
    public void testConstructor()
    {
        assertTrue( "", s.getRating() != s.getForecast() );
    }
    
    @Test
    public void testMakeChangedItem()
    {
        assertTrue(s
            .makeChangedItem()
            .getText()
            .equals( "Open Quote System" ));
    }
    
    @Test
    public void testAnalyze()
    {
        List<StockData> list = new ArrayList<StockData>();
        list.add( new StockData(null, 0, 0, 0, 90.0, 0 ) );
        list.add( new StockData(null, 0, 0, 0, 100.0, 0 ) );
        list.add( new StockData(null, 0, 0, 0, 120.0, 0 ) );
        list.add( new StockData(null, 0, 0, 0, 110.0, 0 ) );
        list.add( new StockData(null, 0, 0, 0, 130.0, 0 ) );
        list.add( new StockData(null, 0, 0, 0, 140.0, 0 ) );
        double forecast = s.analyze(list, "MSFT", 140.0, 90.0, 0, 5);
        assertTrue(forecast != -2);
    }

    
    @Test
    public void testProcessGraph()
    {
        s.textField.setText( "MICROSOFT" );
        s.processGraph();
        assertNotNull(s.getForecast());
    }
    
    
    @Test
    public void testProcessQuote()
    {
        s.textField.setText( "MSFT" );
        s.processQuote();
        assertTrue(s.textArea.getText().contains( "MICROSOFT" ));
    }
    
    @Test
    public void testAROC()
    {
        assertEquals( 4.00, s.AROC( 10.6, 10.0, 20.6, 50.0 ), 0.001 );
    }
    
}