const Sequelize = require('sequelize');
const db = require('../db');

const userTeam = db.define('userTeams', {
    teamId: {
        type: Sequelize.STRING
    },
    userId: {
        type: Sequelize.STRING
    }
});

module.exports = userTeam;