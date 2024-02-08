const dotenv = require('dotenv');
dotenv.config();

module.exports = {
    development: {
      client: 'pg',
      connection: {
        host:'localhost',
        user:process.env.POSTGRES_USER,
        database:'postgres',
        password:process.env.POSTGRES_PWD,
        port:5432,
      },
      migrations: {
        tableName: 'knex_migrations',
        directory: '../Migrations',
        client:'pg',
      },
    },
  };