'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {

    const Location = require('../models').Location;
    const Field = require('../models').Field;

    const locations = await Location.bulkCreate([
      {
        city: "Θεσσαλονίκη",
        address: "Ποσειδώνος 47",
        postalCode: "55102",
        countryCode: "GR",
        latitude: 40.5724977,
        longitude: 22.9743061
      },
      {
        city: "Πυλαία",
        address: "Αντώνη Τρίτση 11",
        postalCode: "55535",
        countryCode: "GR",
        latitude: 40.5663345,
        longitude: 22.9785601
      },
      {
        city: "Θεσσαλονίκη",
        address: "Κουντουριώτου & Λασκαράτου",
        postalCode: "54625",
        countryCode: "GR",
        latitude: 40.5977167,
        longitude: 22.9509051
      }
    ]);

    await Field.bulkCreate([
      {
        name: "FOOTBALL SPORT CENTER",
        contactPhone: "+30 2310472242",
        type: 0,
        sponsored: false,
        imgUrl: "http://www.footballsportcenter.gr/upload/imgproc/91583_eb.jpg",
        locationId: locations[0].id
      },
      {
        name: "ATHLETIC PARK - ΕΠΙΣΚΥΡΟ",
        contactPhone: "+30 6973055499",
        type: 0,
        sponsored: false,
        imgUrl: "http://www.athleticpark.gr/images/cobalt_thumbs/gallery23-85/726/b32354b0e69ccb9a1605278544496aa9.jpg",
        locationId: locations[1].id
      },
      {
        name: "ΓΗΠΕΔΑ ΝΙΚΟΛΟΥΔΗ ΤΑΚΗ",
        contactPhone: "+30 2310435000",
        type: 0,
        sponsored: true,
        imgUrl: "",
        locationId: locations[2].id
      }
    ]);
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
