const Sequelize = require('sequelize');
const db = require('../db');

const Location = db.define('locations', {
    id: {
        type: Sequelize.STRING,
        primaryKey: true
    },
    name: {
        type: Sequelize.STRING,
        validate: {
            notNull: true
        }
    },
    latitude: {
        type: Sequelize.FLOAT,
        validate: {
            isFloat: true
        }
    },
    longitude: {
        type: Sequelize.FLOAT,
        validate: {
            isFloat: true
        }
    }
});


module.exports = Location;