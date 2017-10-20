//package jon.redditawwformykidlet;
package jhutter.awwforreddit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Created by JHutter on 9/4/2017.
 */

public class NavStacks {
    Stack<String> backStack;
    Stack<String> forwardStack;
    UrlSet urlSet;
    String currentUrl;
    HashSet<String> bannedUrls;

    public NavStacks(){
        backStack = new Stack<>();
        forwardStack = new Stack<>();
        urlSet = new UrlSet();
        urlSet.fillSet();
        currentUrl = "";
        bannedUrls = new HashSet<>();
    }

    public String back(){
        if (!currentUrl.isEmpty()){  // shouldn't be able to go back without having gone forward, but CYA
            forwardStack.push(currentUrl);
        }

        currentUrl = backStack.pop();

        return currentUrl;
    }

    public String forward(){
        if (!currentUrl.isEmpty()){
            backStack.push(currentUrl);
        }

        if (forwardStack.isEmpty()){
            currentUrl = urlSet.nextUrl(bannedUrls);
        }
        else {
            currentUrl = forwardStack.pop();
        }

        return currentUrl;
    }

    public boolean canBack(){
        if (!backStack.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    public String bannedForward(){
        bannedUrls.add(currentUrl);
        urlSet.removeUrl(currentUrl);
        forwardStack.removeAll(bannedUrls);
        backStack.remove(bannedUrls);

        currentUrl = "";
        return forward();
    }

    // TODO put banned url list in sqlite class later
    public boolean isBanned(String url){
        if (bannedUrls.contains(url)){
            return true;
        }
        return false;
    }

    public void resetSubreddits(ArrayList<String> newsubs){
        forwardStack.clear();
        urlSet.replaceSubreddits(newsubs);
    }

    public void resetSubreddits(String newsub){
        ArrayList<String> subs = new ArrayList<>();
        subs.add(newsub);
        forwardStack.clear();
        urlSet.replaceSubreddits(subs);
    }
}
