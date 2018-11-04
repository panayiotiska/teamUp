'use strict';
module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('User', {
    id: {
      type: DataTypes.BIGINT,
      primaryKey: true
    },
    firstName: DataTypes.STRING,
    lastName: DataTypes.STRING,
    deviceToken: DataTypes.STRING,
    authToken: DataTypes.STRING
  }, {});
  User.associate = function(models) {
    // User belongsToMany Game
    User.belongsToMany(models.Game, { through: 'userGames', foreignKey: 'userId' });

    // User belongsToMany Rating
    User.belongsToMany(models.Rating, {through: 'userRatings', foreignKey: 'userId'});

    // User belongsToMany Team
    User.belongsToMany(models.Team, { as: 'Player', through: 'teamPlayers', foreignKey: 'userId' });
  };
  return User;
};