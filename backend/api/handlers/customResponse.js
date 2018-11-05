// Customize a successfull response with a HTTP Status Code and data
function sendCustomResponse(res, statusCode, data){
    res.statusCode = statusCode;
    res.json({
        status: {
            result: "success",
            error: null
        },
        payload: data // Should always be an array of data
    });
}

// Send a customized response containing specific error messages
function sendCustomErrorResponse(res, statusCode, errorMessage){
    res.statusCode = statusCode;
    res.json({
        status: {
            result: "error",
            error: errorMessage
        },
        payload: null
    });
}

module.exports.sendCustomResponse =  sendCustomResponse;
module.exports.sendCustomErrorResponse = sendCustomErrorResponse;