let express = require('express');        
let app = express();
let bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());                  

app.use('/api/v1/', require('./routes'));

app.listen(3000, () => {
    console.log("Server running on port 3000");
   });