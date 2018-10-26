function errorChecking(res, data, httpStatusCode, msg){
    if(data){
        res.status(200);
        res.json({data: data});
    } else{
        res.status(httpStatusCode);
        res.json({
            msg: msg
        });
    }
}

// TODO: Break the function into however many pieces...
// sendCustomError and sendCustomResponse

// Send custom response
// TODO: create function to send custom response


function sendCustomResponse(res, statusCode, data){
    res.statusCode = statusCode;
    res.json(data);
}

module.exports.errorChecking = errorChecking;
module.exports.sendCustomResponse =  sendCustomResponse;