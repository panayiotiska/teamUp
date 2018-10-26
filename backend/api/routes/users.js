// API /users route
const users = require('express').Router();
//DateFormat 
const dateFormat = require('dateformat');
// Database models
const User = require('../models/user');
const Ratings = require('../models/rating');
const userRating = require('../models/userRating');
// Custom Response Handler
const {sendCustomResponse, sendCustomErrorResponse} = require('../handlers/customResponse');


const Sequelize = require('sequelize');

// Get user details
users.get('/me', async (req, res) => {
    try {
        // Find user
        const userProfileData = await User.findOne({
            where: {
                // This should be based on the user's id
                authToken: req.headers["access-token"]
            },
            attributes: {
                // Exclude sensitive information from being returned back in the response
                exclude: ['deviceToken', 'authToken']
            }
          });

        // Send response - HTTP 200 OK
        // sendCustomResponse(res, 200, [userProfileData]);
        if(userProfileData){
            sendCustomResponse(res, 200, [userProfileData]);
        }else{
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.")
        }
    } catch (error) {
        // TODO: Log the errors
        // Send a generalized error message - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Internal Server Error\nThere was an error while retrieving the requested profile data.")
    }
});

// Get user ratings
users.get('/:id/ratings', async (req, res) => {
    try {
        // Get all user ratings
        const user = await User.findById(req.params.id, {
            include:
            { 
                model: Ratings,
                through: {
                    model: userRating,
                    attributes: []
                    }
            }
        });

        // Iterate through each rating
        for (const rating of user['ratings']) {
            // Find rating author
            // Extract author's id, firstName and lastName attributes
            const ratingAuthor = await User.findOne({
                where: {
                    id: rating.createdBy
                },
                attributes: ['id', 'firstName', 'lastName']
            });

            // Update createdBy property by reference
            rating.createdBy = {
                id: ratingAuthor.id,
                firstName: ratingAuthor.firstName,
                lastName: ratingAuthor.lastName
            }
        }
        // Send response - HTTP 200 OK
        sendCustomResponse(res, 200, user.ratings);
    } catch (error) {
        // TODO: Log the errors
        sendCustomErrorResponse(res, 500, "Internal Server Error\nCouldn't retrieve ratings.");
    }
});

// Create User
users.post('/', async (req, res) => {
    try {
        // Check if the user is already in our database
        // TODO: Use a combination of user id and device token to check if the user is already registered
        const userAccount = await User.find({
                where: {
                    id: req.body.data[0].id
                }
            });

            // If the user account doesn't exist
            if(userAccount === null){
                // Build a new User instance
                const user = await User.build({
                    id: req.body.data[0].id,
                    firstName: req.body.data[0].firstName,
                    lastName: req.body.data[0].lastName,
                    createdAt: dateFormat("dd-mm-yyyy HH:MM"),
                    phoneNumber: req.body.data[0].phoneNumber,
                    deviceToken: req.body.data[0].deviceToken,
                    authToken: req.body.data[0].authToken
                });

                // Guard
                // Validate the data against the User model
                await user.validate();

                // Create user
                await user.save();

                // Send response - HTTP 201 Created
                sendCustomResponse(res, 201);
            } else {
                // TODO: Log error
                // User already exists in our database
                sendCustomErrorResponse(res, 409, "Couldn't create user. Already exists.")
            }
    } catch (error) {
        // TODO: Log error
        // Send response - 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't create user.");
    }
});

// Generate authorization token 
users.post('/authToken', (req, res) => {
    res.json({msg: "authToken=123456"}); 
});

// Create Rating
users.post('/:id/ratings', async (req, res) => {
    try {
        const user = await User.findOne({
            where: {
                authToken: req.headers["access-token"]
            },
            raw: true,
            attributes: ['id']
        });        
    
        const rating = await Ratings.build({
            createdBy: user.id,
            createdAt: dateFormat("dd-mm-yyyy HH:MM"),
            comment: req.body.data[0].comment,
            onTime: req.body.data[0].onTime,
            skills: req.body.data[0].skills,
            behavior: req.body.data[0].behavior
        });
        
        // Guard
        await rating.validate();

        // Submit rating
        await rating.save();

        await userRating.create({
            userId: req.params.id,
            ratingId: rating.id
        });
        
        // HTTP 201 Created
        await res.sendStatus(201);

    } catch (error) {
        //TODO: Log errors
        res.sendStatus(401);
        //errorChecking(res, null, 401, "Internal Server Error");
    }
});

// Update User Details
users.patch('/me', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        const user = await User.findById(req.body.data[0].id);
        
        // User does exist
        if(user !== null){
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

            // HTTP 204 No Content
            await res.sendStatus(204);
            
        }else{
            // We don't have to expose that the user doesn't exist in our database.
            // HTTP 401 Unauthorized
            await res.sendStatus(401);
        }
    } catch (error) {
        // TODO: Log errors        
        await errorChecking(res, null, 500, "Internal Server Error");
    }
});

// Update Rating
users.patch('/:id/ratings', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        const rating = await Ratings.findById(req.body.data[0].id);
        
        // Rating does exist
        if(rating !== null){
            const tmpRating = await rating.update({
                updatedAt: dateFormat("dd-mm-yyyy HH:MM"),
                comment:req.body.data[0].comment,
                onTime: req.body.data[0].onTime,
                skills: req.body.data[0].skills,
                behavior: req.body.data[0].behavior
            });

            // Guard
            await tmpRating.validate();

            // Update user details
            await tmpRating.save();

            // HTTP 200 OK
            await res.sendStatus(200);
            
        }else{
            await res.sendStatus(401);
        }
    } catch (error) {
        // TODO: Log errors
        errorChecking(res, null, 500, "Internal Server Error");
        
    }
});

// Delete User (my account)
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
        if(user){
            // HTTP 202 Accepted
            await res.sendStatus(202);
            
        }else{
            // HTTP 401 Unauthorized
            await res.sendStatus(401);
        }
    } catch (error) {
        // TODO: Log errors    
        await errorChecking(res, null, 500, "Internal Server Error");
    }
});

// Sign Out - Destroy Access Token
users.delete('/authToken', (req, res) => {
    res.json({msg: "Authorization Token deleted"}); 
});

// Delete Rating
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

        const rating = await Ratings.destroy({
            where: {
                id: req.body.data[0].id,
                createdBy: user.id
            }
        });
        
        // Rating does exist
        if(rating){
            // HTTP 202 Accepted
            await res.sendStatus(202);
            
        }else{
            await res.sendStatus(401);
        }
    } catch (error) {
        // TODO: Log errors
        errorChecking(res, null, 500, "Internal Server Error");
        
    }
});

module.exports = users;