'use strict';
module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('User', {
    id: {
      type: DataTypes.BIGINT,
      primaryKey: true
    },
    firstName: DataTypes.STRING,
    lastName: DataTypes.STRING,
    phoneNumber: DataTypes.STRING,
    deviceToken: DataTypes.STRING,
    authToken: DataTypes.STRING,
    createdAt: {
      type: DataTypes.DATE,
      defaultValue: sequelize.literal('NOW()')
    },
    updatedAt: {
      type: DataTypes.DATE,
      defaultValue: sequelize.literal('NOW()')
    }
  }, {
    timestamps: true,
    charset: 'utf8',
    collate: 'utf8_unicode_ci'
  });
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