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
  loadUserPlaylists: function() {
    return axios.get('https://api.spotify.com/v1/me/playlists?limit=50', { headers: { Authorization: 'Bearer ' + authToken } })
      .then(function(response) {
        if(response) {
          return response.data.items;
        }
      })
      .catch(function(error) {
        return error.response.data.error;
      });
  },

  loadUserPlaylistTracks: function(id) {
    return axios.get(`https://api.spotify.com/v1/playlists/${id}/tracks`, { headers: { Authorization: 'Bearer ' + authToken } })
      .then(function(response) {
        if(response) {
          return response.data.items;
        }
      })
      .catch(function(error) {
        return error.response.data.error;
      });
  },

  loadUserAlbums: function() {
    return axios.get('https://api.spotify.com/v1/me/albums?limit=50', { headers: { Authorization: 'Bearer ' + authToken } })
      .then(function(response) {
        if(response) {
          return response.data.items;
        }
      })
      .catch(function(error) {
        return error.response.data.error;
      });
  },

  loadUserAlbumTracks: function(id) {
    return axios.get(`https://api.spotify.com/v1/albums/${id}/tracks`, { headers: { Authorization: 'Bearer ' + authToken } })
      .then(function(response) {
        if(response) {
          return response.data.items;
        }
      })
      .catch(function(error) {
        return error.response.data.error;
      });
  },

  loadUserArtists: function() {
    return axios.get('https://api.spotify.com/v1/me/following?type=artist', { headers: { Authorization: 'Bearer ' + authToken } })
      .then(function(response) {
        if(response) {
          return response.data.items;
        }
      })
      .catch(function(error) {
        return error.response.data.error;
      });
  },

  loadFeaturedPlaylists: function() {
    return axios.get('https://api.spotify.com/v1/browse/featured-playlists', { headers: { Authorization: 'Bearer ' + authToken } })
      .then(function(response) {
        if(response) {
          return response.data.items;
        }
      })
      .catch(function(error) {
        return error.response.data.error;
      });
  },

  loadFeaturedPlaylistracks: function(id) {
    return axios.get(`https://api.spotify.com/v1/albums/${id}/tracks`, { headers: { Authorization: 'Bearer ' + authToken } })
      .then(function(response) {
        if(response) {
          return response.data.items;
        }
      })
      .catch(function(error) {
        return error.response.data.error;
      });
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
