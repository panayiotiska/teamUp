'use strict';
module.exports = (sequelize, DataTypes) => {
  const Field = sequelize.define('Field', {
    name: DataTypes.STRING,
    createdBy: DataTypes.STRING,
    type: DataTypes.INTEGER,
    sponsored: {
      type: DataTypes.BOOLEAN,
      default: false
    },
    contactPhone: DataTypes.STRING,
    imageUrl: DataTypes.STRING,
    verified: {
      type: DataTypes.BOOLEAN,
      default: false
    }
  }, {
      charset: 'utf8',
      collate: 'utf8_unicode_ci'
    });
  Field.associate = function (models) {
    // A field has a location
    Field.belongsTo(models.Location, { foreignKey: 'locationId' });

    // Field belongsToMany fieldRating
    Field.belongsToMany(models.fieldRating, { through: 'fieldRatingsData', foreignKey: 'fieldId' });

    // Field hasOne fieldAvgRatings
    Field.hasOne(models.fieldAvgRatings, { foreignKey: { field: 'fieldId', unique: true } });
  };
  return Field;
};