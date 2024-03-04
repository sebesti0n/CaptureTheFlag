/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.alterTable('questions', function(table){
      table.double('Latitude').defaultTo(23.8457300);
      table.double('Longitude').defaultTo(91.4256161)
      table.integer('Range').defaultTo(1000);
    })
  };
  
  /**
   * @param { import("knex").Knex } knex
   * @returns { Promise<void> }
   */
  exports.down = function(knex) {
    return knex.schema.alterTable('questions',function(table){
      table.dropColumns(['Latitude','Longitude','Range']);
    })
  };
  