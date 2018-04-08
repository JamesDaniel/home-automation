'use strict';

var mycro = require('./app');
mycro.start(function(err) {
    if (err) {
        mycro.log('error', 'there was an error starting router:', err);
    } else {
        mycro.log('info', 'router started successfully');
    }
});
