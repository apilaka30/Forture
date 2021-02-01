package Forture.v1.analytics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.script.*;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aylien.newsapi.ApiClient;
import com.aylien.newsapi.ApiException;
import com.aylien.newsapi.Configuration;
import com.aylien.newsapi.api.DefaultApi;
import com.aylien.newsapi.auth.ApiKeyAuth;
import com.aylien.newsapi.models.Stories;
import com.aylien.newsapi.models.Story;
import com.aylien.newsapi.parameters.StoriesParams;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 *  Determines the score of a company by using articles to see if they are
 *  positive, negative, or neutral.
 *
 *  @author  Ananth Pilaka, Chinmay Gowdru, Snehith Nayak
 *  @version May 26, 2019
 *  @author  Period: 1
 *  @author  Assignment: The Amazing Stock Steroid
 *
 *  @author  Sources: Our Team
 */
public class ArticleRating 
{
    private final String API_KEY = "bcf66ab6c4msh2505ec7a8b920b6p1e6418jsn8b4838121d98";
    private File file = null;
    public String fileText = "", symbol = "";
    private Scanner scanner;
    private StringBuffer query;
    private List<String> sourceDomain = new ArrayList<String>();

    /**
     * Constructor for ArticleRating. Uses source domains to retrieve articles
     * and analyzes them to determine their score.
     * @param symbol String symbol Parameter
     */
    public ArticleRating(String symbol)
    {
        file = new File("src/main/resources/Article.txt");
        this.symbol = symbol;
        query = new StringBuffer();
        try
        {
            scanner = new Scanner(file);
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        //financial focus news sources
        
    }

    /**
     * Uses the Sentiment Analysis Api to determine the score of the article.
     * @return rating
     */
    public double getRating()
    {
        writeArticle();
        return (1.75 * rating());
        //return 0.0;
    }
    
    /**
     * Checks if the character is anyone of the characters listed below and 
     * returns its symbol, translates the characters to URL characters
     * @param a character a parameter
     * @return the symbol for the hex character
     */
    public String translateToURL(char a)
    {
        switch(a)
        {
            case ' ':
                return "%20";
            case '&':
                return "%26";
            case '!':
                return "%21";
            case '(':
                return "%28";
            case ')':
                return "%29";
            case '$':
                return "%24";
            case '%':
                return "%25";
            case '\'':
                return "%27";
            case '#':
                return "%23";
            case '*':
                return "%2A";
            case '-':
                return "%2D";
            case '+':
                return "%2B";
            case '.':
                return "%2E";
            case '"':
                return "%22";
            case '/':
                return "%2F";
            case '|':
                return "%7C";
            case '{':
                return "%7B";
            case '}':
                return "%7D";
            case '_':
                return "%5F";
                default:
                    return "";
        }
    }
    
    /**
     * Determines the rating of an article by using the Sentiment Analysis and 
     * prints out the score of the article. Determines if the score is positive,
     * negative, or neutral.
     * @return the score of an article
     */
    public double rating()
    {
        int count = 0;
        String checker = "";
        double sum = 0;
        while ( scanner.hasNextLine() )
        {
            fileText = scanner.nextLine();
            checker += fileText;
            for (int i = 0; i < fileText.indexOf( " /" ); i++)
            {
                if (Character.isWhitespace(fileText.charAt(i)))
                {
                    query.append('+');
                }
                else if (!Character.isAlphabetic( fileText.charAt(i) ))
                {
                    query.append(translateToURL(fileText.charAt(i)));
                    //query.append("");
                }
                else
                {
                    query.append( fileText.charAt( i ) );
                }
            }
            if (!checker.contains( query ))
            {
                com.mashape.unirest.http.HttpResponse<JsonNode> response = null;
                try
                {
                    response = Unirest.get("https://twinword-sentiment-analysis.p.rapidapi.com/analyze/?text="+query)
                                    .header("X-RapidAPI-Host", "twinword-sentiment-analysis.p.rapidapi.com")
                                    .header("X-RapidAPI-Key", API_KEY)
                                    .asJson();
                }
                catch ( UnirestException e )
                {
                    e.printStackTrace();
                }
                if ( response.getBody().getObject().has( "score" ))
                {
                    sum += Double.valueOf(response.getBody().getObject().get( "score" ).toString()).doubleValue();
                    System.out.println( response.getBody().getObject().get( "score" ).toString() );
                    count++;
                }
            }
        }
        String append = "";
        if ( sum / count > 0.05 )
        {
            append = "Positive";
        }
        else if (-0.05 <= sum/count && sum/count <= 0.05)
        {
            append = "Neutral";
        }
        else
        {
            append = "Negative";
        }
        return sum / count;
    }
    
    /**
     * Uses the Api key and api identification to go through the article and go
     * line by line to analyze it
     */
    public void writeArticle()
    {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Configure API key authorization: app_id
        ApiKeyAuth app_id = (ApiKeyAuth) defaultClient.getAuthentication("app_id");
        app_id.setApiKey("6167234a");

        // Configure API key authorization: app_key
        ApiKeyAuth app_key = (ApiKeyAuth) defaultClient.getAuthentication("app_key");
        app_key.setApiKey("467d5f2e523b4c9cfbf24ef9c42e3058");

        DefaultApi apiInstance = new DefaultApi();

        StoriesParams.Builder storiesBuilder = StoriesParams.newBuilder();

        storiesBuilder.setTitle(symbol);
        storiesBuilder.setSortBy("social_shares_count.facebook");
        storiesBuilder.setLanguage(Arrays.asList("en"));
        storiesBuilder.setNotLanguage(Arrays.asList("es", "it"));
        storiesBuilder.setPublishedAtStart("NOW-7DAYS");
        storiesBuilder.setPublishedAtEnd("NOW");

        try {
            Stories result = apiInstance.listStories(storiesBuilder.build());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));

            for (Iterator i = result.getStories().iterator(); i.hasNext();){
                
                Story story = (Story)i.next();                
                writer.write( story.getTitle() + " / " + story.getSource().getName() + '\n');
                System.out.println( story.getTitle() + " / " + story.getSource().getName() );

            }
            
            writer.close();

        } 
        catch (ApiException e) {System.err.println("Exception when calling DefaultApi#listStories");e.printStackTrace();} 
        catch ( IOException e ){e.printStackTrace();}
        
    }
    
    /**
     * 
     * Getter method for file of Article 
     * @return source file
     */
    public File getFile()
    {
        return file;
    }
    
    // public static void main (String[] args)
    // {
    // ArticleRating rating = new ArticleRating("Apple");
    // System.out.println( rating.getRating() );
    // }
    
}
