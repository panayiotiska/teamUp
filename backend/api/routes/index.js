const router = require('express').Router();

// Default welcoem message
router.get('/', (req, res) => {
    res.send("Welcome to the teamUp's API service.");
});
// API endpoints
router.use('/users', require('./users'));
router.use('/games', require('./games'));

module.exports = router;