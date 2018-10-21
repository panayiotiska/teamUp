const Sequelize = require('sequelize');
const db = require('../db/connection');

const Rating = db.define('ratings', {
    id: {
        type: Sequelize.STRING,
        allowNull: false,
        primaryKey: true
    },
    createdBy: {
        type: Sequelize.STRING,
        allowNull: false
    },
    createdAt: { 
        type: Sequelize.DATE,
        defaultValue: Sequelize.NOW,
        allowNull: false,
        validate: {
            isDate: true
        }
    },
    updatedAt: {
        type: Sequelize.DATE,
        allowNull: true,
        validate: {
            isDate: true
        }
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