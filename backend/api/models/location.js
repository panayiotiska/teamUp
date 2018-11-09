'use strict';
module.exports = (sequelize, DataTypes) => {
  const Location = sequelize.define('Location', {
    city: DataTypes.STRING,
    address: DataTypes.STRING,
    countryCode: DataTypes.STRING,
    postalCode: DataTypes.STRING,
    latitude: DataTypes.FLOAT,
    longitude: DataTypes.FLOAT,
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
  Location.associate = function(models) {
    // associations can be defined here
  };
  return Location;
};