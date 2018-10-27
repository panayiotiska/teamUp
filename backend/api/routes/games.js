// API /games route
const games = require('express').Router();

//DateFormat
const dateFormat = require('dateformat');
const Sequelize = require('sequelize');

// Custom Response Handler
const {sendCustomResponse, sendCustomErrorResponse} = require('../handlers/customResponse');

// Database models
const User = require('../models/user');
const Game = require('../models/game');
const userGame = require('../models/userGame');
const Location = require('../models/location');

// Get games
games.get('/', async (req, res) => {
    try {
        const games = await Game.findAll({
            include: [
                {
                    model: Location,
                    attributes: {
                        exclude: ['id', 'latitude', 'longitude']
                    }
                }
            ],
            attributes: {
                exclude: ['locationId', 'createdAt', 'updatedAt', 'opponents', 'description']
            }
        });

        // Send response - HTTP 200 OK
        sendCustomResponse(res, 200, games);
    } catch (error) {
        // TODO: Log errors
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get games.")
    }
});

// Get game details
games.get('/:id', async (req, res) => {
    try {
        // Get game details based on its id
        const game = await Game.findOne({
            where: {
                id: req.params.id
            },
            include: [
                {
                    model: Location,
                    attributes: {
                        exclude: ['id']
                    }
                },
                {
                    model: User,
                    attributes: []
                }
            ],
            attributes: {
                include: [[Sequelize.literal('users.phoneNumber'), 'phoneNumber']],
                exclude: ['locationId']
            }
        });
        
        // Send response - HTTP 200 OK
        sendCustomResponse(res, 200, [game]);
    } catch (error) {
        // TODO: Log error
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get game details.");
    }
});

// Get game teams
games.get('/:id/teams', (req, res) => {
    res.json({msg: "Get the teams of a game"});
});

// Create new game
games.post('/', async (req, res) => {
    try {
        // Find the user(id) that wants to create this game
        // TODO: Use authorization and authentication to find out who the user really is.
        const user = await User.findOne({
            where: {
                authToken: req.headers["auth-token"]
            },
            raw: true,
            attributes: ['id']
        });       

        // Build Game instance
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
        // Validate the data against the Game model
        await game.validate();

        // Create game
        await game.save();

        // Send response - HTTP 201 Created
        sendCustomResponse(res, 201);
    } catch (error) {
        //TODO: Log error
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't create game.")
    }
});

// Join a Game Team
games.post('/:id/teams', (req, res) => {
    res.json({msg: "Join game"});
});

// Update game Details
games.patch('/:id', async (req, res) => {
    try {
        // TODO: First you need to check if the user that tries to edit this game is also authorized to perform this action.
        // TODO: Check if the user is the one who created this game. If so, then authorize the action

        // Find the specified game
        const game = await Game.findOne({
            where: {
                id: req.params.id
            }
        });

        // Found the game
        if(game !== null){
            const tmpGame = await game.update({
                name: req.body.data[0].name,
                type: req.body.data[0].type,
                size: req.body.data[0].size,
                opponents: req.body.data[0].opponents,
                eventDate: req.body.data[0].eventDate,
                description: req.body.data[0].description,
                updatedAt: dateFormat("dd-mm-yyyy HH:MM")
            });

            // Guard
            // Validate the data against the Game model
            await tmpGame.validate();

            // Update game details
            await tmpGame.save();

            // Send response - HTTP 200 OK
            sendCustomResponse(res, 200);
        }else{
            // We don't have to expose that the game doesn't exist in our database.
            // Send error response - HTTP 401 Unauthorized
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action. - DEBUG: Game doesn't exist");
        }
        
    } catch (error) {
        // TODO: Log error
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't update game details.");
    }
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