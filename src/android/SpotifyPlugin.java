package cordova.plugin.spotify;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.spotify.sdk.android.player.Config;

import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.net.*;
import java.io.*;

/**
 * Created by Aleksey on 10/10/2016.
 */
public class SpotifyPlugin extends CordovaPlugin implements Player.NotificationCallback, ConnectionStateCallback {
  private static final String TAG = "CordovaSpotifyPlugin";
  private static final String ACTION_AUTH = "auth";
  private static final String ACTION_GET_POSITON = "getPosition";
  private static final String ACTION_GET_TOKEN = "getToken";
  private static final String ACTION_LOAD_FEATURED_PLAYLISTS = "featuredPlaylists";
  private static final String ACTION_LOAD_FEATURED_PLAYLIST_TRACKS = "featuredPlaylistTracks";
  private static final String ACTION_LOAD_USER_ALBUMS = "userAlbums";
  private static final String ACTION_LOAD_USER_ALBUM_TRACKS = "userAlbumTracks";
  private static final String ACTION_LOAD_USER_PLAYLISTS = "userPlaylists";
  private static final String ACTION_LOAD_USER_PLAYLIST_TRACKS = "userPlaylistTracks";
  private static final String ACTION_LOGIN = "login";
  private static final String ACTION_LOGOUT = "logout";
  private static final String ACTION_NEXT = "next";
  private static final String ACTION_PAUSE = "pause";
  private static final String ACTION_PLAY = "play";
  private static final String ACTION_PREV = "prev";
  private static final String ACTION_RESUME = "resume";
  private static final String ACTION_SEEK = "seek";
  private static final String ACTION_SEEK_TO = "seekTo";
  private static final String ACTION_SET_VOLUME = "setVolume";
  private static final String METHOD_SEND_TO_JS_OBJ = "window.cordova.plugins.SpotifyPlugin.Events.";
  private static final int REQUEST_CODE = 1337;

  private static final String EVENT_AUDIO_FLUSH = "kSpPlaybackEventAudioFlush";
  private static final String EVENT_BECAME_ACTIVE = "kSpPlaybackNotifyBecameActive";
  private static final String EVENT_BECAME_INACTIVE = "kSpPlaybackNotifyBecameInactive";
  private static final String EVENT_CONTEXT_CHANGED = "kSpPlaybackNotifyContextChanged";
  private static final String EVENT_DELIVERY_DONE = "kSpPlaybackNotifyAudioDeliveryDone";
  private static final String EVENT_LOST_PERMISSION = "kSpPlaybackNotifyLostPermission";
  private static final String EVENT_METADATA_CHANGED = "kSpPlaybackNotifyMetadataChanged";
  private static final String EVENT_NOTIFY_NEXT = "kSpPlaybackNotifyNext";
  private static final String EVENT_NOTIFY_PAUSE = "kSpPlaybackNotifyPause";
  private static final String EVENT_NOTIFY_PLAY = "kSpPlaybackNotifyPlay";
  private static final String EVENT_NOTIFY_PREV = "kSpPlaybackNotifyPrev";
  private static final String EVENT_REPEAT_OFF = "kSpPlaybackNotifyRepeatOff";
  private static final String EVENT_REPEAT_ON = "kSpPlaybackNotifyRepeatOn";
  private static final String EVENT_SHUFFLE_OFF = "kSpPlaybackNotifyShuffleOff";
  private static final String EVENT_SHUFFLE_ON = "kSpPlaybackNotifyShuffleOn";
  private static final String EVENT_TRACK_CHANGED = "kSpPlaybackNotifyTrackChanged";
  private static final String EVENT_TRACK_DELIVERED = "kSpPlaybackNotifyTrackDelivered";

  private String clientId; // ="4eb7b5c08bee4d759d34dbc1823fd7c5";
  private String redirectUri; // = "testschema://callback";

  private CallbackContext loginCallback;
  private String currentAccessToken;
  private Boolean isLoggedIn = false;

  private SpotifyPlayer currentPlayer;
  private PlaybackState mCurrentPlaybackState;

  private Metadata mMetaData;

  private CordovaWebView mWebView;
  private CordovaInterface mInterface;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    Log.i(TAG, "Initializing...");
    mWebView = webView;
    mInterface = cordova;

    int cbResId = cordova.getActivity().getResources().getIdentifier("redirect_uri", "string",
        cordova.getActivity().getPackageName());

    Log.i(TAG, "cb ID ID" + cbResId);

    redirectUri = cordova.getActivity().getString(cbResId);

    Log.i(TAG, "Set up local vars" + clientId + redirectUri);
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) {
    Boolean success = false;
    Log.i(TAG, "PLUGIN_EXECUTE: " + action);

    if (ACTION_AUTH.equalsIgnoreCase(action)) {
      String token = "";
      String id = "";
      try {
        token = data.getString(0);
        id = data.getString(1);
      } catch (JSONException e) {
        Log.e(TAG, e.toString());
      }
      this.auth(token, id, callbackContext);
      success = true;
    } else if (ACTION_GET_POSITON.equalsIgnoreCase(action)) {
      this.getPosition(callbackContext);
      success = true;
    } else if (ACTION_GET_TOKEN.equalsIgnoreCase(action)) {
      this.getToken(callbackContext);
      success = true;
    } else if (ACTION_LOAD_FEATURED_PLAYLISTS.equalsIgnoreCase(action)) {
      try {
        String token = "";
        try {
          token = data.getString(0);
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        this.loadFeaturedPlaylists(token, callbackContext);
        success = true;
      } catch (Exception error) {
        Log.d(TAG, "LOADING FEATURED PLAYLISTS ERR: " + error.getMessage());

        JSONArray array = new JSONArray();
        array.put(error.getMessage());
        sendUpdate("onError", new Object[] { array });
        callbackContext.error(error.getMessage());
      }
    } else if (ACTION_LOAD_FEATURED_PLAYLIST_TRACKS.equalsIgnoreCase(action)) {
      try {
        String id = "";
        String token = "";
        try {
          id = data.getString(0);
          token = data.getString(1);
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        this.loadFeaturedPlaylistTracks(id, token, callbackContext);
        success = true;
      } catch (Exception error) {
        Log.d(TAG, "LOADING FEATURED PLAYLIST TRACKS ERR: " + error.getMessage());

        JSONArray array = new JSONArray();
        array.put(error.getMessage());
        sendUpdate("onError", new Object[] { array });
        callbackContext.error(error.getMessage());
      }
    } else if (ACTION_LOAD_USER_ALBUMS.equalsIgnoreCase(action)) {
      try {
        String token = "";
        try {
          token = data.getString(0);
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        this.loadUserAlbums(token, callbackContext);
        success = true;
      } catch (Exception error) {
        Log.d(TAG, "LOADING LOADING USER ALBUMS ERR: " + error.getMessage());

        JSONArray array = new JSONArray();
        array.put(error.getMessage());
        sendUpdate("onError", new Object[] { array });
        callbackContext.error(error.getMessage());
      }
    } else if (ACTION_LOAD_USER_ALBUM_TRACKS.equalsIgnoreCase(action)) {
      try {
        String id = "";
        String token = "";
        try {
          id = data.getString(0);
          token = data.getString(1);
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        this.loadUserAlbumTracks(id, token, callbackContext);
        success = true;
      } catch (Exception error) {
        Log.d(TAG, "LOADING USER ALBUM TRACKS ERR: " + error.getMessage());

        JSONArray array = new JSONArray();
        array.put(error.getMessage());
        sendUpdate("onError", new Object[] { array });
        callbackContext.error(error.getMessage());
      }
    } else if (ACTION_LOAD_USER_PLAYLISTS.equalsIgnoreCase(action)) {
      try {
        String token = "";
        try {
          token = data.getString(0);
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        this.loadUserPlaylists(token, callbackContext);
        success = true;
      } catch (Exception error) {
        Log.d(TAG, "LOADING USER PLAYLISTS ERR: " + error.getMessage());

        JSONArray array = new JSONArray();
        array.put(error.getMessage());
        sendUpdate("onError", new Object[] { array });
        callbackContext.error(error.getMessage());
      }
    } else if (ACTION_LOAD_USER_PLAYLIST_TRACKS.equalsIgnoreCase(action)) {
      try {
        String id = "";
        String token = "";
        try {
          id = data.getString(0);
          token = data.getString(1);
        } catch (JSONException e) {
          Log.e(TAG, e.toString());
        }
        this.loadUserPlaylistTracks(id, token, callbackContext);
        success = true;
      } catch (Exception error) {
        Log.d(TAG, "LOADING USER PLAYLIST TRACKS ERR: " + error.getMessage());

        JSONArray array = new JSONArray();
        array.put(error.getMessage());
        sendUpdate("onError", new Object[] { array });
        callbackContext.error(error.getMessage());
      }
    } else if (ACTION_LOGIN.equalsIgnoreCase(action)) {
      JSONArray scopes = new JSONArray();
      Boolean fetchTokenManually = false;

      /*
       * try { scopes = data.getJSONArray(0); // fetchTokenManually =
       * data.getBoolean(1); } catch(JSONException e) { Log.e(TAG, e.toString()); }
       */

      cordova.setActivityResultCallback(this);
      loginCallback = callbackContext;
      String uri = "";

      try {
        uri = data.getString(0);
      } catch (JSONException e) {
        Log.e(TAG, e.toString());
      }
      this.login(uri);
      success = true;
    } else if (ACTION_LOGOUT.equalsIgnoreCase(action)) {
      this.logout();
      success = true;
    } else if (ACTION_NEXT.equalsIgnoreCase(action)) {
      this.next();
      success = true;
    } else if (ACTION_PAUSE.equalsIgnoreCase(action)) {
      this.pause(callbackContext);
      success = true;
    } else if (ACTION_PLAY.equalsIgnoreCase(action)) {
      String uri = "";

      try {
        uri = data.getString(0);
      } catch (JSONException e) {
        Log.e(TAG, e.toString());
      }

      this.play(uri, callbackContext);
      success = true;
    } else if (ACTION_PREV.equalsIgnoreCase(action)) {
      this.prev();
      success = true;
    } else if (ACTION_RESUME.equalsIgnoreCase(action)) {
      this.resume();
      success = true;
    } else if (ACTION_SEEK.equalsIgnoreCase(action)) {
      int val = 0;
      try {
        val = data.getInt(0);
      } catch (JSONException e) {
        Log.e(TAG, e.toString());
      }
      Log.d(TAG, String.valueOf(val));
      this.seek(val);
      success = true;
    } else if (ACTION_SEEK_TO.equalsIgnoreCase(action)) {
      int val = 0;
      try {
        val = data.getInt(0);
      } catch (JSONException e) {
        Log.e(TAG, e.toString());
      }
      Log.d(TAG, String.valueOf(val));
      this.seekTo(val, callbackContext);
      success = true;
    } else if (ACTION_SET_VOLUME.equalsIgnoreCase(action)) {
      int val = 0;
      try {
        val = data.getInt(0);
      } catch (JSONException e) {
        Log.e(TAG, e.toString());
      }
      this.setVolume(val);
      success = true;
    }

    return success;
  }

  private void auth(String token, String id, CallbackContext callbackContext) {
    Log.d(TAG, "auth()");
    if (currentPlayer == null) {
      Config playerConfig = new Config(cordova.getActivity(), token, id);
      currentPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {

        @Override
        public void onInitialized(SpotifyPlayer player) {
          Log.d(TAG, "-- Player initialized --");
          currentPlayer.setConnectivityStatus(mOperationCallback,
              getNetworkConnectivity(cordova.getActivity().getApplicationContext()));
          currentPlayer.addNotificationCallback(SpotifyPlugin.this);
          currentPlayer.addConnectionStateCallback(SpotifyPlugin.this);
          callbackContext.success("Init success");
        }

        @Override
        public void onError(Throwable error) {
          Log.d(TAG, "Error in initialization: " + error.getMessage());
          callbackContext.error("invalid access token");
        }
      });
    } else {
      currentPlayer.login(token);
    }
  }

  private void getPosition(CallbackContext callbackContext) {
    int x = (int) currentPlayer.getPlaybackState().positionMs;
    Log.d(TAG, "position = " + x);
    callbackContext.success(x);
  }

  private void getToken(CallbackContext callbackContext) {
    Log.d(TAG, "getToken()" + callbackContext);
    callbackContext.success(this.currentAccessToken);
  }

  public void loadFeaturedPlaylists(String authToken, CallbackContext callbackContext) throws Exception {
    URL urlConnection = new URL("https://api.spotify.com/v1/browse/featured-playlists");
    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + authToken);

    StringBuilder result = new StringBuilder();
    try {
      InputStream in = new BufferedInputStream(connection.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } finally {
      connection.disconnect();
    }

    callbackContext.success(result.toString());
  }

  public void loadFeaturedPlaylistTracks(String id, String authToken, CallbackContext callbackContext) throws Exception {
    URL urlConnection = new URL("https://api.spotify.com/v1/playlists/" + id + "/tracks");
    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + authToken);

    StringBuilder result = new StringBuilder();
    try {
      InputStream in = new BufferedInputStream(connection.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } finally {
      connection.disconnect();
    }

    callbackContext.success(result.toString());
  }

  public void loadUserAlbums(String authToken, CallbackContext callbackContext) throws Exception {
    URL urlConnection = new URL("https://api.spotify.com/v1/me/albums?limit=50");
    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + authToken);

    StringBuilder result = new StringBuilder();
    try {
      InputStream in = new BufferedInputStream(connection.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } finally {
      connection.disconnect();
    }

    callbackContext.success(result.toString());
  }

  public void loadUserAlbumTracks(String id, String authToken, CallbackContext callbackContext) throws Exception {
    URL urlConnection = new URL("https://api.spotify.com/v1/albums/" + id + "/tracks");
    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + authToken);

    StringBuilder result = new StringBuilder();
    try {
      InputStream in = new BufferedInputStream(connection.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } finally {
      connection.disconnect();
    }

    callbackContext.success(result.toString());
  }

  public void loadUserPlaylists(String authToken, CallbackContext callbackContext) throws Exception {
    URL urlConnection = new URL("https://api.spotify.com/v1/me/playlists?limit=50");
    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + authToken);

    StringBuilder result = new StringBuilder();
    try {
      InputStream in = new BufferedInputStream(connection.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } finally {
      connection.disconnect();
    }

    callbackContext.success(result.toString());
  }

  public void loadUserPlaylistTracks(String id, String authToken, CallbackContext callbackContext) throws Exception {
    URL urlConnection = new URL("https://api.spotify.com/v1/playlists/" + id + "/tracks");
    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + authToken);

    StringBuilder result = new StringBuilder();
    try {
      InputStream in = new BufferedInputStream(connection.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } catch (Exception error) {
      callbackContext.error(error.getMessage());
    } finally {
      connection.disconnect();
    }

    callbackContext.success(result.toString());
  }

  private void login(String val) {
    clientId = val;
    final AuthenticationRequest request = new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN,
        redirectUri)
            .setScopes(new String[] { "user-read-private", "playlist-read", "playlist-read-private", "user-read-email",
                "streaming" })
            .build();
    AuthenticationClient.openLoginActivity(cordova.getActivity(), REQUEST_CODE, request);
  }

  private void logout() {
    AuthenticationClient.clearCookies(cordova.getActivity());

    this.clearPlayerState();
    isLoggedIn = false;
    currentAccessToken = null;
  }

  private void clearPlayerState() {
    if (currentPlayer != null) {
      currentPlayer.pause(mOperationCallback);
      currentPlayer.logout();
    }
  }

  public void next() {
    currentPlayer.skipToNext(mOperationCallback);

  }

  private void pause(CallbackContext callbackContext) {
    if (currentPlayer == null) {
      callbackContext.error("Player did not initialize");
      return;
    }
    if (!currentPlayer.isLoggedIn()) {
      Log.e(TAG,
          "Current Player is initialized but player is not logged in, set access token manually or call login with fetchTokenManually : false");
      callbackContext.error("Player did not authorize");
      return;
    }

    if (currentPlayer.getPlaybackState().isPlaying) {
      currentPlayer.pause(mOperationCallback);
    } else {
      currentPlayer.resume(mOperationCallback);
    }
  }

  private void play(String uri, CallbackContext callbackContext) {
    if (currentPlayer == null) {
      callbackContext.error("Player did not initialize");
      return;
    }
    if (!currentPlayer.isLoggedIn()) {
      Log.e(TAG,
          "Current Player is initialized but player is not logged in, set access token manually or call login with fetchTokenManually : false");
      callbackContext.error("Player did not authorize");
      return;
    }

    Log.i(TAG, "Playing URI: " + uri);
    currentPlayer.playUri(mOperationCallback, uri, 0, 0);
  }

  public void prev() {
    currentPlayer.skipToPrevious(mOperationCallback);
  }

  private void resume() {
    currentPlayer.resume(mOperationCallback);
  }

  private void seek(int val) {
    mMetaData = currentPlayer.getMetadata();
    Log.d(TAG, mMetaData.toString());

    final long dur = mMetaData.currentTrack.durationMs;
    currentPlayer.seekToPosition(mOperationCallback, (int) dur * val / 100);
  }

  private void seekTo(int val, CallbackContext callbackContext) {
    mMetaData = currentPlayer.getMetadata();
    Log.d(TAG, mMetaData.toString());

    final long dur = mMetaData.currentTrack.durationMs;
    if (val > 0 && val < dur / 1000) {
      currentPlayer.seekToPosition(mOperationCallback, val * 1000);
    } else {
      callbackContext.error("incorrect duration");
    }
  }

  private void setVolume(int value) {
    Log.d(TAG, "Volume = " + value);
    cordova.getActivity().setVolumeControlStream(value); // TODO: volume
  }

  /*
   * private final Player.NotificationCallback mNotificationCallback = new
   * Player.NotificationCallback(){
   *
   * @Override public void onPlaybackEvent(PlayerEvent playerEvent) {
   * Log.d(TAG,"NotificationCallback OK! "); }
   *
   * @Override public void onPlaybackError(Error error) {
   * Log.d(TAG,"NotificationCallback ERROR:" + error); } };
   */
  private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
    @Override
    public void onSuccess() {
      Log.d(TAG, "Success!");
      sendUpdate("onSuccess", new Object[] {});
    }

    @Override
    public void onError(Error error) {
      Log.d(TAG, "ERROR: " + error);
      JSONArray array = new JSONArray();
      array.put(error);
      sendUpdate("onError", new Object[] { array });
    }
  };

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    Log.i(TAG, "ACTIVITY RESULT: ");
    Log.i(TAG, "Request Code: " + requestCode);
    Log.i(TAG, "Result Code: " + resultCode);

    JSONObject ret = new JSONObject();

    // Check if result comes from the correct activity
    if (requestCode == REQUEST_CODE) {
      AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
      switch (response.getType()) {
      case TOKEN:
        isLoggedIn = true;
        Log.i(TAG, "TOKEN: " + response.getAccessToken());
        currentAccessToken = response.getAccessToken();
        JSONArray array = new JSONArray();
        array.put(currentAccessToken);
        loginCallback.success(ret);
        onAuthenticationComplete(response);
        break;

      case CODE:
        isLoggedIn = false;
        Log.i(TAG, "RECEIVED CODE: " + response.getCode());

        try {
          ret.put("authCode", response.getCode());
        } catch (JSONException e) {
          Log.e(TAG, e.getMessage());
        }

        break;

      case ERROR:
        Log.e(TAG, response.getError());
        loginCallback.error(response.getError());
        break;

      default:
        JSONArray array1 = new JSONArray();
        array1.put(response.getType());
        sendUpdate("onLoginResponse", new Object[] { array1 });
        break;
      }

      loginCallback = null;
    }
  }

  private Connectivity getNetworkConnectivity(Context context) {
    ConnectivityManager connectivityManager;
    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    if (activeNetwork != null && activeNetwork.isConnected()) {
      Log.d(TAG, "active network" + activeNetwork.isConnected());
      return Connectivity.fromNetworkType(activeNetwork.getType());
    } else {
      return Connectivity.OFFLINE;
    }
  }

  private void onAuthenticationComplete(AuthenticationResponse response) {
    // Once we have obtained an authorization token, we can proceed with creating a
    // Player.

    if (currentPlayer == null) {
      Config playerConfig = new Config(cordova.getActivity(), response.getAccessToken(), clientId);
      // Since the Player is a static singleton owned by the Spotify class, we pass
      // "this" as
      // the second argument in order to refcount it properly. Note that the method
      // Spotify.destroyPlayer() also takes an Object argument, which must be the same
      // as the
      // one passed in here. If you pass different instances to Spotify.getPlayer()
      // and
      // Spotify.destroyPlayer(), that will definitely result in resource leaks.
      currentPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
        @Override
        public void onInitialized(SpotifyPlayer player) {
          Log.d(TAG, "-- Player initialized --");
          currentPlayer.setConnectivityStatus(mOperationCallback,
              getNetworkConnectivity(cordova.getActivity().getApplicationContext()));
          currentPlayer.addNotificationCallback(SpotifyPlugin.this);
          currentPlayer.addConnectionStateCallback(SpotifyPlugin.this);
          // Trigger UI refresh
        }

        @Override
        public void onError(Throwable error) {
          Log.d(TAG, "Error in initialization: " + error.getMessage());
          JSONArray array = new JSONArray();
          array.put(error);
          sendUpdate("onError", new Object[] { array });
        }
      });
    } else {
      Log.d(TAG, "call login onAuthenticationComplete");
      currentPlayer.login(response.getAccessToken());
    }
  }

  @Override
  public void onConnectionMessage(String message) {
    Log.d("MainActivity", "Received connection message: " + message);
    JSONArray array = new JSONArray();
    array.put(message);
    sendUpdate("onConnectionMessage", new Object[] { array });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onLoggedIn() {
    Log.d("MainActivity", "User logged in");
    sendUpdate("onLoggedIn", new Object[] {});
  }

  @Override
  public void onLoggedOut() {
    Log.d("MainActivity", "User logged out");
    sendUpdate("onLoggedOut", new Object[] {});
  }

  @Override
  public void onLoginFailed(int i) {
    Log.d("MainActivity", "Login failed: " + i);
    sendUpdate("onLoginFailed", new Object[] {i});
  }

  @Override
  public void onTemporaryError() {
    Log.d("MainActivity", "Temporary error occurred");
    sendUpdate("onTemporaryError", new Object[] {});
  }

  @Override
  public void onPlaybackEvent(PlayerEvent playerEvent) {
    mMetaData = currentPlayer.getMetadata();
    mCurrentPlaybackState = currentPlayer.getPlaybackState();
    JSONArray array = new JSONArray();

    Log.d("MainActivity", "Playback event received: " + playerEvent.name());

    if (playerEvent.name().equals(EVENT_AUDIO_FLUSH)) {
      Log.d(TAG, "playback audio flush: " + mCurrentPlaybackState.positionMs + "ms");
      array.put(mCurrentPlaybackState.positionMs);
      sendUpdate("onPlayback.AudioFlush", new Object[] { array });
    } else if (playerEvent.name().equals(EVENT_BECAME_ACTIVE)) {
      sendUpdate("onPlayback.BecameActive", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_BECAME_INACTIVE)) {
      sendUpdate("onPlayback.BecameInactive", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_CONTEXT_CHANGED)) {
      Log.d(TAG, "playback context changed: " + mMetaData.contextName);
      array.put(mMetaData.contextName);
      sendUpdate("onPlayback.ContextChanged", new Object[] { array });
    } else if (playerEvent.name().equals(EVENT_DELIVERY_DONE)) {
      sendUpdate("onPlayback.DeliveryDone", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_LOST_PERMISSION)) {
      sendUpdate("onPlayback.LostPermission", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_METADATA_CHANGED)) {
      Log.d(TAG, "playback metadata changed: " + mMetaData);
      array.put(mMetaData.currentTrack.name);
      array.put(mMetaData.currentTrack.artistName);
      array.put(mMetaData.currentTrack.albumName);
      array.put(mMetaData.currentTrack.durationMs);
      sendUpdate("onPlayback.MetadataChanged", new Object[] { array });
    } else if (playerEvent.name().equals(EVENT_NOTIFY_NEXT)) {
      sendUpdate("onPlayback.Next", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_NOTIFY_PAUSE)) {
      sendUpdate("onPlayback.Pause", new Object[] {});
      int x = (int) currentPlayer.getPlaybackState().positionMs;
      Log.d(TAG, "position = " + x);
      array.put(x);
      sendUpdate("onPlayback.Position", new Object[]{ array });
    } else if (playerEvent.name().equals(EVENT_NOTIFY_PLAY)) {
      sendUpdate("onPlayback.Play", new Object[] {});
      int x = (int) currentPlayer.getPlaybackState().positionMs;
      Log.d(TAG, "position = " + x);
      array.put(x);
      sendUpdate("onPlayback.Position", new Object[]{ array });
    } else if (playerEvent.name().equals(EVENT_NOTIFY_PREV)) {
      sendUpdate("onPlayback.Prev", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_REPEAT_OFF)) {
      sendUpdate("onPlayback.RepeatOff", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_REPEAT_ON)) {
      sendUpdate("onPlayback.RepeatOn", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_SHUFFLE_OFF)) {
      sendUpdate("onPlayback.ShuffleOff", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_SHUFFLE_ON)) {
      sendUpdate("onPlayback.ShuffleOn", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_TRACK_CHANGED)) {
      sendUpdate("onPlayback.TrackChanged", new Object[] {});
    } else if (playerEvent.name().equals(EVENT_TRACK_DELIVERED)) {
      sendUpdate("onPlayback.TrackDelivered", new Object[] {});
    }

    // Remember kids, always use the English locale when changing case for non-UI
    // strings!
    // Otherwise you'll end up with mysterious errors when running in the Turkish
    // locale.
    // See: http://java.sys-con.com/node/46241
  }

  @Override
  public void onPlaybackError(Error error) {
    Log.d("MainActivity", "Playback error received: " + error.toString());
    JSONArray array = new JSONArray();
    array.put(error);
    sendUpdate("onPlayback.Error", new Object[] { array });
  }

  public void sendUpdate(final String action, final Object[] params) {
    String method = String.format("%s%s", METHOD_SEND_TO_JS_OBJ, action);
    final StringBuilder jsCommand = new StringBuilder();

    jsCommand.append("javascript:").append(method).append("(");
    int nbParams = params.length;
    for (int i = 0; i < nbParams;) {
      Log.d(TAG, "sendUpdate" + params[i]);
      jsCommand.append(params[i++]);
      if (i != nbParams) {
        jsCommand.append(",");
      }
    }
    jsCommand.append(")");

    Log.d(TAG, "sendUpdate jsCommand : " + jsCommand.toString());

    mWebView.getView().post(new Runnable() {
      public void run() {
        mWebView.loadUrl(jsCommand.toString());
      }
    });
  }
}
