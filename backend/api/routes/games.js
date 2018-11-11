const games = require('express').Router();

const Sequelize = require('sequelize');
const uuid = require('uuid/v4');

// Custom Response Handler
const { sendCustomResponse, sendCustomErrorResponse } = require('../handlers/customResponse');

// Sequelize models
const User = require('../models').User;
const Game = require('../models').Game;
const Team = require('../models').Team;
const Location = require('../models').Location;

async function getGameTeams(gameId) {
    // Returns the first and second team of a given game
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
            firstTeam: firstTeam.Player,
            secondTeam: secondTeam.Player
        }

    } catch (error) {
        console.log(error);
    }
}

async function getUserGame(gameId, authToken) {
    // Returns a user object only if the given game has been created by the currently authenticated user
    return await User.findOne({
        where: {
            authToken: authToken
        },
        include: [{
            model: Game,
            through: 'userGames',
            where: {
                id: gameId
            },
            required: true
        }]
    });
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

                // Automatically add user to the first team
                firstTeam.addPlayer(user, { through: 'teamPlayers' });

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
        // Get game based id
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

        // Game found
        if (game !== null) {
            // Get first and second team
            const teams = await getGameTeams(game.id);

            // Modify game JSON object
            // Rename 'Location' attribute to 'location'
            game.dataValues.location = game.Location;
            delete game.dataValues.Location;

            // Include first and second team
            game.dataValues.teams = [teams.firstTeam];
            game.dataValues.teams[1] = teams.secondTeam;

            // Send response - HTTP 200 OK
            sendCustomResponse(res, 200, [game]);
        } else {
            sendCustomErrorResponse(res, 404, "No game with such id.");
        }
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

        if (games !== null) {
            // Rename Location attribute to location
            games.map((game) => {
                game.dataValues.location = game.Location;
                delete game.dataValues.Location;
            });
            // Send response - HTTP 200 OK
            sendCustomResponse(res, 200, games);
        } else {
            // Send empty array of games
            sendCustomResponse(res, 200, []);
        }
    } catch (error) {
        // TODO: Log errors
        console.log(error);

        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Error while listing games.");
    }
});

// Update game
games.patch('/:id', async (req, res) => {
    try {
        // Check if the authenticated user has created the requested game
        const userGame = await getUserGame(req.params.id, req.headers['auth-token']);

        if (userGame !== null) {
            const game = await userGame.Games[0].update({
                name: req.body.data[0].name,
                type: req.body.data[0].type,
                size: req.body.data[0].size,
                opponents: req.body.data[0].opponents,
                eventDate: Date.now(),
                description: req.body.data[0].description
            });

            // Guard
            // Validate the data against the Game model
            await game.validate();

            // Update game details
            await game.save();

            // Send response - HTTP 200 OK
            sendCustomResponse(res, 200, null);
        } else {
            // Send error response - HTTP 401 Unauthorized
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action. ");
        }

    } catch (error) {
        // TODO: Log error
        console.log(error);
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
        sendCustomResponse(res, 200, [teams.firstTeam, teams.secondTeam]);
    } catch (error) {
        //TODO: Log error
        console.log(error);
        // Send error response - HTTP 500 Internal Server Error      
        sendCustomErrorResponse(res, 500, "Error while listing game teams.");
    }
});

// Delete game
games.delete('/:id', async (req, res) => {
    try {
        // Get the game if user is its creator
        const userGame = await getUserGame(req.params.id, req.headers['auth-token']);

        // User found
        if (userGame !== null) {
            await userGame.Games[0].destroy();
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
                    if (teams.firstTeam.find(usr => usr.id === user.id)) {
                        sendCustomErrorResponse(res, 409, "User is already member of the team.");
                    } else {
                        // Check if first team is not full
                        if (teams.firstTeam.length < game.size) {
                            // Check if user is part of the second team on this game
                            if (teams.secondTeam.find(usr => usr.id === user.id)) {
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
                    if (teams.secondTeam.find(usr => usr.id === user.id)) {
                        sendCustomErrorResponse(res, 409, "User is already member of the team.");
                    } else {
                        // Check if second team is not full                       
                        if (teams.secondTeam.length < game.size) {
                            // Check if user is part of the first team on this game
                            if (teams.firstTeam.find(usr => usr.id === user.id)) {
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
                    sendCustomErrorResponse(res, 500, "Wrong game / team combination.");
                }

            } else {
                sendCustomErrorResponse(res, 404, "Couldn't find the specified game.");
            }
        } else {
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action. Unrecognized User.");
        }
    } catch (error) {
        // TODO: Log error
        console.log(error);
        sendCustomErrorResponse(res, 500, "Error while joining team.");
    }
});

// Remove user from game team
games.delete('/:gameId/teams/:teamId/users/:userId', async (req, res) => {
    try {
        const user = await getUserGame(req.params.gameId, req.headers['auth-token']);

        // User has created this game
        if (user !== null) {
            // Get game
            const userGame = user.Games[0];

            // Add extra check to see if game actually belongs to user
            if (userGame !== null) {
                // Find target user
                const targetUser = await User.findOne({
                    where: {
                        id: req.params.userId
                    }
                });
                // Check if target user exists
                if (targetUser !== null) {
                    const teams = await getGameTeams(req.params.gameId);
                    const firstTeam = await userGame.getFirstTeam();
                    const secondTeam = await userGame.getSecondTeam();

                    if (firstTeam.id == req.params.teamId) {
                        // Remove from first team
                        // Check if the user is part of the first team
                        if (teams.firstTeam.find(usr => usr.id === targetUser.id)) {
                            // Check if first team is not empty
                            if (teams.firstTeam.length >= 1) {
                                await firstTeam.removePlayer(targetUser, { through: 'teamPlayers' });
                                sendCustomResponse(res, 200, null);
                            } else {
                                sendCustomErrorResponse(res, 500, "Cannot remove user from team. Looks like team is empty.");
                            }
                        } else {
                            sendCustomErrorResponse(res, 409, "User is not a member of this team.");
                        }
                    } else if (secondTeam.id == req.params.teamId) {
                        // Remove from second team
                        // Check if user is part of the second team
                        if (teams.secondTeam.find(usr => usr.id === targetUser.id)) {
                            // Check if second team is not empty                       
                            if (teams.secondTeam.length >= 1) {
                                // Check if user is part of the first team on this game
                                await secondTeam.removePlayer(targetUser, { through: 'teamPlayers' });
                                sendCustomResponse(res, 200, null);
                            } else {
                                sendCustomErrorResponse(res, 500, "Cannot remove user from team. Looks like team is empty.");
                            }
                        } else {
                            sendCustomErrorResponse(res, 409, "User is not member of the team.");
                        }
                    } else {
                        // Team doesn't belong to the specified game
                        // Invalid combination of gameId and teamId
                        sendCustomErrorResponse(res, 500, "Wrong game / team combination.");
                    }
                } else {
                    sendCustomErrorResponse(res, 500, "An error has occurred while removing user from team.");
                }
            } else {
                sendCustomErrorResponse(res, 500, "An error occurred while retrieving user game.");
            }
        } else {
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.");
        }
    } catch (error) {
        // TODO: Log error
        console.log(error);
        sendCustomErrorResponse(res, 500, "An error occurred while trying to remove player from team.");
    }
});

module.exports = games;