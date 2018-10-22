const Sequelize = require('sequelize');
const config = require('../db/config.json');

const db = new Sequelize(config.database, config.username, config.password, {
  host: 'localhost',
  dialect: 'mysql',
  operatorsAliases: false,
  define: {
    timestamps: false
  },

  pool: {
    max: 10,
    min: 0,
    acquire: 30000,
    idle: 10000
  }
});

(async () => {
  try {
    await db.authenticate();
    await console.log('Connection has been established successfully.');
  } catch (error) {
    console.error('Unable to connect to database.', err);
  }
})();

module.exports = db;