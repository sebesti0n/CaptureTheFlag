/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.alterTable('events', function(table) {
      table.integer('node_at_each_level').defaultTo(1)
      table.integer('levels').defaultTo(1);
    })
  };
  
  /**
   * @param { import("knex").Knex } knex
   * @returns { Promise<void> }
   */
  exports.down = function(knex) {
      return knex.schema.alterTable('events', function(table) {
      table.dropColumns(['node_at_each_level','levels']);
      } )
  };
  