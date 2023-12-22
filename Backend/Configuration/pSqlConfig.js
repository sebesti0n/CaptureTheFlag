const {Client} = require('pg');

const client = new Client({
    host:'localhost',
    user:'postgres',
    database:'postgres',
    password:'new_password',
    port:5432,
});
console.log("Postgres connect successfully");
module.exports = client;