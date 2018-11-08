const users = require('express').Router();
const dateFormat = require('dateformat');

// Custom Response Handler
const { sendCustomResponse, sendCustomErrorResponse } = require('../handlers/customResponse');

const User = require('../models').User;
const Rating = require('../models').Rating;
const Sequelize = require('../models').Sequelize;
const avgRatings = require('../models').avgRatings;

// Create user
users.post('/', async (req, res) => {
    try {
        // Check if the user is already in our database
        // TODO: Use a combination of user id and firebase token to check if the user is already registered
        const userAccount = await User.findOne({
            where: {
                $or: {
                    id: req.body.id
                }
            }
        });

        // User account doesn't exist
        if (userAccount === null) {
            // Build new User instance
            const user = await User.build({
                id: req.body.id,
                firstName: req.body.firstName,
                lastName: req.body.lastName,
                phoneNumber: req.body.phoneNumber,
                createdAt: new Date()
            });

            // Guard
            // Validate the input data against the User model
            await user.validate();

            // Create user
            await user.save();

            // Initialize average rating data
            const avgRating = await avgRatings.create({
                avgSkills: 0,
                avgOnTime: 0,
                avgBehavior: 0,
                totalAvg: 0,
                ratingsCount: 0
            });

            // Associate user with the avgRating
            await avgRating.setUser(user);

            // Modify avgRating JSON Object
            delete avgRating.dataValues.id;
            delete avgRating.dataValues.userId;
            delete avgRating.dataValues.createdAt;
            delete avgRating.dataValues.updatedAt;

            // Modify User JSON object
            // Delete timestamps
            delete user.dataValues.createdAt;
            delete user.dataValues.updatedAt;

            // Add avgRating
            user.dataValues.rating = avgRating.dataValues;

            // Send response - HTTP 201 Created
            sendCustomResponse(res, 201, [user]);
        } else {
            // TODO: Log error
            // User already exists in our database
            sendCustomErrorResponse(res, 409, "Couldn't create user. Already exists.")
        }
    } catch (error) {
        // TODO: Log error
        console.log(error);
        // Send error response - 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't create user.");
    }
});

// Get user
users.get('/:id', async (req, res) => {
    try {
        // Search for the user in the database
        const user = await User.findOne({
            where: {
                id: req.params.id
            },
            include: [{
                model: avgRatings,
                attributes: {
                    exclude: ['id', 'createdAt', 'updatedAt', 'userId']
                }
            }],
            attributes: {
                exclude: ['deviceToken', 'authToken']
            }
        });

        // User has been found successfully
        if (user) {
            // Modify user JSON object
            user.dataValues.rating = user.dataValues.avgRating;
            delete user.dataValues.avgRating;

            sendCustomResponse(res, 200, [user]);
        } else {
            sendCustomErrorResponse(res, 404, "[DEBUG]: Couldn't find user with that id.")
        }
    } catch (error) {
        // TODO: Log the error
        // Send error request - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't search for user.")
    }
});

// Create rating
users.post('/:id/ratings', async (req, res) => {
    try {
        // TODO: Find the author of this post by checking via the auth token
        const ratingAuthor = await User.findOne({
            where: {
                authToken: req.headers["auth-token"]
            },
            raw: true,
            attributes: ['id']
        });

        if (ratingAuthor !== null) {
            // Find target user
            const user = await User.findOne({
                where: { id: req.params.id },
                include: [{
                    model: avgRatings
                }]
            });

            if (user !== null) {
                // Build a Rating instance    
                const rating = await Rating.build({
                    createdBy: ratingAuthor.id,
                    createdAt: new Date(),
                    comment: req.body.comment,
                    onTime: req.body.onTime,
                    skills: req.body.skills,
                    behavior: req.body.behavior
                });

                // Guard
                // Validate the data against the Rating model
                await rating.validate();

                // Submit rating
                await rating.save();

                // Add rating to user
                await user.addRating(rating);

                // Get current averages
                const ratingsCount = user.avgRating.dataValues.ratingsCount;
                const avgSkills = user.avgRating.dataValues.avgSkills;
                const avgOnTime = user.avgRating.dataValues.avgOnTime;
                const avgBehavior = user.avgRating.dataValues.avgBehavior;

                // Increment ratingsCount
                const newRatingsCount = ratingsCount + 1;

                // The following calculations are based on this formula
                /* 
                    newAverage = (((oldAverage * numberOfRatings) + newAverage) / numberOfRatings + 1 ) 
                */

                // Calculate new averages for skills, onTime, behavior and totalAvg
                const newAvgSkills = (((avgSkills * ratingsCount) + rating.dataValues.skills) / newRatingsCount);
                const newAvgOnTime = (((avgOnTime * ratingsCount) + rating.dataValues.onTime) / newRatingsCount);
                const newAvgBehavior = (((avgBehavior * ratingsCount) + rating.dataValues.behavior) / newRatingsCount);
                const newTotalAvg = ((newAvgSkills + newAvgOnTime + newAvgBehavior) / 3);

                // Find existing avgRating
                const avgRating = await avgRatings.findOne({
                    where: {
                        userId: user.id
                    }
                });

                // Update avgRating
                await avgRating.update({
                    avgSkills: newAvgSkills,
                    avgOnTime: newAvgOnTime,
                    avgBehavior: newAvgBehavior,
                    totalAvg: newTotalAvg,
                    ratingsCount: newRatingsCount
                });

                // Modify rating JSON values
                delete rating.dataValues.createdBy;
                delete rating.dataValues.createdAt;
                delete rating.dataValues.updatedAt;

                // Send response - HTTP 201 Created
                sendCustomResponse(res, 201, [rating]);
            } else sendCustomErrorResponse(res, 500, "The user you are trying to rate does not exist.");
        } else sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action."); // Author doesn't exist.

    } catch (error) {
        //TODO: Log errors
        // Send error response - HTTP 500 Internal Server Error
        console.log(error);

        sendCustomErrorResponse(res, 500, "Couldn't submit rating.");
    }
});

// List user ratings
users.get('/:id/ratings', async (req, res) => {
    try {
        // Find target user
        const user = await User.findOne({
            where: {
                id: req.params.id
            },
            attributes: [],
            include: [{
                model: Rating,
                through: 'userRatings',
                through: {
                    attributes: []
                }
            }],
            order: [[Rating, 'createdAt', 'DESC']]
        });

        // User found
        if (user !== null) {
            for (const rating of user.Ratings) {
                // Find rating author
                const ratingAuthor = await User.findOne({
                    where: {
                        id: rating.createdBy
                    },
                    attributes: ['id', 'firstName', 'lastName']
                });

                // Update createdBy property by reference.
                rating.createdBy = {
                    id: ratingAuthor.id,
                    firstName: ratingAuthor.firstName,
                    lastName: ratingAuthor.lastName
                }
            }
            sendCustomResponse(res, 200, user.Ratings);
        } else {
            // User doesn't exist
            sendCustomErrorResponse(res, 404, "Couldn't find ratings for that user.");
        }
    } catch (error) {
        // TODO: Log the errors
        console.log(error);
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get ratings.");
    }
});

// Update user
users.patch('/me', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        const user = await User.findById(req.body.data[0].id);

        // User does exist
        if (user !== null) {
            const tmpUser = await user.update({
                firstName: req.body.data[0].firstName,
                lastName: req.body.data[0].lastName,
                updatedAt: dateFormat("dd-mm-yyyy HH:MM"),
                phoneNumber: req.body.data[0].phoneNumber,
                deviceToken: req.body.data[0].deviceToken
            });

            // Guard
            await tmpUser.validate();

            // Update user details
            await tmpUser.save();

            // Send response - HTTP 204 No Content
            sendCustomResponse(res, 204, null);

        } else {
            // We don't have to expose that the user doesn't exist in our database.
            // Send error response - HTTP 401 Unauthorized
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.");
        }
    } catch (error) {
        // TODO: Log errors  
        // Send error response - HTTP 500 Internal Server Error      
        sendCustomErrorResponse(res, 500, "Couldn't update profile.")
    }
});

// Update rating
users.patch('/:id/ratings', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        // Find rating by its id
        const rating = await Ratings.findById(req.body.data[0].id);

        // Rating does exist
        if (rating !== null) {
            const tmpRating = await rating.update({
                updatedAt: dateFormat("dd-mm-yyyy HH:MM"),
                comment: req.body.data[0].comment,
                onTime: req.body.data[0].onTime,
                skills: req.body.data[0].skills,
                behavior: req.body.data[0].behavior
            });

            // Guard
            await tmpRating.validate();

            // Update user details
            await tmpRating.save();

            // Send response - HTTP 200 OK
            sendCustomResponse(res, 200);

        } else {
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.")
        }
    } catch (error) {
        // TODO: Log errors
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't update rating.");
    }
});

// Delete user (my account)
users.delete('/me', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        const user = await User.destroy({
            where: {
                id: req.body.data[0].id,
                deviceToken: req.body.data[0].deviceToken
            }
        });

        // User was deleted successfully
        if (user) {
            // Send response - HTTP 202 Accepted
            sendCustomResponse(res, 202, null);

        } else {
            // Send response - HTTP 401 Unauthorized
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.");
        }
    } catch (error) {
        // TODO: Log error
        // Send response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't delete user.");
    }
});

// Delete rating
users.delete('/:id/ratings', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        const user = await User.findOne({
            where: {
                authToken: req.headers["access-token"]
            },
            raw: true,
            attributes: ['id']
        });

        // Delete rating from the database
        const rating = await Ratings.destroy({
            where: {
                id: req.body.data[0].id,
                createdBy: user.id
            }
        });

        // Rating deleted successfully
        if (rating) {
            // Send response - HTTP 202 Accepted
            sendCustomResponse(res, 202);

        } else {
            // Send response - HTTP 401 Unauthorized
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.")
        }
    } catch (error) {
        // TODO: Log errors
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't delete rating.");

    }
});

module.exports = users;