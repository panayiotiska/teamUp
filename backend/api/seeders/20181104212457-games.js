'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {
    /*
      Add altering commands here.
      Return a promise to correctly handle asynchronicity.
    */

    // Create some teams
    const Team = require('../models').Team;
    const Game = require('../models').Game;
    const User = require('../models').User;

    const users = await User.findAll();

    await Team.bulkCreate([
      {}, {}, {}, {}, {}, {}
    ], {});

    const teams = await Team.findAll();

    // Create some games
    const games = await Game.bulkCreate([
      {
        name: "Rafaellos's Game",
        description: "We are the best. Let's see what you got.",
        size: 5,
        type: 0,
        opponents: true,
        eventDate: new Date(),
        locationId: 1
      },
      {
        name: "Panayiotis's Game",
        description: "I only see the best results. More shoots than you can count.",
        size: 11,
        type: 1,
        opponents: true,
        eventDate: new Date(),
        locationId: 2
      },
      {
        name: "Aldi's Game",
        description: "Don't forget to bring your own attitude. You're gonna need it.",
        size: 6,
        type: 0,
        opponents: true,
        eventDate: new Date(),
        locationId: 3
      }
  ], {});

    await games[0].setFirstTeam(teams[0]);
    await games[0].setSecondTeam(teams[1]);

    await games[1].setFirstTeam(teams[2]);
    await games[1].setSecondTeam(teams[3]);

    await games[2].setFirstTeam(teams[4]);
    await games[2].setSecondTeam(teams[5]);

    await users[0].addGame(games[0], { through: 'userGames' });
    await users[1].addGame(games[1], { through: 'userGames' });
    await users[2].addGame(games[2], { through: 'userGames' });

    // Add Players to teams

    const gameOneFirstTeam = await games[0].getFirstTeam();
    const gameOneSecondTeam = await games[0].getSecondTeam();

    await gameOneFirstTeam.addPlayer(users[0], {through: 'teamPlayers'});
    await gameOneFirstTeam.addPlayer(users[3], {through: 'teamPlayers'});
    await gameOneFirstTeam.addPlayer(users[4], {through: 'teamPlayers'});
    await gameOneFirstTeam.addPlayer(users[6], {through: 'teamPlayers'});
    await gameOneFirstTeam.addPlayer(users[20], {through: 'teamPlayers'});

    await gameOneSecondTeam.addPlayer(users[2], {through: 'teamPlayers'});
    await gameOneSecondTeam.addPlayer(users[1], {through: 'teamPlayers'});
    await gameOneSecondTeam.addPlayer(users[8], {through: 'teamPlayers'});
    await gameOneSecondTeam.addPlayer(users[19], {through: 'teamPlayers'});
    await gameOneSecondTeam.addPlayer(users[13], {through: 'teamPlayers'});
    

    
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
