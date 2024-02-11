const dotenv = require('dotenv');
dotenv.config();

module.exports = {
  development: {
    client: 'pg',
    connection: {
    // connectionString: process.env.DB_URL,
    connectionString: "postgres://sebesti0n:gw7QfAMiXEOuba91HTExYDhgTOcbNbZR@dpg-cn3nuu21hbls73adigng-a.oregon-postgres.render.com/dcc_ctf_db",
    ssl: { rejectUnauthorized: false }, 
    },
    migrations: {
      tableName: 'knex_migrations',
      directory: '../Migrations',
      client:'pg',
    },
  },
}; 
