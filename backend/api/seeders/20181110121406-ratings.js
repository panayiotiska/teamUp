'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {

   const Rating = require('../models').Rating;
   const fieldRating = require('../models').fieldRating;
   const Field = require('../models').Field;
   const User = require('../models').User;

   const users = await User.findAll();
   const fields = await Field.findAll();

   console.log(fields);
   

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

   const fieldRatings = await fieldRating.bulkCreate([
    {
      createdBy: users[1].id,
      comment: "Very nice tacticts.",
      rating: 4
    },
    {
      createdBy: users[6].id,
      comment: "Very fast moving in the field. Would like you in my team.", 
      rating: 3
    },
    {
      createdBy: users[2].id,
      comment: "This field is freakin awesome.",
      rating: 5
    },
    {
      createdBy: users[8].id,
      comment: "Not so good of a stadium",
      rating: 1
    }
   ]);

   // Add ratings to user

   await users[0].addRating(ratings[0], {through: 'userRatings'});
   await users[0].addRating(ratings[1], {through: 'userRatings'});
   await users[0].addRating(ratings[2], {through: 'userRatings'});
   await users[0].addRating(ratings[3], {through: 'userRatings'});

   // Add ratings to fields
   await fields[0].addFieldRating(fieldRatings[0], { through: 'fieldRatingsData' });
   await fields[1].addFieldRating(fieldRatings[1], { through: 'fieldRatingsData' });
   await fields[2].addFieldRating(fieldRatings[2], { through: 'fieldRatingsData' });
   await fields[3].addFieldRating(fieldRatings[3], { through: 'fieldRatingsData' });

  //  // Add field ratings to user
  //  await users[0].addRating(fieldRatings[0], {through: 'userRatings'});
  //  await users[0].addRating(fieldRatings[1], {through: 'userRatings'});
  //  await users[0].addRating(fieldRatings[2], {through: 'userRatings'});
  //  await users[0].addRating(fieldRatings[3], {through: 'userRatings'});
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
