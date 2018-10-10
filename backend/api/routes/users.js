const users = require('express').Router();

// Get profile details
users.get('/me', (req, res) => {
    res.json([require('../data/user')]); 
});

// Get profile ratings
users.get('/me/ratings', (req, res) => {
    res.json({msg: "My profile ratings"}); 
});

// Get user profile ratings
users.get('/:id/ratings', (req, res) => {
    res.json({msg: "Another user profile ratings"}); 
});

// Create new user account
users.post('/', (req, res) => {
    res.json({msg: "New user account created"}); 
});

// Generate access token
users.post('/accessToken', (req, res) => {
    res.json({msg: "accessToken=123456"}); 
});

// Submit rating
users.post('/:id/ratings', (req, res) => {
    res.json({msg: "Rating submitted"}); 
});

// Update profile details
users.patch('/me', (req, res) => {
    res.json({msg: "Profile updated"}); 
});

// Update rating
users.patch('/:id/ratings', (req, res) => {
    res.json({msg: "Profile updated"}); 
});

// Delete my account
users.delete('/me', (req, res) => {
    res.json({msg: "Account deleted"}); 
});

// Sign out - Destroy session access token
users.delete('/accessToken', (req, res) => {
    res.json({msg: "Access Token deleted"}); 
});

// Delete rating
users.delete('/:id/ratings', (req, res) => {
    res.json({msg: "Rating deleted"}); 
});

// // rate user
// users.use('/ratings', require('./ratings'));

module.exports = users;