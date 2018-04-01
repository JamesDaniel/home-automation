var restify = require('restify');
var mongoose = require("mongoose");

// todo Add correct values for the following variables.
var listeningPort = -1;
var databaseUser = "";
var databasePass = "";
var databaseAddr = "";
var databaseName = "";

var server = restify.createServer({
    version: '1.0.0'
});
server.use(restify.plugins.bodyParser({ mapParams: false }));
server.get('/livingRoom/oilFilled1', function (req, res, next) {
    respondLivingOilFilled1(res);
    next();
});
server.post('/livingRoom/oilFilled1/off', function(req, res, next) {
    console.log("Turning oilFilled1 off");
    heatingStatus.update({}, {oilFilled1: 'off'}, function() {
        res.json({oilFilled1: "off"});
    });
    next();
});
server.post('/livingRoom/oilFilled1/on', function(req, res, next) {
    console.log("Turning oilFilled1 on");
    heatingStatus.update({}, {oilFilled1: 'on'}, function() {
        res.json({oilFilled1: "on"});
    });
    next();
});

server.listen(listeningPort, function() {
    console.log('%s listening at %s', server.name, server.url);
});

// Connect to the db
mongoose.connect('mongodb://'+databaseUser+':'+databasePass+'@'+databaseAddr+'/'+databaseName);

// create model for data
var Schema = mongoose.Schema;
var heatingStatus = mongoose.model('heatingStatus', new Schema({
    oilFilled1: {type:String}
}), 'heatingStatus');

// response with database data
function respondLivingOilFilled1(res) {
    heatingStatus.find({}).exec(function(error,collection) {
        console.log("callback for model query");
        console.log(JSON.stringify(collection[0], null, 4));
        var jsonResponse = {
            oilFilled1: collection[0].oilFilled1
        };
        res.json(jsonResponse);
    });
}
