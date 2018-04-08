var restify = require('restify');
var mongoose = require("mongoose");
var dateTime = require('node-datetime');
var moment = require('moment');

// Server setup
var heartbeat = restify.createServer({
    version: '1.0.0'
});
heartbeat.use(restify.plugins.bodyParser({ mapParams: false }));
heartbeat.listen(8081, function() {
    console.log('%s listening at %s', heartbeat.name, heartbeat.url);
});

// todo database setup
mongoose.connect('mongodb://');
var systemHeartbeat = mongoose.model('systemHeartbeat', new mongoose.Schema({
    heatingUnit1Beat: {type:String}
}), 'systemHeartbeat');

// Utilities
function getCurrentTime() {
    var dt = dateTime.create();
    var formatted = dt.format('Y-m-d H:M:S');
    console.log(formatted);
    return formatted;
}

// Endpoints
// 1. Unit 1 pulse as server timestamp
heartbeat.post('/livingRoom/heartbeatOilFilled1', function(req, res, next) {
    console.log("Heartbeat Oil filled 1: " + getCurrentTime());
    systemHeartbeat.update({}, {heatingUnit1Beat: getCurrentTime()}, function() {
        console.log("{heatingUnit1Beat: "+getCurrentTime()+"}");
        res.json({heatingUnit1Beat: getCurrentTime()});
    });
    next();
});
// 2. Unit 1 Alive based on how old last heartbeat was
heartbeat.get('/livingRoom/heartbeatOilFilled1', function(req, res, next) {
    console.log(getCurrentTime() + " GET Heartbeat Oil filled 1");
    systemHeartbeat.find({}).exec(function (error, collection) {
        console.log(getCurrentTime() + " callback for model query on /livingRoom/heartbeatOilFilled1");
        var currentTime = Date.parse(getCurrentTime());
        var databaseDateTime = Date.parse(collection[0].heatingUnit1Beat);
        var heartbeatExpiryTime = Date.parse(moment(databaseDateTime).add(20, 's').toDate());
        var oilFilled1Active = currentTime < heartbeatExpiryTime;
        var jsonResponse = {
            heartbeatOilFilled1Active: oilFilled1Active
        };
        res.json(jsonResponse);
        next();
    });
});
