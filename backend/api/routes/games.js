const games = require('express').Router();
const dateFormat = require('dateformat');

// Models
const User = require('../models/user');
const Game = require('../models/game');
const userGame = require('../models/userGame');
const Location = require('../models/location');

// Get Games
games.get('/', async (req, res) => {
    try {
        const games = await Game.findAll({
            include: [
                {
                    model: Location,
                    attributes: {
                        exclude: ['id']
                    }
                }
            ],
            attributes: {
                exclude: ['locationId']
            }
        });

        // Send data - HTTP 200 OK
        await res.json({data: games});    
    } catch (error) {
        // TODO: Log errors
        console.log(error);        
        await res.sendStatus(500);
    }
});

// Game Details
games.get('/:id', async (req, res) => {
    try {
        const game = await Game.findById(req.params.id, {
            attributes: {
                exclude: ['locationId']
            },
            include: {
                model: Location,
                attributes: {
                    exclude: ['id']
                }
            }
        });
        res.json({data: [game]});
    } catch (error) {
        console.log(error);
        res.sendStatus(401);
    }
});

// Game Teams
games.get('/:id/teams', (req, res) => {
    res.json({msg: "Get the teams of a game"});
});

// Create Game
games.post('/', async (req, res) => {
    try {
        const user = await User.findOne({
            where: {
                accessToken: req.headers["access-token"]
            },
            raw: true,
            attributes: ['id']
        });

        const game = await Game.create({
            createdBy: user.id,
            createdAt: dateFormat("dd-mm-yyyy HH:MM"),
            name: req.body.data[0].name,
            type: req.body.data[0].type,
            size: req.body.data[0].size,
            opponents: req.body.data[0].opponents,
            description: req.body.data[0].description,
            eventDate: req.body.data[0].eventDate,
            locationId: req.body.data[0].locationId,
            include: {
                model: Location
            }
        });

        // Guard
        await game.validate();

        await game.save();

        await res.sendStatus(201);
    } catch (error) {
        console.log(error);
        res.sendStatus(401);
    }
});

// Join a Game Team
games.post('/:id/teams', (req, res) => {
    res.json({msg: "Join game"});
});

// Update Game Details
games.patch('/:id', (req, res) => {
    res.json({msg: "Update game details"});
});

// Change Game Team
games.patch('/:id/teams', (req, res) => {
    res.json({msg: "Change team"});
});

// Delete Game
games.delete('/', (req, res) => {
    res.json({msg: "Delete a game"});
});

// Remove User from Game Team
games.delete('/:id/teams', (req, res) => {
    res.json({msg: "Remove a player from the team"});
});

module.exports = games;