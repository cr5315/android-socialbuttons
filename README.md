# Android Social Buttons
---

A set of views which allow for easy sharing of content. The views can get the share count of the URL provided. This library was inspired by [gcacace/android-socialbuttons](https://github.com/gcacace/android-socialbuttons).

Supported social networks:

 * Facebook Like
 * Facebook Share
 
Planned social networks:
 
 * Twitter

## Installation

### build.gradle

```sh
repositories {
	jcenter()
}
    
dependencies {
    compile 'com.cr5315:socialbuttons:0.2.0'
}
```
 
### AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Usage

### Facebook Like Button

The like button will open the Facebook app to your page (if the Facebook app is installed), or open the browser. To use the like button, you need to set your page ID and URL. You can get your page ID [from this nifty site](http://findmyfacebookid.com/).

```xml
<com.cr5315.socialbuttons.FacebookLikeButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:pageId="580369862000929"
    app:pageUrl="https://www.facebook.com/J2Official"
    />
```

### Facebook Share Button

Please note, an access token is required get the share count. You can read about getting an access token [here](https://developers.facebook.com/docs/facebook-login/access-tokens). If you don't want the share count, set annotation="none".

```xml
<com.cr5315.socialbuttons.FacebookShareButton
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:accessToken="@string/facebook_access_token"
    app:annotation="bubble"
    app:buttonDrawable="@drawable/ic_share_button"
    app:buttonText="Share our app"
    app:progressType="spinner"
    app:shareUrl="http://www.imdb.com/title/tt2015381/"
    />
```

---

### Dependencies:
 * [retrofit](https://github.com/square/retrofit)

---

### License
    Copyright 2015 Ben Butzow

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.