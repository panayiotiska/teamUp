const express = require('express');        
const app = express();
const port = process.env.PORT || 3000;

app.use('/api/v1', require('./routes/index'));

app.listen(port, () => {
    console.log("MSG: teamUp API service is running on local port 3000");
   });