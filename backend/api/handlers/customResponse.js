// Customize a successfull response with a HTTP Status Code and data
function sendCustomResponse(res, statusCode, data){
    res.statusCode = statusCode;
    res.json({
        status: {
            code: 0,
            errors: []
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
            errors: [errMsg]
        },
        data: []
    });
}

module.exports.sendCustomResponse =  sendCustomResponse;
module.exports.sendCustomErrorResponse = sendCustomErrorResponse;