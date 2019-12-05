var exec = require('cordova/exec');

module.exports = {
  auth: function(success, error, token, id) {
    exec(
      success,
      error,
      "SpotifyPlugin",
      "auth",
      [token, id]
    );
  },
  getPosition: function(success, error) { exec(success, error, "SpotifyPlugin", "getPosition", []); },
  getToken: function(success, error) {
    exec(
      success, // function(res){alert(res);},//res - TOKEN
      error, // function(){console.log("error");},
      "SpotifyPlugin",
      "getToken",
      []
    );
  },
  loadFeaturedPlaylists: function(success, error, token) { exec(success, error, "SpotifyPlugin", "featuredPlaylists", [token]); },
  loadFeaturedPlaylistTracks: function(success, error, id, token) { exec(success, error, "SpotifyPlugin", "featuredPlaylistTracks", [id, token]); },
  loadUserAlbums: function(success, error, token) { exec(success, error, "SpotifyPlugin", "userAlbums", [token]); },
  loadUserAlbumTracks: function(success, error, id, token) { exec(success, error, "SpotifyPlugin", "userAlbumTracks", [id, token]); },
  loadUserPlaylists: function(success, error, token) { exec(success, error, "SpotifyPlugin", "userPlaylists", [token]); },
  loadUserPlaylistTracks: function(success, error, id, token) { exec(success, error, "SpotifyPlugin", "userPlaylistTracks", [id, token]); },
  login: function(success, error, a, b, url) {
    var swap = url + '/swap';
    var refresh = url + '/refresh';

    exec(
      success,
      error,
      "SpotifyPlugin",
      "login",
      [a, b, swap, refresh]
    );
  },
  logout: function() {
    exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "logout",
      []
    );
  },
  next: function() {
    exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "next",
      []
    );
  },
  pause: function(error) {
    exec(
      function() {},
      error,
      "SpotifyPlugin",
      "pause",
      []
    );
  },
  play: function(success, error, val) {
    exec(
      success,
      error,
      "SpotifyPlugin",
      "play",
      [val]
    );
  },
  prev: function() {
    exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "prev",
      []
    );
  },
  resume: function() {
    exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "resume",
      []
    );
  },
  seek: function(val) {
    exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "seek",
      [val]
    );
  },
  seekTo: function(error, val) {
    exec(
      function() {},
      error,
      "SpotifyPlugin",
      "seekTo",
      [val]
    );
  },
  setVolume: function(val) {
    exec(
      function() {},
      function() {},
      "SpotifyPlugin",
      "setVolume",
      [val]
    );
  },

  Events: {
    onConnectionMessage: function(args) {
      alert(args[0]); // message
    },
    onError: function(args) {
      alert(args[0]); // error
    },
    onLoggedIn: function(args) {
      alert(args[0]); // access token
    },
    onLoggedOut: function() {},
    onLoginFailed: function(args) {
      alert(args[0]); // error code
    },
    onLoginResponse: function(args) {
      alert(args[0]); // response type
    },
    onPlayback: {
      AudioFlush: function(args) {
        alert(args[0]); // position (ms)
      },
      BecameActive: function() {},
      BecameInactive: function() {},
      ContextChanged: function(args) {
        alert(arg[0]); // context name
      },
      DeliveryDone: function() {},
      Error: function(args) {
        alert(args[0]); // error message
      },
      LostPermission: function() {},
      MetadataChanged: function(args) {
        alert(args);
        // args[0] - currentTrack.name
        // args[1] - currentTrack.artistName
        // args[2] - currentTrack.albumName
        // args[3] - currentTrack.durationMs
      },
      Next: function() {},
      Pause: function() {},
      Play: function() {},
      Position: function(args) {
        alert(arg[0]); // position (ms)
      },
      Prev: function() {},
      RepeatOff: function() {},
      RepeatOn: function() {},
      ShuffleOff: function() {},
      ShuffleOn: function() {},
      TrackChanged: function() {},
      TrackDelivered: function() {}
    },
    onSuccess: function() {},
    onTemporaryError: function() {}
  }
};
