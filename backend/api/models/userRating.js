const Sequelize = require('sequelize');
const db = require('../db');

const userRating = db.define('userRatings', {
    userId: {
        type: Sequelize.INTEGER
    },
    ratingId: {
        type: Sequelize.STRING
    }
});

  module.exports = userRating;