const Sequelize = require('sequelize');
const db = require('../db');

const Rating = db.define('ratings', {
    id: {
        type: Sequelize.INTEGER,
        allowNull: false,
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
        type: Sequelize.STRING,
        allowNull: true
    },
    comment: {
        type: Sequelize.STRING,
        allowNull: true,
        validate: {
            len: [0,50]
        }
    },
    onTime: {
        type: Sequelize.INTEGER,
        defaultValue: 0,
        validate: {
            isNumeric: true,
            isInt: true,
            min: 0,
            max: 5
        }
    },
    skills: {
        type: Sequelize.INTEGER,
        defaultValue: 0,
        validate: {
            isNumeric: true,
            isInt: true,
            min: 0,
            max: 5
        }
    },
    behavior: {
        type: Sequelize.INTEGER,
        defaultValue: 0,
        validate: {
            isNumeric: true,
            isInt: true,
            min: 0,
            max: 5
        }
    }    
  });

  module.exports = Rating;