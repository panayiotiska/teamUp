const Sequelize = require('sequelize');
const db = require('../db');

const Location = require('./location');
const userGame = require('./userGame');


const Game = db.define('games', {
    id: {
        type: Sequelize.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    createdBy: {
        type: Sequelize.STRING,
        allowNull: false
    },
    createdAt: {
        type: Sequelize.STRING,
        allowNull: false
    },
    updatedAt: {
        type: Sequelize.STRING
    },
    name: {
        type: Sequelize.STRING(25),
        allowNull: false
    },
    type: {
        type: Sequelize.INTEGER,
        validate: {
            isInt: true
        }
    },
    size: {
        type: Sequelize.INTEGER,
        validate: {
            min: 0,
            max: 11
        }
    },
    opponents: {
        type: Sequelize.BOOLEAN
    },
    eventDate: {
        type: Sequelize.STRING
    },
    description: {
        type: Sequelize.STRING(50)
    }
});

Game.belongsTo(Location, {through: userGame, constraints: false});

module.exports = Game;