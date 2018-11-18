'use strict';
module.exports = (sequelize, DataTypes) => {
  const fieldAvgRatings = sequelize.define('fieldAvgRatings', {
    totalAvg: DataTypes.FLOAT,
    ratingsCount: DataTypes.INTEGER
  }, {});
  fieldAvgRatings.associate = function(models) {
    // fieldAvgRatings belongsTo Field
    fieldAvgRatings.belongsTo(models.Field, { foreignKey: {field: 'fieldId', unique: true} });
  };
  return fieldAvgRatings;
};