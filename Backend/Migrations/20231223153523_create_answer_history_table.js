/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = async function (knex) {
    return await knex.schema.createTable('answers_history', (table) => {
      table.increments('answer_id').primary();
      table.integer('user_id').unsigned().references('user_id').inTable('users').notNullable();
      table.integer('question_id').unsigned().references('question_id').inTable('questions').notNullable();
      table.text('answer_text').notNullable();
      table.timestamp('timestamp').defaultTo(knex.fn.now());
    });
  };

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = async function (knex) {
    return await knex.schema.dropTableIfExists('answers_history');
  };
  
