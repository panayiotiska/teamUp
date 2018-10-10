const games = require('express').Router();

// list games
games.get('/', (req, res) => {
    res.json({msg: "Get list of available games"});
});

games.get('/:id', (req, res) => {
    res.json({msg: ""});
});

games.get('/:id/teams', (req, res) => {
    res.json({msg: "Get the teams of a game"});
});

games.get('/:id/ratings', (req, res) => {
    res.json({msg: "Get the ratings of a game"});
});


// create game
games.post('/', (req, res) => {
    res.json({msg: "Create new game"});
});

games.post('/:id', (req, res) => {
    res.json({msg: "Join game"});
});

games.post('/:id/teams', (req, res) => {
    res.json({msg: "Join team of a game"});
});

games.post('/:id/ratings', (req, res) => {
    res.json({msg: "Submit a rating for the game"});
});

games.patch('/:id', (req, res) => {
    res.json({msg: "Update game details"});
});

games.patch('/:id/teams', (req, res) => {
    res.json({msg: "Change team"});
});

games.patch('/:id/ratings', (req, res) => {
    res.json({msg: "Update a rating of a game"});
});


games.delete('/', (req, res) => {
    res.json({msg: "Delete a game"});
});

games.delete('/:id', (req, res) => {
    res.json({msg: "Remove a player from the game"});
});

games.delete('/:id/teams', (req, res) => {
    res.json({msg: "Remove a player from the team"});
});

games.delete('/:id/ratings', (req, res) => {
    res.json({msg: "Delete a game rating"});
});

module.exports = games;