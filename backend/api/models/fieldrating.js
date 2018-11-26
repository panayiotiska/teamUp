'use strict';
module.exports = (sequelize, DataTypes) => {
  const fieldRating = sequelize.define('fieldRating', {
    rating: DataTypes.FLOAT,
    comment: DataTypes.STRING,
    createdBy: DataTypes.STRING
  }, {});
  fieldRating.associate = function (models) {
    // fieldRating belongsToMany Field
    fieldRating.belongsToMany(models.Field, { through: 'fieldRatingsData', foreignKey: 'fieldRatingId' });
  };
  return fieldRating;
};