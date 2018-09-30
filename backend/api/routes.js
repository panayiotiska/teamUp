let express = require('express');
let bodyParser = require('body-parser');
let router = express.Router();

// Dummy data
let games = require('./data/games.json');
let gameDetails = require('./data/gameDetails.json');
let users = require('./data/users.json');

// Debug
router.get('/', function(req, res) {
    res.json({ message: 'DEBUG: API is Online!' });   
});

// Create user
router.post('/users/create', (req, res) => {
    res.json([{hello: 'hello'}]);
});

// Authenticate user
router.post('/users/auth', (req, res) => {
    
});

// User profile details
router.post('/users/profile', (req, res) => {
    res.json(users);
});

// Submit ratings
router.post('/users/ratings', (req, res) => {

});

// Update ratings
router.post('/users/ratings/update', (req, res) => {

});

// List games
router.post('/games', (req, res) => {
    res.json(games);
});

// Game details
router.post('/games/details', (req, res) => {
    res.json(gameDetails);
});

// Create game
router.post('/games/create', (req, res) => {

});

// Delete game
router.post('/games/delete', (req, res) => {

});

// Kick player from game
router.post('/games/kick', (req, res) => {

});

// Join game
router.post('/games/join', (req, res) => {

});


// Quit game
router.post('/games/quit', (req, res) => {

});

// Game teams
router.post('/games/teams', (req, res) => {

});

// Change team
router.post('/games/teams/change', (req, res) => {

});

module.exports = router;