'use strict';
module.exports = (sequelize, DataTypes) => {
  const Game = sequelize.define('Game', {
    name: DataTypes.STRING,
    description: DataTypes.STRING,
    size: DataTypes.INTEGER,
    type: DataTypes.INTEGER,
    opponents: DataTypes.BOOLEAN,
    eventDate: DataTypes.DATE
  }, {});
  Game.associate = function(models) {
    // Game belongsToMany User
    Game.belongsToMany(models.User, { through: 'userGames', foreignKey: 'gameId' });

    // Game belongsTo Location
    Game.belongsTo(models.Location, { foreignKey: 'locationId' });

    Game.belongsTo(models.Team, {as: 'firstTeam', foreignKey : 'firstTeamId' });
    Game.belongsTo(models.Team, {as: 'secondTeam', foreignKey : 'secondTeamId' });

  };
  return Game;
};