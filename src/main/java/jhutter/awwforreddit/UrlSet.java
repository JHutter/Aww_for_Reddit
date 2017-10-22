//package jhutter.awwforreddit;
package jhutter.awwforreddit;



import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;


/**
 * Created by JHutter on 9/4/2017.
 */

public class UrlSet{
    LinkedHashSet<String> urlSet;
    RedditScraper scraper;

    public UrlSet(){
        urlSet = new LinkedHashSet<>();
        scraper = new RedditScraper();
    }

    public boolean fillSet(){
        int preSize = urlSet.size();
        urlSet.addAll(scraper.grabLinks());

        if (preSize == urlSet.size()) {
            return false;
        }
        else {
            return false;
        }
    }

    public String nextUrl(Collection<String> excluded){
        String result;

        if (urlSet.isEmpty()){
            fillSet();
        }
        urlSet.removeAll(excluded);

        Iterator<String> iter = urlSet.iterator();

        if (iter.hasNext()){
            result = iter.next();
            iter.remove();
            return result;
        }
        else {
            return "";
        }
    }

    public boolean removeUrl(String url){
        return urlSet.remove(url);
    }

    public boolean removeAll(Collection<String> c){
        return urlSet.removeAll(c);
    }

    public void clear(){
        urlSet.clear();
    }

    public void replaceSubreddits(Collection<String> c){
        clear();
        scraper.replaceSubreddits(c);
    }
}
