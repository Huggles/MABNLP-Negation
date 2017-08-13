// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    	= require('express');        // call express
var app        	= express();                 // define our app using express
var bodyParser 	= require('body-parser');
var exec 		= require('child_process').exec;
var timeout = require('connect-timeout');
var fs = require('fs');


// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(timeout('100s'));
app.use(function (req, res, next) {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    // Pass to next layer of middleware
    next();
});

var port = process.env.PORT || 9000;        // set our port


var RootFolder = "/Users/hugovankrimpen/NodeJS_Server";

// ROUTES FOR OUR API
// =============================================================================
var router = express.Router();              // get an instance of the express Router

// test route to make sure everything is working (accessed at GET http://localhost:8080/api)

router.get('/GetLastOutput', function(req, res){
	var parsedJSON = require('./textfiles/input.txt.json');
	res.json(parsedJSON);
	
});
router.post('/clinical', function(req, res) {
    var Text = req.body.text;
    Text = Text.toString().split('"').join('..--..}');
    
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
    res.setHeader('Access-Control-Allow-Credentials', true);



    var command = 'java -cp ' + '".:negex/CallKit.class:StanfordCoreNLP/stanford-corenlp-3.7.0.jar:StanfordCoreNLP/stanford-english-corenlp-2017-06-09-models.jar:StanfordCoreNLP/:StanfordCoreNLP/stanford-corenlp-3.7.0-sources.jar"' + '  NLPClinicalExtraction "' + Text + '"';
    //res.json(command);
    exec(command,
    function (error, stdout, stderr){
        res.send(stdout)
        if(error){
            res.json(error + stderr);
        }
        else{
            res.json(JSON.parse(stdout));
        }
    });
}); 
router.post('/negation', function(req, res) {
	res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
    res.setHeader('Access-Control-Allow-Credentials', true);

    fs = require('fs');
    
    var Text = req.body.text;
    var documentid = req.body.docid;    
    var extractor = req.body.extractor;    

    var textfile_name = "NegationTermsAndText/" + documentid + '_' + extractor + '_text.txt';
    var termfile_name = "NegationTermsAndText/" + documentid + '_' + extractor + '_terms.txt';

	fs.writeFileSync(textfile_name, Text);

    var Terms = req.body.terms;
    fs.writeFileSync(termfile_name, Terms);

    var command = 'java -cp ".:negex" CallKit ' + textfile_name + " " + termfile_name;    
    //res.json(command);
    var childprocess = exec(command, 
    function (error, stdout, stderr){
        if(error){
            res.send(error + stderr);
        }
        else{
            res.send(stdout);
        }        
    });
    
}); 

// more routes for our API will happen here

// REGISTER OUR ROUTES -------------------------------
// all of our routes will be prefixed with /api
app.use('/api', router);
// Add headers
app.use(function (req, res, next) {

    

    // Pass to next layer of middleware
    next();
});

// START THE SERVER
// =============================================================================
app.listen(port);