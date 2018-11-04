const Sequelize = require('sequelize');
const db = require('../db');

const userGame = db.define('userGames', {
    userId: {
        type: Sequelize.STRING
    },
    gameId: {
        type: Sequelize.STRING
    },
    locationId: {
        type: Sequelize.STRING,
        unique: false
    },
    teamId: {
        type: Sequelize.STRING,
        unique: true
    }
});

module.exports = userGame;