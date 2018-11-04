const router = require('express').Router();

router.get('/', (req, res) => {
    // Default welcome message
    res.send("Welcome to the teamUp's API service.");
});

// API endpoints
router.use('/users', require('./users'));
router.use('/games', require('./games'));

module.exports = router;