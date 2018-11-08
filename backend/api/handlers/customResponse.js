// Customize a successful HTTP response
function sendCustomResponse(res, statusCode, data){
    res.statusCode = statusCode;
    res.json({
        result: {
            status: "success",
            error: null
        },
        payload: data // Should be always an array of data
    });
}

// Send a customized HTTP response containing specific error messages
function sendCustomErrorResponse(res, statusCode, errorMessage){
    res.statusCode = statusCode;
    res.json({
        result: {
            status: "error",
            error: errorMessage
        },
        payload: null
    });
}

module.exports.sendCustomResponse =  sendCustomResponse;
module.exports.sendCustomErrorResponse = sendCustomErrorResponse;