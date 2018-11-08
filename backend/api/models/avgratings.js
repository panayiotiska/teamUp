'use strict';
module.exports = (sequelize, DataTypes) => {
  const avgRatings = sequelize.define('avgRatings', {
    avgSkills: DataTypes.FLOAT,
    avgOnTime: DataTypes.FLOAT,
    avgBehavior: DataTypes.FLOAT,
    totalAvg: DataTypes.FLOAT,
    ratingsCount: DataTypes.INTEGER
  }, {});
  avgRatings.associate = function(models) {
    // avgRatings belongsTo User
    avgRatings.belongsTo(models.User, { foreignKey: 'userId' });
  };
  return avgRatings;
};