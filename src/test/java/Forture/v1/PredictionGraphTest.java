package Forture.v1;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;

import Forture.v1.analytics.StockTool;
import Forture.v1.predict_sys.PredictionGraph;
import Forture.v1.reg_sys.StockGraph;

public class PredictionGraphTest
{
    PredictionGraph graph = new PredictionGraph("MSFT", 0.27);
    ArrayList<LocalDateTime> dates = new ArrayList<>();
    ArrayList<Double> vals = stockValues("MSFT");
    boolean predict = false;
    double max = Double.MIN_VALUE;
    double min = Double.MAX_VALUE;
    public ArrayList<Double> stockValues(String stockSymbol)
    {
        ArrayList<Double> values = new ArrayList<>();
        int iLoveYou = 3000;
        AlphaVantageConnector apiConnection = new AlphaVantageConnector( StockTool.API_KEY, iLoveYou );
        TimeSeries stockTimeSeries = new TimeSeries( apiConnection );
        //
        try
        {
            IntraDay response = stockTimeSeries.intraDay( stockSymbol, Interval.FIFTEEN_MIN, OutputSize.COMPACT );
            Map<String, String> metaData = response.getMetaData();

            List<StockData> stockData = response.getStockData();
            stockData.forEach( stock -> {
                dates.add( stock.getDateTime() );
                values.add( stock.getClose() ); 
            } );
        }
        catch ( AlphaVantageException e )
        {}
        
        return values;
    }
    

    @Test
    public void testConstructor()
    {
        assertEquals("MSFT", graph.getTitle().toUpperCase());
        assertTrue(graph.getPredict());
        assertEquals(0.27, graph.getForecast(), 0);
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
        assertEquals(Math.min( graph.getForecast(), minValue ), graph.minValue() + 2, 0.01);
        assertEquals(Math.max( graph.getForecast(), maxValue ), graph.maxValue() - 2, 0.01);
    }
    
    @Test
    public void testGetForecast()
    {
        assertEquals(0.27, graph.getForecast(), 0);
    }

}
