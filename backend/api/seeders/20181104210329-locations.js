'use strict';

module.exports = {
  up: (queryInterface, Sequelize) => {
    /*
      Add altering commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
    */

      // Create some locations
      return queryInterface.bulkInsert('Locations', [
        {
          city: "Σταυρούπολη",
          address: "Οδυσσέα Ελύτη 4, Σταυρούπολη 564 30",
          countryCode: "GR",
          latitude: 40.6519117,
          longitude: 22.9385447 
        },
        {
          city: "Θεσσαλονίκη",
          address: "Αλ. Παπαναστασίου 158, Θεσσαλονίκη 542 49",
          countryCode: "GR",
          latitude: 40.6001553,
          longitude: 22.9691805 
        },
        {
          city: "Καλαμαριά",
          address: "Πόντου 115, Καλαμαριά 551 33",
          countryCode: "GR",
          latitude: 40.58523,
          longitude: 22.961799
        }
      ], {});    
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