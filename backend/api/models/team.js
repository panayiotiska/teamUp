const Sequelize = require('sequelize');
const db = require('../db');

const Team = db.define('teams', {
    id: {
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    firstTeamId: {
        type: Sequelize.STRING
    },
    secondTeamId: {
        type: Sequelize.STRING
    }
});

module.exports = Team;