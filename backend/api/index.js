const express = require('express');
const bodyParser = require('body-parser');        
const app = express();
const port = process.env.PORT || 3000;

// Use bodyParser in order to transmit data in JSON format.
app.use(bodyParser.json());

// The API service is listening under the path '/api/v1/'
app.use('/api/v1', require('./routes/'));

app.listen(port, () => {
    console.log("[DEBUG] teamUp API service is running on local port " + port);
});