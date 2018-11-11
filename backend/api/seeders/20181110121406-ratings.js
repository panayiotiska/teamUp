'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {

   const Rating = require('../models').Rating;
   
   const User = require('../models').User;

   const users = await User.findAll();   

   // Create some ratings
   const ratings = await Rating.bulkCreate([
    {
      createdBy: users[13].id,
      comment: "Very nice tacticts.",
      onTime: 4,
      skills: 2,
      behavior: 4
    },
    {
      createdBy: users[6].id,
      comment: "Very fast moving in the field. Would like you in my team.",
      onTime: 5,
      skills: 5,
      behavior: 5
    },
    {
      createdBy: users[2].id,
      comment: "I was tired chasing you. Well done.",
      onTime: 3,
      skills: 5,
      behavior: 4
    },
    {
      createdBy: users[8].id,
      comment: "Precise like a sniper. Making every goal a highlight.",
      onTime: 4,
      skills: 4,
      behavior: 3
    }
   ]);
   // Add ratings to user

   await users[0].addRating(ratings[0], {through: 'userRatings'});
   await users[0].addRating(ratings[1], {through: 'userRatings'});
   await users[0].addRating(ratings[2], {through: 'userRatings'});
   await users[0].addRating(ratings[3], {through: 'userRatings'});
  },

  down: (queryInterface, Sequelize) => {
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.bulkDelete('Person', null, {});
    */
  }
};
