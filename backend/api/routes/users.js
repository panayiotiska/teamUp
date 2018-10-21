const users = require('express').Router();
const errorChecking = require('../error_handler');
const User = require('../models/user');
const Ratings = require('../models/rating');
const userdata  = require('../data/user.json');

// Get profile details
users.get('/me', isAuthenticated, async (req, res) => {
    try {
        // Find user profile via ID
        const profileData = await User.find({
            where: {
                accessToken: req.headers["access-token"]
            },
            attributes: {
                exclude: ['deviceToken', 'accessToken']
            }
          });

        errorChecking(res, ((profileData) ? [profileData] : null), 404, "Couldn't retrieve profile data.");
    } catch (error) {
        // TODO: Log the errors
        errorChecking(res, null, 500, "Internal Server Error\nThere was an error while retrieving profile data.");
    }
});

// Get profile ratings
users.get('/:id/ratings', isAuthenticated, async (req, res) => {
    try {
        const ratings = await User.findById(req.params.id, {
            include: { 
                model: Ratings,
                through: {attributes: []}
            },
        });

        errorChecking(res, ((ratings) ? ratings['ratings']: ratings), 404, "Couldn't find any ratings.");
    } catch (error) {
        // TODO: Log the errors
        errorChecking(res, null, 500, "Couldn't retrieve ratings.");
    }
});

// Create user account
users.post('/', async (req, res) => {
    try {
        // Check if user is already registered
        const userAccount = await User.find({
                where: {id: req.body.data[0].id},
            });

            // User account doesn't exist
            if(userAccount === null){
                // Build a new user instance
                const user = await User.build({
                    id: req.body.data[0].id,
                    firstName: req.body.data[0].firstName,
                    lastName: req.body.data[0].lastName,
                    createdAt: Date.now(),
                    phoneNumber: req.body.data[0].phoneNumber,
                    deviceToken: req.body.data[0].deviceToken,
                    accessToken: req.body.data[0].accessToken
                });

                // Guard
                // Validate the data against the model
                await user.validate();

                // Create user
                await user.save();

                await errorChecking(res, null, 201, "User created.")
            } else {
                // User already exists.
                errorChecking(res, null, 409, "User already exists.")
            }
    } catch (error) {
        // TODO: Log errors
        console.log(error);
        
        errorChecking(res, null, 500, "Internal server error.\nCouldn't create user.")
    }
    
});

// Generate access token
users.post('/accessToken', (req, res) => {
    res.json({msg: "accessToken=123456"}); 
});

// Submit rating
users.post('/:id/ratings', async (req, res) => {
    try {
        const user = await User.findOne({
            where: {
                accessToken: req.headers["access-token"]
            },
            raw: true,
            attributes: ['id']
        });
    
        const rating = Ratings.build({
            id: 12,
            createdBy: user.id,
            createdAt: Date.now(),
            comment: "Very good",
            onTime: 5,
            skills: 2,
            behavior: 1
        });
        
        // Guard
        await rating.validate();

        // Submit rating
        await rating.save();
        
        errorChecking(res, null, 201, "Rating submitted successfully.");

    } catch (error) {
        //TODO: Log errors
        errorChecking(res, null, 500, "Internal Server Error");
    }
});

// Update profile details
users.patch('/me', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        const user = await User.findById(req.body.data[0].id);
        
        // User does exist
        if(user !== null){
            const tmpUser = await user.update({
                firstName: req.body.data[0].firstName,
                lastName: req.body.data[0].lastName,
                updatedAt: Date.now(),
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

// Update rating
users.patch('/:id/ratings', async (req, res) => {
    // TODO Check for authorization & authentication before making any user changes
    try {
        const rating = await Ratings.findById(req.body.data[0].id);
        
        // Rating does exist
        if(rating !== null){
            const tmpRating = await rating.update({
                updatedAt: Date.now(),
                comment:req.body.data[0].comment,
                onTime: req.body.data[0].onTime,
                skills: req.body.data[0].skills,
                behavior: req.body.data[0].behavior
            });

            // Guard
            await tmpRating.validate();

            // Update user details
            await tmpRating.save();

            // HTTP 204 No Content
            await res.sendStatus(204);
            
        }else{
            await res.sendStatus(401);
        }
    } catch (error) {
        // TODO: Log errors
        errorChecking(res, null, 500, "Internal Server Error");
        
    }
});

// Delete my account
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

// Sign out - Destroy session access token
users.delete('/accessToken', (req, res) => {
    res.json({msg: "Access Token deleted"}); 
});

// Delete rating
users.delete('/:id/ratings', async (req, res) => {
   // TODO Check for authorization & authentication before making any user changes
    try {
        const user = await User.findOne({
            where: {
                accessToken: req.headers["access-token"]
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

// // rate user
// users.use('/ratings', require('./ratings'));




function isAuthenticated(req, res, next) {
    // do any checks you want to in here
  
    // CHECK THE USER STORED IN SESSION FOR A CUSTOM VARIABLE
    // you can do this however you want with whatever variables you set up
    if (true) {
        return next();
    } else {
        res.sendStatus(401);
    }
       
  }

module.exports = users;