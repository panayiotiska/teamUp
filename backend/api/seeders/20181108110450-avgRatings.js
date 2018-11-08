'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {
    /*
      Add altering commands here.
      Return a promise to correctly handle asynchronicity.

    */
    const User = require('../models').User;
    const avgRating = require('../models').avgRatings;

    const usr = await User.findOne({
      where: {
        firstName: "Aldi"
      }
    });

    if(usr !== null){
      const avgRt = await avgRating.create({
        avgSkills: 2.2,
        avgBehavior: 3.1,
        avgOnTime: 5,
        avgTotal: 3
      });

      avgRt.setUser(usr);
    }
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
