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
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get profile data.")
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
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't get ratings.");
    }
});

// Create new user
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
        // Send error response - 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't create user.");
    }
});

// Generate authorization token 
users.post('/authToken', (req, res) => {
    res.json({msg: "authToken=123456"}); 
});

// Create new rating
users.post('/:id/ratings', async (req, res) => {
    try {
        // TODO: Find the author of this post by checking via the auth token
        const user = await User.findOne({
            where: {
                authToken: req.headers["authToken"]
            },
            raw: true,
            attributes: ['id']
        });
        
        // Build an instance of the Rating model    
        const rating = await Ratings.build({
            createdBy: user.id,
            createdAt: dateFormat("dd-mm-yyyy HH:MM"),
            comment: req.body.data[0].comment,
            onTime: req.body.data[0].onTime,
            skills: req.body.data[0].skills,
            behavior: req.body.data[0].behavior
        });
        
        // Guard
        // Validate the data agains the Rating model
        await rating.validate();

        // Submit rating
        await rating.save();

        // Update 'userRating' association table
        await userRating.create({
            userId: req.params.id,
            ratingId: rating.id
        });
        
        // Send response - HTTP 201 Created
        sendCustomResponse(res, 201);

    } catch (error) {
        //TODO: Log errors
        // Send error response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't submit rating.");
    }
});

// Update user details
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

            // Send response - HTTP 204 No Content
            sendCustomResponse(res, 204);
            
        }else{
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

            // Send response - HTTP 200 OK
            sendCustomResponse(res, 200);
            
        }else{
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
        if(user){
            // Send response - HTTP 202 Accepted
            sendCustomResponse(res, 202);
            
        }else{
            // Send response - HTTP 401 Unauthorized
            sendCustomErrorResponse(res, 401, "You are unauthorized to perform this action.");
        }
    } catch (error) {
        // TODO: Log error
        // Send response - HTTP 500 Internal Server Error
        sendCustomErrorResponse(res, 500, "Couldn't delete user.");
    }
});

// Sign Out - Destroy Auth Token
users.delete('/authToken', (req, res) => {
    res.json({msg: "Authorization Token deleted"}); 
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
        if(rating){
            // Send response - HTTP 202 Accepted
            sendCustomResponse(res, 202);
            
        }else{
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