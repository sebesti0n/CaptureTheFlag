/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
  return knex.schema.createTable('teams', function(table){
    table.increments('team_id').primary();
    table.string('team_name',50).defaultTo("team1");
    table.string('player1_name',50).defaultTo("player1");
    table.string('player1_eid',15).notNullable();
    table.string('player2_name',50).defaultTo("player2");
    table.string('player2_eid',15).notNullable();
    table.string('player3_name',50).defaultTo("player3");
    table.string('player3_eid',15).notNullable();
    table.string('leader_email',50).defaultTo("abc@gmail.com");
    table.string('wa_number',15).notNullable()
  });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
  return knex.schema.dropTableIfExists('teams');
};
