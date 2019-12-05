[![N|Solid](http://procoders.tech/art/powered.png)](http://procoders.tech/)

# Cordova Spotify Plugin
This plugin included all main functions of audio player for Spotify servise :smirk:

## Installation
> To communicate with Spotify you need to register your application’s id in the [Developer Portal](https://developer.spotify.com/). This value is used to authenticate your application against Spotify client.



You may install latest version from master
```sh
cordova plugin add https://github.com/adnotam/cordova-plugin-spotify
```
### Removing the Plugin from project
```sh
cordova plugin rm cordova-plugin-spotify
```
## Supported Platforms
> - Android
> - iOS

### Platform specific
:warning: for Android platform not implemented method `setVolume()` and `event onVolumeChanged()`
It will be fixed when this methods will be in Spotify SDK

# Using the plugin
> **You must have premium account from Spotify service for playing music** :exclamation:

After device is ready you must defined the main variable:
```javascript
var Spotify = window.cordova.plugins.SpotifyPlugin;
```
:thumbsup: *After this you may use all method in your code.*

## Methods
All methods returning promises, but you can also use standard callback functions.

```javascript
Spotify.auth(success, error, token, clientId);
```
> - success - success callback
> - error - error callback
> - token - spotify access token;
> - clientId - your application id in Spotify.

```javascript
Spotify.getPosition(success, error);
```
> - success - success callback
> - error - error callback

```javascript
Spotify.getToken(success, error);
```
> - success - success callback
> - error - error callback

```javascript
Spotify.loadFeaturedPlaylists (success, error, token);
```
> - success - success callback
> - error - error callback
> - token - access token

```javascript
Spotify.loadFeaturedPlaylistTracks (success, error, id, token);
```
> - success - success callback
> - error - error callback
> - id - Featured Playlist's ID
> - token - access token

```javascript
Spotify.loadUserAlbums (success, error, token);
```
> - success - success callback
> - error - error callback
> - token - access token


```javascript
Spotify.loadUserAlbumTracks (success, error, id, token);
```
> - success - success callback
> - error - error callback
> - id - User album's ID
> - token - access token

```javascript
Spotify.loadUserPlaylists (success, error, token);
```
> - success - success callback
> - error - error callback
> - token - access token

```javascript
Spotify.loadUserPlaylistTracks (success, error, id, token);
```
> - success - success callback
> - error - error callback
> - id - User Playlist's ID
> - token - access token

```javascript
Spotify.login(success, error, appId, redirectURL, mode);
```
> - success - success callback
> - error - error callback
> - *appId* - your application id in Spotify
> - *redirectURL* - White-listed addresses to redirect to after authentication success OR failure
> - *mode* - The mode of debugging, if you use Xcode emulator its value should be "debug" else empty string

```javascript
Spotify.logout();
```

```javascript
Spotify.next();
```

```javascript
Spotify.pause(error);
```
> - error - error callback

```javascript
Spotify.play(success, error, value);
```
> - success - success callback
> - error - error callback
> - *value* - track id or album id or playlist id

    Example:
    > Spotify.play(success, error, "spotify:track:3qRNQHagYiiDLdWMSOkPGG");
    > Spotify.play(success, error, "spotify:album:75Sgdm3seM5KXkEd46vaDb");
    > Spotify.play(success, error, "spotify:user:spotify:playlist:2yLXxKhhziG2xzy7eyD4TD");

```javascript
Spotify.prev();
```

```javascript
Spotify.resume();
```

```javascript
Spotify.seek(position);
```
> *position* - value between 0...100 %

```javascript
Spotify.seekTo(error, position);
```
> - error - error callback
> *position* - value in seconds

## Events
```javascript
Spotify.Events.onConnectionMessage = function(args){};
```
> *args[0]* - message;

```javascript
Spotify.Events.onLoggedIn = function(args){};
```
> *args[0]* - access token;

```javascript
Spotify.Events.onLoggedOut = function(){};
```

```javascript
Spotify.Events.onLoginFailed = function(args){};
```
> *args[0]* - error code;

```javascript
Spotify.Events.onLoginResponse = function(args){};
```
> *args[0]* - response type;

```javascript
Spotify.Events.onPlayback.AudioFlush = function(args){};
```
> *args[0]*  - position (ms);

```javascript
Spotify.Events.onPlayback.BecameActive = function(){};
```

```javascript
Spotify.Events.onPlayback.BecameInactive = function(){};
```

```javascript
Spotify.Events.onPlayback.ContextChanged = function(args){};
```
> *args[0]*  - position (ms);

```javascript
Spotify.Events.onPlayback.DeliveryDone = function(){};
```

```javascript
Spotify.Events.onPlayback.Error = function(args){};
```
> *args[0]*  - error message

```javascript
Spotify.Events.onPlayback.LostPermission = function(){};
```

```javascript
Spotify.Events.onPlayback.MetadataChanged = function(args){};
```
> *args[0]* - current track name
> *args[1]* - artist name
> *args[2]* - album name
> *args[3]* - track duration (ms)

```javascript
Spotify.Events.onPlayback.Next = function(args){};
```

```javascript
Spotify.Events.onPlayback.Pause = function(){};
```

```javascript
Spotify.Events.onPlayback.Play = function(){};
```

```javascript
Spotify.Events.onPlayback.Position = function(args){};
```
> *args[0]*  - position (ms)

```javascript
Spotify.Events.onPlayback.Prev = function(){};
```

```javascript
Spotify.Events.onPlayback.RepeatOff = function(){};
```

```javascript
Spotify.Events.onPlayback.RepeatOn = function(){};
```

```javascript
Spotify.Events.onPlayback.ShuffleOff = function(){};
```

```javascript
Spotify.Events.onPlayback.ShuffleOn = function(){};
```

```javascript
Spotify.Events.onPlayback.TrackChanged = function(){};
```

```javascript
Spotify.Events.onPlayback.TrackDelivered = function(){};
```

```javascript
Spotify.Events.onSuccess = function(){};
```

```javascript
Spotify.Events.onTemporaryError = function(){};
```

### Authors
 - Aleksey Stepanets
 - Antonio Facciolo
