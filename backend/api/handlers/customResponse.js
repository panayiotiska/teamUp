// Customize a successfull response with a HTTP Status Code and data
function sendCustomResponse(res, statusCode, data){
    res.statusCode = statusCode;
    res.json({
        status: {
            code: 0,
            description: "success"
        },
        data: data // Should always be an array of data
    });
}

// Send a customized response containing specific error messages
function sendCustomErrorResponse(res, statusCode, errorMsg){
    res.statusCode = statusCode;
    res.json({
        status: {
            code: -1,
            description: "error",
            errorMsg: errorMsg
        }
    });
}

module.exports.sendCustomResponse =  sendCustomResponse;
module.exports.sendCustomErrorResponse = sendCustomErrorResponse;