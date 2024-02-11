const dotenv = require('dotenv');
dotenv.config();

module.exports = {
  development: {
    client: 'pg',
    connection: {
      connectionString: process.env.DB_URL,
      // ssl: { rejectUnauthorized: false },
    },
    migrations: {
      tableName: 'knex_migrations',
      directory: '../Migrations',
      client: 'pg',
    },
  },
};
