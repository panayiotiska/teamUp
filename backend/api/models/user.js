const Sequelize = require('sequelize');
const Rating = require('./rating');
const userRating = require('./userRating');
const db = require('../db/connection');

// Mock data to insert into the db
const userdata  = require('../data/user.json');


const User = db.define('user', {
    id: {
        type: Sequelize.STRING,
        isNull: false,
        primaryKey: true
    },
    firstName: {
        type: Sequelize.STRING(25)
    },
    lastName:  {
        type: Sequelize.STRING(25)
    },
    createdAt: {
        type: Sequelize.DATE
    },
    updatedAt: {
        type: Sequelize.DATE
    },
    phoneNumber: {
        type: Sequelize.STRING(20)
    },
    deviceToken: {
        type: Sequelize.STRING
    },
    accessToken: {
        type: Sequelize.STRING
    }
  });

// A user can have many reviews
User.belongsToMany(Rating, {through: userRating, constraints: false});

// Create and populate the database tables with mock data
db.sync({force: true}).then(async () => {
    await User.bulkCreate([
        {
            id: userdata.user.id,
            firstName: userdata.user.firstName,
            lastName: userdata.user.lastName,
            phoneNumber: userdata.user.phoneNumber,
            deviceToken: "11111111",
            accessToken: "MzQ2MjhlOTE2ZTZmYTQ4ZDhiYjVlNWYyY2M4NDRmOTk2ZmViNWQ2NDA0OGJiZDQzNGRjODFiMjNlN2ZhYzU2ZA=="
        },
        {
            id: "100000273908940",
            firstName: "Johny",
            lastName: "Doe",
            phoneNumber: "6981000000",
            deviceToken: "22222222"
        }
    ]);

    await Rating.bulkCreate([
        {
            id: 10,
            createdBy: "100000273908936",
            comment: "The best on top of the rest",
            onTime: 5,
            skills: 2,
            behavior: 1
        },
        {
            id: 11,
            createdBy: "100000273908936",
            comment: "Very good player",
            onTime: 4,
            skills: 4,
            behavior: 5
        },
        {
            id: 22,
            createdBy: "100000273908932",
            comment: "John was a very fast pl_ayer",
            onTime: 5,
            skills: 3,
            behavior: 4
        }
    ]);

    await userRating.bulkCreate([
        {
            userId: "100000273908932",
            ratingId: 10
        },
        {
            userId: "100000273908932",
            ratingId: 11
        },
        {
            userId: "100000273908940",
            ratingId: 22
        }
    ]);
  });

  module.exports = User;