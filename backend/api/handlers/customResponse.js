// Customize a successfull response with a HTTP Status Code and data
function sendCustomResponse(res, statusCode, data){
    res.statusCode = statusCode;
    res.json({
        status: {
            code: 0,
            description: "success"
            // Missing the 'errors' field cause we don't need it on a successfull response
        },
        data: data
    });
}

// Send a customized response containing specific error messages
function sendCustomErrorResponse(res, statusCode, errMsg){
    res.statusCode = statusCode;
    res.json({
        status: {
            code: -1,
            description: "error",
            errors: [errMsg]
        },
        data: [] // Should always be returned as an array
    });
}

module.exports.sendCustomResponse =  sendCustomResponse;
module.exports.sendCustomErrorResponse = sendCustomErrorResponse;