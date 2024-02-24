/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
  return knex.schema.alterTable('questions', function(table){
    table.text('Hint1').defaultTo("No Hint Available");
    table.text('Hint2').defaultTo("No Hint Available");
    table.text('Hint3').defaultTo("No Hint Available");
    table.integer('point').defaultTo(1000);
  })
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
  return knex.schema.alterTable('questions',function(table){
    table.dropColumns(['Hint1','Hint2','Hint3','points']);
  })
};
