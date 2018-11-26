const router = require('express').Router();

router.get('/', (req, res) => {
    // Default welcome message
    res.send("Welcome to the teamUp's API service.");
});

// REST API resources
router.use('/users', require('./users'));
router.use('/games', require('./games'));
router.use('/fields', require('./fields'));

module.exports = router;