//package jhutter.awwforreddit;
package jhutter.awwforreddit;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * Created by JHutter on 9/3/2017.
 * Purpose: class to perform scraping of imgur and redditmedia links to images
 *
 * TODO: add/complete subreddit validator
 * TODO add gif compatibility (currently sifted out)
 * TODO why are some images not going? look into this. perhaps size of image??
 */

public class RedditScraper {
    // member vars
    private String domain;
    private ArrayList<String> subreddits; // arraylist of subreddits
    private int upvoteThreshold;
    private String newHot;
    private int limit;


    public RedditScraper() {
        upvoteThreshold = 10;
        domain = new String("https://www.reddit.com/");
        subreddits = new ArrayList<String>();
        newHot = "hot";
        limit = 50;

        subreddits.add("r/aww");
        //subreddits.add("r/puppysmiles");
        //subreddits.add("r/babyanimals");
        //subreddits.add("r/cats");
        //subreddits.add("r/eyebleach");
    }

    // getters
    public int getUpvoteThreshold(){ return upvoteThreshold;}
    public String getDomain(){ return domain;}
    public ArrayList<String> getSubreddits(){ return subreddits;}

    // setters (well, only one)
    public boolean setUpvoteThreshold(int newThreshold){
        if (newThreshold > -1){
            upvoteThreshold = newThreshold;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean addSubreddit(String newSub){
        if (validSubreddit(newSub)){
            subreddits.add(newSub);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean delSubreddit(String delSub){
        subreddits.remove(delSub);
        return true; // TODO add checking to delSubreddit? else make this void
    }



    /**
     * @name validSubreddit
     * @param subreddit string of subreddit (without r/) to check
     * @return true/false based on http call to domain+subreddit and subreddit name length
     */
    public boolean validSubreddit(String subreddit){
        return true;
        // check length
        // check "r/" at start
        // check call to domain+subreddit
    }

    public ArrayList<String> grabLinks(){
        ArrayList<String> linksArr = new ArrayList<>();
        String linkText;

        for (String subr: subreddits){
            try{
                Document doc = Jsoup.connect(constructReqString(subr)).get();
                Elements links = doc.getElementsByAttribute("data-url");
                for (Element link : links){
                    //sift for data-url=
                    if (aboveThreshold(link)){
                        linkText = link.attr("data-url");
                        if (isImgUrl(linkText) || isGifUrl(linkText)){
                            linkText.replace("m.imgur.com","i.imgur.com");
                            linksArr.add(linkText);
                        }
                        else {
                            if (linkText.contains("imgur.com")){
                                //linksArr.add(imgurToDirect(linkText));
                            }
                        }
                    }
                }
            }
            catch (IOException e) {
                System.err.println("Caught IOException: " + e.getMessage());
            }
        }

        Collections.shuffle(linksArr);
        return linksArr;
    }

    public static boolean isImgUrl(String url){
        if (url.endsWith(".jpeg") || url.endsWith(".jpg") || url.endsWith(".png")){
            return true;
        }
        return false;
    }

    public static boolean isGifUrl(String url){
        if (url.endsWith(".gif")){
            return true;
        }
        return false;
    }

    private boolean aboveThreshold(Element link){
        int score = Integer.parseInt(link.attr("data-score"));
        if (score >= upvoteThreshold){
            return true;
        }
        return false;
    }

    public String constructReqString(String subreddit){
        return domain+subreddit+"/"+newHot+"/?limit="+limit;
    }

    public static String imgurToDirect(String url){
        if (isGifUrl(url)) {
            return url;
        }
        if (isImgUrl(url)){
            return url.replace(".jpg", "m.jpg");
        }
        if (url.endsWith(".gifv")){
            return url.substring(0, url.length()-1);
        }
        else {
            String id = url.substring(url.indexOf('/', url.lastIndexOf(".com"))+1);
            return "https://i.imgur.com/"+id+".jpg";
        }

        /*String id = url.substring(url.indexOf('/', url.lastIndexOf(".com"))+1);  // assumes no extra slashes after .com (ie no queries)
        try {
            Document doc = Jsoup.connect(url).get();
            if (doc.toString().contains(id+".gif") || doc.toString().contains(id+".mp4")){
                return "https://i.imgur.com/"+id+".gif";
            }
            else {
                return "https://i.imgur.com/"+id+"m.jpg";
            }

        }
        catch (IOException e){
            System.err.println("Caught IOException: " + e.getMessage());
        }*/

    }

    public void replaceSubreddits(Collection<String> c) {
        if (!subreddits.isEmpty()){
            subreddits.clear();
        }
        subreddits.addAll(c);
    }
}
