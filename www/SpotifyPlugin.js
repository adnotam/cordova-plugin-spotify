var exec = require('cordova/exec');
var axios = require('axios);

module.exports = {
  login: function(a, b, url) {
      var swap = url + '/swap';
      var refresh = url + '/refresh';

      exec(
        function() { },
        function() { },
        "SpotifyPlugin",
        "login",
        [a, b, swap, refresh]
      );
    },
  loadUserPlaylists: function(success, error, token) { exec(success, error, "SpotifyPlugin", "playlists", [token]); },
  loadUserAlbums: function(success, error, token) { exec(success, error, "SpotifyPlugin", "albums", [token]); },
  loadFeaturedPlaylists: function(success, error, token) { exec(success, error, "SpotifyPlugin", "featuredPls", [token]); },
  loadUserAlbumTracks: function(success, error, id, token) { exec(success, error, "SpotifyPlugin", "albumTracks", [id, token]); },
  loadUserPlaylistTracks: function(success, error, id, token) { exec(success, error, "SpotifyPlugin", "playlistTracks", [id, token]); },
  loadFeaturedTracks: function(success, error, id, token) { exec(success, error, "SpotifyPlugin", "featuredTracks", [id, token]); },
  getPosition: function(success, error) { exec(success, error, "SpotifyPlugin", "getPosition", []); },
  auth: function(success, error, token, id) {
    exec(
      success,
      error,
      "SpotifyPlugin",
      "auth",
      [token, id]
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
  pause: function() {
    exec(
      function() { },
      function() { },
      "SpotifyPlugin",
      "pause",
      []
    );
  },
  next: function() {
    exec(
      function() { },
      function() { },
      "SpotifyPlugin",
      "next",
      []
    );
  },
  prev: function() {
    exec(
      function() { },
      function() { },
      "SpotifyPlugin",
      "prev",
      []
    );
  },
  logout: function() {
    exec(
      function() { },
      function() { },
      "SpotifyPlugin",
      "logout",
      []
    );
  },
  seek: function(val) {
    exec(
      function() { },
      function() { },
      "SpotifyPlugin",
      "seek",
      [val]
    );
  },
  seekTo: function(val) {
    exec(
      function() { },
      function() { },
      "SpotifyPlugin",
      "seekTo",
      [val]
    );
  },
  setVolume: function(val) {
    exec(
      function() { },
      function() { },
      "SpotifyPlugin",
      "volume",
      [val]
    );
  },
  getToken: function(success, error) {
    exec(
      success, // function(res){alert(res);},//res - TOKEN
      error, // function(){console.log("error");},
      "SpotifyPlugin",
      "getToken",
      []
    );
  },

  Events: {
    onPlayerPlay: function(args) { },
    onMetadataChanged: function(args) { },
    onPrev: function(args) {
      // arg[0] - action
    },
    onNext: function(args) {
      // arg[0] - action
    },
    onPause: function(args) {
      // arg[0] - action
    },
    onPlay: function(args) {
      // arg[0] - action
    },
    onAudioFlush: function(arg) {
      // arg[0] - position (ms)
    },
    onTrackChanged: function(arg) {
      // arg[0] - action
    },
    onPosition: function(arg) {
      // arg[0] - position ms
    },
    onVolumeChanged: function(arg) {
      // arg - volume betwen 0.0 ....1.0
    },
    onLogedIn: function(arg) {
      alert(arg);
    },
    onDidNotLogin: function(arg) {
      alert(arg);
    },
    onPlayError: function(error) {
      alert(error[0]); // error[0] - error message
    }

  }
};
