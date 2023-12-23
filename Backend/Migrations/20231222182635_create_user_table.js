
exports.up = function(knex) {
    console.log('Creating users table...');
    return knex.schema.createTable('users', function (table) {
        table.increments('user_id').primary();
        table.string('name', 50).unique().notNullable();
        table.string('email', 100).unique().notNullable();
      });
};

exports.down = function(knex) {
    return knex.schema.dropTable('users');
};
