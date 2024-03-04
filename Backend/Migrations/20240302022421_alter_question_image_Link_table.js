/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.alterTable('questions', function(table){
      table.text('imageLink').defaultTo("null");
      table.text('riddleLocation').defaultTo("nita");

      
    })
  };
  
  /**
   * @param { import("knex").Knex } knex
   * @returns { Promise<void> }
   */
  exports.down = function(knex) {
    return knex.schema.alterTable('questions',function(table){
      table.dropColumns(['imageLink','riddleLocation']);
    })
  };
  