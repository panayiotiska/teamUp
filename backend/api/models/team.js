'use strict';
module.exports = (sequelize, DataTypes) => {
  const Team = sequelize.define('Team', {
    createdAt: {
      type: DataTypes.DATE,
      defaultValue: sequelize.literal('NOW()')
    },
    updatedAt: {
      type: DataTypes.DATE,
      defaultValue: sequelize.literal('NOW()')
    }
  }, { timestamps: true });
  Team.associate = function(models) {
    // Team belongsToMany User
    Team.belongsToMany(models.User, { as: 'Player', through: 'teamPlayers', foreignKey: 'teamId' });
  };
  return Team;
};