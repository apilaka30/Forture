package Forture.v1;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import Forture.v1.analytics.ArticleRating;

public class ArticleRatingTest
{

    File file = new File("src/main/resources/Article.txt");
    ArticleRating rating = new ArticleRating("MSFT");
    
    
    @Test
    public void testConstructor()
    {
        assertEquals(file, rating.getFile());
    }
    
    @Test
    public void testGetRating()
    {
        assertTrue(rating.getRating() != Double.NaN);
    }
    
    @Test
    public void testTranslateToUrl()
    {
        assertTrue(rating.translateToURL( '&' ).equals( "%26" ));
    }

}
