## Synopsis

This app for Android 6.0 and above delivers images and gifs from cute animal subreddits in a simple UI. In its current iteration, there are four subreddits to choose from: r/aww, r/puppysmiles, r/cats, and r/rarepuppers, and these can be selected individually via a basic menu. Back and forward buttons allow navigation, and images that users don't like can be added to a ban list for the session by long click.

## Motivation

This app came about because my 6 year old wanted to look at cute pictures of cats and dogs, but I didn't trust reddit enough to just hand my phone over, as it was too easy to click from reddit.com/r/aww to something less child appropriate. So I planned out a reddit scraper that would pull a large number of image links from certain safe subreddits, with an upvote threshold to further filter out questionable content.

## Future features

Upcoming features include more flexibility of subreddit selection via user-added subreddits and a SQLite db to remember banned image links across user sessions. 

Moving to a project structure where lists of image links are collected and vetted more centrally on a server-side is under consideration, to allow for calls to content moderation APIs. However, a combination of using upvote thresholds and selecting subreddits that don't allow NSFW content per their rules/moderation and that focus on all-ages-friendly content such as cat pictures has so far been sufficient for client-side content moderation.

## Installation

There are two methods for installation: using Google Play or by cloning and building the project yourself.

#### Google Play

The app can be found on Google Play at https://play.google.com/store/apps/details?id=jhutter.awwforreddit. Click Install to install on your Android phone or tablet (requires Android 6.0 or above).

#### Building the APK yourself

If you want to build the APK yourself, follow these steps:

1. Clone or download the project.

2. Either fill out or remove banner ad.
- To complete the information for the banner ad, make an admob.xml file in main/res/values. For example:
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="adunitid">ca-app-pub-3940256099942544/6300978111</string>
    <string name="adappid">ca-app-pub-3940256099942544~3347511713</string>
</resources>
```
- To remove the banner ad, remove the adView in res/layouts/content_main.xml and remove this chunk from MainActivity.java:
```
AdRequest adRequest = new AdRequest.Builder().build();
String adAppId = this.getResources().getString(R.string.adappid);
MobileAds.initialize(this, adAppId);
mAdView = (AdView) findViewById(R.id.adView);
mAdView.loadAd(adRequest);
```
Optionally, you can also remove the import statements `android.gms.ads` and `private AdView mAdView;` in MainActivity.java to clean up the code smells relating to getting rid of the ads.

3. Build the APK (debug APK is fine for non-publishing purposes). You can do this by importing your cloned version of the project into Android Studio and selecting Build>Build APK. If successful, Android Studio will pop up a message telling you the APK was built and where to find the APK itself.

> APK(s) generated successfully. Show in Explorer

4. On your Android phone or tablet, go to Settings to allow installation of apps from sources other than Google Play Store.

5. Put the APK on your phone. You can email it, share it by Google Drive, or transfer it by USB.

6. A system dialog will come up asking if you want to install this APK. Select Install to install it.



## Contributors

Jon Hutter, 2017

## Attribution

Bumptech/glide was used for image processing. jhy/jsoup was used to implement the reddit scraper.

## License

MIT License.

Copyright (c) 2017 Jon Hutter

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

See https://github.com/JHutter/Aww_for_Reddit/blob/master/LICENSE for detailed license, 
including attribution and license for libraries used (notably bumptech/glide and jhy/jsoup).
