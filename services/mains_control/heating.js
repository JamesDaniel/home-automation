var restify = require('restify');
var mongoose = require("mongoose");
var dateTime = require('node-datetime');

// Server setup
var heating = restify.createServer({
    version: '1.0.0'
});
heating.use(restify.plugins.bodyParser({ mapParams: false }));
heating.listen(8080, function() {
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
// 1. Get heating status (e.g. on/off)
heating.get('/livingRoom/oilFilled1', function (req, res, next) {
    heatingStatus.find({}).exec(function(error,collection) {
        console.log("callback for model query");
        console.log(JSON.stringify(collection[0], null, 4));
        var jsonResponse = {
            oilFilled1: collection[0].oilFilled1
        };
        res.json(jsonResponse);
    });
    next();
});
// 2. Turn heating off
heating.post('/livingRoom/oilFilled1/off', function(req, res, next) {
    console.log("Turning oilFilled1 off");
    heatingStatus.update({}, {oilFilled1: 'off', lastUpdated: getCurrentTime()}, function() {
        res.json({oilFilled1: "off"});
    });
    next();
});
// 3. Turn heating on
heating.post('/livingRoom/oilFilled1/on', function(req, res, next) {
    console.log("Turning oilFilled1 on");
    heatingStatus.update({}, {oilFilled1: 'on', lastUpdated: getCurrentTime()}, function() {
        res.json({oilFilled1: "on"});
    });
    next();
});