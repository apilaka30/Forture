package Forture.v1;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.junit.Assert;
import org.junit.Test;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;

import Forture.v1.analytics.StockTool;
import Forture.v1.reg_sys.StockGraph;


public class StockGraphTest
{

    ArrayList<LocalDateTime> dates = new ArrayList<>();

    String symbol = "MSFT";

    StockGraph s = new StockGraph( symbol, false );

    ArrayList<Double> vals = stockValues( symbol );

    boolean predict = false;

    double max = Double.MIN_VALUE;

    double min = Double.MAX_VALUE;


    public ArrayList<Double> stockValues( String stockSymbol )
    {
        ArrayList<Double> values = new ArrayList<>();
        int iLoveYou = 3000;
        AlphaVantageConnector apiConnection = new AlphaVantageConnector( StockTool.API_KEY,
            iLoveYou );
        TimeSeries stockTimeSeries = new TimeSeries( apiConnection );
        //
        try
        {
            IntraDay response = stockTimeSeries
                .intraDay( stockSymbol, Interval.FIFTEEN_MIN, OutputSize.COMPACT );
            Map<String, String> metaData = response.getMetaData();

            List<StockData> stockData = response.getStockData();
            stockData.forEach( stock -> {
                dates.add( stock.getDateTime() );
                values.add( stock.getClose() );
            } );
        }
        catch ( AlphaVantageException e )
        {
        }

        return values;
    }

    @Test
    public void testGetPredict()
    {
        assertTrue(s.getPredict() == false);
    }
    
    @Test
    public void testGetValues()
    {
        int index = (int) (Math.random() * vals.size());
        assertEquals(vals.get( index ), s.getValues().get( index ));
    }

    @Test
    public void testGetForecast()
    {
        assertEquals( 0.0, s.getForecast(), 0 );
    }


    @Test
    public void testMinMaxValue()
    {
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        for ( Double value : vals )
        {
            if ( value < minValue )
                minValue = value;
            else if ( value > maxValue )
                maxValue = value;
        }
        assertEquals( minValue, s.minValue() + 2, 0.01 );
        assertEquals( maxValue, s.maxValue() - 2, 0.01 );
    }


    @Test
    public void testConstructor()
    {
        assertTrue( "", s.getPredict() == predict && s.getTitle().equals( symbol ) );
    }


    @Test
    public void testStockValues()
    {
        assertEquals( vals, s.getValues() );
    }


    @Test
    public void testForecast()
    {
        assertEquals( 0.0, s.getForecast(), 0 );
    }


    @Test
    public void testAROC()
    {
        double x0 = 5.2;
        double y0 = 20.6;
        double x1 = 10.2;
        double y1 = 100.0;
        assertEquals( 15.88, s.AROC( x0, y0, x1, y1 ), 0.001 );
    }


    @Test
    public void testPredict()
    {
        assertTrue( predict == s.getPredict() );
    }

}