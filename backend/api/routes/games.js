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

// List game teams
async function getGameTeams(gameId) {
    try {
        // Find game
        const game = await Game.findOne({
            where: {
                id: gameId
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

        // Get players of the second team
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

        return {
            firstTeam: firstTeam,
            secondTeam: secondTeam
        }

    } catch (error) {
        console.log(error);
    }
}

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

// Get game
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

// List games
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
        sendCustomResponse(res, 200, games);
    } catch (error) {
        // TODO: Log errors
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get games.");
        console.log(error);

    }
});

// Update game
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

// List game teams
games.get('/:id/teams', async (req, res) => {
    try {
        // Get teams
        const teams = await getGameTeams(req.params.id);

        // Send response - HTTP 200 OK
        sendCustomResponse(res, 200, [teams.firstTeam.Player, teams.secondTeam.Player]);
    } catch (error) {
        //TODO: Log error
        console.log(error);
        // Send error response - HTTP 500 Internal Server Error      
        sendCustomErrorResponse(res, 500, "Couldn't get teams.")
    }
});

// Delete game
games.delete('/:id', async (req, res) => {
    try {
        // Find user
        // Returns a user object only if the given game has been created by the authenticated user
        const user = await User.findOne({
            where: {
                authToken: req.headers['auth-token']
            },
            include: [{
                model: Game,
                through: 'userGames',
                where: {
                    id: req.params.id
                },
                required: true
            }]
        });

        // User found
        if (user !== null) {
            // Delete game
            const game = await Game.destroy({
                where: {
                    id: req.params.id
                }
            });
            sendCustomResponse(res, 200, null);
        } else {
            // User is not the creator of this game
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.");
        }
    } catch (error) {
        // TODO: Log error
        console.log(error);
        sendCustomErrorResponse(res, 500, "Couldn't delete game.");
    }
});

// Join a game's team
games.post('/:gameId/teams/:teamId', async (req, res) => {
    try {
        // Get game teams
        const teams = await getGameTeams(req.params.gameId);

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
                // Get game's first and second team
                const firstTeam = await game.getFirstTeam();
                const secondTeam = await game.getSecondTeam();

                if (firstTeam.id == req.params.teamId) {
                    // Join first team
                    // Check if the user is already part of the first team
                    if (teams.firstTeam.Player.find(usr => usr.id === user.id)) {
                        sendCustomErrorResponse(res, 409, "User is already member of the team.");
                    } else {
                        // Check if first team is not full
                        if (teams.firstTeam.Player.length < game.size) {
                            // Check if user is part of the second team on this game
                            if (teams.secondTeam.Player.find(usr => usr.id === user.id)) {
                                // Remove user from second team
                                await secondTeam.removePlayer(user, { through: 'teamPlayers' });
                            }
                            await firstTeam.addPlayer(user, { through: 'teamPlayers' });
                            sendCustomResponse(res, 200, null);
                        } else {
                            sendCustomErrorResponse(res, 500, "Cannot join team. Team is full.");
                        }
                    }
                } else if (secondTeam.id == req.params.teamId) {
                    // Join second team
                    // Check if user is already part of the second team
                    if (teams.secondTeam.Player.find(usr => usr.id === user.id)) {
                        sendCustomErrorResponse(res, 409, "User is already member of the team.");
                    } else {
                        // Check if second team is not full                       
                        if (teams.secondTeam.Player.length < game.size) {
                            // Check if user is part of the first team on this game
                            if (teams.firstTeam.Player.find(usr => usr.id === user.id)) {
                                // Remove user from second team
                                await firstTeam.removePlayer(user, { through: 'teamPlayers' });
                            }
                            await secondTeam.addPlayer(user, { through: 'teamPlayers' });
                            sendCustomResponse(res, 200, null);
                        } else {
                            sendCustomErrorResponse(res, 500, "Cannot join team. Team full.");
                        }
                    }

                } else {
                    // Team doesn't belong to the specified game
                    // Invalid combination of gameId and teamId
                    sendCustomErrorResponse(res, 500, "Wrong team / game combination.");
                }

            } else {
                sendCustomResponse(res, 404, "Couldn't find the specified game.");
            }
        } else {
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action. Unrecognized User.");
        }


    } catch (error) {
        // TODO: Log error
        console.log(error);
        sendCustomErrorResponse(res, 500, "Couldn't join team");
    }
});

// Remove User from Game Team
games.delete('/:gameId/teams/:teamId', (req, res) => {
    res.json({ msg: "Remove a player from the team" });
});

module.exports = games;