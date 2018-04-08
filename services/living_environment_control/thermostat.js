var restify = require('restify');
var mongoose = require("mongoose");
var dateTime = require('node-datetime');

// Server setup
var heating = restify.createServer({
    version: '1.0.0'
});
heating.use(restify.plugins.bodyParser({ mapParams: false }));
heating.listen(8082, function() {
    console.log('%s listening at %s', heating.name, heating.url);
});

// todo Database setup
mongoose.connect('mongodb://');
var heatingStatus = mongoose.model('heatingStatus', new mongoose.Schema({
    oilFilled1: {type:String},
    temperatureC: {type:String},
    lastUpdated: {type:String}
}), 'heatingStatus');

// Utilities
function getCurrentTime() {
    var dt = dateTime.create();
    var formatted = dt.format('Y-m-d H:M:S');
    console.log(formatted);
    return formatted;
}

// Endpoints
// 1. Store temperature in database
heating.post('/livingRoom/temperature/:temperature', function(req, res, next) {
    console.log("TempC: " + req.params.temperature);
    heatingStatus.update({}, {temperatureC: req.params.temperature, lastUpdated: getCurrentTime()}, function() {
        res.json({temperatureC: req.params.temperature});
    });
    next();
});
// 2. Get temperature
heating.get('/livingRoom/temperature/', function (req, res, next) {
    heatingStatus.find({}).exec(function(error,collection) {
        console.log("callback for model query");
        console.log(JSON.stringify(collection[0], null, 4));
        var jsonResponse = {
            temperatureC: collection[0].temperatureC
        };
        res.json(jsonResponse);
    });
    next();
});