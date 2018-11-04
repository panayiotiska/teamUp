'use strict';
module.exports = (sequelize, DataTypes) => {
  const Rating = sequelize.define('Rating', {
    createdBy: DataTypes.STRING,
    comment: DataTypes.STRING,
    onTime: DataTypes.INTEGER,
    skills: DataTypes.INTEGER,
    behavior: DataTypes.INTEGER
  }, {});
  Rating.associate = function(models) {
    // associations can be defined here
    Rating.belongsToMany(models.User, { through: 'userRatings', foreignKey: 'ratingId' });
  };
  return Rating;
};