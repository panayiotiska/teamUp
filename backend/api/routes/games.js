// API /games route
const games = require('express').Router();

const Sequelize = require('sequelize');
const uuid = require('uuid/v4');

// Custom Response Handler
const { sendCustomResponse, sendCustomErrorResponse } = require('../handlers/customResponse');

// // Database models
const User = require('../models').User;
const Game = require('../models').Game;
const Team = require('../models').Team;
const Location = require('../models').Location;

// Create game
games.post('/', async (req, res) => {
    try {
        // Find the user(id) that wants to create this game
        // TODO: Use authorization and authentication to find out who the user really is.
        const user = await User.findOne({
            where: {
                authToken: req.headers["auth-token"]
            }
        });

        // Create location
        await Location.findOrCreate({
            where: {
                city: req.body.location.city,
                address: req.body.location.address,
                countryCode: req.body.location.countryCode
            }, defaults: {
                city: req.body.location.city,
                address: req.body.location.address,
                country: req.body.location.country,
                countryCode: req.body.location.countryCode,
                postalCode: req.body.location.postalCode,
                latitude: req.body.location.latitude,
                longitude: req.body.location.longitude
            }
        }).spread(async (location, created) => {

            // Location created successfully
            if (location !== null && user !== null) {
                // Build Game instance
                const game = await Game.create({
                    createdBy: user.id,
                    name: req.body.name,
                    type: req.body.type,
                    size: req.body.size,
                    status: 'active',
                    opponents: req.body.opponents,
                    description: req.body.description,
                    eventDate: req.body.eventDate,
                    locationId: location.id
                });

                // Create teams
                const firstTeam = await Team.create();
                const secondTeam = await Team.create();

                // Guard
                // Validate the data against the Game model
                await game.validate();

                // Create game
                await game.save();

                // Add teams to game
                await game.setFirstTeam(firstTeam);
                await game.setSecondTeam(secondTeam);

                // Link game to user
                await user.addGame(game, { through: 'userGames' });

                // Delete timestamps
                delete game.dataValues.createdAt;
                delete game.dataValues.updatedAt;

                // Replace locationId with actual location data
                delete game.dataValues.locationId;
                delete location.dataValues.createdAt;
                delete location.dataValues.updatedAt;
                game.dataValues.location = location.dataValues;

                // Send response - HTTP 201 Created
                sendCustomResponse(res, 201, [game]);
            } else {
                sendCustomErrorResponse(res, 401, "Unauthorized")
            }
        });

    } catch (error) {
        //TODO: Log error
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't create game.");

        console.log(error);

    }
});

// Get games
games.get('/', async (req, res) => {
    try {
        const games = await Game.findAll({
            where: {
                status: 'active'
            },
            include: [
                {
                    model: Location,
                    attributes: {
                        exclude: ['id', 'latitude', 'longitude', 'createdAt', 'updatedAt']
                    }
                }
            ],
            attributes: {
                exclude: ['locationId', 'createdAt', 'updatedAt', 'opponents', 'description', 'firstTeamId', 'secondTeamId']
            },
            order: [['eventDate', 'ASC']]
        });

        // Send response - HTTP 200 OK
        sendCustomResponse(res, 200, [games]);
    } catch (error) {
        // TODO: Log errors
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get games.");
        console.log(error);

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
                        exclude: ['id', 'createdAt', 'updatedAt']
                    }
                },
                {
                    model: User,
                    through: 'userGames',
                    through: {
                        attributes: []
                    },
                    attributes: []
                }
            ],
            attributes: {
                include: [[Sequelize.literal('Users.id'), 'createdBy'], [Sequelize.literal('Users.phoneNumber'), 'contact']],
                exclude: ['locationId', 'updatedAt']
            }
        });

        // Rename Location attribute to location
        game.dataValues.location = game.Location;
        delete game.dataValues.Location;

        // Get players of the first team       
        const firstTeam = await game.getFirstTeam({
            include: {
                model: User,
                as: 'Player',
                through: 'teamPlayers',
                through: {
                    attributes: []
                },
                attributes: ['id', 'firstName', 'lastName']
            },
            attributes: {
                exclude: ['createdAt', 'updatedAt']
            }
        });

        // get players of the second team
        const secondTeam = await game.getSecondTeam({
            include: {
                model: User,
                as: 'Player',
                through: 'teamPlayers',
                through: {
                    attributes: []
                },
                attributes: ['id', 'firstName', 'lastName']
            },
            attributes: {
                exclude: ['createdAt', 'updatedAt']
            }
        });

        // Include teams
        game.dataValues.teams = [firstTeam.Player];
        game.dataValues.teams[1] = secondTeam.Player;


        // Send response - HTTP 200 OK
        sendCustomResponse(res, 200, [game]);
    } catch (error) {
        // TODO: Log error
        console.log(error);

        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get game details.");
    }
});

// Get game teams
games.get('/:id/teams', async (req, res) => {
    try {
        // Find game
        const game = await Game.findOne({
            where: {
                id: req.params.id
            }
        });

        // Get players of the first team       
        const firstTeam = await game.getFirstTeam({
            include: {
                model: User,
                as: 'Player',
                through: 'teamPlayers',
                through: {
                    attributes: []
                },
                attributes: ['id', 'firstName', 'lastName']
            },
            attributes: {
                exclude: ['createdAt', 'updatedAt']
            }
        });

        // get players of the second team
        const secondTeam = await game.getSecondTeam({
            include: {
                model: User,
                as: 'Player',
                through: 'teamPlayers',
                through: {
                    attributes: []
                },
                attributes: ['id', 'firstName', 'lastName']
            },
            attributes: {
                exclude: ['createdAt', 'updatedAt']
            }
        });

        // Send response - HTTP 200 OK
        sendCustomResponse(res, 200, [firstTeam.Player, secondTeam.Player]);
    } catch (error) {
        //TODO: Log error
        // Send error response - HTTP 500 Internal Server Error      
        sendCustomErrorResponse(res, 500, "Couldn't get teams.")
    }
});

// Join a Game Team
games.post('/:gameId/teams/:teamId', async (req, res) => {
    try {

        // Find user
        const user = await User.findOne({
            where: {
                authToken: req.headers['auth-token']
            }
        });

        if (user !== null) {
            // Find game
            const game = await Game.findOne({
                where: {
                    id: req.params.gameId
                }
            });

            // Game found
            if (game !== null) {
                // Get first and second team
                // TODO: Check if the user is already part of the team 
                if (req.params.teamId == game.firstTeamId) {
                    const firstTeam = await game.getFirstTeam();
                    // Check if first team is full
                    if(firstTeam.length < game.size){
                        firstTeam.addPlayer(user, { through: 'teamPlayers' });
                    }else{
                        sendCustomErrorResponse(res, 500, "Cannot join first team. Team full.");
                    }
                } else {
                    const secondTeam = await game.getSecondTeam();
                    // Check if second team is full
                    if(secondTeam.length < game.size){
                        secondTeam.addPlayer(user, { through: 'teamPlayers' });
                    }else{
                        sendCustomErrorResponse(res, 500, "Cannot join the  team. Team full.");
                    }
                }

                sendCustomResponse(res, 200, null);
            } else sendCustomResponse(res, 404, "Couldn't find the specified game.");
        } else sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action. Unrecognized User.")


    } catch (error) {
        console.log(error);
        sendCustomErrorResponse(res, 500, "Couldn't join team")

    }
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
        if (game !== null) {
            const tmpGame = await game.update({
                name: req.body.data[0].name,
                type: req.body.data[0].type,
                size: req.body.data[0].size,
                opponents: req.body.data[0].opponents,
                eventDate: Date.now(),
                description: req.body.data[0].description
            });

            // Guard
            // Validate the data against the Game model
            await tmpGame.validate();

            // Update game details
            await tmpGame.save();

            // Send response - HTTP 200 OK
            sendCustomResponse(res, 200, null);
        } else {
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
games.patch('/:gameId/teams/:teamId', (req, res) => {
    res.json({ msg: "Change team" });
});

// Delete Game
games.delete('/', (req, res) => {
    res.json({ msg: "Delete a game" });
});

// Remove User from Game Team
games.delete('/:gameId/teams/:teamId', (req, res) => {
    res.json({ msg: "Remove a player from the team" });
});

module.exports = games;