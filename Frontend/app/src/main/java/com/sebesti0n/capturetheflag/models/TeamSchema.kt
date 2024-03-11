package com.sebesti0n.capturetheflag.models

data class TeamSchema(
    val event_id: Int,
    val leader_email: String,
    val player1_eid: String,
    val player1_name: String,
    val player2_eid: String,
    val player2_name: String,
    val player3_eid: String,
    val player3_name: String,
    val team_name: String,
    val wa_number: String
)