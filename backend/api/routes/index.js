const router = require('express').Router();

router.get('/', (req, res) => {
    res.send("Welcome to the teamUp's API service.");
});
router.use('/users', require('./users'));
router.use('/games', require('./games'));

module.exports = router;