'use strict';
module.exports = (sequelize, DataTypes) => {
  const Team = sequelize.define('Team', {
  }, {});
  Team.associate = function(models) {
    // Team belongsToMany User
    Team.belongsToMany(models.User, { as: 'Player', through: 'teamPlayers', foreignKey: 'teamId' });
  };
  return Team;
};